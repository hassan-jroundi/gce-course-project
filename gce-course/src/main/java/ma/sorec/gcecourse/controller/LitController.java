package ma.sorec.gcecourse.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.sorec.gcecourse.Utils.SharedUtil;
import ma.sorec.gcecourse.data.Lit;
import ma.sorec.gcecourse.dto.LitDTO;
import ma.sorec.gcecourse.exceptions.ObjectNotFoundException;
import ma.sorec.gcecourse.mapper.LitMapper;
import ma.sorec.gcecourse.service.LitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lits")
@Api("Lit Controller")
public class LitController {

    @Autowired
    LitService service;

    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie de l'entité identifiée par son id")
    public LitDTO get(@PathVariable("id") String id) throws ObjectNotFoundException {

        Long idL = SharedUtil.getIdLong(id);
        Lit entity = service.get(idL);
        if (entity != null) {
            return LitMapper.INSTANCE.entityToDto(entity);
        } else {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
    }

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées")
    public List<LitDTO> getAll() {
        return LitMapper.INSTANCE.entitiesToDTOs(service.listAll());
    }

    @PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Enregistre une nouvelle'entité")
    public LitDTO save(@RequestBody LitDTO dto) {
        Lit entity = service.save(LitMapper.INSTANCE.dtoToEntity(dto));
        return LitMapper.INSTANCE.entityToDto(entity);
    }

    @DeleteMapping(value = "/{id}/{idSession}", produces = { MediaType.APPLICATION_JSON_VALUE })
    @ApiOperation(value = "Supprime une entité.")
    @ResponseBody
    public void delete(@PathVariable("id") String id, @PathVariable("idSession") String idSession) {

        service.delete(SharedUtil.getIdLong(id), idSession);

    }

    @PutMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "met à jour une entité")
    public LitDTO update(@PathVariable("id") String id, @RequestBody LitDTO dto) {
        Lit entity = service.update(LitMapper.INSTANCE.dtoToEntity(dto));
        return LitMapper.INSTANCE.entityToDto(entity);
    }

    @GetMapping(value = "/chambre/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées selong Id Chambre")
    public List<LitDTO> getAllByChambreId(@PathVariable("id") String id) throws ObjectNotFoundException {

        Long idL = SharedUtil.getIdLong(id);
        List<Lit> entities = service.getAllByChambreId(idL);
        return LitMapper.INSTANCE.entitiesToDTOs(entities);
    }

    @GetMapping(value = "/site/{nom}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées selon Nom Site")
    public List<LitDTO> getAllByNomSite(@PathVariable("nom") String nom) {
        return LitMapper.INSTANCE.entitiesToDTOs(service.getAllByNomSite(nom));
    }

    @GetMapping(value = "/immeuble/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées selong Id Immeuble")
    public List<LitDTO> getAllByImmeubleId(@PathVariable("id") String id) {
        Long idL = SharedUtil.getIdLong(id);
        return LitMapper.INSTANCE.entitiesToDTOs(service.getAllByImmeubleId(idL));
    }

    @GetMapping(value = "/actif/chambre/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées actives selong Id Chambre")
    public List<LitDTO> getAllActifLit(@PathVariable("id") String id) throws ObjectNotFoundException {

        Long idL = SharedUtil.getIdLong(id);
        List<Lit> entities = service.getAllActifLit(idL);
        return LitMapper.INSTANCE.entitiesToDTOs(entities);
    }

    @GetMapping(value = "/ajouter/{idChambre}/session/{idSession}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Enregistre une nouvelle'entité")
    public LitDTO ajouterLit(@PathVariable("idChambre") String idChambre,
                             @PathVariable("idSession") String idSession) {
        Lit entity = service.ajouterLit(idChambre, idSession);
        return LitMapper.INSTANCE.entityToDto(entity);
    }

}
