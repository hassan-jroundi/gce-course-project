package ma.sorec.gcecourse.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.sorec.gcecourse.data.PrixLit;
import ma.sorec.gcecourse.dto.PrixLitDTO;
import ma.sorec.gcecourse.exceptions.InternalServerException;
import ma.sorec.gcecourse.exceptions.ObjectNotFoundException;
import ma.sorec.gcecourse.mapper.PrixLitMapper;
import ma.sorec.gcecourse.service.PrixLitService;
import ma.sorec.gcecourse.Utils.SharedUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/prixLits")
@Api("PrixLit Controller")
public class PrixLitController {

    @Autowired
    PrixLitService service;

    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie de l'entité identifiée par son id")
    public PrixLitDTO get(@PathVariable("id") String id) throws ObjectNotFoundException {

        Long idL = SharedUtil.getIdLong(id);
        PrixLit entity = service.get(idL);
        if (entity != null) {
            return PrixLitMapper.INSTANCE.entityToDto(entity);
        } else {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
    }

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées")
    public List<PrixLitDTO> getAll() {
        return PrixLitMapper.INSTANCE.entitiesToDTOs(service.listAll());
    }

    @PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Enregistre une nouvelle'entité")
    public PrixLitDTO save(@RequestBody PrixLitDTO dto) throws InternalServerException {

        try {
            PrixLit entity = service.save(PrixLitMapper.INSTANCE.dtoToEntity(dto));
            return PrixLitMapper.INSTANCE.entityToDto(entity);
        } catch (Exception e) {
            throw new InternalServerException("IE", "Erreur lors de la création de l'objet");
        }
    }

    @PutMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "met à jour une entité")
    public PrixLitDTO update(@PathVariable("id") String id, @RequestBody PrixLitDTO dto) {

        Long idL = SharedUtil.getIdLong(id);
        PrixLit entity = service.get(idL);
        if (entity == null) {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
        entity = service.save(PrixLitMapper.INSTANCE.dtoToEntity(dto));
        return PrixLitMapper.INSTANCE.entityToDto(entity);
    }

    @GetMapping(value = "/lit/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées selon Id Lit")
    public List<PrixLitDTO> getByIdLit(@PathVariable("id") String id) throws ObjectNotFoundException {

        List<PrixLit> entities = service.getAllByIdLit(id);
        return PrixLitMapper.INSTANCE.entitiesToDTOs(entities);
    }

    @GetMapping(value = "/reservation/lit", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie le prix de réservation")
    public Long getPrixReservation(String idLit, Date dateCreation, String typePrix) throws ObjectNotFoundException {

        return service.getPrixReservation(idLit, dateCreation, typePrix).getMontant();
    }

    @GetMapping(value = "/actuel/unitaire/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie le prix actuel Unitaire selon Id Lit")
    public PrixLitDTO getLastPrixUnitaireChambre(@PathVariable("id") String id) {

        PrixLit entity = service.getLasPrixLit(id, "U");
        return PrixLitMapper.INSTANCE.entityToDto(entity);
    }

    @GetMapping(value = "/actuel/forfaitaire/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie le prix actuel Forfaitaire selon Id Lit")
    public PrixLitDTO getLastPrixForfaitaireChambre(@PathVariable("id") String id) {

        PrixLit entity = service.getLasPrixLit(id, "F");
        return PrixLitMapper.INSTANCE.entityToDto(entity);
    }

    @GetMapping(value = "/unitaire/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées Unitaire selon Id Chambre")
    public List<PrixLitDTO> getAllPrixUnitaireChambre(@PathVariable("id") String id) {

        List<PrixLit> entities = service.getAllByIdChambre(id, "U");
        return PrixLitMapper.INSTANCE.entitiesToDTOs(entities);
    }

    @GetMapping(value = "/forfaitaire/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées Forfaitaire selon Id Chambre")
    public List<PrixLitDTO> getAllPrixForfaitaireChambre(@PathVariable("id") String id) {

        List<PrixLit> entities = service.getAllByIdChambre(id, "F");
        return PrixLitMapper.INSTANCE.entitiesToDTOs(entities);
    }

    @GetMapping(value = "/changerPrix/{montant}/typePrix/{typePrix}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Changer le prix de tous les Lits")
    public void changePrixDeTousLesLits(@PathVariable("montant") String montant, @PathVariable("typePrix") String typePrix) throws ObjectNotFoundException {

        service.changerPrixDeTousLesLits(new Long(montant), typePrix);
    }

}
