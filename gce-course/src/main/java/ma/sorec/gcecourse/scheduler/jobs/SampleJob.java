package ma.sorec.gcecourse.scheduler.jobs;

import com.itextpdf.text.DocumentException;
import ma.sorec.gcecourse.Utils.SharedUtil;
import ma.sorec.gcecourse.controller.ChevalController;
import ma.sorec.gcecourse.data.*;
import ma.sorec.gcecourse.data.mohr.DetailDeclarationEffectif;
import ma.sorec.gcecourse.repository.GroupeUtilisateurRepository;
import ma.sorec.gcecourse.repository.NotificationRepository;
import ma.sorec.gcecourse.repository.UtilisateurRepository;
import ma.sorec.gcecourse.repository.mohr.DetailDeclarationEffectifRepository;
import ma.sorec.gcecourse.service.*;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Transactional
public class SampleJob implements Job {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private AtomicInteger count = new AtomicInteger();

    public static final long EXECUTION_TIME = 20000L;

    @Autowired
    ChevalController chevalController;

    @Autowired
    FactureService factureService;

    @Autowired
    ChevalService chevalService;

    @Autowired
    ReservationService reservationService;

    @Autowired
    DetailDeclarationEffectifRepository detailDeclarationEffectifRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    GroupeUtilisateurRepository groupeUtilisateurRepository;

    @Autowired
    UtilisateurRepository utilisateurRepository;

    @Autowired
    ChevalBoxService chevalBoxService;

    @Autowired
    EmailService emailService;

    @Autowired
    ProfilService profilService;

    @Autowired
    NotificationProfilService notificationProfilService;

    @Autowired
    ChevalReservationEtatService chevalReservationEtatService;

    @Autowired
    RelanceFacturationService relanceFacturationService;

    @Autowired
    private ChevalPisteService chevalPisteService;

    @Value("${envoyer.mail}")
    String envoyerMail;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        long startTime = System.currentTimeMillis();

        logger.info("Début du job");

        try {
            Thread.sleep(EXECUTION_TIME);
            // Mise à jour des statuts des chevaux avec une réservation en cours
            List<Reservation> reservations = reservationService.getReservationsChevalBoxEtChevalPisteEnCours();
            for (Reservation reservation : reservations) {

                // ChevalBox
                ChevalBox chevalBox = chevalBoxService.getChevalBoxByReservationId(reservation.getId());
                if (chevalBox != null) {
                    DetailDeclarationEffectif detailDeclarationEffectif = detailDeclarationEffectifRepository.findFirstByIdChevalOrderByDateCreationDesc(chevalBox.getCheval().getId());
                    ChevalReservationEtat chevalReservationEtat = chevalReservationEtatService.getByReservationId(reservation.getId());
                    if (chevalReservationEtat.getCodeEtat().equals("AENTR")) {
                        if (detailDeclarationEffectif.getCodeStatutCheval().equals("HENTR")) {
                            chevalReservationEtat.setCodeEtat("HENTR");
                            chevalReservationEtatService.save(chevalReservationEtat);
                            Notification notification = Notification.builder()
                                    .dateEnvoi(new Date())
                                    .description("Le cheval " + chevalBox.getCheval().getNom() + " avec le numéro ESRIMA : " + chevalBox.getCheval().getNumeroEsrima() + " a été déclaré Hors Entrainement.")
                                    .type("SE")
                                    .cheval(chevalBox.getCheval())
                                    .build();
                            notification = notificationRepository.save(notification);
                            NotificationProfil notificationProfilGES = NotificationProfil.builder()
                                    .notification(notification)
                                    .profil(profilService.getByCode("GES"))
                                    .build();
                            notificationProfilService.save(notificationProfilGES);

                            NotificationProfil notificationProfilADM = NotificationProfil.builder()
                                    .notification(notification)
                                    .profil(profilService.getByCode("ADM"))
                                    .build();
                            notificationProfilService.save(notificationProfilADM);
                        }
                    }
                }
                // ChevalPiste
//                ChevalPiste chevalPiste = chevalPisteService.getChevalPisteByReservationId(reservation.getId());
//                DetailDeclarationEffectif detailDeclarationEffectifCP = detailDeclarationEffectifRepository.findFirstByIdChevalOrderByDateCreationDesc(chevalPiste.getCheval().getId());
//                ChevalReservationEtat chevalReservationEtatCP = chevalReservationEtatService.getByReservationId(reservation.getId());
//                if (chevalReservationEtatCP.getCodeEtat().equals("AENTR")) {
//                    if (detailDeclarationEffectifCP.getCodeStatutCheval().equals("HENTR")) {
//                        chevalReservationEtatCP.setCodeEtat("HENTR");
//                        chevalReservationEtatService.save(chevalReservationEtatCP);
//                        Notification notification = Notification.builder()
//                                .dateEnvoi(new Date())
//                                .description("Le cheval " + chevalPiste.getCheval().getNom() + " avec le numéro ESRIMA : " + chevalPiste.getCheval().getNumeroEsrima() + " a été déclaré Hors Entrainement.")
//                                .type("SE")
//                                .cheval(chevalPiste.getCheval())
//                                .build();
//                        notification = notificationRepository.save(notification);
//                        NotificationProfil notificationProfilGES = NotificationProfil.builder()
//                                .notification(notification)
//                                .profil(profilService.getByCode("GES"))
//                                .build();
//                        notificationProfilService.save(notificationProfilGES);
//
//                        NotificationProfil notificationProfilADM = NotificationProfil.builder()
//                                .notification(notification)
//                                .profil(profilService.getByCode("ADM"))
//                                .build();
//                        notificationProfilService.save(notificationProfilADM);
//                    }
//                }
            }

            // Envoi d'un mail de relance après 15 jours de non paiement d'une facture
            Thread.sleep(EXECUTION_TIME);
            List<Facture> factures = factureService.getAllEnCoursDePaiement();
            for (Facture facture : factures) {
                if (SharedUtil.getDifferenceDays(facture.getDateFacture(), new Date()) > 15) {
                    RelanceFacturation relanceFacturation = relanceFacturationService.getByIdFacture(facture.getId().toString());
                    if (relanceFacturation == null) {
                        // Envoi du mail de relance
                        if (envoyerMail.equals("1")) {
                            emailService.envoyerMailDeRelance(facture, "");
                        }
                    }
                }
            }

        } catch (InterruptedException | IOException | DocumentException | MessagingException e) {
            logger.error("Erreur lors de l'exécution du Job", e);
        } finally {
            count.incrementAndGet();
            logger.info("Job terminé");
            long endTime = System.currentTimeMillis();
            System.out.println("Temps d'exécution " + (endTime - startTime) + " milliseconds");
        }
        logger.info("Fin du job");

    }
}
