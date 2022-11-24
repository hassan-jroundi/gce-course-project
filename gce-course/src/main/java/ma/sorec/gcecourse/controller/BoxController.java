package ma.sorec.gcecourse.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.sorec.gcecourse.Utils.SharedUtil;
import ma.sorec.gcecourse.data.Box;
import ma.sorec.gcecourse.data.Lit;
import ma.sorec.gcecourse.dto.BoxDTO;
import ma.sorec.gcecourse.dto.LitDTO;
import ma.sorec.gcecourse.exceptions.ObjectNotFoundException;
import ma.sorec.gcecourse.mapper.BoxMapper;
import ma.sorec.gcecourse.mapper.LitMapper;
import ma.sorec.gcecourse.repository.BoxRepository;
import ma.sorec.gcecourse.service.BoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boxs")
@Api("Box Controller")
public class BoxController {

    @Autowired
    BoxService service;

    @Autowired
    BoxRepository repository;

    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie de l'entité identifiée par son id")
    public BoxDTO get(@PathVariable("id") String id) throws ObjectNotFoundException {

        Long idL = SharedUtil.getIdLong(id);
        Box entity = service.get(idL);
        if (entity != null) {
            return BoxMapper.INSTANCE.entityToDto(entity);
        } else {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
    }

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées")
    public List<BoxDTO> getAll() {
        return BoxMapper.INSTANCE.entitiesToDTOs(service.listAll());
    }

    @PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Enregistre une nouvelle entité")
    public BoxDTO save(@RequestBody BoxDTO dto) {
        Box entity = service.save(BoxMapper.INSTANCE.dtoToEntity(dto));
        return BoxMapper.INSTANCE.entityToDto(entity);
    }

    @PutMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Met à jour une entité")
    public BoxDTO update(@PathVariable("id") String id, @RequestBody BoxDTO dto) {
        Box entity = service.update(BoxMapper.INSTANCE.dtoToEntity(dto));
        return BoxMapper.INSTANCE.entityToDto(entity);
    }

    @GetMapping(value = "/ecurie/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entités selon Id Ecurie")
    public List<BoxDTO> getAllByEcurieId(@PathVariable("id") String id) {
        Long idL = SharedUtil.getIdLong(id);
        return BoxMapper.INSTANCE.entitiesToDTOs(service.getAllByEcurieId(idL));
    }

    @DeleteMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    @ApiOperation(value = "Supprime une entité")
    @ResponseBody
    public void delete(@PathVariable("id") String id) {

        service.delete(SharedUtil.getIdLong(id));

    }

    @GetMapping(value = "/site/{nom}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entités selon Nom Site")
    public List<BoxDTO> getAllByNomSite(@PathVariable("nom") String nom) {
        return BoxMapper.INSTANCE.entitiesToDTOs(service.getAllByNomSite(nom));
    }

    @PostMapping(value = "/ajouter", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Ajoute automatiquement un Box selon l'ordre")
    public BoxDTO ajouterBox(@RequestBody String idEcurie) {
        Box entity = service.ajouterBox(idEcurie);
        return BoxMapper.INSTANCE.entityToDto(entity);
    }

}
