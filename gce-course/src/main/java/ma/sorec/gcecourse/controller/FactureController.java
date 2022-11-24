package ma.sorec.gcecourse.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.sorec.gcecourse.Utils.SharedUtil;
import ma.sorec.gcecourse.data.Facture;
import ma.sorec.gcecourse.dto.FactureDTO;
import ma.sorec.gcecourse.exceptions.InternalServerException;
import ma.sorec.gcecourse.exceptions.ObjectNotFoundException;
import ma.sorec.gcecourse.mapper.FactureMapper;
import ma.sorec.gcecourse.service.FactureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/factures")
@Api("Facture Controller")
public class FactureController {

    @Autowired
    FactureService service;

    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie de l'entité identifiée par son id")
    public FactureDTO get(@PathVariable("id") String id) throws ObjectNotFoundException {

        Long idL = SharedUtil.getIdLong(id);
        Facture entity = service.get(idL);
        if (entity != null) {
            return FactureMapper.INSTANCE.entityToDto(entity);
        } else {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
    }

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées")
    public List<FactureDTO> getAll() {
        return FactureMapper.INSTANCE.entitiesToDTOs(service.listAll());
    }

    @PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Enregistre une nouvelle'entité")
    public FactureDTO save(@RequestBody FactureDTO dto) throws InternalServerException {

        try {
            Facture entity = service.save(FactureMapper.INSTANCE.dtoToEntity(dto));
            return FactureMapper.INSTANCE.entityToDto(entity);
        } catch (Exception e) {
            throw new InternalServerException("IE", "Erreur lors de la création de l'objet");
        }
    }

    @PutMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "met à jour une entité")
    public FactureDTO update(@PathVariable("id") String id, @RequestBody FactureDTO dto) {

        Long idL = SharedUtil.getIdLong(id);
        Facture entity = service.get(idL);
        if (entity == null) {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
        entity = service.save(FactureMapper.INSTANCE.dtoToEntity(dto));
        return FactureMapper.INSTANCE.entityToDto(entity);
    }

    @GetMapping(value = "/personneAFacturer/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées selon Personne à facturer")
    public List<FactureDTO> getFacturesByPersonneAFacturer(@PathVariable("id") String id) {
        return FactureMapper.INSTANCE.entitiesToDTOs(service.getAllByPersonneId(id).stream().distinct().collect(Collectors.toList()));
    }

    @GetMapping(value = "/montantHT/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie le montant HT selon IdFacture")
    public Double getMontantHTFacture(@PathVariable("id") String id) {
        return service.getMontantHTFacture(SharedUtil.getIdLong(id));
    }

    @GetMapping(value = "/mettreEnPaye/{id}/session/{idSession}/modePaiement/{codeModePaiement}")
    @ApiOperation(value = "Met en payé une facture")
    public FactureDTO mettreEnPaye(@PathVariable("id") String id, @PathVariable("idSession") String idSession, @PathVariable("codeModePaiement") String codeModePaiement) {
        return FactureMapper.INSTANCE.entityToDto(service.mettreEnPaye(new Long(id), idSession, codeModePaiement));
    }

}
