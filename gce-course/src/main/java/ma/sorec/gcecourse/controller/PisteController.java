package ma.sorec.gcecourse.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.sorec.gcecourse.Utils.SharedUtil;
import ma.sorec.gcecourse.data.Piste;
import ma.sorec.gcecourse.dto.PisteDTO;
import ma.sorec.gcecourse.exceptions.InternalServerException;
import ma.sorec.gcecourse.exceptions.ObjectNotFoundException;
import ma.sorec.gcecourse.mapper.PisteMapper;
import ma.sorec.gcecourse.service.PisteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pistes")
@Api("Piste Controller")
public class PisteController {

    @Autowired
    PisteService service;

    @Autowired
    PrixPisteController prixPisteController;

    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie de l'entité identifiée par son id")
    public PisteDTO get(@PathVariable("id") String id) throws ObjectNotFoundException {

        Long idL = SharedUtil.getIdLong(id);
        Piste entity = service.get(idL);
        if (entity != null) {
            return PisteMapper.INSTANCE.entityToDto(entity);
        } else {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
    }

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées")
    public List<PisteDTO> getAll() {
        return PisteMapper.INSTANCE.entitiesToDTOs(service.listAll());
    }

    @GetMapping(value = "/site/{nom}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées")
    public List<PisteDTO> getAllByNomSite(@PathVariable("nom") String nom) {
        return PisteMapper.INSTANCE.entitiesToDTOs(service.getAllByNomSite(nom));
    }

    @PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Enregistre une nouvelle'entité")
    public PisteDTO save(@RequestBody PisteDTO dto) throws InternalServerException {

        try {
            Piste entity = service.save(PisteMapper.INSTANCE.dtoToEntity(dto));
            return PisteMapper.INSTANCE.entityToDto(entity);
        } catch (Exception e) {
            throw new InternalServerException("IE", "Erreur lors de la création de l'objet");
        }
    }

    @PutMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "met à jour une entité")
    public PisteDTO update(@PathVariable("id") String id, @RequestBody PisteDTO dto) {

        Long idL = SharedUtil.getIdLong(id);
        Piste entity = service.get(idL);
        if (entity == null) {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
        entity = service.update(PisteMapper.INSTANCE.dtoToEntity(dto));
        return PisteMapper.INSTANCE.entityToDto(entity);
    }

    @DeleteMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    @ApiOperation(value = "Supprime une entité.")
    @ResponseBody
    public void delete(@PathVariable("id") String id) {

        service.delete(SharedUtil.getIdLong(id));

    }

}
