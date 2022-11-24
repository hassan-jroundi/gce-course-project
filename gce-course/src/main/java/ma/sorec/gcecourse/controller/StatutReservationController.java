package ma.sorec.gcecourse.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.sorec.gcecourse.data.StatutReservation;
import ma.sorec.gcecourse.dto.StatutReservationDTO;
import ma.sorec.gcecourse.exceptions.ObjectNotFoundException;
import ma.sorec.gcecourse.mapper.StatutReservationMapper;
import ma.sorec.gcecourse.service.StatutReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statutReservations")
@Api("Statut Reservation Controller")
public class StatutReservationController {

    @Autowired
    StatutReservationService service;

    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie une entité selon Id")
    public StatutReservationDTO get(@PathVariable("id") String id) throws ObjectNotFoundException {

        StatutReservation entity = service.get(id);
        if (entity != null) {
            return StatutReservationMapper.INSTANCE.entityToDto(entity);
        } else {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
    }

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entités")
    public List<StatutReservationDTO> getAll() {
        return StatutReservationMapper.INSTANCE.entitiesToDTOs(service.listAll());
    }

    @GetMapping(value = "/designation/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la désignation de Statut Reservation")
    public String getStatutReservationDesignation(@PathVariable("id") String id) throws ObjectNotFoundException {

        StatutReservation entity = service.get(id);
        if (entity != null) {
            return entity.getDesignation();
        } else {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
    }

}
