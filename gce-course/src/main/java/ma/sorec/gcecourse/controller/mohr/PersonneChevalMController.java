package ma.sorec.gcecourse.controller.mohr;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.sorec.gcecourse.data.mohr.PersonneChevalM;
import ma.sorec.gcecourse.exceptions.ObjectNotFoundException;
import ma.sorec.gcecourse.repository.mohr.PersonneChevalMRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mohr-personneChevals")
@Api("MOHR Personne Cheval Controller")
public class PersonneChevalMController {

    @Autowired
    private PersonneChevalMRepository repository;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées", nickname = "getAllMohrPersonneChevals")
    public List<PersonneChevalM> getAll() {
        return repository.findAll();
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie de l'entité identifiée par son id", nickname = "getOneMohrPersonneCheval")
    public PersonneChevalM get(@PathVariable("id") String id) throws ObjectNotFoundException {

        PersonneChevalM entity = repository.findById(id).get();
        if (entity != null) {
            return entity;
        } else {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
    }

    @GetMapping(value = "/search", produces = {MediaType.APPLICATION_JSON_VALUE})
    public PersonneChevalM getByIdChevalAndCodeNatureRelation(@RequestParam(required = false, name = "idCheval", defaultValue = "") String idCheval,
                                                              @RequestParam(required = false, name = "codeNatureRelation", defaultValue = "") String codeNatureRelation) {
        PersonneChevalM entity = repository.findByIdChevalAndCodeNatureRelationAndDateFinIsNull(idCheval, codeNatureRelation);
        if (entity != null) {
            return entity;
        } else {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
    }

    @GetMapping(value = "/search2", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<PersonneChevalM> getByIdPersonneAndCodeNatureRelation(@RequestParam(required = false, name = "idPersonne", defaultValue = "") String idPersonne,
                                                              @RequestParam(required = false, name = "codeNatureRelation", defaultValue = "") String codeNatureRelation) {
        List<PersonneChevalM> entities = repository.findAllByIdPersonneAndCodeNatureRelationAndDateFinIsNull(idPersonne, codeNatureRelation);
        if (entities != null) {
            return entities;
        } else {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
    }
}
