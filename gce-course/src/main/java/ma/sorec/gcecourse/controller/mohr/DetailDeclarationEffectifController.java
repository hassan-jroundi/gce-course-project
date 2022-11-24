package ma.sorec.gcecourse.controller.mohr;

import io.swagger.annotations.Api;
import ma.sorec.gcecourse.data.mohr.DetailDeclarationEffectif;
import ma.sorec.gcecourse.exceptions.ObjectNotFoundException;
import ma.sorec.gcecourse.repository.mohr.DetailDeclarationEffectifRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mohr-detailDeclarationEffectif")
@Api("MOHR Detail Declaration Effectif Controller")
public class DetailDeclarationEffectifController {

    @Autowired
    private DetailDeclarationEffectifRepository repository;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<DetailDeclarationEffectif> getAll() {
        return repository.findAll();
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public DetailDeclarationEffectif get(@PathVariable("id") String id) throws ObjectNotFoundException {

        DetailDeclarationEffectif entity = repository.findById(id).get();
        if (entity != null) {
            return entity;
        } else {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
    }


}
