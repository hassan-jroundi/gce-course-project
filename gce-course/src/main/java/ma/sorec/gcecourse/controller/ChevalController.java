package ma.sorec.gcecourse.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.sorec.gcecourse.Utils.SharedUtil;
import ma.sorec.gcecourse.controller.mohr.PersonneChevalMController;
import ma.sorec.gcecourse.controller.mohr.StatutChevalMController;
import ma.sorec.gcecourse.data.Cheval;
import ma.sorec.gcecourse.data.mohr.DetailDeclarationEffectif;
import ma.sorec.gcecourse.data.mohr.PersonneChevalM;
import ma.sorec.gcecourse.data.mohr.PersonneM;
import ma.sorec.gcecourse.data.mohr.TranspondeurChevalM;
import ma.sorec.gcecourse.dto.ChevalDTO;
import ma.sorec.gcecourse.exceptions.ObjectNotFoundException;
import ma.sorec.gcecourse.mapper.ChevalMapper;
import ma.sorec.gcecourse.repository.ChevalRepository;
import ma.sorec.gcecourse.repository.mohr.DetailDeclarationEffectifRepository;
import ma.sorec.gcecourse.repository.mohr.PersonneMRepository;
import ma.sorec.gcecourse.repository.mohr.TranspondeurChevalMRepository;
import ma.sorec.gcecourse.service.ChevalService;
import ma.sorec.gcecourse.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/chevals")
@Api("Cheval Controller")
public class ChevalController {

    @Autowired
    ChevalService service;

    @Autowired
    ChevalRepository repository;

    @Autowired
    PersonneMRepository personneMRepository;

    @Autowired
    PersonneChevalMController personneChevalMController;

    @Autowired
    ReservationService reservationService;

    @Autowired
    DetailDeclarationEffectifRepository detailDeclarationEffectifRepository;

    @Autowired
    TranspondeurChevalMRepository transpondeurChevalMRepository;

