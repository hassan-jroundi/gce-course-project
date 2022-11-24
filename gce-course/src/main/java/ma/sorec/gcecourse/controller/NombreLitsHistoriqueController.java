package ma.sorec.gcecourse.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.sorec.gcecourse.Utils.SharedUtil;
import ma.sorec.gcecourse.data.NombreLitsHistorique;
import ma.sorec.gcecourse.dto.NombreLitsHistoriqueDTO;
import ma.sorec.gcecourse.exceptions.InternalServerException;
import ma.sorec.gcecourse.exceptions.ObjectNotFoundException;
import ma.sorec.gcecourse.mapper.NombreLitsHistoriqueMapper;
import ma.sorec.gcecourse.service.NombreLitsHistoriqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nombreLitsHistoriques")
@Api("NombreLitsHistorique Controller")
public class NombreLitsHistoriqueController {

    @Autowired
    NombreLitsHistoriqueService service;

    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie de l'entité identifiée par son id")
    public NombreLitsHistoriqueDTO get(@PathVariable("id") String id) throws ObjectNotFoundException {

        Long idL = SharedUtil.getIdLong(id);
        NombreLitsHistorique entity = service.get(idL);
        if (entity != null) {
            return NombreLitsHistoriqueMapper.INSTANCE.entityToDto(entity);
        } else {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
    }

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées")
    public List<NombreLitsHistoriqueDTO> getAll() {
        return NombreLitsHistoriqueMapper.INSTANCE.entitiesToDTOs(service.listAll());
    }

    @PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Enregistre une nouvelle'entité")
    public NombreLitsHistoriqueDTO save(@RequestBody NombreLitsHistoriqueDTO dto) throws InternalServerException {

        try {
            NombreLitsHistorique entity = service.save(NombreLitsHistoriqueMapper.INSTANCE.dtoToEntity(dto));
            return NombreLitsHistoriqueMapper.INSTANCE.entityToDto(entity);
        } catch (Exception e) {
            throw new InternalServerException("IE", "Erreur lors de la création de l'objet");
        }
    }

    @PutMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "met à jour une entité")
    public NombreLitsHistoriqueDTO update(@PathVariable("id") String id, @RequestBody NombreLitsHistoriqueDTO dto) {

        Long idL = SharedUtil.getIdLong(id);
        NombreLitsHistorique entity = service.get(idL);
        if (entity == null) {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
        entity = service.save(NombreLitsHistoriqueMapper.INSTANCE.dtoToEntity(dto));
        return NombreLitsHistoriqueMapper.INSTANCE.entityToDto(entity);
    }

    @GetMapping(value = "/chambre/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées selon Id Chambre")
    public List<NombreLitsHistoriqueDTO> getAllByIdChambre(@PathVariable("id") String id) throws ObjectNotFoundException {

        List<NombreLitsHistorique> entities = service.getAllByIdChambre(id);
        return NombreLitsHistoriqueMapper.INSTANCE.entitiesToDTOs(entities);
    }

    @GetMapping(value = "/actuel/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie le nombre de lits actuel")
    public NombreLitsHistoriqueDTO getLastNombreLitsHistorique(@PathVariable("id") String id) throws ObjectNotFoundException {

        NombreLitsHistorique entity = service.getLastNombreLitsHistorique(id);
        return NombreLitsHistoriqueMapper.INSTANCE.entityToDto(entity);
    }

}
