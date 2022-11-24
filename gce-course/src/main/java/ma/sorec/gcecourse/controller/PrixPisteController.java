package ma.sorec.gcecourse.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.sorec.gcecourse.data.PrixPiste;
import ma.sorec.gcecourse.dto.PrixPisteDTO;
import ma.sorec.gcecourse.exceptions.InternalServerException;
import ma.sorec.gcecourse.exceptions.ObjectNotFoundException;
import ma.sorec.gcecourse.mapper.PrixPisteMapper;
import ma.sorec.gcecourse.service.PrixPisteService;
import ma.sorec.gcecourse.Utils.SharedUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prixPistes")
@Api("PrixPiste Controller")
public class PrixPisteController {

    @Autowired
    PrixPisteService service;

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie de l'entité identifiée par son id")
    public PrixPisteDTO get(@PathVariable("id") String id) throws ObjectNotFoundException {

        Long idL = SharedUtil.getIdLong(id);
        PrixPiste entity = service.get(idL);
        if (entity != null) {
            return PrixPisteMapper.INSTANCE.entityToDto(entity);
        } else {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées")
    public List<PrixPisteDTO> getAll() {
        return PrixPisteMapper.INSTANCE.entitiesToDTOs(service.listAll());
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Enregistre une nouvelle'entité")
    public PrixPisteDTO save(@RequestBody PrixPisteDTO dto) throws InternalServerException {

        try {
            PrixPiste entity = service.save(PrixPisteMapper.INSTANCE.dtoToEntity(dto));
            return PrixPisteMapper.INSTANCE.entityToDto(entity);
        } catch (Exception e) {
            throw new InternalServerException("IE", "Erreur lors de la création de l'objet");
        }
    }

    @PutMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "met à jour une entité")
    public PrixPisteDTO update(@PathVariable("id") String id, @RequestBody PrixPisteDTO dto) {

        Long idL = SharedUtil.getIdLong(id);
        PrixPiste entity = service.get(idL);
        if (entity == null) {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
        entity = service.save(PrixPisteMapper.INSTANCE.dtoToEntity(dto));
        return PrixPisteMapper.INSTANCE.entityToDto(entity);
    }

    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Supprime une entité.")
    @ResponseBody
    public void delete(@PathVariable("id") String id) {

        service.delete(SharedUtil.getIdLong(id));

    }

    @GetMapping(value = "/piste/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées selon Nom Piste")
    public List<PrixPisteDTO> getByNomPiste(@PathVariable("id") String id) throws ObjectNotFoundException {

        List<PrixPiste> entities = service.getAllByIdPiste(id);
        return PrixPisteMapper.INSTANCE.entitiesToDTOs(entities);
    }

    @GetMapping(value = "/actuel/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie le dernière prix")
    public PrixPisteDTO getLastPrixPiste(@PathVariable("id") String id) throws ObjectNotFoundException {

        PrixPiste entity = service.getLastPrixPiste(id);
        return PrixPisteMapper.INSTANCE.entityToDto(entity);
    }

}
