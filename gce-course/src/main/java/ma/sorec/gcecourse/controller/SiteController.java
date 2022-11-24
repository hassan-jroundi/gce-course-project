package ma.sorec.gcecourse.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.sorec.gcecourse.Utils.SharedUtil;
import ma.sorec.gcecourse.data.Site;
import ma.sorec.gcecourse.dto.SiteDTO;
import ma.sorec.gcecourse.exceptions.InternalServerException;
import ma.sorec.gcecourse.exceptions.ObjectNotFoundException;
import ma.sorec.gcecourse.mapper.SiteMapper;
import ma.sorec.gcecourse.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sites")
@Api("Site Controller")
public class SiteController {

    @Autowired
    SiteService service;

    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie de l'entité identifiée par son id")
    public SiteDTO get(@PathVariable("id") String id) throws ObjectNotFoundException {

        Long idL = SharedUtil.getIdLong(id);
        Site entity = service.get(idL);
        if (entity != null) {
            return SiteMapper.INSTANCE.entityToDto(entity);
        } else {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
    }

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées")
    public List<SiteDTO> getAll() {
        return SiteMapper.INSTANCE.entitiesToDTOs(service.listAll());
    }

    @PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Enregistre une nouvelle'entité")
    public SiteDTO save(@RequestBody SiteDTO dto) throws InternalServerException {

        try {
            Site entity = service.save(SiteMapper.INSTANCE.dtoToEntity(dto));
            return SiteMapper.INSTANCE.entityToDto(entity);
        } catch (Exception e) {
            throw new InternalServerException("IE", "Erreur lors de la création de l'objet");
        }
    }

    @PutMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "met à jour une entité")
    public SiteDTO update(@PathVariable("id") String id, @RequestBody SiteDTO dto) {

        Long idL = SharedUtil.getIdLong(id);
        Site entity = service.get(idL);
        if (entity == null) {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
        entity = service.save(SiteMapper.INSTANCE.dtoToEntity(dto));
        return SiteMapper.INSTANCE.entityToDto(entity);
    }

}
