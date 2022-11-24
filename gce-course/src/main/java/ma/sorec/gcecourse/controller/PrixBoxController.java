package ma.sorec.gcecourse.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.sorec.gcecourse.Utils.SharedUtil;
import ma.sorec.gcecourse.data.PrixBox;
import ma.sorec.gcecourse.dto.PrixBoxDTO;
import ma.sorec.gcecourse.exceptions.InternalServerException;
import ma.sorec.gcecourse.exceptions.ObjectNotFoundException;
import ma.sorec.gcecourse.mapper.PrixBoxMapper;
import ma.sorec.gcecourse.service.PrixBoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prixBoxs")
@Api("PrixBox Controller")
public class PrixBoxController {

    @Autowired
    PrixBoxService service;

    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie de l'entité identifiée par son id")
    public PrixBoxDTO get(@PathVariable("id") String id) throws ObjectNotFoundException {

        Long idL = SharedUtil.getIdLong(id);
        PrixBox entity = service.get(idL);
        if (entity != null) {
            return PrixBoxMapper.INSTANCE.entityToDto(entity);
        } else {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
    }

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées")
    public List<PrixBoxDTO> getAll() {
        return PrixBoxMapper.INSTANCE.entitiesToDTOs(service.listAll());
    }

    @PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Enregistre une nouvelle'entité")
    public PrixBoxDTO save(@RequestBody PrixBoxDTO dto) throws InternalServerException {

        try {
            PrixBox entity = service.save(PrixBoxMapper.INSTANCE.dtoToEntity(dto));
            return PrixBoxMapper.INSTANCE.entityToDto(entity);
        } catch (Exception e) {
            throw new InternalServerException("IE", "Erreur lors de la création de l'objet");
        }
    }

    @PutMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "met à jour une entité")
    public PrixBoxDTO update(@PathVariable("id") String id, @RequestBody PrixBoxDTO dto) {

        Long idL = SharedUtil.getIdLong(id);
        PrixBox entity = service.get(idL);
        if (entity == null) {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
        entity = service.save(PrixBoxMapper.INSTANCE.dtoToEntity(dto));
        return PrixBoxMapper.INSTANCE.entityToDto(entity);
    }

    @GetMapping(value = "/box/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées selon Id Box")
    public List<PrixBoxDTO> getAllByIdBox(@PathVariable("id") String id) throws ObjectNotFoundException {

        List<PrixBox> entities = service.getAllByIdBox(id);
        return PrixBoxMapper.INSTANCE.entitiesToDTOs(entities);
    }

    @GetMapping(value = "/box/unitaire/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie le prix Unitaire selon Id Box")
    public List<PrixBoxDTO> getAllPrixUnitaireByIdBox(@PathVariable("id") String id) throws ObjectNotFoundException {

        List<PrixBox> entities = service.getAllPrixUnitaireByIdBox(id);
        return PrixBoxMapper.INSTANCE.entitiesToDTOs(entities);
    }

    @GetMapping(value = "/box/forfaitaire/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie le prix Forfaitaire selon Id Box")
    public List<PrixBoxDTO> getAllPrixForfaitaireByIdBox(@PathVariable("id") String id) throws ObjectNotFoundException {

        List<PrixBox> entities = service.getAllPrixForfaitaireByIdBox(id);
        return PrixBoxMapper.INSTANCE.entitiesToDTOs(entities);
    }

    @GetMapping(value = "/actuels/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie les prix actuels selon Id Box")
    public List<PrixBoxDTO> getLastPrixBox(@PathVariable("id") String id) throws ObjectNotFoundException {

        List<PrixBox> entity = service.getLastPrixBox(id);
        return PrixBoxMapper.INSTANCE.entitiesToDTOs(entity);
    }

    @GetMapping(value = "/changerPrix/{montant}/typePrix/{typePrix}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Changer le prix de tous les Boxs")
    public void changerPrixDeTousLesBoxs(@PathVariable("montant") String montant, @PathVariable("typePrix") String typePrix) throws ObjectNotFoundException {

        service.changerPrixDeTousLesBoxs(new Long(montant), typePrix);
    }

}
