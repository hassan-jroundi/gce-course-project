package ma.sorec.gcecourse.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import ma.sorec.gcecourse.Utils.*;
import ma.sorec.gcecourse.controller.mohr.PersonneMController;
import ma.sorec.gcecourse.data.*;
import ma.sorec.gcecourse.data.mohr.PersonneM;
import ma.sorec.gcecourse.data.mohr.RapportImage;
import ma.sorec.gcecourse.repository.RapportImageRepository;
import ma.sorec.gcecourse.repository.mohr.PersonneMRepository;
import ma.sorec.gcecourse.service.*;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Value("${url.jrxml.file}")
    private String urlJrxmlFile; // chemin du fichier JRXML

    @Value("${font.times}")
    private String font;

    @Autowired
    ReservationService reservationService;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Autowired
    FactureService factureService;

    @Autowired
    PrixLitService prixLitService;

    @Autowired
    PrixBoxService prixBoxService;

    @Autowired
    PrixPisteService prixPisteService;

    @Autowired
    RapportImageRepository rapportImageRepository;

    @Autowired
    ModePaiementService modePaiementService;

    @Autowired
    PersonneService personneService;

    @Autowired
    PersonneMRepository personneMRepository;

    @Autowired
    PersonneMController personneMController;

    @Autowired
    UtilisateurService utilisateurService;

    @Autowired
    private DetailReservationService detailReservationService;

    @Value("${sorec.label.fr}")
    private String SOREC_LABEL_FR;

    @Value("${sorec.adresse1.fr}")
    private String SOREC_ADRESSE1_FR;

    @Value("${sorec.adresse2.fr}")
    private String SOREC_ADRESSE2_FR;

    @Value("${sorec.label.ar}")
    private String SOREC_LABEL_AR;

    @Value("${sorec.adresse1.ar}")
    private String SOREC_ADRESSE1_AR;

    @Value("${sorec.adresse2.ar}")
    private String SOREC_ADRESSE2_AR;

    final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void generateReportFromJson(OutputStream out, DataJson data) throws Exception {

        String rawJsonData = data.getData();

        InputStream input = new FileInputStream(new File(urlJrxmlFile + data.getReportName() + ".jrxml"));
        JasperDesign jasperDesign = JRXmlLoader.load(input);
        JasperReport jp = JasperCompileManager.compileReport(jasperDesign);

        ByteArrayInputStream jsonDataStream = new ByteArrayInputStream(rawJsonData.getBytes());
        JsonDataSource ds = new JsonDataSource(jsonDataStream);
        Map<String, Object> parameters = data.getParameters();

        JasperPrint print = JasperFillManager.fillReport(jp, parameters, ds);

        if (data.getFormat().equals("pdf"))
            generatePdfReport(print, out);
        else if (data.getFormat().equals("xls"))
            generateExcelReport(print, out);
        else
            throw new Exception("Extension/format invalide");

    }

    @Override
    public ByteArrayInputStream creerFacture(Long idFacture, boolean avecMail) throws DocumentException, IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 30, 30, 50, 30);

        int moisFacturation = 0;
        int anneeFacturation = 0;

        String typePrix = null;

        // Création d'un fichier pdf temporaire pour l'attacher au mail
        if (avecMail) {
            String fileName = "facture-numero-" + idFacture + ".pdf";
            File file = new File(fileName);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file.getPath()));
        }

        List<Reservation> reservations = new ArrayList<>();
        List<DetailReservation> detailReservations = detailReservationService.getAllByIdFacture(idFacture);
        for (DetailReservation detailReservation : detailReservations) {
            moisFacturation = DateUtil.convertToLocalDateViaInstant(detailReservation.getDateDebut()).getMonthValue();
            anneeFacturation = DateUtil.convertToLocalDateViaInstant(detailReservation.getDateDebut()).getYear();
            Reservation reservation = reservationService.get(new Long(detailReservation.getIdReservation()));
            typePrix = reservation.getCodeTypePrix();
            reservations.add(reservation);
        }
        Facture facture = factureService.get(idFacture);
        try {

            PdfWriter pdfWriter = PdfWriter.getInstance(document, baos);

            document.open();

            // Construire l'entete

            // Logo
            RapportImage rapportImage = rapportImageRepository.findById("logo_sorec").orElse(null);
            Blob imageBlob1 = rapportImage.getImage();
            byte[] imageBytes1 = imageBlob1.getBytes(1, (int) imageBlob1.length());
            Image logo = Image.getInstance(imageBytes1);
            logo.scaleToFit(150, 150);

            PdfPTable tabEntete = new PdfPTable(5);
            tabEntete.setWidths(new int[]{27, 8, 29, 3, 36});
            tabEntete.getDefaultCell().setBorder(0);
            tabEntete.setWidthPercentage(100);
            tabEntete.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell c = new PdfPCell(logo);
            c.setHorizontalAlignment(Element.ALIGN_LEFT);
            c.setBorder(0);
            tabEntete.addCell(c);

            tabEntete.addCell(new Phrase("  "));

            // Numéro Facture
            c = new PdfPCell(ReportingUtil.SET_TIMES_BOLD_WITHE("FACTURE N° " + anneeFacturation + idFacture.toString(), 13));
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            c.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c.setBackgroundColor(ReportingUtil.BASE_COLOR_BRONZE_DENSE);
            c.setExtraParagraphSpace(7f);
            c.setBorder(0);
            tabEntete.addCell(c);

            tabEntete.addCell(new Phrase(""));

            //Chargement nom du site
            String nomSite = "";
            for (Reservation item : reservations) {
                if (item.getChevalBoxs().size() > 0) {
                    for (ChevalBox item2 : item.getChevalBoxs()) {
                        nomSite = item2.getBox().getEcurie().getSite().getNom();
                    }
                }
                if (item.getChevalPistes().size() > 0) {
                    for (ChevalPiste item2 : item.getChevalPistes()) {
                        nomSite = item2.getPiste().getSite().getNom();
                    }
                }
                if (item.getPersonneLits().size() > 0) {
                    for (PersonneLit item2 : item.getPersonneLits()) {
                        nomSite = item2.getLit().getChambre().getImmeuble().getSite().getNom();
                    }
                }
            }

            // Nom du site
            c = new PdfPCell(ReportingUtil.SET_TIMES_BOLD_GRIS_BARBE_DENSE(
                    "DIRECTION DES COURSES - " + nomSite, 10));
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            c.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c.setExtraParagraphSpace(7f);
            c.setBorder(0);
            tabEntete.addCell(c);

            document.add(tabEntete);

            document.add(new Paragraph("\n"));

            PdfPCell cDeuxPoints = new PdfPCell(ReportingUtil.SET_TIMES_BOLD_GRIS_BARBE_DENSE(" : "));
            cDeuxPoints.setHorizontalAlignment(Element.ALIGN_LEFT);
            cDeuxPoints.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cDeuxPoints.setExtraParagraphSpace(7f);
            cDeuxPoints.setBorder(0);

            PdfPTable tabEdition = new PdfPTable(3);
            tabEdition.setWidths(new int[]{107, 5, 28});
            tabEdition.getDefaultCell().setBorder(0);
            tabEdition.setWidthPercentage(70);
            tabEdition.setHorizontalAlignment(Element.ALIGN_RIGHT);

            LocalDate today = LocalDate.now();

            PdfPCell dateEdition = new PdfPCell(ReportingUtil.SET_TIMES_BOLD_GRIS_BARBE_DENSE_SMALLER("Date édition"));
            dateEdition.setHorizontalAlignment(Element.ALIGN_RIGHT);
            dateEdition.setVerticalAlignment(Element.ALIGN_TOP);
            dateEdition.setExtraParagraphSpace(7f);
            dateEdition.setBorder(0);
            tabEdition.addCell(dateEdition);

            tabEdition.addCell(cDeuxPoints);

            dateEdition = new PdfPCell(ReportingUtil
                    .SET_TIMES_ROMANS_GRIS_BARBE_DENSE_SMALLER(DateFormatUtil.toFrenchFormat(today)));
            dateEdition.setHorizontalAlignment(Element.ALIGN_LEFT);
            dateEdition.setVerticalAlignment(Element.ALIGN_TOP);
            dateEdition.setExtraParagraphSpace(7f);
            dateEdition.setBorder(0);
            tabEdition.addCell(dateEdition);

            document.add(tabEdition);

            document.add(new Paragraph("\n\n"));

            tabEntete = new PdfPTable(3);
            tabEntete.setWidths(new int[]{30, 5, 65});
            tabEntete.getDefaultCell().setBorder(0);
            tabEntete.setWidthPercentage(80);
            tabEntete.setHorizontalAlignment(Element.ALIGN_CENTER);

            c = new PdfPCell(ReportingUtil.SET_TIMES_BOLD("\n"));
            c.setColspan(3);
            c.setBorder(0);
            tabEntete.addCell(c);

            c = new PdfPCell(ReportingUtil.SET_TIMES_BOLD_GRIS_BARBE_DENSE_SMALLER("Client"));
            c.setHorizontalAlignment(Element.ALIGN_LEFT);
            c.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c.setExtraParagraphSpace(7f);
            c.setBorder(0);
            tabEntete.addCell(c);

            tabEntete.addCell(cDeuxPoints);

            PersonneM personne = personneMController.get(facture.getIdPersonneAFacturer());
            switch (personne.getCodeNaturePersonne()) {
                case "P":
                    c = new PdfPCell(
                            ReportingUtil.SET_TIMES_ROMANS_GRIS_BARBE_DENSE_SMALLER(SharedUtil.convertToTitleCaseIteratingChars(personne.getNom()) + " " + SharedUtil.convertToTitleCaseIteratingChars(personne.getPrenom())));
                    break;
                case "A":
                    c = new PdfPCell(
                            ReportingUtil.SET_TIMES_ROMANS_GRIS_BARBE_DENSE_SMALLER(personne.getDesignation()));
                    break;
                case "M":
                    c = new PdfPCell(
                            ReportingUtil.SET_TIMES_ROMANS_GRIS_BARBE_DENSE_SMALLER(personne.getRaisonSociale()));
                    break;
                default:
                    break;
            }
            c.setHorizontalAlignment(Element.ALIGN_LEFT);
            c.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c.setExtraParagraphSpace(7f);
            c.setBorder(0);
            tabEntete.addCell(c);

            DecimalFormat decimalFormat = new DecimalFormat("###,##0.00");

            c = new PdfPCell(ReportingUtil.SET_TIMES_BOLD_GRIS_BARBE_DENSE_SMALLER("Date facturation"));
            c.setHorizontalAlignment(Element.ALIGN_LEFT);
            c.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c.setExtraParagraphSpace(7f);
            c.setBorder(0);
            tabEntete.addCell(c);

            tabEntete.addCell(cDeuxPoints);

            DateFormat df = new SimpleDateFormat("EEEE dd MMMM yyyy");

            c = new PdfPCell(ReportingUtil
                    .SET_TIMES_ROMANS_GRIS_BARBE_DENSE_SMALLER(df.format((Date) facture.getDateFacture())));
            c.setHorizontalAlignment(Element.ALIGN_LEFT);
            c.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c.setExtraParagraphSpace(7f);
            c.setBorder(0);
            tabEntete.addCell(c);

            c = new PdfPCell(ReportingUtil.SET_TIMES_BOLD_GRIS_BARBE_DENSE_SMALLER("Date limite de paiement"));
            c.setHorizontalAlignment(Element.ALIGN_LEFT);
            c.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c.setExtraParagraphSpace(7f);
            c.setBorder(0);
            tabEntete.addCell(c);

            tabEntete.addCell(cDeuxPoints);

            c = new PdfPCell(ReportingUtil
                    .SET_TIMES_ROMANS_GRIS_BARBE_DENSE_SMALLER(df.format(new Date(facture.getDateFacture().getTime() + (15 * (1000 * 60 * 60 * 24))))));
            c.setHorizontalAlignment(Element.ALIGN_LEFT);
            c.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c.setExtraParagraphSpace(7f);
            c.setBorder(0);
            tabEntete.addCell(c);

//            c = new PdfPCell(ReportingUtil.SET_TIMES_BOLD_GRIS_BARBE_DENSE("Mode de paiement"));
//            c.setHorizontalAlignment(Element.ALIGN_LEFT);
//            c.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            c.setExtraParagraphSpace(7f);
//            c.setBorder(0);
//            tabEntete.addCell(c);
//
//            tabEntete.addCell(cDeuxPoints);
//
//            c = new PdfPCell(ReportingUtil
//                    .SET_TIMES_ROMANS_GRIS_BARBE_DENSE(modePaiementService.get(facture.getCodeModePaiement()).getDesignation()));
//            c.setHorizontalAlignment(Element.ALIGN_LEFT);
//            c.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            c.setExtraParagraphSpace(7f);
//            c.setBorder(0);
//            tabEntete.addCell(c);


            //Partie date de paiement
//            if (facture.getDatePaiement() != null) {
//                c = new PdfPCell(ReportingUtil.SET_TIMES_BOLD_GRIS_BARBE_DENSE("Date de paiement"));
//                c.setHorizontalAlignment(Element.ALIGN_LEFT);
//                c.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                c.setExtraParagraphSpace(7f);
//                c.setBorder(0);
//                tabEntete.addCell(c);
//
//                tabEntete.addCell(cDeuxPoints);
//
//                DateFormat dfBis = new SimpleDateFormat("EEEE dd MMMM yyyy");
//
//                c = new PdfPCell(ReportingUtil
//                        .SET_TIMES_ROMANS_GRIS_BARBE_DENSE(dfBis.format((Date) facture.getDatePaiement())));
//                c.setHorizontalAlignment(Element.ALIGN_LEFT);
//                c.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                c.setExtraParagraphSpace(7f);
//                c.setBorder(0);
//                tabEntete.addCell(c);
//            }

            c = new PdfPCell(ReportingUtil.SET_TIMES_BOLD_GRIS_BARBE_DENSE_SMALLER("Opérateur"));
            c.setHorizontalAlignment(Element.ALIGN_LEFT);
            c.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c.setExtraParagraphSpace(7f);
            c.setBorder(0);
            tabEntete.addCell(c);

            tabEntete.addCell(cDeuxPoints);

            Personne operateur = utilisateurService.get(new Long(facture.getIdOperateur())).getPersonne();
            String nomOperateur = SharedUtil.convertToTitleCaseIteratingChars(operateur.getNom()) + " " + SharedUtil.convertToTitleCaseIteratingChars(operateur.getPrenom());
            c = new PdfPCell(ReportingUtil
                    .SET_TIMES_ROMANS_GRIS_BARBE_DENSE_SMALLER(nomOperateur));
            c.setHorizontalAlignment(Element.ALIGN_LEFT);
            c.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c.setExtraParagraphSpace(7f);
            c.setBorder(0);
            tabEntete.addCell(c);

            document.add(tabEntete);

            document.add(new Paragraph("\n\n"));

            tabEntete = new PdfPTable(5);
            tabEntete.setWidths(new int[]{37, 8, 12, 12, 10});
            tabEntete.setWidthPercentage(100);
            tabEntete.setHorizontalAlignment(Element.ALIGN_CENTER);

            c = new PdfPCell(ReportingUtil.SET_TIMES_BOLD_WITHE("Détails des prestations "));
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            c.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c.setBackgroundColor(ReportingUtil.BASE_COLOR_BRONZE_DENSE);
            c.setColspan(6);
            c.setExtraParagraphSpace(7f);
            tabEntete.addCell(c);

            c = new PdfPCell(ReportingUtil.SET_TIMES_BOLD_WITHE("Prestation "));
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            c.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c.setBackgroundColor(ReportingUtil.BASE_COLOR_BRONZE_DENSE);
            c.setExtraParagraphSpace(7f);
            tabEntete.addCell(c);

            c = new PdfPCell(ReportingUtil.SET_TIMES_BOLD_WITHE("Qt� "));
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            c.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c.setBackgroundColor(ReportingUtil.BASE_COLOR_BRONZE_DENSE);
            c.setExtraParagraphSpace(7f);
            tabEntete.addCell(c);

            c = new PdfPCell(ReportingUtil.SET_TIMES_BOLD_WITHE("PU HT "));
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            c.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c.setBackgroundColor(ReportingUtil.BASE_COLOR_BRONZE_DENSE);
            c.setExtraParagraphSpace(7f);
            tabEntete.addCell(c);

            c = new PdfPCell(ReportingUtil.SET_TIMES_BOLD_WITHE("Devise "));
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            c.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c.setBackgroundColor(ReportingUtil.BASE_COLOR_BRONZE_DENSE);
            c.setExtraParagraphSpace(7f);
            tabEntete.addCell(c);

            c = new PdfPCell(ReportingUtil.SET_TIMES_BOLD_WITHE("Montant HT "));
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            c.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c.setBackgroundColor(ReportingUtil.BASE_COLOR_BRONZE_DENSE);
            c.setExtraParagraphSpace(7f);
            tabEntete.addCell(c);


            BigDecimal totalHT = BigDecimal.ZERO;
            BigDecimal totalTVA = BigDecimal.ZERO;
            BigDecimal totalTTC = BigDecimal.ZERO;

            int nombreChevalBoxs = 0;
            int nombrePersonneLits = 0;

            Long prixLit = 0L;
            Long prixBox = 0L;

            for (DetailReservation detailReservation : detailReservations) {
                Reservation reservation = reservationService.get(new Long(detailReservation.getIdReservation()));
                if (detailReservation.getLibelleTypeReservation().equals("Hébergement Personne")) {
                    nombrePersonneLits++;
                    for (PersonneLit item : reservation.getPersonneLits()) {
                        String idChambre = item.getLit().getChambre().getId().toString();
                        prixLit = prixLitService.getPrixReservation(idChambre, reservation.getDateCreation(), reservation.getCodeTypePrix()).getMontant();
                    }
                }

                if (detailReservation.getLibelleTypeReservation().equals("Hébergement Cheval")) {
                    nombreChevalBoxs++;
                    for (ChevalBox item : reservation.getChevalBoxs()) {
                        String idBox = item.getBox().getId().toString();
                        prixBox = prixBoxService.getPrixReservation(idBox, reservation.getDateCreation(), reservation.getCodeTypePrix()).getMontant();
                    }
                }
            }

            // Tarif Unitaire
            if (typePrix.equals("U")) {
                LocalDate dateDebut = DateUtil.convertToLocalDateViaInstant(detailReservations.get(0).getDateDebut());
                LocalDate dateFin = DateUtil.convertToLocalDateViaInstant(detailReservations.get(0).getDateFin());
                String dateDebutStr = (dateDebut.getDayOfMonth() < 10 ? "0" + dateDebut.getDayOfMonth() : dateDebut.getDayOfMonth()) + "/"
                        + (dateDebut.getMonthValue() < 10 ? "0" + dateDebut.getMonthValue() : dateDebut.getMonthValue()) + "/"
                        + dateDebut.getYear();
                String dateFinStr = (dateFin.getDayOfMonth() < 10 ? "0" + dateFin.getDayOfMonth() : dateFin.getDayOfMonth()) + "/"
                        + (dateFin.getMonthValue() < 10 ? "0" + dateFin.getMonthValue() : dateFin.getMonthValue()) + "/"
                        + dateFin.getYear();
                if (nombreChevalBoxs > 0) {
                    c = new PdfPCell(ReportingUtil.SET_TIMES_ROMANS_GRIS_BARBE_DENSE(
                            "Occupation de Boxes pour chevaux du " + dateDebutStr
                                    + " au " + dateFinStr));
                    c.setHorizontalAlignment(Element.ALIGN_LEFT);
                    c.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    c.setExtraParagraphSpace(7f);
                    tabEntete.addCell(c);

                    BigDecimal quantite = new BigDecimal(nombreChevalBoxs);
                    c = new PdfPCell(ReportingUtil.SET_TIMES_ROMANS_GRIS_BARBE_DENSE("" + quantite));
                    c.setHorizontalAlignment(Element.ALIGN_CENTER);
                    c.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    c.setExtraParagraphSpace(7f);
                    tabEntete.addCell(c);

                    Long daysDifference = new Long(0);
                    Long prixBox2 = new Long(0);
                    Long idBox = new Long(0);
                    daysDifference = SharedUtil.getDifferenceDays(detailReservations.get(0).getDateDebut(), detailReservations.get(0).getDateFin());
                    for (DetailReservation detailReservation : detailReservations) {
                        Reservation reservation = reservationService.get(new Long(detailReservation.getIdReservation()));
                        if (reservation.getChevalBoxs() != null) {
                            for (ChevalBox chevalBox : reservation.getChevalBoxs()) {
                                idBox = chevalBox.getBox().getId();
                                prixBox2 = prixBoxService.getPrixReservation(idBox.toString(), reservation.getDateCreation(), reservation.getCodeTypePrix()).getMontant();
                                break;
                            }
                        }
                        break;
                    }

                    BigDecimal montantHt = new BigDecimal(prixBox2 * daysDifference);
                    totalHT = totalHT.add(montantHt.multiply(quantite));
                    c = new PdfPCell(ReportingUtil.SET_TIMES_ROMANS_GRIS_BARBE_DENSE(decimalFormat.format(montantHt)));
                    c.setHorizontalAlignment(Element.ALIGN_CENTER);
                    c.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    c.setExtraParagraphSpace(7f);
                    tabEntete.addCell(c);

                    Double calculTVA = new Long(String.valueOf(montantHt)) * 1.2;
                    BigDecimal montantTTC = new BigDecimal(calculTVA);
                    c = new PdfPCell(
                            ReportingUtil.SET_TIMES_ROMANS_GRIS_BARBE_DENSE("MAD"));
                    c.setHorizontalAlignment(Element.ALIGN_CENTER);
                    c.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    c.setExtraParagraphSpace(7f);
                    tabEntete.addCell(c);

                    BigDecimal montantTtc = quantite.multiply(montantHt);
                    totalTTC = totalTTC.add(montantTtc);
                    c = new PdfPCell(ReportingUtil.SET_TIMES_ROMANS_GRIS_BARBE_DENSE(decimalFormat.format(montantTtc)));
                    c.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    c.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    c.setExtraParagraphSpace(7f);
                    tabEntete.addCell(c);

                    BigDecimal montantTva = (BigDecimal) new BigDecimal(0);
                    totalTVA = totalTVA.add(montantTva.multiply(quantite));
                }
                if (nombrePersonneLits > 0) {
                    c = new PdfPCell(ReportingUtil.SET_TIMES_ROMANS_GRIS_BARBE_DENSE(
                            "Hébergement des cavaliers d'entrainement du " + dateDebutStr
                                    + " au " + dateFinStr));
                    c.setHorizontalAlignment(Element.ALIGN_LEFT);
                    c.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    c.setExtraParagraphSpace(7f);
                    tabEntete.addCell(c);

                    BigDecimal quantite = new BigDecimal(nombrePersonneLits);
                    c = new PdfPCell(ReportingUtil.SET_TIMES_ROMANS_GRIS_BARBE_DENSE("" + quantite));
                    c.setHorizontalAlignment(Element.ALIGN_CENTER);
                    c.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    c.setExtraParagraphSpace(7f);
                    tabEntete.addCell(c);

                    Long daysDifference = new Long(0);
                    Long prixLit2 = new Long(0);
                    Long idChambre = new Long(0);
                    daysDifference = SharedUtil.getDifferenceDays(detailReservations.get(0).getDateDebut(), detailReservations.get(0).getDateFin());
                    for (DetailReservation detailReservation : detailReservations) {
                        Reservation reservation = reservationService.get(new Long(detailReservation.getIdReservation()));
                        if (reservation.getPersonneLits() != null) {
                            for (PersonneLit personneLit : reservation.getPersonneLits()) {
                                idChambre = personneLit.getLit().getChambre().getId();
                                prixLit2 = prixLitService.getPrixReservation(idChambre.toString(), reservation.getDateCreation(), reservation.getCodeTypePrix()).getMontant();
                                break;
                            }
                        }
                        break;
                    }

                    BigDecimal montantHt = new BigDecimal(prixLit2 * daysDifference);
                    totalHT = totalHT.add(montantHt.multiply(quantite));
                    c = new PdfPCell(ReportingUtil.SET_TIMES_ROMANS_GRIS_BARBE_DENSE(decimalFormat.format(montantHt)));
                    c.setHorizontalAlignment(Element.ALIGN_CENTER);
                    c.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    c.setExtraParagraphSpace(7f);
                    tabEntete.addCell(c);

                    Double calculTVA = new Long(String.valueOf(montantHt)) * 1.2;
                    BigDecimal montantTTC = new BigDecimal(calculTVA);
                    c = new PdfPCell(
                            ReportingUtil.SET_TIMES_ROMANS_GRIS_BARBE_DENSE("MAD"));
                    c.setHorizontalAlignment(Element.ALIGN_CENTER);
                    c.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    c.setExtraParagraphSpace(7f);
                    tabEntete.addCell(c);

                    BigDecimal montantTtc = quantite.multiply(montantHt);
                    totalTTC = totalTTC.add(montantTtc);
                    c = new PdfPCell(ReportingUtil.SET_TIMES_ROMANS_GRIS_BARBE_DENSE(decimalFormat.format(montantTtc)));
                    c.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    c.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    c.setExtraParagraphSpace(7f);
                    tabEntete.addCell(c);

                    BigDecimal montantTva = (BigDecimal) new BigDecimal(0);
                    totalTVA = totalTVA.add(montantTva.multiply(quantite));
                }
            }

            // Tarif Forfaitaire
            if (typePrix.equals("F")) {
                if (nombreChevalBoxs > 0) {

                    c = new PdfPCell(ReportingUtil.SET_TIMES_ROMANS_GRIS_BARBE_DENSE("Occupation de Boxes pour chevaux Mois de " + DateUtil.getFrenchMonth(moisFacturation) + " " + anneeFacturation));
                    c.setHorizontalAlignment(Element.ALIGN_LEFT);
                    c.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    c.setExtraParagraphSpace(7f);
                    tabEntete.addCell(c);

                    BigDecimal quantite = new BigDecimal(nombreChevalBoxs);
                    c = new PdfPCell(ReportingUtil.SET_TIMES_ROMANS_GRIS_BARBE_DENSE("" + quantite));
                    c.setHorizontalAlignment(Element.ALIGN_CENTER);
                    c.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    c.setExtraParagraphSpace(7f);
                    tabEntete.addCell(c);

                    BigDecimal montantHt = new BigDecimal(prixBox);
                    totalHT = totalHT.add(montantHt.multiply(quantite));
                    c = new PdfPCell(ReportingUtil.SET_TIMES_ROMANS_GRIS_BARBE_DENSE(decimalFormat.format(montantHt)));
                    c.setHorizontalAlignment(Element.ALIGN_CENTER);
                    c.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    c.setExtraParagraphSpace(7f);
                    tabEntete.addCell(c);

                    Double calculTVA = new Long(String.valueOf(montantHt)) * 1.2;
                    BigDecimal montantTTC = new BigDecimal(calculTVA);
                    c = new PdfPCell(
                            ReportingUtil.SET_TIMES_ROMANS_GRIS_BARBE_DENSE("MAD"));
                    c.setHorizontalAlignment(Element.ALIGN_CENTER);
                    c.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    c.setExtraParagraphSpace(7f);
                    tabEntete.addCell(c);

                    BigDecimal montantTtc = quantite.multiply(new BigDecimal(prixBox));
                    totalTTC = totalTTC.add(montantTtc);
                    c = new PdfPCell(ReportingUtil.SET_TIMES_ROMANS_GRIS_BARBE_DENSE(decimalFormat.format(montantTtc)));
                    c.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    c.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    c.setExtraParagraphSpace(7f);
                    tabEntete.addCell(c);

                    BigDecimal montantTva = (BigDecimal) new BigDecimal(0);
                    totalTVA = totalTVA.add(montantTva.multiply(quantite));
                }

                if (nombrePersonneLits > 0) {

                    c = new PdfPCell(ReportingUtil.SET_TIMES_ROMANS_GRIS_BARBE_DENSE("Hébergement des cavaliers d'entrainement Mois de " + DateUtil.getFrenchMonth(moisFacturation) + " " + anneeFacturation));
                    c.setHorizontalAlignment(Element.ALIGN_LEFT);
                    c.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    c.setExtraParagraphSpace(7f);
                    tabEntete.addCell(c);

                    BigDecimal quantite = new BigDecimal(nombrePersonneLits);
                    c = new PdfPCell(ReportingUtil.SET_TIMES_ROMANS_GRIS_BARBE_DENSE("" + quantite));
                    c.setHorizontalAlignment(Element.ALIGN_CENTER);
                    c.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    c.setExtraParagraphSpace(7f);
                    tabEntete.addCell(c);

                    BigDecimal montantHt = new BigDecimal(prixLit);
                    totalHT = totalHT.add(montantHt.multiply(quantite));
                    c = new PdfPCell(ReportingUtil.SET_TIMES_ROMANS_GRIS_BARBE_DENSE(decimalFormat.format(montantHt)));
                    c.setHorizontalAlignment(Element.ALIGN_CENTER);
                    c.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    c.setExtraParagraphSpace(7f);
                    tabEntete.addCell(c);

                    Double calculTVA = new Long(String.valueOf(montantHt)) * 1.2;
                    BigDecimal montantTTC = new BigDecimal(calculTVA);
                    c = new PdfPCell(
                            ReportingUtil.SET_TIMES_ROMANS_GRIS_BARBE_DENSE("MAD"));
                    c.setHorizontalAlignment(Element.ALIGN_CENTER);
                    c.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    c.setExtraParagraphSpace(7f);
                    tabEntete.addCell(c);

                    BigDecimal montantTtc = quantite.multiply(new BigDecimal(prixLit));
                    totalTTC = totalTTC.add(montantTtc);
                    c = new PdfPCell(ReportingUtil.SET_TIMES_ROMANS_GRIS_BARBE_DENSE(decimalFormat.format(montantTtc)));
                    c.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    c.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    c.setExtraParagraphSpace(7f);
                    tabEntete.addCell(c);

                    BigDecimal montantTva = (BigDecimal) new BigDecimal(0);
                    totalTVA = totalTVA.add(montantTva.multiply(quantite));
                }
            }

            document.add(tabEntete);

            document.add(new Paragraph("\n"));

            tabEntete = new PdfPTable(2);
            tabEntete.setWidths(new int[]{30, 40});
            tabEntete.setWidthPercentage(40);
            tabEntete.setHorizontalAlignment(Element.ALIGN_RIGHT);

            c = new PdfPCell(ReportingUtil.SET_TIMES_BOLD_WITHE("Total HT"));
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            c.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c.setBackgroundColor(ReportingUtil.BASE_COLOR_BRONZE_DENSE);
            c.setExtraParagraphSpace(7f);
            tabEntete.addCell(c);

            c = new PdfPCell(ReportingUtil.SET_TIMES_ROMANS_GRIS_BARBE_DENSE(decimalFormat.format(totalHT) + " DH"));
            c.setHorizontalAlignment(Element.ALIGN_RIGHT);
            c.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c.setExtraParagraphSpace(7f);
            tabEntete.addCell(c);

            c = new PdfPCell(ReportingUtil.SET_TIMES_BOLD_WITHE("TVA 20%"));
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            c.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c.setBackgroundColor(ReportingUtil.BASE_COLOR_BRONZE_DENSE);
            c.setExtraParagraphSpace(7f);
            tabEntete.addCell(c);


            c = new PdfPCell(ReportingUtil.SET_TIMES_ROMANS_GRIS_BARBE_DENSE(decimalFormat.format(totalHT.multiply(new BigDecimal(1.2)).subtract(totalHT)) + " DH"));
            c.setHorizontalAlignment(Element.ALIGN_RIGHT);
            c.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c.setExtraParagraphSpace(7f);
            tabEntete.addCell(c);

            c = new PdfPCell(ReportingUtil.SET_TIMES_BOLD_WITHE("Total TTC"));
            c = new PdfPCell(ReportingUtil.SET_TIMES_BOLD_WITHE("Total TTC"));
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            c.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c.setBackgroundColor(ReportingUtil.BASE_COLOR_BRONZE_DENSE);
            c.setExtraParagraphSpace(7f);
            tabEntete.addCell(c);

            c = new PdfPCell(ReportingUtil.SET_TIMES_ROMANS_GRIS_BARBE_DENSE(decimalFormat.format(totalHT.multiply(new BigDecimal(1.2))) + " DH"));
            c.setHorizontalAlignment(Element.ALIGN_RIGHT);
            c.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c.setExtraParagraphSpace(7f);
            tabEntete.addCell(c);

            document.add(tabEntete);

            document.add(new Paragraph("\n\n"));

            PdfPTable tabSomme = new PdfPTable(1);
            tabSomme.setWidths(new int[]{800});
            tabSomme.getDefaultCell().setBorder(0);
            tabSomme.setWidthPercentage(100);
            tabSomme.setHorizontalAlignment(Element.ALIGN_LEFT);

            Long montantTotal = totalHT.multiply(new BigDecimal(1.2)).longValue();
            PdfPCell cellSomme = new PdfPCell(ReportingUtil.SET_TIMES_ROMANS_GRIS_BARBE_DENSE_SMALLER("Arrêtée la présente facture à la somme de : " + SharedUtil.convertToTitleCaseIteratingChars(FrenchNumberToWords.convert(montantTotal + 1)) + " Dirhams TTC"));
            cellSomme.setHorizontalAlignment(Element.ALIGN_LEFT);
            cellSomme.setVerticalAlignment(Element.ALIGN_TOP);
            cellSomme.setExtraParagraphSpace(7f);
            cellSomme.setBorder(0);
            tabSomme.addCell(cellSomme);

            document.add(tabSomme);

            document.add(new Paragraph("\n"));

            tabEntete = new PdfPTable(3);
            tabEntete.setWidths(new int[]{30, 5, 65});
            tabEntete.getDefaultCell().setBorder(0);
            tabEntete.setWidthPercentage(80);
            tabEntete.setHorizontalAlignment(Element.ALIGN_LEFT);

            c = new PdfPCell(ReportingUtil.SET_TIMES_ROMANS_GRIS_BARBE_DENSE_SMALLER("Coordonnées bancaires "));
            c.setHorizontalAlignment(Element.ALIGN_LEFT);
            c.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c.setExtraParagraphSpace(7f);
            c.setBorder(0);
            tabEntete.addCell(c);

            c = new PdfPCell(ReportingUtil.SET_TIMES_ROMANS_GRIS_BARBE_DENSE_SMALLER(""));
            c.setHorizontalAlignment(Element.ALIGN_LEFT);
            c.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c.setExtraParagraphSpace(7f);
            c.setBorder(0);
            tabEntete.addCell(c);

            c = new PdfPCell(ReportingUtil.SET_TIMES_ROMANS_GRIS_BARBE_DENSE(""));
            c.setHorizontalAlignment(Element.ALIGN_LEFT);
            c.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c.setExtraParagraphSpace(7f);
            c.setBorder(0);
            tabEntete.addCell(c);

            c = new PdfPCell(ReportingUtil.SET_TIMES_BOLD_GRIS_BARBE_DENSE_SMALLER("Banque "));
            c.setHorizontalAlignment(Element.ALIGN_LEFT);
            c.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c.setBorder(0);
            tabEntete.addCell(c);

            tabEntete.addCell(cDeuxPoints);

            c = new PdfPCell(ReportingUtil.SET_TIMES_ROMANS_GRIS_BARBE_DENSE_SMALLER("ATTIJARIWAFA BANK"));
            c.setHorizontalAlignment(Element.ALIGN_LEFT);
            c.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c.setBorder(0);
            tabEntete.addCell(c);

            c = new PdfPCell(ReportingUtil.SET_TIMES_BOLD_GRIS_BARBE_DENSE_SMALLER("Compte N° "));
            c.setHorizontalAlignment(Element.ALIGN_LEFT);
            c.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            c.setExtraParagraphSpace(7f);
            c.setBorder(0);
            tabEntete.addCell(c);

            tabEntete.addCell(cDeuxPoints);

            c = new PdfPCell(ReportingUtil.SET_TIMES_ROMANS_GRIS_BARBE_DENSE_SMALLER("007 810 000 181 200 000 088 437"));
            c.setHorizontalAlignment(Element.ALIGN_LEFT);
            c.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c.setBorder(0);
            tabEntete.addCell(c);

            c = new PdfPCell(ReportingUtil.SET_TIMES_BOLD_GRIS_BARBE_DENSE_SMALLER("Code SWIFT "));
            c.setHorizontalAlignment(Element.ALIGN_LEFT);
            c.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c.setBorder(0);
            tabEntete.addCell(c);

            tabEntete.addCell(cDeuxPoints);

            c = new PdfPCell(ReportingUtil.SET_TIMES_ROMANS_GRIS_BARBE_DENSE_SMALLER("BCMAMAMC"));
            c.setHorizontalAlignment(Element.ALIGN_LEFT);
            c.setVerticalAlignment(Element.ALIGN_MIDDLE);
            c.setBorder(0);
            tabEntete.addCell(c);

            document.add(tabEntete);

            BaseFont bf = BaseFont.createFont(
                    BaseFont.TIMES_ROMAN, BaseFont.CP1252, BaseFont.EMBEDDED);

            BaseFont baseFontTimes = BaseFont.createFont("fonts/times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            BaseFont baseFontBold = BaseFont.createFont("fonts/timesbd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            Phrase phraseFooterFr = new Phrase(new Chunk("" + SOREC_LABEL_FR + "\n",
                    FontFactory.getFont(FontFactory.TIMES_BOLD, 5, 0, ReportingUtil.BASE_COLOR_BRONZE)));
            phraseFooterFr.add(new Chunk("" + SOREC_ADRESSE1_FR + "\n",
                    FontFactory.getFont(FontFactory.TIMES_BOLD, 5, 0, ReportingUtil.BASE_COLOR_GRIS_BARBE_DENSE)));
            phraseFooterFr.add(new Chunk("" + SOREC_ADRESSE2_FR,
                    FontFactory.getFont(FontFactory.TIMES_BOLD, 5, 0, ReportingUtil.BASE_COLOR_GRIS_BARBE_DENSE)));

            Chunk chunkFooterAr = new Chunk("" + SOREC_LABEL_AR + "\n",
                    new Font(baseFontBold, 8, 0, ReportingUtil.BASE_COLOR_BRONZE));
            Phrase phraseFooterAr = new Phrase(chunkFooterAr);

            phraseFooterAr.add(new Chunk("" + SOREC_ADRESSE1_AR + "\n",
                    new Font(baseFontTimes, 6, 0, BaseColor.BLACK)));
            phraseFooterAr.add(new Chunk("" + SOREC_ADRESSE2_AR,
                    new Font(baseFontTimes, 6, 0, BaseColor.BLACK)));

            PdfPTable footer = new PdfPTable(2);
            try {
                footer.setWidths(new int[]{80, 80});
                footer.setTotalWidth(540);
                footer.setLockedWidth(false);
                footer.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                footer.getDefaultCell().setExtraParagraphSpace(3f);

                PdfPCell pdfPCell = new PdfPCell(phraseFooterFr);
                pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfPCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                pdfPCell.setVerticalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setExtraParagraphSpace(4f);
                pdfPCell.setBorder(0);
                footer.addCell(phraseFooterFr);

//                RapportImage adresseAr = rapportImageRepository.findById("adresse_ar").orElse(null);
//                Blob imageBlob2 = adresseAr.getImage();
//                byte[] imageBytes2 = imageBlob2.getBytes(1, (int) imageBlob2.length());
//                Image adresseImageAr = Image.getInstance(imageBytes2);
//                adresseImageAr.scaleToFit(230, 230);

                pdfPCell = new PdfPCell(phraseFooterAr);
                pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                pdfPCell.setVerticalAlignment(Element.ALIGN_TOP);
                pdfPCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                pdfPCell.setExtraParagraphSpace(2.5f);
                pdfPCell.setPaddingTop(-1.7f);
                pdfPCell.setBorder(0);
                footer.addCell(pdfPCell);

                // write page
                PdfContentByte canvas = pdfWriter.getDirectContent();
                canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
                footer.writeSelectedRows(0, -1, 34, 55, canvas);
                canvas.endMarkedContentSequence();

            } catch (DocumentException de) {
                throw new ExceptionConverter(de);
            }
            document.close();
        } catch (DocumentException | SQLException e) {
            log.error(e.toString());
        }
        return new ByteArrayInputStream(baos.toByteArray());
    }

    /***** generate PDF report *****/
    public void generatePdfReport(JasperPrint print, OutputStream out) throws Exception {
        JRPdfExporter exporterPdf = new JRPdfExporter();
        exporterPdf.setExporterInput(new SimpleExporterInput(print));
        exporterPdf.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
        SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
        exporterPdf.setConfiguration(configuration);
        exporterPdf.exportReport();
    }

    /**** generate Excel report ****/
    public void generateExcelReport(JasperPrint print, OutputStream out) throws Exception {
        JRXlsExporter exporterExcel = new JRXlsExporter();

        exporterExcel.setExporterInput(new SimpleExporterInput(print));
        exporterExcel.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
        SimpleXlsReportConfiguration configurationXls = new SimpleXlsReportConfiguration();
        configurationXls.setOnePagePerSheet(true);
        configurationXls.setDetectCellType(true);
        configurationXls.setCollapseRowSpan(false);
        exporterExcel.setConfiguration(configurationXls);

        exporterExcel.exportReport();

    }

}
