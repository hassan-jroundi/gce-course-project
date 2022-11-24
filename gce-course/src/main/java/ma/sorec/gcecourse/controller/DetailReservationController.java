package ma.sorec.gcecourse.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.sorec.gcecourse.Utils.DateUtil;
import ma.sorec.gcecourse.Utils.SharedUtil;
import ma.sorec.gcecourse.data.DetailReservation;
import ma.sorec.gcecourse.dto.DetailReservationDTO;
import ma.sorec.gcecourse.exceptions.InternalServerException;
import ma.sorec.gcecourse.exceptions.ObjectNotFoundException;
import ma.sorec.gcecourse.mapper.DetailReservationMapper;
import ma.sorec.gcecourse.service.DetailReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/detailReservation")
@Api("DetailReservation Controller")
public class DetailReservationController {

    @Autowired
    DetailReservationService service;

    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie de l'entité identifiée par son id")
    public DetailReservationDTO get(@PathVariable("id") String id) throws ObjectNotFoundException {

        Long idL = SharedUtil.getIdLong(id);
        DetailReservation entity = service.get(idL);
        if (entity != null) {
            return DetailReservationMapper.INSTANCE.entityToDto(entity);
        } else {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
    }

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées")
    public List<DetailReservationDTO> getAll() {
        return DetailReservationMapper.INSTANCE.entitiesToDTOs(service.listAll());
    }

    @PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Enregistre une nouvelle'entité")
    public DetailReservationDTO save(@RequestBody DetailReservationDTO dto) throws InternalServerException {

        try {
            DetailReservation entity = service.save(DetailReservationMapper.INSTANCE.dtoToEntity(dto));
            return DetailReservationMapper.INSTANCE.entityToDto(entity);
        } catch (Exception e) {
            throw new InternalServerException("IE", "Erreur lors de la création de l'objet");
        }
    }

    @PutMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "met à jour une entité")
    public DetailReservationDTO update(@PathVariable("id") String id, @RequestBody DetailReservationDTO dto) {

        Long idL = SharedUtil.getIdLong(id);
        DetailReservation entity = service.get(idL);
        if (entity == null) {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
        entity = service.save(DetailReservationMapper.INSTANCE.dtoToEntity(dto));
        return DetailReservationMapper.INSTANCE.entityToDto(entity);
    }

    @GetMapping(value = "/date/{date}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la  liste des entités selon la date")
    public List<DetailReservationDTO> getDetailReservationByDateRange(@PathVariable("date") Date date) {
        return DetailReservationMapper.INSTANCE.entitiesToDTOs(service.getAllDetailReservationByDate(DateUtil.convertToLocalDateViaInstant(date)));
    }

    @GetMapping(value = "/facture/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la  liste des entités selon Id Facture")
    public List<DetailReservationDTO> getDetailReservationByIdFacture(@PathVariable("id") String id) {
        return DetailReservationMapper.INSTANCE.entitiesToDTOs(service.getAllByIdFacture(SharedUtil.getIdLong(id)));
    }

}
