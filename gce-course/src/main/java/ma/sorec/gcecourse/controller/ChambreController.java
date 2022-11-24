package ma.sorec.gcecourse.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.sorec.gcecourse.Utils.SharedUtil;
import ma.sorec.gcecourse.data.Chambre;
import ma.sorec.gcecourse.dto.ChambreDTO;
import ma.sorec.gcecourse.exceptions.ObjectNotFoundException;
import ma.sorec.gcecourse.mapper.ChambreMapper;
import ma.sorec.gcecourse.service.ChambreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chambres")
@Api("Chambre Controller")
public class ChambreController {

    @Autowired
    ChambreService service;

    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie de l'entité identifiée par son id")
    public ChambreDTO get(@PathVariable("id") String id) throws ObjectNotFoundException {

        Long idL = SharedUtil.getIdLong(id);
        Chambre entity = service.get(idL);
        if (entity != null) {
            return ChambreMapper.INSTANCE.entityToDto(entity);
        } else {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
    }

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées")
    public List<ChambreDTO> getAll() {
        return ChambreMapper.INSTANCE.entitiesToDTOs(service.listAll());
    }

    @PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Enregistre une nouvelle'entité")
    public ChambreDTO save(@RequestBody ChambreDTO dto) {

        Chambre entity = service.save(ChambreMapper.INSTANCE.dtoToEntity(dto));
        return ChambreMapper.INSTANCE.entityToDto(entity);
    }

    @PutMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "met à jour une entité")
    public ChambreDTO update(@PathVariable("id") String id, @RequestBody ChambreDTO dto) {

        Chambre entity = service.update(ChambreMapper.INSTANCE.dtoToEntity(dto));
        return ChambreMapper.INSTANCE.entityToDto(entity);
    }

    @GetMapping(value = "/immeuble/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entités selon Id Immeuble")
    public List<ChambreDTO> getAllByImmeubleId(@PathVariable("id") String id) throws ObjectNotFoundException {

        Long idL = SharedUtil.getIdLong(id);
        List<Chambre> entities = service.getAllByImmeubleId(idL);
        return ChambreMapper.INSTANCE.entitiesToDTOs(entities);
    }

    @GetMapping(value = "/actif/immeuble/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des Chambres actives")
    public List<ChambreDTO> getAllActifChambre(@PathVariable("id") String id) throws ObjectNotFoundException {

        Long idL = SharedUtil.getIdLong(id);
        List<Chambre> entities = service.getAllActifChambre(idL);
        return ChambreMapper.INSTANCE.entitiesToDTOs(entities);
    }

    @DeleteMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    @ApiOperation(value = "Supprime une entité")
    @ResponseBody
    public void delete(@PathVariable("id") String id) {

        service.delete(SharedUtil.getIdLong(id));

    }

}
