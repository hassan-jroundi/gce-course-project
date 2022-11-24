package ma.sorec.gcecourse.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.sorec.gcecourse.Utils.SharedUtil;
import ma.sorec.gcecourse.data.Immeuble;
import ma.sorec.gcecourse.dto.ImmeubleDTO;
import ma.sorec.gcecourse.exceptions.InternalServerException;
import ma.sorec.gcecourse.exceptions.ObjectNotFoundException;
import ma.sorec.gcecourse.mapper.ImmeubleMapper;
import ma.sorec.gcecourse.service.ImmeubleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/immeubles")
@Api("Immeuble Controller")
public class ImmeubleController {

    @Autowired
    ImmeubleService service;

    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie de l'entité identifiée par son id")
    public ImmeubleDTO get(@PathVariable("id") String id) throws ObjectNotFoundException {

        Long idL = SharedUtil.getIdLong(id);
        Immeuble entity = service.get(idL);
        if (entity != null) {
            return ImmeubleMapper.INSTANCE.entityToDto(entity);
        } else {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
    }

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées")
    public List<ImmeubleDTO> getAll() {
        return ImmeubleMapper.INSTANCE.entitiesToDTOs(service.listAll());
    }

    @PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Enregistre une nouvelle'entité")
    public ImmeubleDTO save(@RequestBody ImmeubleDTO dto) throws InternalServerException {

        try {
            Immeuble entity = service.save(ImmeubleMapper.INSTANCE.dtoToEntity(dto));
            return ImmeubleMapper.INSTANCE.entityToDto(entity);
        } catch (Exception e) {
            throw new InternalServerException("IE", "Erreur lors de la création de l'objet");
        }
    }

    @PutMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "met à jour une entité")
    public ImmeubleDTO update(@PathVariable("id") String id, @RequestBody ImmeubleDTO dto) {

        Long idL = SharedUtil.getIdLong(id);
        Immeuble entity = service.get(idL);
        if (entity == null) {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
        entity = service.save(ImmeubleMapper.INSTANCE.dtoToEntity(dto));
        return ImmeubleMapper.INSTANCE.entityToDto(entity);
    }

    @GetMapping(value = "/site/{nom}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées selon Nom Site")
    public List<ImmeubleDTO> getAllByNomSite(@PathVariable("nom") String nom) {
        return ImmeubleMapper.INSTANCE.entitiesToDTOs(service.getAllByNomSite(nom));
    }

    @GetMapping(value = "/actif/site/{nom}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées actives selon Nom Site")
    public List<ImmeubleDTO> getAllActifImmeuble(@PathVariable("nom") String nom) {
        return ImmeubleMapper.INSTANCE.entitiesToDTOs(service.getAllActifImmeuble(nom));
    }

    @DeleteMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    @ApiOperation(value = "Supprime une entité")
    public void delete(@PathVariable("id") String id) {

        service.delete(SharedUtil.getIdLong(id));

    }

}
