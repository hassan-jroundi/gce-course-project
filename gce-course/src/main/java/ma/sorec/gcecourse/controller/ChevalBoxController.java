package ma.sorec.gcecourse.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.sorec.gcecourse.Utils.SharedUtil;
import ma.sorec.gcecourse.data.ChevalBox;
import ma.sorec.gcecourse.dto.ChevalBoxDTO;
import ma.sorec.gcecourse.exceptions.InternalServerException;
import ma.sorec.gcecourse.exceptions.ObjectNotFoundException;
import ma.sorec.gcecourse.mapper.ChevalBoxMapper;
import ma.sorec.gcecourse.service.ChevalBoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chevalBoxs")
@Api("ChevalBox Controller")
public class ChevalBoxController {

    @Autowired
    ChevalBoxService service;

    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie de l'entité identifiée par son id")
    public ChevalBoxDTO get(@PathVariable("id") String id) throws ObjectNotFoundException {

        Long idL = SharedUtil.getIdLong(id);
        ChevalBox entity = service.get(idL);
        if (entity != null) {
            return ChevalBoxMapper.INSTANCE.entityToDto(entity);
        } else {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
    }

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées")
    public List<ChevalBoxDTO> getAll() {
        return ChevalBoxMapper.INSTANCE.entitiesToDTOs(service.listAll());
    }

    @PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Enregistre une nouvelle'entité")
    public ChevalBoxDTO save(@RequestBody ChevalBoxDTO dto) throws InternalServerException {

        try {
            ChevalBox entity = service.save(ChevalBoxMapper.INSTANCE.dtoToEntity(dto));
            return ChevalBoxMapper.INSTANCE.entityToDto(entity);
        } catch (Exception e) {
            throw new InternalServerException("IE", "Erreur lors de la création de l'objet");
        }
    }

    @PutMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Met à jour une entité")
    public ChevalBoxDTO update(@PathVariable("id") String id, @RequestBody ChevalBoxDTO dto) {

        Long idL = SharedUtil.getIdLong(id);
        ChevalBox entity = service.get(idL);
        if (entity == null) {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
        entity = service.save(ChevalBoxMapper.INSTANCE.dtoToEntity(dto));
        return ChevalBoxMapper.INSTANCE.entityToDto(entity);
    }

    @GetMapping(value = "/nombreJours/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie le nombre de jours de la réservation")
    public Long getNombreJoursReservation(@PathVariable("id") String id) {
        return service.getNombreJoursReservation(SharedUtil.getIdLong(id));
    }

    @GetMapping(value = "/reservation/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie l'entité selon Id Reservation")
    public ChevalBoxDTO getChevalBoxByReservationId(@PathVariable("id") String id) {
        return ChevalBoxMapper.INSTANCE.entityToDto(service.getChevalBoxByReservationId(SharedUtil.getIdLong(id)));
    }

    @GetMapping(value = "/box/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entités selon Id Box")
    public List<ChevalBoxDTO> getChevalBoxByBoxId(@PathVariable("id") String id) {
        return ChevalBoxMapper.INSTANCE.entitiesToDTOs(service.getChevalBoxByBoxId(SharedUtil.getIdLong(id)));
    }

    @GetMapping(value = "/cheval/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entités selon Id Cheval")
    public List<ChevalBoxDTO> getChevalBoxByChevalId(@PathVariable("id") String id) {
        return ChevalBoxMapper.INSTANCE.entitiesToDTOs(service.getChevalBoxByChevalId(SharedUtil.getIdLong(id)));
    }

}
