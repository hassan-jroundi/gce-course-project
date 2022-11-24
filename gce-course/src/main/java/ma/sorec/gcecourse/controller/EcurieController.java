package ma.sorec.gcecourse.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.sorec.gcecourse.Utils.SharedUtil;
import ma.sorec.gcecourse.data.Ecurie;
import ma.sorec.gcecourse.dto.EcurieDTO;
import ma.sorec.gcecourse.exceptions.InternalServerException;
import ma.sorec.gcecourse.exceptions.ObjectNotFoundException;
import ma.sorec.gcecourse.mapper.EcurieMapper;
import ma.sorec.gcecourse.service.EcurieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ecuries")
@Api("Ecurie Controller")
public class EcurieController {

    @Autowired
    EcurieService service;

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie de l'entité identifiée par son id")
    public EcurieDTO get(@PathVariable("id") String id) throws ObjectNotFoundException {

        Long idL = SharedUtil.getIdLong(id);
        Ecurie entity = service.get(idL);
        if (entity != null) {
            return EcurieMapper.INSTANCE.entityToDto(entity);
        } else {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées")
    public List<EcurieDTO> getAll() {
        return EcurieMapper.INSTANCE.entitiesToDTOs(service.listAll());
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Enregistre une nouvelle'entité")
    public EcurieDTO save(@RequestBody EcurieDTO dto) throws InternalServerException {

        try {
            Ecurie entity = service.save(EcurieMapper.INSTANCE.dtoToEntity(dto));
            return EcurieMapper.INSTANCE.entityToDto(entity);
        } catch (Exception e) {
            throw new InternalServerException("IE", "Erreur lors de la création de l'objet");
        }
    }

    @PutMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "met à jour une entité")
    public EcurieDTO update(@PathVariable("id") String id, @RequestBody EcurieDTO dto) {

        return EcurieMapper.INSTANCE.entityToDto(service.save(EcurieMapper.INSTANCE.dtoToEntity(dto)));
    }

    @GetMapping(value = "/site/{nom}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<EcurieDTO> getAllByNomSite(@PathVariable("nom") String nom) {
        return EcurieMapper.INSTANCE.entitiesToDTOs(service.getAllByNomSite(nom));
    }

    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Supprime une entité.")
    @ResponseBody
    public void delete(@PathVariable("id") String id) {

        service.delete(SharedUtil.getIdLong(id));

    }

}
