package ma.sorec.gcecourse.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.sorec.gcecourse.Utils.SharedUtil;
import ma.sorec.gcecourse.data.PersonneLit;
import ma.sorec.gcecourse.dto.PersonneLitDTO;
import ma.sorec.gcecourse.exceptions.InternalServerException;
import ma.sorec.gcecourse.exceptions.ObjectNotFoundException;
import ma.sorec.gcecourse.mapper.PersonneLitMapper;
import ma.sorec.gcecourse.service.PersonneLitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/personneLits")
@Api("PersonneLit Controller")
public class PersonneLitController {

    @Autowired
    PersonneLitService service;

    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie de l'entité identifiée par son id")
    public PersonneLitDTO get(@PathVariable("id") String id) throws ObjectNotFoundException {

        Long idL = SharedUtil.getIdLong(id);
        PersonneLit entity = service.get(idL);
        if (entity != null) {
            return PersonneLitMapper.INSTANCE.entityToDto(entity);
        } else {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
    }

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées")
    public List<PersonneLitDTO> getAll() {
        return PersonneLitMapper.INSTANCE.entitiesToDTOs(service.listAll());
    }

    @PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Enregistre une nouvelle'entité")
    public PersonneLitDTO save(@RequestBody PersonneLitDTO dto) throws InternalServerException {

        try {
            PersonneLit entity = service.save(PersonneLitMapper.INSTANCE.dtoToEntity(dto));
            return PersonneLitMapper.INSTANCE.entityToDto(entity);
        } catch (Exception e) {
            throw new InternalServerException("IE", "Erreur lors de la création de l'objet");
        }
    }

    @PutMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "met à jour une entité")
    public PersonneLitDTO update(@PathVariable("id") String id, @RequestBody PersonneLitDTO dto) {

        Long idL = SharedUtil.getIdLong(id);
        PersonneLit entity = service.get(idL);
        if (entity == null) {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
        entity = service.save(PersonneLitMapper.INSTANCE.dtoToEntity(dto));
        return PersonneLitMapper.INSTANCE.entityToDto(entity);
    }

    @GetMapping(value = "/nombreJours/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie le nombre de jours de réservation selon Id Reservation")
    public Long getNombreJoursReservation(@PathVariable("id") String id) {
        return service.getNombreJoursReservation(SharedUtil.getIdLong(id));
    }

    @GetMapping(value = "/reservation/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entités selon Id Reservation")
    public PersonneLitDTO getPersonneLitByReservationId(@PathVariable("id") String id) {
        return PersonneLitMapper.INSTANCE.entityToDto(service.getPersonneLitByReservationId(SharedUtil.getIdLong(id)));
    }

    @GetMapping(value = "/lit/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entités selon Id Lit")
    public List<PersonneLitDTO> getPersonneLitByLitId(@PathVariable("id") String id) {
        return PersonneLitMapper.INSTANCE.entitiesToDTOs(service.getPersonneLitByLitId(SharedUtil.getIdLong(id)));
    }

    @GetMapping(value = "/personne/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entités selon Id Personne")
    public List<PersonneLitDTO> getPersonneLitByPersonneId(@PathVariable("id") String id) {
        return PersonneLitMapper.INSTANCE.entitiesToDTOs(service.getPersonneLitByPersonneId(id));
    }

}