    @Autowired
    StatutChevalMController statutChevalMController;

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie de l'entité identifiée par son id")
    public ChevalDTO get(@PathVariable("id") String id) throws ObjectNotFoundException {

        Cheval entity = service.get(SharedUtil.getIdLong(id));
        if (entity != null) {
            return ChevalMapper.INSTANCE.entityToDto(entity);
        } else {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées")
    public List<ChevalDTO> getAll() {
        return ChevalMapper.INSTANCE.entitiesToDTOs(service.listAll());
    }

    @GetMapping(value = "/search", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées selon N° ESRIMA, Nom Cheval, N° Transpondeur, Nom Propriétaire")
    public List<ChevalDTO> getAllByNumeroEsrimaAndNomCheval(@RequestParam(required = false, name = "numeroEsrima", defaultValue = "") String numeroEsrima,
                                                            @RequestParam(required = false, name = "nomCheval", defaultValue = "") String nomCheval,
                                                            @RequestParam(required = false, name = "numeroTranspondeur", defaultValue = "") String numeroTranspondeur,
                                                            @RequestParam(required = false, name = "nomProprietaire", defaultValue = "") String nomProprietaire) {
        List<Cheval> entities = new ArrayList<>();
        if (numeroEsrima.length() == 0 && nomCheval.length() == 0 && numeroTranspondeur.length() == 0 && nomProprietaire.length() == 0) {
            return ChevalMapper.INSTANCE.entitiesToDTOs(entities);
        } else if (nomProprietaire.length() > 0) {
            List<PersonneM> personneMS = personneMRepository.findAllByNomContainingIgnoreCaseOrDesignationContainingIgnoreCaseOrRaisonSocialeContainingIgnoreCase(nomProprietaire, nomProprietaire, nomProprietaire);
            for (PersonneM personneM : personneMS) {
                List<PersonneChevalM> personneChevalMS = personneChevalMController.getByIdPersonneAndCodeNatureRelation(personneM.getId(), "PROPR");
                for (PersonneChevalM personneChevalM : personneChevalMS) {
                    entities.add(service.get(new Long(personneChevalM.getIdCheval())));
                }
            }
            for (Cheval item : entities) {
                DetailDeclarationEffectif detailDeclarationEffectif = detailDeclarationEffectifRepository.findFirstByIdChevalOrderByDateCreationDesc(item.getId());
                if (detailDeclarationEffectif != null) {
                    item.setEtat(detailDeclarationEffectif.getCodeStatutCheval());
                    item.setLibelleEtat(statutChevalMController.get(detailDeclarationEffectif.getCodeStatutCheval()).getDesignation());
                    item.setDateEtat(detailDeclarationEffectif.getDateCreation());
                } else {
                    item.setEtat("HENTR");
                    item.setLibelleEtat(statutChevalMController.get("HENTR").getDesignation());
                    item.setDateEtat(new Date());
                }
                TranspondeurChevalM transpondeurChevalM = transpondeurChevalMRepository.findFirstByIdChevalContainingIgnoreCaseAndDateFinIsNull(item.getId().toString());
                if (transpondeurChevalM != null) {
                    item.setTranspondeur(transpondeurChevalM.getNumeroTranspondeur());
                }
            }
            return ChevalMapper.INSTANCE.entitiesToDTOs(entities);
        } else {
            entities = service.findAllByNumeroEsrimaAndNomChevalAndNumeroTranspondeur(numeroEsrima, nomCheval, numeroTranspondeur);
            for (Cheval item : entities) {
                DetailDeclarationEffectif detailDeclarationEffectif = detailDeclarationEffectifRepository.findFirstByIdChevalOrderByDateCreationDesc(item.getId());
                if (detailDeclarationEffectif != null) {
                    item.setEtat(detailDeclarationEffectif.getCodeStatutCheval());
                    item.setLibelleEtat(statutChevalMController.get(detailDeclarationEffectif.getCodeStatutCheval()).getDesignation());
                    item.setDateEtat(detailDeclarationEffectif.getDateCreation());
                } else {
                    item.setEtat("HENTR");
                    item.setLibelleEtat(statutChevalMController.get("HENTR").getDesignation());
                    item.setDateEtat(new Date());
                }
            }
            return ChevalMapper.INSTANCE.entitiesToDTOs(entities);
        }
    }

    @GetMapping(value = "/search/esrima", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées selon N° ESRIMA")
    public List<ChevalDTO> getAllByNumeroEsrima(@RequestParam(required = false, name = "numeroEsrima", defaultValue = "") String numeroEsrima) {
        List<Cheval> entities = service.findAllByNumeroEsrima(numeroEsrima);
        for (Cheval item : entities) {
            DetailDeclarationEffectif detailDeclarationEffectif = detailDeclarationEffectifRepository.findFirstByIdChevalOrderByDateCreationDesc(item.getId());
            if (detailDeclarationEffectif != null) {
                item.setEtat(detailDeclarationEffectif.getCodeStatutCheval());
                item.setDateEtat(detailDeclarationEffectif.getDateCreation());
            } else {
                item.setEtat("HENTR");
                item.setDateEtat(new Date());
            }
        }
        return ChevalMapper.INSTANCE.entitiesToDTOs(entities);
    }

    @GetMapping(value = "/search2", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées selon N° ESRIMA N° Transpondeur")
    public List<ChevalDTO> getAllByNumeroEsrimaAndNumeroTranspondeur(@RequestParam(required = false, name = "numeroEsrima", defaultValue = "") String numeroEsrima,
                                                                     @RequestParam(required = false, name = "numeroTranspondeur", defaultValue = "") String numeroTranspondeur) {
        List<ChevalDTO> entities = new ArrayList<>();
        if (numeroEsrima.length() == 0 && numeroTranspondeur.length() != 0) {
            entities = ChevalMapper.INSTANCE.entitiesToDTOs(service.findAllByNumeroTranspondeur(numeroTranspondeur));
        } else if (numeroEsrima.length() != 0 && numeroTranspondeur.length() == 0) {
            entities = ChevalMapper.INSTANCE.entitiesToDTOs(service.findAllByNumeroEsrima(numeroEsrima));
        } else if (numeroEsrima.length() != 0 && numeroTranspondeur.length() != 0) {
            entities = ChevalMapper.INSTANCE.entitiesToDTOs(service.findAllByNumeroEsrimaAndNumeroTranspondeur(numeroEsrima, numeroTranspondeur));
        }
        return entities;
    }

    @GetMapping(value = "/reservation", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées avec réservation en cours")
    public List<ChevalDTO> getAllByChevalAvecReservationEnCours() {
        return ChevalMapper.INSTANCE.entitiesToDTOs(service.findAllChevalAvecReservationEnCours());
    }

    @GetMapping(value = "numeroEsrima/{numeroEsrima}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie une entité selon N° ESRIMA")
    public ChevalDTO getByNumeroEsrima(@PathVariable("numeroEsrima") String numeroEsrima) throws ObjectNotFoundException {

        Cheval entity = repository.findFirstByNumeroEsrima(numeroEsrima);
        if (entity != null) {
            return ChevalMapper.INSTANCE.entityToDto(entity);
        } else {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
    }

}
