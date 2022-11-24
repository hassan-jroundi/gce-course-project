package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.Utils.DateUtil;
import ma.sorec.gcecourse.data.*;
import ma.sorec.gcecourse.data.mohr.PersonneM;
import ma.sorec.gcecourse.repository.DetailReservationRepository;
import ma.sorec.gcecourse.repository.mohr.PersonneMRepository;
import ma.sorec.gcecourse.service.DetailReservationService;
import ma.sorec.gcecourse.service.ReservationService;
import ma.sorec.gcecourse.service.StatutDetailReservationService;
import ma.sorec.gcecourse.service.UtilisateurSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class DetailReservationServiceImpl implements DetailReservationService {

    @Autowired
    private DetailReservationRepository repository;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private PersonneMRepository personneMRepository;

    @Autowired
    private StatutDetailReservationService statutDetailReservationService;

    @Override
    public List<DetailReservation> listAll() {
        return repository.findAll();
    }

    @Override
    public DetailReservation save(DetailReservation detailReservation) {

        detailReservation.setDateCreation(new Date());
//        detailReservation.setUserCreation(utilisateurSessionService.getByIdSession(detailReservation.getIdSession()).getUtilisateur().getId());

        return repository.save(detailReservation);
    }

    @Override
    public DetailReservation get(Long id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {

        DetailReservation entity = this.get(id);

        repository.delete(entity);
    }

    @Override
    public List<DetailReservation> getAllByIdReservation(String idReservation) {
        return repository.findAllByIdReservationOrderByDateDebutAsc(idReservation);
    }

    @Override
    public DetailReservation creerDetailReservation(String idReservation, Date dateDebut, Date dateFin, String typePrix, String idSession) {

        DetailReservation detailReservation = DetailReservation.builder()
                .idReservation(idReservation)
                .codeStatutDetailReservation("NP")
                .dateDebut(dateDebut)
                .dateFin(dateFin)
                .userCreation(utilisateurSessionService.getByIdSession(idSession).getUtilisateur().getId())
                .dateCreation(new Date())
                .build();

        detailReservation = this.save(detailReservation);

        return detailReservation;
    }

    @Override
    public List<DetailReservation> getAllDetailReservationByDate(LocalDate date) {
        int finDuMois = DateUtil.getFinDuMois(date.getMonthValue(), date.getYear());
        LocalDate dateDebut = LocalDate.of(date.getYear(), date.getMonthValue(), 1);
        LocalDate dateFin = LocalDate.of(date.getYear(), date.getMonthValue(), finDuMois);
        List<DetailReservation> entities = repository.findAllByDateDebutGreaterThanEqualAndDateFinLessThanEqualAndIdReservationIsNotNull(DateUtil.convertToDateViaInstant(dateDebut), DateUtil.convertToDateViaInstant(dateFin));
        for (DetailReservation detailReservation : entities) {
            detailReservation.setLibelleStatut(statutDetailReservationService.get(detailReservation.getCodeStatutDetailReservation()).getDesignation());
            Reservation reservation = reservationService.get(new Long(detailReservation.getIdReservation()));
            detailReservation.setIdPersonneAFacturer(reservation.getIdPersonneFacture());
            PersonneM personneM = personneMRepository.getById(reservation.getIdPersonneFacture());
            if (!(reservation.getChevalPistes().isEmpty())) {
                detailReservation.setLibelleTypeReservation("Entrainement Cheval");
                for (ChevalPiste chevalPiste : reservation.getChevalPistes()) {
                    if (chevalPiste.getCheval() != null) {
                        detailReservation.setNomConcerne(chevalPiste.getCheval().getNom());
                    }
                    detailReservation.setNomPiste(chevalPiste.getPiste().getNom());
                }
            }
            if (!(reservation.getPersonneLits().isEmpty())) {
                detailReservation.setLibelleTypeReservation("Hébergement Personne");
                    for (PersonneLit personneLit : reservation.getPersonneLits()) {
                    if (personneLit.getPersonne() != null) {
                        detailReservation.setNomConcerne(personneLit.getPersonne().getNom() + " " + personneLit.getPersonne().getPrenom());
                    }
                    detailReservation.setNomLit(personneLit.getLit().getNom());
                }
            }
            if (!(reservation.getChevalBoxs().isEmpty())) {
                detailReservation.setLibelleTypeReservation("Hébergement Cheval");
                for (ChevalBox chevalBox : reservation.getChevalBoxs()) {
                    if (chevalBox.getCheval() != null) {
                        detailReservation.setNomConcerne(chevalBox.getCheval().getNom());
                    }
                    detailReservation.setNomBox(chevalBox.getBox().getNom());
                }
            }
            if (personneM.getCodeNaturePersonne().equals("P")) {
                detailReservation.setNomPersonneAFacturer(personneM.getNom() + " " + personneM.getPrenom());
            }
            if (personneM.getCodeNaturePersonne().equals("M")) {
                detailReservation.setNomPersonneAFacturer(personneM.getRaisonSociale());
            }
            if (personneM.getCodeNaturePersonne().equals("A")) {
                detailReservation.setNomPersonneAFacturer(personneM.getDesignation());
            }
        }
        return entities;
    }

    @Override
    public List<DetailReservation> getAllByIdFacture(Long idFacture) {
        List<DetailReservation> detailReservations = repository.findAllByFactureId(idFacture);
        for (DetailReservation detailReservation : detailReservations) {
            Reservation reservation = reservationService.get(new Long(detailReservation.getIdReservation()));
            if (!(reservation.getChevalBoxs().isEmpty())) {
                detailReservation.setLibelleTypeReservation("Hébergement Cheval");
                for (ChevalBox chevalBox : reservation.getChevalBoxs()) {
                    if (chevalBox.getCheval() != null) {
                        detailReservation.setNomConcerne(chevalBox.getCheval().getNom());
                    }
                }
            }
            if (!(reservation.getPersonneLits().isEmpty())) {
                detailReservation.setLibelleTypeReservation("Hébergement Personne");
                for (PersonneLit personneLit : reservation.getPersonneLits()) {
                    if (personneLit.getPersonne() != null) {
                        detailReservation.setNomConcerne(personneLit.getPersonne().getNom() + " " + personneLit.getPersonne().getPrenom());
                    }
                }
            }
            if (!(reservation.getChevalPistes().isEmpty())) {
                detailReservation.setLibelleTypeReservation("Entrainement Cheval");
                for (ChevalPiste chevalPiste : reservation.getChevalPistes()) {
                    if (chevalPiste.getCheval() != null) {
                        detailReservation.setNomConcerne(chevalPiste.getCheval().getNom());
                    }
                }
            }
        }
        return detailReservations;
    }
}
