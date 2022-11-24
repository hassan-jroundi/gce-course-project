package ma.sorec.gcecourse.controller;

import com.itextpdf.text.DocumentException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.sorec.gcecourse.Utils.SharedUtil;
import ma.sorec.gcecourse.data.Reservation;
import ma.sorec.gcecourse.dto.FactureDTO;
import ma.sorec.gcecourse.dto.ReservationDTO;
import ma.sorec.gcecourse.exceptions.InternalServerException;
import ma.sorec.gcecourse.exceptions.ObjectNotFoundException;
import ma.sorec.gcecourse.mapper.FactureMapper;
import ma.sorec.gcecourse.mapper.ReservationMapper;
import ma.sorec.gcecourse.repository.ChevalRepository;
import ma.sorec.gcecourse.repository.PersonneRepository;
import ma.sorec.gcecourse.repository.ReservationRepository;
import ma.sorec.gcecourse.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/reservations")
@Api("Reservation Controller")
public class ReservationController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ChevalRepository chevalRepository;

    @Autowired
    PersonneRepository personneRepository;

    @Autowired
    ReservationRepository repository;

    @Autowired
    ReservationService service;

    @Autowired
    BoxController boxController;

    @Autowired
    BoxService boxService;

    @Autowired
    ChevalBoxController chevalBoxController;

    @Autowired
    ChevalBoxService chevalBoxService;

    @Autowired
    ChevalController chevalController;

    @Autowired
    ChevalService chevalService;

    @Autowired
    PersonneService personneService;

    @Autowired
    FactureService factureService;

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie de l'entité identifiée par son id")
    public ReservationDTO get(@PathVariable("id") String id) throws ObjectNotFoundException {

        Long idL = SharedUtil.getIdLong(id);
        Reservation entity = service.get(idL);
        if (entity != null) {
            return ReservationMapper.INSTANCE.entityToDto(entity);
        } else {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées")
    public List<ReservationDTO> getAll() {
        return ReservationMapper.INSTANCE.entitiesToDTOs(service.listAll());
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Enregistre une nouvelle'entité")
    public ReservationDTO save(@RequestBody ReservationDTO dto) throws InternalServerException {

        try {
            Reservation entity = service.save(ReservationMapper.INSTANCE.dtoToEntity(dto));
            return ReservationMapper.INSTANCE.entityToDto(entity);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new InternalServerException("IE", "Erreur lors de la création de l'objet");
        }
    }

    @GetMapping(value = "creer", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Crée une réservation")
    public ReservationDTO creerReservation(@RequestParam(required = false, name = "dateDebut") Date dateDebut,
                                           @RequestParam(required = false, name = "dateFin") Date dateFin,
                                           @RequestParam(required = false, name = "idBox", defaultValue = "") String idBox,
                                           @RequestParam(required = false, name = "idPiste", defaultValue = "") String idPiste,
                                           @RequestParam(required = false, name = "idLit", defaultValue = "") String idLit,
                                           @RequestParam(required = false, name = "idPersonne", defaultValue = "") String idPersonne,
                                           @RequestParam(required = false, name = "idPersonneAFacturer", defaultValue = "") String idPersonneAFacturer,
                                           @RequestParam(required = false, name = "idCheval", defaultValue = "") String idCheval,
                                           @RequestParam(required = false, name = "typePrix", defaultValue = "") String typePrix,
                                           @RequestParam(required = false, name = "typeReservation", defaultValue = "") String typeReservation,
                                           @RequestParam(required = false, name = "idSession", defaultValue = "") String idSession) throws InternalServerException {
        return ReservationMapper.INSTANCE.entityToDto(service.creerReservation(dateDebut, dateFin, SharedUtil.getIdLong(idPersonneAFacturer), SharedUtil.getIdLong(idPersonne), SharedUtil.getIdLong(idBox), SharedUtil.getIdLong(idPiste), SharedUtil.getIdLong(idLit), !(idCheval.equals("")) ? SharedUtil.getIdLong(idCheval) : new Long(0), typePrix, typeReservation, idSession));
    }

    @GetMapping(value = "modifier", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Modifie une réservation")
    public ReservationDTO modifierReservation(
            @RequestParam(required = false, name = "idReservation") String idReservation,
            @RequestParam(required = false, name = "dateDebut") Date dateDebut,
            @RequestParam(required = false, name = "dateFin") Date dateFin,
            @RequestParam(required = false, name = "idBox", defaultValue = "") String idBox,
            @RequestParam(required = false, name = "idPiste", defaultValue = "") String idPiste,
            @RequestParam(required = false, name = "idLit", defaultValue = "") String idLit,
            @RequestParam(required = false, name = "idPersonne", defaultValue = "") String idPersonne,
            @RequestParam(required = false, name = "idPersonneAFacturer", defaultValue = "") String idPersonneAFacturer,
            @RequestParam(required = false, name = "idCheval", defaultValue = "") String idCheval,
            @RequestParam(required = false, name = "typePrix", defaultValue = "") String typePrix,
            @RequestParam(required = false, name = "typeReservation", defaultValue = "") String typeReservation,
            @RequestParam(required = false, name = "idSession", defaultValue = "") String idSession) throws InternalServerException {

        if (!(idCheval.equals(""))) {
            Reservation entity = service.modifierReservation(SharedUtil.getIdLong(idReservation), dateDebut, dateFin, SharedUtil.getIdLong(idPersonneAFacturer), SharedUtil.getIdLong(idPersonne), SharedUtil.getIdLong(idBox), SharedUtil.getIdLong(idPiste), SharedUtil.getIdLong(idLit), SharedUtil.getIdLong(idCheval), typePrix, typeReservation, idSession);
            return ReservationMapper.INSTANCE.entityToDto(entity);
        } else {
            Reservation entity = service.modifierReservation(SharedUtil.getIdLong(idReservation), dateDebut, dateFin, SharedUtil.getIdLong(idPersonneAFacturer), SharedUtil.getIdLong(idPersonne), SharedUtil.getIdLong(idBox), SharedUtil.getIdLong(idPiste), SharedUtil.getIdLong(idLit), new Long(0), typePrix, typeReservation, idSession);
            return ReservationMapper.INSTANCE.entityToDto(entity);
        }
    }

    @PutMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "met à jour une entité", nickname = "updateReservation")
    public ReservationDTO update(@PathVariable("id") String id, @RequestBody ReservationDTO dto) {

        Long idL = SharedUtil.getIdLong(id);
        Reservation entity = service.get(idL);
        if (entity == null) {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
        entity = service.save(ReservationMapper.INSTANCE.dtoToEntity(dto));
        return ReservationMapper.INSTANCE.entityToDto(entity);
    }

    @GetMapping(value = "personneAFacturer/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entités selon Id Personne A Facturer")
    public List<ReservationDTO> getAllByPersonneAFacturerId(@PathVariable("id") String id) {
        return ReservationMapper.INSTANCE.entitiesToDTOs(service.getAllByPersonneAFacturerId(id));
    }

    @GetMapping(value = "chevalBoxs", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des Réservations selon ChevalBox")
    public List<ReservationDTO> getAllChevalBoxReservations() {
        return ReservationMapper.INSTANCE.entitiesToDTOs(service.getAllChevalBoxReservations());
    }

    @GetMapping(value = "chevalBoxs/criteria", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Recherche selon Nom Site, Mois, Année")
    public List<ReservationDTO> getAllChevalBoxReservationsByCriteria(
            @RequestParam(required = false, name = "nomSite") String nomSite,
            @RequestParam(required = false, name = "mois") String mois,
            @RequestParam(required = false, name = "annee") String annee
    ) {
        return ReservationMapper.INSTANCE.entitiesToDTOs(service.getAllChevalBoxReservationsByCriteria(nomSite, SharedUtil.getIdLong(mois), SharedUtil.getIdLong(annee)));
    }

    @GetMapping(value = "chevalBoxs/cheval/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des réservations de type ChevalBox selon Id Cheval")
    public List<ReservationDTO> getAllChevalBoxReservationsByChevalId(@PathVariable("id") String id) {
        return ReservationMapper.INSTANCE.entitiesToDTOs(service.getAllChevalBoxReservationsByChevalId(SharedUtil.getIdLong(id)));
    }

    @GetMapping(value = "cheval/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des réservations selon Id Cheval")
    public List<ReservationDTO> getAllReservationsByChevalId(@PathVariable("id") String id) {
        return ReservationMapper.INSTANCE.entitiesToDTOs(service.getAllByChevalId(SharedUtil.getIdLong(id)));
    }

    @GetMapping(value = "personne/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des réservations selong Id Personne")
    public List<ReservationDTO> getAllReservationsByPersonneId(@PathVariable("id") String id) {
        return ReservationMapper.INSTANCE.entitiesToDTOs(service.getAllByPersonneId(id));
    }

    @GetMapping(value = "personneLits", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des réservations de type PersonneLit")
    public List<ReservationDTO> getAllPersonneLitReservations() {
        return ReservationMapper.INSTANCE.entitiesToDTOs(service.getAllPersonneLitReservations());
    }

    @GetMapping(value = "personneLits/personne/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des réservations de type PersonneLit selon Id Personne")
    public List<ReservationDTO> getAllPersonneLitReservationsByPersonneId(@PathVariable("id") String id) {
        return ReservationMapper.INSTANCE.entitiesToDTOs(service.getAllPersonneLitReservationsByPersonneId(id));
    }

    @GetMapping(value = "personneLits/criteria", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Recherche selon Nom Site, Mois, Année / Méthode 2")
    public List<ReservationDTO> getAllPersonneLitReservationsByCriteria(
            @RequestParam(required = false, name = "nomSite") String nomSite,
            @RequestParam(required = false, name = "mois") String mois,
            @RequestParam(required = false, name = "annee") String annee
    ) {
        return ReservationMapper.INSTANCE.entitiesToDTOs(service.getAllPersonneLitReservationsByCriteria(nomSite, SharedUtil.getIdLong(mois), SharedUtil.getIdLong(annee)));
    }

    @GetMapping(value = "chevalPistes", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des réservations de type ChevalPiste")
    public List<ReservationDTO> getAllChevalPisteReservations() {
        return ReservationMapper.INSTANCE.entitiesToDTOs(service.getAllChevalPisteReservations());
    }

    @GetMapping(value = "chevalPistes/criteria", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Recherche selon Nom Site, Id Piste, Mois, Année")
    public List<ReservationDTO> getAllChevalPisteReservationsByCriteria(
            @RequestParam(required = false, name = "nomSite") String nomSite,
            @RequestParam(required = false, name = "idPiste") String idPiste,
            @RequestParam(required = false, name = "mois") String mois,
            @RequestParam(required = false, name = "annee") String annee
    ) {
        return ReservationMapper.INSTANCE.entitiesToDTOs(service.getAllChevalPisteReservationsByCriteria(nomSite, SharedUtil.getIdLong(idPiste), SharedUtil.getIdLong(mois), SharedUtil.getIdLong(annee)));
    }

    @GetMapping(value = "chevalPistes/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des réservations selon Id Piste")
    public List<ReservationDTO> getAllChevalPisteReservationsByPisteId(@PathVariable("id") String id) {
        return ReservationMapper.INSTANCE.entitiesToDTOs(service.getAllChevalPisteReservationsByPisteId(SharedUtil.getIdLong(id)));
    }

    @GetMapping(value = "chevalPistes/date", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des réservation selon Id Piste, Date")
    public List<ReservationDTO> getAllChevalPisteReservationsByDate(@RequestParam(required = false, name = "date") Date date,
                                                                    @RequestParam(required = false, name = "heure") String heure,
                                                                    @RequestParam(required = false, name = "idPiste") String idPiste) throws ParseException {
        return ReservationMapper.INSTANCE.entitiesToDTOs(service.getAllChevalPisteReservationsByDate(date, SharedUtil.getIdLong(heure), SharedUtil.getIdLong(idPiste)));
    }

    @GetMapping(value = "chevalPistes/cheval/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des réservations de type ChevalPiste selon Id Cheval")
    public List<ReservationDTO> getAllChevalPisteReservationsByChevalId(@PathVariable("id") String id) {
        return ReservationMapper.INSTANCE.entitiesToDTOs(service.getAllChevalPisteReservationsByChevalId(SharedUtil.getIdLong(id)));
    }

    @GetMapping(value = "prix/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie le prix de réservation")
    public Long getPrixReservation(@PathVariable("id") String id) {
        return service.getReservationPrixEnCours(new Long(id));
    }

    @GetMapping(value = "encours/cheval/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des réservations en cours selon Id Cheval")
    public List<ReservationDTO> getReservationsByChevalId(@PathVariable("id") String id) {
        return ReservationMapper.INSTANCE.entitiesToDTOs(service.getAllByChevalIdEnCours(SharedUtil.getIdLong(id)));
    }

    @PostMapping(value = "facturer", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Facturer une liste de DetailReservations")
    public FactureDTO facturer(@RequestBody List<String> detailReservationIds) throws DocumentException, MessagingException, IOException {
        return FactureMapper.INSTANCE.entityToDto(service.facturer(detailReservationIds));
    }

    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    @ApiOperation(value = "Supprime une entité")
    public void delete(@PathVariable("id") String id) {

        service.supprimerReservation(SharedUtil.getIdLong(id));

    }

    @DeleteMapping(value = "arreter/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    @ApiOperation(value = "Arrêter une réservation")
    public void arreterReservation(@PathVariable("id") String id) {

        service.arreterReservation(SharedUtil.getIdLong(id));

    }

    @GetMapping(value = "/reservationFacturee/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie vrai si la réservation est partiellement facturée")
    public Boolean reservationDejaFacturee(@PathVariable("id") String id) {
        return service.reservationDejaFacturee(new Long(id));
    }

}
