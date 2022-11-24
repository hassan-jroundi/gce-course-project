package ma.sorec.gcecourse.service.impl;

import com.itextpdf.text.DocumentException;
import ma.sorec.gcecourse.Utils.DateUtil;
import ma.sorec.gcecourse.Utils.SharedUtil;
import ma.sorec.gcecourse.data.*;
import ma.sorec.gcecourse.data.mohr.DetailDeclarationEffectif;
import ma.sorec.gcecourse.exceptions.CustomException;
import ma.sorec.gcecourse.repository.ReservationRepository;
import ma.sorec.gcecourse.repository.mohr.DetailDeclarationEffectifRepository;
import ma.sorec.gcecourse.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository repository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UtilisateurSessionService utilisateurSessionService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private DetailDeclarationEffectifRepository detailDeclarationEffectifRepository;

    @Autowired
    ChevalReservationEtatService chevalReservationEtatService;

    @Autowired
    private PrixLitService prixLitService;

    @Autowired
    private PrixBoxService prixBoxService;

    @Autowired
    private PrixPisteService prixPisteService;

    @Autowired
    private FactureService factureService;

    @Autowired
    private ChevalPisteService chevalPisteService;

    @Autowired
    private ChevalBoxService chevalBoxService;

    @Autowired
    private PersonneLitService personneLitService;

    @Autowired
    private DetailReservationService detailReservationService;

    @Autowired
    private LitService litService;

    @Autowired
    private BoxService boxService;

    @Autowired
    private PisteService pisteService;

    @Autowired
    private ChevalService chevalService;

    @Autowired
    private PersonneService personneService;

    @Value("${envoyer.mail}")
    String envoyerMail;

    @Override
    public Reservation creerReservation(Date dateDebut, Date dateFin, Long idPersonneAFacturer, Long idPersonne, Long idBox, Long idPiste, Long idLit, Long idCheval, String typePrix, String typeReservation, String idSession) {

        List<Reservation> reservations = null;
        dateDebut = DateUtil.trim(dateDebut);
        dateFin = DateUtil.trim(dateFin);
        switch (typeReservation) {
            case "CB": {
                reservations = repository.findAllByChevalBoxsChevalId(idCheval);
                break;
            }
            case "PL": {
                reservations = repository.findAllByPersonneLitsPersonneId(idPersonne.toString());
                break;
            }
            case "CP": {
                reservations = repository.findAllByChevalPistesChevalId(idCheval);
                break;
            }
            default:
                break;
        }
        for (Reservation reservation : reservations) {
            if (!((dateDebut.before(reservation.getDateDebut()) && dateFin.before(reservation.getDateDebut())) || (dateDebut.after(reservation.getDateDebut()) && dateFin.after(reservation.getDateFin())))) {
                throw new CustomException("A", "Cette personne a déjà une réservation durant la période sélectionnée");
            }
        }
        LocalDate dateDebutRes = DateUtil.convertToLocalDateViaInstant(dateDebut);
        LocalDate dateFinRes = DateUtil.convertToLocalDateViaInstant(dateFin);

        Date dateCreation = new Date();

        Reservation reservation = Reservation.builder()
                .codeStatutReservation("NP")
                .codeTypePrix(typePrix)
                .dateCreation(dateCreation)
                .userCreation(utilisateurSessionService.getByIdSession(idSession).getUtilisateur().getId())
                .idPersonneFacture(idPersonneAFacturer.toString())
                .dateDebut(dateDebut)
                .dateFin(dateFin)
                .build();
        reservation = this.save(reservation);

        DetailDeclarationEffectif detailDeclarationEffectif = detailDeclarationEffectifRepository.findFirstByIdChevalOrderByDateCreationDesc(idCheval);

        if (detailDeclarationEffectif != null) {
            ChevalReservationEtat chevalReservationEtat = ChevalReservationEtat.builder()
                    .codeEtat(detailDeclarationEffectif.getCodeStatutCheval())
                    .dateCreation(new Date())
                    .userCreation(utilisateurSessionService.getByIdSession(idSession).getUtilisateur().getId())
                    .reservation(reservation)
                    .idSession(idSession)
                    .build();
            chevalReservationEtatService.save(chevalReservationEtat);
        } else {
            ChevalReservationEtat chevalReservationEtat = ChevalReservationEtat.builder()
                    .codeEtat("AENTR")
                    .dateCreation(new Date())
                    .userCreation(utilisateurSessionService.getByIdSession(idSession).getUtilisateur().getId())
                    .reservation(reservation)
                    .idSession(idSession)
                    .build();
            chevalReservationEtatService.save(chevalReservationEtat);
        }


        if (typeReservation.equals("CB")) {
            ChevalBox chevalBox = ChevalBox.builder()
                    .box(boxService.get(idBox))
                    .cheval(chevalService.get(idCheval))
                    .reservation(reservation)
                    .userCreation(utilisateurSessionService.getByIdSession(idSession).getUtilisateur().getId())
                    .dateCreation(new Date())
                    .idSession(idSession)
                    .build();
            chevalBoxService.save(chevalBox);
        }

        if (typeReservation.equals("PL")) {
            PersonneLit personneLit = PersonneLit.builder()
                    .lit(litService.get(idLit))
                    .personne(personneService.get(idPersonne.toString()))
                    .reservation(reservation)
                    .userCreation(utilisateurSessionService.getByIdSession(idSession).getUtilisateur().getId())
                    .dateCreation(new Date())
                    .idSession(idSession)
                    .build();
            personneLitService.save(personneLit);
        }

        if (typeReservation.equals("CP")) {
            ChevalPiste chevalPiste = ChevalPiste.builder()
                    .piste(pisteService.get(idPiste))
                    .cheval(chevalService.get(idCheval))
                    .reservation(reservation)
                    .userCreation(utilisateurSessionService.getByIdSession(idSession).getUtilisateur().getId())
                    .dateCreation(new Date())
                    .idSession(idSession)
                    .build();
            chevalPisteService.save(chevalPiste);
        }

        if (typeReservation.equals("PL") || typeReservation.equals("CB")) {
            this.decouperReservationParMois(dateDebutRes, dateFinRes, reservation, idLit, idBox, idPersonne, idCheval, idPiste, typePrix, idSession);
        }

        return reservation;
    }

    @Override
    public Reservation modifierReservation(Long idReservation, Date dateDebut, Date dateFin, Long idPersonneAFacturer, Long idPersonne, Long idBox, Long idPiste, Long idLit, Long idCheval, String typePrix, String typeReservation, String idSession) {

        Reservation reservation = this.get(idReservation);
        LocalDate today = LocalDate.now();

        if (!(reservation.getIdPersonneFacture().equals(idPersonneAFacturer.toString()))) {
            List<DetailReservation> detailReservations = detailReservationService.getAllByIdReservation(reservation.getId().toString());
            for (DetailReservation detailReservation : detailReservations) {
                if (detailReservation.getFacture() != null) {
                    throw new CustomException("O", "Il n'est pas possible de modifier la personne à facturer d'une réservation partiellement facturée.");
                } else {
                    reservation.setIdPersonneFacture(idPersonneAFacturer.toString());
                }
            }
        }

        LocalDate dateDebutAncien = DateUtil.convertToLocalDateViaInstant(reservation.getDateDebut());
        LocalDate dateFinAncien = DateUtil.convertToLocalDateViaInstant(reservation.getDateFin());
        reservation.setDateDebut(dateDebut);
        reservation.setDateFin(dateFin);
        reservation.setCodeTypePrix(typePrix);
        reservation.setDateModification(new Date());
        reservation.setUserModification(utilisateurSessionService.getByIdSession(idSession).getUtilisateur().getId());

        reservation = this.save(reservation);

        switch (typeReservation) {
            case "CB": {
                ChevalBox chevalBox = chevalBoxService.getChevalBoxByReservationId(idReservation);
                if (idCheval == 0) {
                    if (chevalBox.getCheval() != null) {
                        chevalBox.setCheval(null);
                    }
                } else {
                    chevalBox.setCheval(chevalService.get(idCheval));
                }
//                if (chevalBox.getCheval() != null && idCheval == 0) {
//                    chevalBox.setCheval(null);
//                } else if ((chevalBox.getCheval() == null && idCheval != 0) || (chevalBox.getCheval() != null && idCheval != 0)) {
//                    chevalBox.setCheval(chevalService.get(idCheval));
//                }
                if (!(chevalBox.getBox().getId().equals(idBox))) {
                    chevalBox.setBox(boxService.get(idBox));
                }
                chevalBox.setDateModification(new Date());
                chevalBox.setUserModification(utilisateurSessionService.getByIdSession(idSession).getUtilisateur().getId());
                chevalBoxService.save(chevalBox);
                break;
            }
            case "PL": {
                PersonneLit personneLit = personneLitService.getPersonneLitByReservationId(idReservation);
                if (personneLit.getPersonne() != null && idPersonne == 0) {
                    personneLit.setPersonne(null);
                } else if ((personneLit.getPersonne() == null && idPersonne != 0) || (personneLit.getPersonne() != null && idPersonne != 0)) {
                    personneLit.setPersonne(personneService.get(idPersonne.toString()));
                }
                if (!(personneLit.getLit().getId().equals(idLit))) {
                    personneLit.setLit(litService.get(idLit));
                }
                personneLit.setDateModification(new Date());
                personneLit.setUserModification(utilisateurSessionService.getByIdSession(idSession).getUtilisateur().getId());
                personneLitService.save(personneLit);
                break;
            }
            case "CP": {
                chevalPisteService.getChevalPisteByReservationId(idReservation);
                break;
            }
            default:
                break;
        }

        if (!(dateDebutAncien.equals(DateUtil.convertToLocalDateViaInstant(dateDebut))) || !(dateFinAncien.equals(DateUtil.convertToLocalDateViaInstant(dateFin)))) {

            List<DetailReservation> detailReservationsAncien = detailReservationService.getAllByIdReservation(idReservation.toString());
            for (DetailReservation detailReservation : detailReservationsAncien) {
                detailReservationService.delete(detailReservation.getId());
            }

            LocalDate dateDebutRes = DateUtil.convertToLocalDateViaInstant(dateDebut);
            LocalDate dateFinRes = DateUtil.convertToLocalDateViaInstant(dateFin);

            this.decouperReservationParMois(dateDebutRes, dateFinRes, reservation, idLit, idBox, idPersonne, idCheval, idPiste, typePrix, idSession);
        }

        return reservation;
    }

    @Override
    public void supprimerReservation(Long idReservation) {

        LocalDate today = LocalDate.now();
        Reservation reservation = this.get(idReservation);

        if (DateUtil.convertToLocalDateViaInstant(reservation.getDateDebut()).isBefore(today)) {
            throw new CustomException("O", "Il n'est pas possible de supprimer une réservation en cours");
        }

        List<DetailReservation> detailReservations = detailReservationService.getAllByIdReservation(reservation.getId().toString());
        for (DetailReservation detailReservation : detailReservations) {
            if (detailReservation.getFacture() != null) {
                throw new CustomException("O", "Il n'est pas possible de supprimer une réservation partiellement facturée ! ");
            }
        }

        for (DetailReservation detailReservation : detailReservations) {
            detailReservationService.delete(detailReservation.getId());
        }

        ChevalBox chevalBox = chevalBoxService.getChevalBoxByReservationId(idReservation);
        if (chevalBox != null) {
            chevalBoxService.delete(chevalBox.getId());
        }

        ChevalPiste chevalPiste = chevalPisteService.getChevalPisteByReservationId(idReservation);
        if (chevalPiste != null) {
            chevalPisteService.delete(chevalPiste.getId());
        }

        PersonneLit personneLit = personneLitService.getPersonneLitByReservationId(idReservation);
        if (personneLit != null) {
            personneLitService.delete(personneLit.getId());
        }

        ChevalReservationEtat chevalReservationEtat = chevalReservationEtatService.getByReservationId(idReservation);
        if (chevalReservationEtat != null) {
            chevalReservationEtatService.delete(chevalReservationEtat.getId());
        }

        this.delete(reservation.getId());
    }

    @Override
    public void arreterReservation(Long idReservation) {
        LocalDate today = LocalDate.now();
        Reservation reservation = this.get(idReservation);
        boolean moisCourant = false;
        List<DetailReservation> detailReservations = detailReservationService.getAllByIdReservation(reservation.getId().toString());
        for (DetailReservation detailReservation : detailReservations) {
            Integer moisReservation = DateUtil.convertToLocalDateViaInstant(detailReservation.getDateDebut()).getMonthValue();
            if (moisCourant == true) {
                if (detailReservation.getFacture() != null) {
                    throw new CustomException("A", "Il est impossible d'arrêter une réservation facturée après la date d'ajourd'hui");
                } else {
                    detailReservation.setIdReservation(null);
                    detailReservationService.save(detailReservation);
                }
            }
            if (moisReservation == today.getMonthValue()) {
                if (detailReservation.getFacture() != null) {
                    throw new CustomException("A", "Il est impossible d'arrêter une réservation facturée après la date d'ajourd'hui");
                } else {
                    moisCourant = true;
                    detailReservation.setDateFin(DateUtil.convertToDateViaInstant(today));
                    detailReservationService.save(detailReservation);
                }
            }
        }
        reservation.setDateFin(DateUtil.convertToDateViaInstant(today));
        repository.save(reservation);
    }

    @Override
    public void decouperReservationParMois(LocalDate dateDebutRes, LocalDate dateFinRes, Reservation reservation, Long idLit, Long idBox, Long idPersonne, Long idCheval, Long idPiste, String typePrix, String idSession) {
        if (dateFinRes.getYear() > dateDebutRes.getYear()) {
            // année différente
            Integer anneeDifference = dateFinRes.getYear() - dateDebutRes.getYear();
            for (int i = 0; i <= anneeDifference; i++) {
                if (i == 0) {
                    // première année
                    for (int j = dateDebutRes.getMonthValue(); j <= 12; j++) {
                        if (j == dateDebutRes.getMonthValue()) {
                            // premier mois
                            LocalDate dateDebutCalcule = LocalDate.parse(dateDebutRes.getYear() + "-" + (j < 10 ? "0" + j : j) + "-" + (dateDebutRes.getDayOfMonth() < 10 ? "0" + dateDebutRes.getDayOfMonth() : dateDebutRes.getDayOfMonth()));
                            LocalDate dateFinCalcule = LocalDate.parse(dateDebutRes.getYear() + "-" + (j < 10 ? "0" + j : j) + "-" + DateUtil.getFinDuMois(j, dateDebutRes.getYear()));
                            detailReservationService.creerDetailReservation(reservation.getId().toString(), DateUtil.convertToDateViaInstant(dateDebutCalcule), DateUtil.convertToDateViaInstant(dateFinCalcule), typePrix, idSession);
                        } else {
                            // mois lambda
                            LocalDate dateDebutCalcule = LocalDate.parse(dateDebutRes.getYear() + "-" + (j < 10 ? "0" + j : j) + "-" + "01");
                            LocalDate dateFinCalcule = LocalDate.parse(dateDebutRes.getYear() + "-" + (j < 10 ? "0" + j : j) + "-" + DateUtil.getFinDuMois(j, dateDebutRes.getYear()));
                            detailReservationService.creerDetailReservation(reservation.getId().toString(), DateUtil.convertToDateViaInstant(dateDebutCalcule), DateUtil.convertToDateViaInstant(dateFinCalcule), typePrix, idSession);
                        }
                    }
                } else if (i == anneeDifference) {
                    // dernière année
                    for (int j = 1; j <= dateFinRes.getMonthValue(); j++) {
                        if (j == dateFinRes.getMonthValue()) {
                            // dernier mois
                            LocalDate dateDebutCalcule = LocalDate.parse(dateFinRes.getYear() + "-" + (j < 10 ? "0" + j : j) + "-" + "01");
                            LocalDate dateFinCalcule = LocalDate.parse(dateFinRes.getYear() + "-" + (j < 10 ? "0" + j : j) + "-" + (dateFinRes.getDayOfMonth() < 10 ? "0" + dateFinRes.getDayOfMonth() : dateFinRes.getDayOfMonth()));
                            detailReservationService.creerDetailReservation(reservation.getId().toString(), DateUtil.convertToDateViaInstant(dateDebutCalcule), DateUtil.convertToDateViaInstant(dateFinCalcule), typePrix, idSession);
                        } else {
                            // mois lambda
                            LocalDate dateDebutCalcule = LocalDate.parse(dateFinRes.getYear() + "-" + (j < 10 ? "0" + j : j) + "-" + "01");
                            LocalDate dateFinCalcule = LocalDate.parse(dateFinRes.getYear() + "-" + (j < 10 ? "0" + j : j) + "-" + DateUtil.getFinDuMois(j, dateFinRes.getYear()));
                            detailReservationService.creerDetailReservation(reservation.getId().toString(), DateUtil.convertToDateViaInstant(dateDebutCalcule), DateUtil.convertToDateViaInstant(dateFinCalcule), typePrix, idSession);
                        }
                    }
                } else {
                    // année lambda
                    for (int j = 1; j <= 12; j++) {
                    }
                }
            }
        } else {
            // même année
            Integer moisDifference = dateFinRes.getMonth().getValue() - dateDebutRes.getMonth().getValue();

            if (moisDifference == 0) {
                // même mois
                LocalDate dateDebutCalcule = LocalDate.parse(dateDebutRes.getYear() + "-" + (dateDebutRes.getMonthValue() < 10 ? "0" + dateDebutRes.getMonthValue() : dateDebutRes.getMonthValue()) + "-" + (dateDebutRes.getDayOfMonth() < 10 ? "0" + dateDebutRes.getDayOfMonth() : dateDebutRes.getDayOfMonth()));
                LocalDate dateFinCalcule = LocalDate.parse(dateFinRes.getYear() + "-" + (dateFinRes.getMonthValue() < 10 ? "0" + dateFinRes.getMonthValue() : dateFinRes.getMonthValue()) + "-" + (dateFinRes.getDayOfMonth() < 10 ? "0" + dateFinRes.getDayOfMonth() : dateFinRes.getDayOfMonth()));
                detailReservationService.creerDetailReservation(reservation.getId().toString(), DateUtil.convertToDateViaInstant(dateDebutCalcule), DateUtil.convertToDateViaInstant(dateFinCalcule), typePrix, idSession);
            } else {
                // mois différent
                for (int i = dateDebutRes.getMonthValue(); i <= dateFinRes.getMonthValue(); i++) {
                    if (i == dateDebutRes.getMonthValue()) {
                        //premier mois
                        LocalDate dateDebutCalcule = LocalDate.parse(dateDebutRes.getYear() + "-" + (dateDebutRes.getMonthValue() < 10 ? "0" + dateDebutRes.getMonthValue() : dateDebutRes.getMonthValue()) + "-" + (dateDebutRes.getDayOfMonth() < 10 ? "0" + dateDebutRes.getDayOfMonth() : dateDebutRes.getDayOfMonth()));
                        LocalDate dateFinCalcule = LocalDate.parse(dateDebutRes.getYear() + "-" + (dateDebutRes.getMonthValue() < 10 ? "0" + dateDebutRes.getMonthValue() : dateDebutRes.getMonthValue()) + "-" + DateUtil.getFinDuMois(dateDebutRes.getMonthValue(), dateDebutRes.getYear()));
                        detailReservationService.creerDetailReservation(reservation.getId().toString(), DateUtil.convertToDateViaInstant(dateDebutCalcule), DateUtil.convertToDateViaInstant(dateFinCalcule), typePrix, idSession);
                    } else if (i == dateFinRes.getMonthValue()) {
                        //dernier mois
                        LocalDate dateDebutCalcule = LocalDate.parse(dateFinRes.getYear() + "-" + (dateFinRes.getMonthValue() < 10 ? "0" + dateFinRes.getMonthValue() : dateFinRes.getMonthValue()) + "-" + "01");
                        LocalDate dateFinCalcule = LocalDate.parse(dateFinRes.getYear() + "-" + (dateFinRes.getMonthValue() < 10 ? "0" + dateFinRes.getMonthValue() : dateFinRes.getMonthValue()) + "-" + (dateFinRes.getDayOfMonth() < 10 ? "0" + dateFinRes.getDayOfMonth() : dateFinRes.getDayOfMonth()));
                        detailReservationService.creerDetailReservation(reservation.getId().toString(), DateUtil.convertToDateViaInstant(dateDebutCalcule), DateUtil.convertToDateViaInstant(dateFinCalcule), typePrix, idSession);
                    } else {
                        // mois lambda
                        LocalDate dateDebutCalcule = LocalDate.parse(dateDebutRes.getYear() + "-" + (i < 10 ? "0" + i : i) + "-" + "01");
                        LocalDate dateFinCalcule = LocalDate.parse(dateDebutRes.getYear() + "-" + (i < 10 ? "0" + i : i) + "-" + DateUtil.getFinDuMois(i, dateDebutRes.getYear()));
                        detailReservationService.creerDetailReservation(reservation.getId().toString(), DateUtil.convertToDateViaInstant(dateDebutCalcule), DateUtil.convertToDateViaInstant(dateFinCalcule), typePrix, idSession);
                    }
                }
            }

        }
    }

    @Override
    public List<Reservation> listAll() {
        return repository.findAll();
    }

    @Override
    public Reservation save(Reservation reservation) {

        return repository.save(reservation);
    }

    @Override
    public Reservation get(Long id) {

        Reservation reservation = repository.findById(id).orElse(null);
        if (reservation != null) {
            Date today = new Date();
            if (today.after(reservation.getDateDebut()) && today.before(reservation.getDateFin())) {
                reservation.setEnCours(true);
            } else {
                reservation.setEnCours(false);
            }
            List<DetailReservation> detailReservations = detailReservationService.getAllByIdReservation(reservation.getId().toString());
            reservation.setDetailReservations(detailReservations);
            return reservation;
        } else {
            return new Reservation();
        }
    }

    @Override
    public void delete(Long id) {

        Reservation entity = this.get(id);

        repository.delete(entity);
    }

    @Override
    public List<Reservation> getAllByPersonneAFacturerId(String idPersonneAFacturer) {
        return repository.findAllByIdPersonneFactureOrderByDateDebutDesc(idPersonneAFacturer);
    }

    @Override
    public List<Reservation> getAllChevalBoxReservations() {
        return repository.findAllByChevalBoxsIsNotNull();
    }

    @Override
    public List<Reservation> getAllChevalBoxReservationsByCriteria(String nomSite, Long mois, Long annee) {
        List<Reservation> reservations = repository.findAllByChevalBoxsIsNotNullAndChevalBoxsBoxEcurieSiteNomAndChevalBoxsBoxIsActif(nomSite, true);
        List<Reservation> result = new ArrayList<>();
        for (Reservation item : reservations) {
            Date today = new Date();
            if (today.after(item.getDateDebut()) && today.before(item.getDateFin())) {
                item.setEnCours(true);
            } else {
                item.setEnCours(false);
            }
            List<DetailReservation> detailReservations = detailReservationService.getAllByIdReservation(item.getId().toString());
            for (DetailReservation detailReservation : detailReservations) {
                String detailReservationMoisDebut = new SimpleDateFormat("M").format(detailReservation.getDateDebut());
                String detailReservationMoisFin = new SimpleDateFormat("M").format(detailReservation.getDateFin());
                String detailReservationAnneeDebut = new SimpleDateFormat("YYYY").format(detailReservation.getDateDebut());
                String detailReservationAnneeFin = new SimpleDateFormat("YYYY").format(detailReservation.getDateFin());
                if (detailReservationAnneeDebut.equals(annee.toString()) || detailReservationAnneeFin.equals(annee.toString())) {
                    if (detailReservationMoisDebut.equals(mois.toString()) || detailReservationMoisFin.equals(mois.toString())) {
                        item.setDetailReservations(Arrays.asList(detailReservation));
                        result.add(item);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<Reservation> getAllPersonneLitReservationsByCriteria(String nomSite, Long mois, Long annee) {
        List<Reservation> reservations = repository.findAllByPersonneLitsIsNotNullAndPersonneLitsLitChambreImmeubleSiteNomAndPersonneLitsLitIsActif(nomSite, true);
        List<Reservation> result = new ArrayList<>();
        for (Reservation item : reservations) {
            Date today = new Date();
            if (today.after(item.getDateDebut()) && today.before(item.getDateFin())) {
                item.setEnCours(true);
            } else {
                item.setEnCours(false);
            }
            List<DetailReservation> detailReservations = detailReservationService.getAllByIdReservation(item.getId().toString());
            for (DetailReservation detailReservation : detailReservations) {
                String detailReservationMoisDebut = new SimpleDateFormat("M").format(detailReservation.getDateDebut());
                String detailReservationMoisFin = new SimpleDateFormat("M").format(detailReservation.getDateFin());
                String detailReservationAnneeDebut = new SimpleDateFormat("YYYY").format(detailReservation.getDateDebut());
                String detailReservationAnneeFin = new SimpleDateFormat("YYYY").format(detailReservation.getDateFin());
                if (detailReservationAnneeDebut.equals(annee.toString()) || detailReservationAnneeFin.equals(annee.toString())) {
                    if (detailReservationMoisDebut.equals(mois.toString()) || detailReservationMoisFin.equals(mois.toString())) {
                        item.setDetailReservations(Arrays.asList(detailReservation));
                        result.add(item);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<Reservation> getAllPersonneLitReservations() {
        return repository.findAllByPersonneLitsIsNotNull();
    }

    @Override
    public List<Reservation> getAllPersonneLitReservationsByPersonneId(String id) {
        return repository.findAllByPersonneLitsIsNotNullAndPersonneLitsPersonneId(id);
    }

    @Override
    public List<Reservation> getAllChevalBoxReservationsByChevalId(Long id) {
        return repository.findAllByChevalBoxsIsNotNullAndChevalBoxsChevalId(id);
    }

    @Override
    public List<Reservation> getAllChevalPisteReservations() {
        return repository.findAllByChevalPistesIsNotNull();
    }

    @Override
    public List<Reservation> getAllChevalPisteReservationsByPisteId(Long id) {
        return repository.findAllByChevalPistesIsNotNullAndChevalPistesPisteId(id);
    }

    @Override
    public List<Reservation> getAllChevalPisteReservationsByCriteria(String nomSite, Long idPiste, Long mois, Long annee) {
        List<Reservation> reservations = repository.findAllByChevalPistesIsNotNull();

        List<Reservation> result = new ArrayList<>();
        for (Reservation item : reservations) {
            for (ChevalPiste chevalPiste : item.getChevalPistes()) {
                String chevalPisteMoisDebut = new SimpleDateFormat("M").format(chevalPiste.getDateDebut());
                String chevalPisteMoisFin = new SimpleDateFormat("M").format(chevalPiste.getDateFin());
                String chevalPisteAnneeDebut = new SimpleDateFormat("YYYY").format(chevalPiste.getDateDebut());
                String chevalPisteAnneeFin = new SimpleDateFormat("YYYY").format(chevalPiste.getDateFin());
                if (chevalPisteAnneeDebut.equals(annee.toString()) || chevalPisteAnneeFin.equals(annee.toString())) {
                    if (chevalPisteMoisDebut.equals(mois.toString()) || chevalPisteMoisFin.equals(mois.toString())) {
                        result.add(item);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<Reservation> getAllChevalPisteReservationsByDate(Date date, Long heure, Long idPiste) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        List<Reservation> reservations = repository.findAllByChevalPistesIsNotNullAndChevalPistesPisteId(idPiste);
        List<Reservation> result = new ArrayList<>();
        for (Reservation item : reservations) {
            for (ChevalPiste chevalPiste : item.getChevalPistes()) {
                if (formatter.parse(formatter.format(date)).equals(formatter.parse(formatter.format(chevalPiste.getDateDebut())))) {
                    System.out.println("date Debut résevation : " + SharedUtil.getIdLong(new SimpleDateFormat("HH").format(chevalPiste.getDateDebut())));
                    if ((SharedUtil.getIdLong(new SimpleDateFormat("HH").format(chevalPiste.getDateDebut())).equals(heure))) {
                        result.add(item);
                    }
                    if (SharedUtil.getIdLong(new SimpleDateFormat("HH").format(chevalPiste.getDateDebut())) < heure) {
                        if (SharedUtil.getIdLong(new SimpleDateFormat("HH").format(chevalPiste.getDateFin())) >= heure) {
                            result.add(item);
                        }
                    }
                }
            }
        }

        return result;
    }

    @Override
    public List<Reservation> getAllChevalPisteReservationsByChevalId(Long id) {
        return repository.findAllByChevalPistesIsNotNullAndChevalPistesChevalId(id);
    }

    @Override
    public Long getReservationPrixEnCours(Long idReservation) {
        return null;
    }

    @Override
    public List<Reservation> getAllByChevalIdEnCours(Long id) {
        List<Reservation> reservations = repository.findAllByChevalBoxsChevalIdOrChevalPistesChevalId(id, id);
        List<Reservation> result = new ArrayList<>();
        for (Reservation item : reservations) {
            if (item.getDateFin().after(new Date())) {
                result.add(item);
            }
        }
        return result;
    }

    @Override
    public List<Reservation> getReservationsEnCours() {
        List<Reservation> reservations = repository.findAll();
        List<Reservation> result = new ArrayList<>();
        for (Reservation item : reservations) {
            if (item.getDateFin().after(new Date())) {
                result.add(item);
            }
        }
        return result;
    }

    @Override
    public List<Reservation> getReservationsChevalBoxEtChevalPisteEnCours() {
        List<Reservation> reservations = repository.findAll();
        List<Reservation> result = new ArrayList<>();
        for (Reservation item : reservations) {
            if (item.getDateFin().after(new Date())) {
                result.add(item);
            }
        }
        return result;
    }

    @Override
    public List<Reservation> getAllByPersonneIdEnCours(String id) {
        List<Reservation> reservations = repository.findAllByPersonneLitsPersonneId(id);
        List<Reservation> result = new ArrayList<>();
        for (Reservation item : reservations) {
            if (item.getDateFin().after(new Date())) {
                result.add(item);
            }
        }
        return result;
    }

    @Override
    public List<Reservation> GetAllByPersonneIdNonFacture(String id) {
        List<Reservation> reservations = repository.findAllByIdPersonneFactureOrderByDateDebutDesc(id);
        List<Reservation> result = new ArrayList<>();
        for (Reservation item : reservations) {
            result.add(item);
        }
        return result;
    }

    @Override
    public List<Reservation> getAllByChevalId(Long id) {
        return repository.findAllByChevalBoxsChevalIdOrChevalPistesChevalId(id, id);
    }

    @Override
    public List<Reservation> getAllByPersonneId(String id) {
        return repository.findAllByPersonneLitsPersonneId(id);
    }

    @Override
    public Facture facturer(List<String> detailReservationIds) throws DocumentException, MessagingException, IOException {
        List<DetailReservation> detailReservations = new ArrayList<>();
//        String codeModePaiement = detailReservationIds.get(0);
        String operateur = detailReservationIds.get(0);
        String idSession = detailReservationIds.get(1);
        String idPersonneAFacturer = detailReservationIds.get(2);
        List<String> reservationIdsClean = detailReservationIds;
        reservationIdsClean.remove(0);
        reservationIdsClean.remove(0);
        reservationIdsClean.remove(0);
//        reservationIdsClean.remove(0);
        for (String item : reservationIdsClean) {
            detailReservations.add(detailReservationService.get(new Long(item)));
        }
        int nombreForfaitaire = 0;
        for (DetailReservation detailReservation : detailReservations) {
            Reservation reservation = reservationService.get(new Long(detailReservation.getIdReservation()));
            if (reservation.getCodeTypePrix().equals("F")) {
                nombreForfaitaire++;
            }
        }
        if (nombreForfaitaire != 0 && nombreForfaitaire != detailReservations.size()) {
            throw new CustomException("A", "Il est impossible de facturer des réservations avec un type de prix différent");
        }
        Facture facture = Facture.builder()
                .dateFacture(new Date())
//                .codeModePaiement(codeModePaiement)
                .idPersonneAFacturer(idPersonneAFacturer)
                .codeStatutFacture("ECP")
                .idOperateur(operateur)
                .idSession(idSession)
                .build();

        facture = factureService.save(facture);

        for (DetailReservation item : detailReservations) {
            item.setFacture(facture);
            item.setCodeStatutDetailReservation("ECP");
            detailReservationService.save(item);

        }

        if (envoyerMail.equals("1")) {
            emailService.envoyerMailFacture(facture, idSession);
        }

        return facture;
    }

    @Override
    public List<Reservation> getAllByChevalPistePisteId(Long id) {
        return repository.findAllByChevalPistesPisteId(id);
    }

    @Override
    public List<Reservation> getAllByChevalBoxBoxId(Long id) {
        return repository.findAllByChevalBoxsBoxId(id);
    }

    @Override
    public List<Reservation> getAllByPersonneLitLitId(Long id) {
        return repository.findAllByPersonneLitsLitId(id);
    }

    @Override
    public List<Reservation> getAllByPersonneLitLitChambreId(Long id) {
        return repository.findAllByPersonneLitsLitChambreId(id);
    }

    @Override
    public List<Reservation> getAllByPersonneLitLitChambreImmeubleId(Long id) {
        return repository.findAllByPersonneLitsLitChambreImmeubleId(id);
    }

    @Override
    public Reservation getByChevalBoxId(Long id) {
        return repository.findFirstByChevalBoxsId(id);
    }

    @Override
    public Reservation getByPersonneLitId(Long id) {
        return repository.findFirstByPersonneLitsId(id);
    }

    @Override
        public Boolean reservationDejaFacturee(Long id) {
        Boolean facture = false;
        List<DetailReservation> detailReservations = detailReservationService.getAllByIdReservation(id.toString());
        for (DetailReservation detailReservation : detailReservations) {
            if (detailReservation.getFacture() != null) {
                facture = true;
                break;
            }
        }
        return facture;
    }
}
