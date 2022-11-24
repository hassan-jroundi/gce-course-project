package ma.sorec.gcecourse.controller.mohr;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.sorec.gcecourse.data.*;
import ma.sorec.gcecourse.data.mohr.PersonneChevalM;
import ma.sorec.gcecourse.data.mohr.PersonneM;
import ma.sorec.gcecourse.exceptions.ObjectNotFoundException;
import ma.sorec.gcecourse.repository.mohr.PersonneChevalMRepository;
import ma.sorec.gcecourse.repository.mohr.PersonneMRepository;
import ma.sorec.gcecourse.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/mohr-personnes")
@Api("MOHR Personne Controller")
public class PersonneMController {

    @Autowired
    private PersonneMRepository repository;

    @Autowired
    private PersonneChevalMRepository personneChevalMRepository;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private DetailReservationService detailReservationService;

    @Autowired
    private StatutReservationService statutReservationService;

    @Autowired
    private StatutDetailReservationService statutDetailReservationService;

    @Autowired
    private ChevalService chevalService;

    @Autowired
    private FactureService factureService;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées", nickname = "getAllMohrPersonnes")
    public List<PersonneM> getAll() {
        return repository.findAll();
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie de l'entité identifiée par son id", nickname = "getOneMohrPersonne")
    public PersonneM get(@PathVariable("id") String id) throws ObjectNotFoundException {

        PersonneM entity = repository.findById(id).get();
        if (entity != null) {
            return entity;
        } else {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
    }

    @GetMapping(value = "/search/relation", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<PersonneM> test(@RequestParam(required = false, name = "idCheval", defaultValue = "") String idCheval) {

        List<PersonneM> result = new ArrayList<>();
        PersonneChevalM entraineurRelation = null;
        PersonneChevalM proprietaireRelation = null;
        List<PersonneChevalM> entraineurRelations = personneChevalMRepository.findAllByIdChevalAndCodeNatureRelation(idCheval, "ENTRA");
        for (PersonneChevalM item : entraineurRelations) {
            if (item.getDateFin() == null || item.getDateFin().after(new Date())) {
                entraineurRelation = item;
            }
        }

        List<PersonneChevalM> proprietaireRelations = personneChevalMRepository.findAllByIdChevalAndCodeNatureRelation(idCheval, "PROPR");
        for (PersonneChevalM item : proprietaireRelations) {
            if (item.getDateFin() == null || item.getDateFin().after(new Date())) {
                proprietaireRelation = item;
            }
        }

        if (entraineurRelation != null && proprietaireRelation != null) {
            if (entraineurRelation.getIdPersonne().equals(proprietaireRelation.getIdPersonne())) {
                PersonneM personneM = this.get(proprietaireRelation.getIdPersonne());
                result.add(personneM);
            }
            if (!(entraineurRelation.getIdPersonne().equals(proprietaireRelation.getIdPersonne()))) {
                PersonneM proprio = this.get(proprietaireRelation.getIdPersonne());
                proprio.setCodeNatureRelation("PROPR");
                result.add(proprio);
                PersonneM entrai = this.get(entraineurRelation.getIdPersonne());
                entrai.setCodeNatureRelation("ENTRA");
                result.add(entrai);
            }
        }
        return result;
    }

    @GetMapping(value = "/search", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<PersonneM> getListByCriteria(@RequestParam(required = false, name = "numeroPieceIdentite", defaultValue = "") String numeroPieceIdentite,
                                             @RequestParam(required = false, name = "raisonSociale", defaultValue = "") String raisonSociale,
                                             @RequestParam(required = false, name = "designation", defaultValue = "") String designation,
                                             @RequestParam(required = false, name = "codeNaturePersonne", defaultValue = "") String codeNaturePersonne) {

        List<PersonneM> entities = new ArrayList<>();

        if (codeNaturePersonne.equals("P")) {
            entities = repository.findAllByNumeroPieceIdentiteContainingIgnoreCase(numeroPieceIdentite);
        }

        if (codeNaturePersonne.equals("A")) {
            entities = repository.findAllByDesignationContainingIgnoreCase(designation);
        }

        if (codeNaturePersonne.equals("M")) {
            entities = repository.findAllByRaisonSocialeContainingIgnoreCase(raisonSociale);
        }

        return entities;
    }

    @GetMapping(value = "/search2", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<PersonneM> getListByCriteria2(@RequestParam(required = false, name = "numeroPieceIdentite", defaultValue = "") String numeroPieceIdentite,
                                              @RequestParam(required = false, name = "nom", defaultValue = "") String nom,
                                              @RequestParam(required = false, name = "prenom", defaultValue = "") String prenom,
                                              @RequestParam(required = false, name = "designation", defaultValue = "") String designation,
                                              @RequestParam(required = false, name = "raisonSociale", defaultValue = "") String raisonSociale) {

        List<PersonneM> personnes = new ArrayList<>();
        if (!(designation.equals(""))) {
            personnes = repository.findAllByDesignationContainingIgnoreCase(designation);
        } else if (!(raisonSociale.equals(""))) {
            personnes = repository.findAllByRaisonSocialeContainingIgnoreCase(raisonSociale);
        } else {
            personnes = repository.findAllByNumeroPieceIdentiteContainingIgnoreCaseAndNomContainingIgnoreCaseAndPrenomContainingIgnoreCase(numeroPieceIdentite, nom, prenom);
        }
        if (personnes.size() == 1) {
            for (PersonneM personneM : personnes) {
                personneM.setReservations(reservationService.getAllByPersonneAFacturerId(personneM.getId()));
                for (Reservation reservation : personneM.getReservations()) {
                    String codeStatutReservation = statutReservationService.get(reservation.getCodeStatutReservation()).getDesignation();
                    reservation.setLibelleStatut(codeStatutReservation);
                    List<DetailReservation> detailReservations = detailReservationService.getAllByIdReservation(reservation.getId().toString());
                    reservation.setDetailReservations(detailReservations);
                    for (DetailReservation detailReservation : reservation.getDetailReservations()) {
                        String codeStatutDetailReservation = statutDetailReservationService.get(detailReservation.getCodeStatutDetailReservation()).getDesignation();
                        detailReservation.setLibelleStatut(codeStatutDetailReservation);
                        detailReservation.setReservation(new Reservation());
                        if (detailReservation.getFacture() != null) {
                            detailReservation.setIdFacture(detailReservation.getFacture().getId().toString());
                        }
                    }
                }
                for (Reservation reservation : personneM.getReservations()) {
                    for (ChevalBox chevalBox : reservation.getChevalBoxs()) {
                        chevalBox.setReservation(new Reservation());
                    }
                    for (PersonneLit personneLit : reservation.getPersonneLits()) {
                        personneLit.setReservation(new Reservation());
                    }
                    for (ChevalPiste chevalPiste : reservation.getChevalPistes()) {
                        chevalPiste.setReservation(new Reservation());
                    }
                }
            }
        }
        return personnes;
    }

    @GetMapping(value = "/personne/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public PersonneM getPersonneInfos(@PathVariable("id") String id) {

        PersonneM personne = this.get(id);
        personne.setReservations(reservationService.getAllByPersonneAFacturerId(personne.getId()));
        for (Reservation reservation : personne.getReservations()) {
            String codeStatutReservation = statutReservationService.get(reservation.getCodeStatutReservation()).getDesignation();
            reservation.setLibelleStatut(codeStatutReservation);
            List<DetailReservation> detailReservations = detailReservationService.getAllByIdReservation(reservation.getId().toString());
            reservation.setDetailReservations(detailReservations);
            for (DetailReservation detailReservation : reservation.getDetailReservations()) {
                String codeStatutDetailReservation = statutDetailReservationService.get(detailReservation.getCodeStatutDetailReservation()).getDesignation();
                detailReservation.setLibelleStatut(codeStatutDetailReservation);
                detailReservation.setReservation(new Reservation());
                if (detailReservation.getFacture() != null) {
                    detailReservation.setIdFacture(detailReservation.getFacture().getId().toString());
                }
            }
        }
        for (Reservation reservation : personne.getReservations()) {
            for (ChevalBox chevalBox : reservation.getChevalBoxs()) {
                chevalBox.setReservation(new Reservation());
            }
            for (PersonneLit personneLit : reservation.getPersonneLits()) {
                personneLit.setReservation(new Reservation());
            }
            for (ChevalPiste chevalPiste : reservation.getChevalPistes()) {
                chevalPiste.setReservation(new Reservation());
            }
        }
        return personne;
    }

    @GetMapping(value = "/proprietaire/cheval/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public PersonneM getProprietaireCheval(@PathVariable("id") String id) {
        PersonneChevalM proprietaireRelation = personneChevalMRepository.findFirstByIdChevalAndCodeNatureRelationAndDateFinIsNull(id, "PROPR");
        PersonneM personneM = this.get(proprietaireRelation.getIdPersonne());
        return personneM;
    }
}
