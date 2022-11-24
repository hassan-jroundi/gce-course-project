package ma.sorec.gcecourse.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.sorec.gcecourse.data.ModePaiement;
import ma.sorec.gcecourse.dto.ModePaiementDTO;
import ma.sorec.gcecourse.exceptions.InternalServerException;
import ma.sorec.gcecourse.exceptions.ObjectNotFoundException;
import ma.sorec.gcecourse.mapper.ModePaiementMapper;
import ma.sorec.gcecourse.service.ModePaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/modePaiements")
@Api("ModePaiement Controller")
public class ModePaiementController {

    @Autowired
    ModePaiementService service;

    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie de l'entité identifiée par son id")
    public ModePaiementDTO get(@PathVariable("id") String id) throws ObjectNotFoundException {

        ModePaiement entity = service.get(id);
        if (entity != null) {
            return ModePaiementMapper.INSTANCE.entityToDto(entity);
        } else {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
    }

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées")
    public List<ModePaiementDTO> getAll() {
        return ModePaiementMapper.INSTANCE.entitiesToDTOs(service.listAll());
    }

    @PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Enregistre une nouvelle'entité")
    public ModePaiementDTO save(@RequestBody ModePaiementDTO dto) throws InternalServerException {

        try {
            ModePaiement entity = service.save(ModePaiementMapper.INSTANCE.dtoToEntity(dto));
            return ModePaiementMapper.INSTANCE.entityToDto(entity);
        } catch (Exception e) {
            throw new InternalServerException("IE", "Erreur lors de la création de l'objet");
        }
    }

    @PutMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "met à jour une entité")
    public ModePaiementDTO update(@PathVariable("id") String id, @RequestBody ModePaiementDTO dto) {

        ModePaiement entity = service.get(id);
        if (entity == null) {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
        entity = service.save(ModePaiementMapper.INSTANCE.dtoToEntity(dto));
        return ModePaiementMapper.INSTANCE.entityToDto(entity);
    }

}
