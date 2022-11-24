package ma.sorec.gcecourse.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.sorec.gcecourse.Utils.SharedUtil;
import ma.sorec.gcecourse.data.ChevalPiste;
import ma.sorec.gcecourse.dto.ChevalPisteDTO;
import ma.sorec.gcecourse.exceptions.InternalServerException;
import ma.sorec.gcecourse.exceptions.ObjectNotFoundException;
import ma.sorec.gcecourse.mapper.ChevalPisteMapper;
import ma.sorec.gcecourse.service.ChevalPisteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chevalPistes")
@Api("ChevalPiste Controller")
public class ChevalPisteController {

    @Autowired
    ChevalPisteService service;

    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie de l'entité identifiée par son id")
    public ChevalPisteDTO get(@PathVariable("id") String id) throws ObjectNotFoundException {

        Long idL = SharedUtil.getIdLong(id);
        ChevalPiste entity = service.get(idL);
        if (entity != null) {
            return ChevalPisteMapper.INSTANCE.entityToDto(entity);
        } else {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
    }

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées")
    public List<ChevalPisteDTO> getAll() {
        return ChevalPisteMapper.INSTANCE.entitiesToDTOs(service.listAll());
    }

    @PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Enregistre une nouvelle'entité")
    public ChevalPisteDTO save(@RequestBody ChevalPisteDTO dto) throws InternalServerException {

        try {
            ChevalPiste entity = service.save(ChevalPisteMapper.INSTANCE.dtoToEntity(dto));
            return ChevalPisteMapper.INSTANCE.entityToDto(entity);
        } catch (Exception e) {
            throw new InternalServerException("IE", "Erreur lors de la création de l'objet");
        }
    }

    @PutMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Met à jour une entité")
    public ChevalPisteDTO update(@PathVariable("id") String id, @RequestBody ChevalPisteDTO dto) {

        Long idL = SharedUtil.getIdLong(id);
        ChevalPiste entity = service.get(idL);
        if (entity == null) {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
        entity = service.save(ChevalPisteMapper.INSTANCE.dtoToEntity(dto));
        return ChevalPisteMapper.INSTANCE.entityToDto(entity);
    }

    @GetMapping(value = "/nombreHeures/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie le nombre d'heures de la réservation")
    public Long getNombreJoursReservation(@PathVariable("id") String id) {
        return service.getNombreHeuresReservation(SharedUtil.getIdLong(id));
    }

    @GetMapping(value = "/heure/{heure}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entités selon l'heure")
    public List<ChevalPisteDTO> getChevalPisteByHour(@PathVariable("heure") String heure) {
        return ChevalPisteMapper.INSTANCE.entitiesToDTOs(service.getChevalPisteByHour(heure));
    }

}
