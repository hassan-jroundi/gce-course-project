package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.Utils.DateUtil;
import ma.sorec.gcecourse.Utils.SharedUtil;
import ma.sorec.gcecourse.controller.mohr.PersonneMController;
import ma.sorec.gcecourse.data.*;
import ma.sorec.gcecourse.data.mohr.PersonneM;
import ma.sorec.gcecourse.repository.FactureRepository;
import ma.sorec.gcecourse.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class FactureServiceImpl implements FactureService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FactureRepository repository;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private StatutFactureService statutFactureService;

    @Autowired
    private ModePaiementService modePaiementService;

    @Autowired
    private DetailReservationService detailReservationService;

    @Autowired
    private RelanceFacturationService relanceFacturationService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PersonneMController personneMController;

    @Autowired
    private UtilisateurService utilisateurService;

    @Override
    public List<Facture> listAll() {
        return repository.findAll();
    }

    @Override
    public Facture save(Facture facture) {

        facture.setDateCreation(new Date());
        facture.setUserCreation(utilisateurSessionService.getByIdSession(facture.getIdSession()).getUtilisateur().getId());

        return repository.save(facture);
    }

    @Override
    public Facture get(Long id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {

        Facture entity = this.get(id);

        repository.delete(entity);
    }

    @Override
    public List<Facture> getAllByPersonneId(String id) {
        List<Facture> factures = repository.findAllByIdPersonneAFacturer(id);
        for (Facture facture : factures) {
            facture.setLibelleStatut(statutFactureService.get(facture.getCodeStatutFacture()).getDesignation());
            facture.setNomOperateur(SharedUtil.convertToTitleCaseIteratingChars(utilisateurService.get(new Long(facture.getIdOperateur())).getPersonne().getNom()) +
                    " " + SharedUtil.convertToTitleCaseIteratingChars(utilisateurService.get(new Long(facture.getIdOperateur())).getPersonne().getPrenom()));
            if (facture.getCodeModePaiement() != null) {
                facture.setLibelleModePaiement(modePaiementService.get(facture.getCodeModePaiement()).getDesignation());
            }
        }
        return factures;
    }

    @Override
    public Double getMontantHTFacture(Long id) {
        return null;
    }

    @Override
    public Facture mettreEnPaye(Long id, String idSession, String codeModePaiement) {
        Facture facture = this.get(id);
        facture.setDatePaiement(new Date());
        facture.setCodeModePaiement(codeModePaiement);
        facture.setCodeStatutFacture("P");
        facture.setDateModification(new Date());
        facture.setUserModification(utilisateurSessionService.getByIdSession(idSession).getUtilisateur().getId());
        facture.setIdSession(idSession);
        facture = this.save(facture);
        List<DetailReservation> detailReservations = detailReservationService.getAllByIdFacture(facture.getId());
        for (DetailReservation detailReservation : detailReservations) {
            detailReservation.setCodeStatutDetailReservation("P");
            detailReservation.setDateModification(new Date());
            detailReservation.setUserModification(utilisateurSessionService.getByIdSession(idSession).getUtilisateur().getId());
            detailReservation = detailReservationService.save(detailReservation);
            Reservation reservation = reservationService.get(new Long(detailReservation.getIdReservation()));
            List<DetailReservation> detailReservationsReservation = detailReservationService.getAllByIdReservation(reservation.getId().toString());
            int count = 0;
            for (DetailReservation detailReservation1 : detailReservationsReservation) {
                if (detailReservation1.getCodeStatutDetailReservation().equals("P")) {
                    count++;
                }
            }
            if (count > 0 && count < detailReservationsReservation.size()) {
                reservation.setCodeStatutReservation("PP");
            }
            if (count == detailReservationsReservation.size()) {
                reservation.setCodeStatutReservation("P");
            }
            reservation = reservationService.save(reservation);
        }
        return facture;
    }

    @Override
    public void relanceFactures() {

        List<Facture> factures = repository.findAllByCodeStatutFacture("ECP");

        for (Facture facture : factures) {
            RelanceFacturation relanceFacturation = relanceFacturationService.getByIdFacture(facture.getId().toString());
            if (relanceFacturation != null) {
                logger.info("Relance déjà effectuée pour la facture numéro {} à la date {}", facture.getId(), DateUtil.convertToLocalDateViaInstant(relanceFacturation.getDateCreation()));
            } else {
                RelanceFacturation entity = RelanceFacturation.builder()
                        .dateCreation(new Date())
                        .idFacture(facture.getId().toString())
                        .build();
                relanceFacturationService.save(entity);
                PersonneM personne = personneMController.get(facture.getIdPersonneAFacturer());
                if (personne.getEmail().equals("")) {
                    logger.info("Relance impossible pour la facture numéro {}. Email introuvable pour la personne à facturer numéro {}.", facture.getId(), personne.getId());
                } else {
                    // Envoi d'un mail de relance avec la facture comme attachement

                }
            }
        }

    }

    @Override
    public List<Facture> getAllByIdOperateur(String idOperateur) {
        return repository.findAllByIdOperateur(idOperateur);
    }

    @Override
    public List<Facture> getAllEnCoursDePaiement() {
        return repository.findAllByDateFactureIsNotNullAndDatePaiementIsNull();
    }
}
