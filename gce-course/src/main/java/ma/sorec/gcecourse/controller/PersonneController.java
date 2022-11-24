package ma.sorec.gcecourse.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.sorec.gcecourse.data.Personne;
import ma.sorec.gcecourse.dto.PersonneDTO;
import ma.sorec.gcecourse.exceptions.ObjectNotFoundException;
import ma.sorec.gcecourse.mapper.PersonneMapper;
import ma.sorec.gcecourse.service.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/personnes")
@Api("Personne Controller")
public class PersonneController {

    @Autowired
    PersonneService service;

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie de l'entité identifiée par son id")
    public PersonneDTO get(@PathVariable("id") String id) throws ObjectNotFoundException {

        Personne entity = service.get(id);
        if (entity != null) {
            return PersonneMapper.INSTANCE.entityToDto(entity);
        } else {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
    }

    @GetMapping(value = "/employeur/{idLAD}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie l'employeur du LAD selong Id Personne")
    public PersonneDTO getLADEmployeur(@PathVariable("idLAD") String idLAD) throws ObjectNotFoundException {

        Personne entity = service.findLADEmployeur(idLAD);
        if (entity != null) {
            return PersonneMapper.INSTANCE.entityToDto(entity);
        } else {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées")
    public List<PersonneDTO> getAll() {
        return PersonneMapper.INSTANCE.entitiesToDTOs(service.listAll());
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Enregistre une nouvelle'entité")
    public PersonneDTO save(@RequestBody PersonneDTO dto) {
        Personne entity = service.save(PersonneMapper.INSTANCE.dtoToEntity(dto));
        return PersonneMapper.INSTANCE.entityToDto(entity);
    }

    @PutMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "met à jour une entité")
    public PersonneDTO update(@PathVariable("id") String id, @RequestBody PersonneDTO dto) {

        Personne entity = service.update(PersonneMapper.INSTANCE.dtoToEntity(dto));
        return PersonneMapper.INSTANCE.entityToDto(entity);
    }

    @GetMapping(value = "/search", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Recherche selon Nom, Prénom, CIN, Code Nature Personne")
    public List<PersonneDTO> getAllByNomAndPrenomAndCinAndCodeNaturePersonne(@RequestParam(required = false, name = "nom", defaultValue = "") String nom,
                                                                             @RequestParam(required = false, name = "prenom", defaultValue = "") String prenom,
                                                                             @RequestParam(required = false, name = "cin", defaultValue = "") String cin,
                                                                             @RequestParam(required = false, name = "codeNaturePersonne", defaultValue = "") String codeNaturePersonne) {
        if (nom.length() == 0 && prenom.length() == 0 && cin.length() == 0 && codeNaturePersonne.length() == 0) {
            return PersonneMapper.INSTANCE.entitiesToDTOs(service.listAll());
        } else {
            return PersonneMapper.INSTANCE.entitiesToDTOs(service.findAllByNomAndPrenomAndNumeroPieceIdentiteAndCodeNaturePersonne(nom, prenom, cin, codeNaturePersonne));
        }
    }

    @GetMapping(value = "/search2", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Recherche selon Nom, Prénom, CIN, Désignation, Raison Sociale")
    public List<PersonneDTO> getAllByCriteria(@RequestParam(required = false, name = "nom", defaultValue = "") String nom,
                                              @RequestParam(required = false, name = "prenom", defaultValue = "") String prenom,
                                              @RequestParam(required = false, name = "cin", defaultValue = "") String cin,
                                              @RequestParam(required = false, name = "designation", defaultValue = "") String designation,
                                              @RequestParam(required = false, name = "raisonSociale", defaultValue = "") String raisonSociale) {
        return PersonneMapper.INSTANCE.entitiesToDTOs(service.findAllByCriteria(nom, prenom, cin, designation, raisonSociale).stream().distinct().collect(Collectors.toList()));
    }

    @GetMapping(value = "/search3", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Recherche selon Nom, Prénom, CIN, Désignation, Raison Sociale / Méthode 2")
    public List<PersonneDTO> getAllByCriteriaBis(@RequestParam(required = false, name = "nom", defaultValue = "") String nom,
                                              @RequestParam(required = false, name = "prenom", defaultValue = "") String prenom,
                                              @RequestParam(required = false, name = "cin", defaultValue = "") String cin,
                                              @RequestParam(required = false, name = "designation", defaultValue = "") String designation,
                                              @RequestParam(required = false, name = "raisonSociale", defaultValue = "") String raisonSociale) {
        return PersonneMapper.INSTANCE.entitiesToDTOs(service.findAllByCriteriaBis(nom, prenom, cin, designation, raisonSociale).stream().distinct().collect(Collectors.toList()));
    }

    @DeleteMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    @ApiOperation(value = "Supprime une entité")
    public void delete(@PathVariable("id") String id) {

        service.delete(id);

    }
}
