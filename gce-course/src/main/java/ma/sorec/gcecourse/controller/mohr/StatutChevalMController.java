package ma.sorec.gcecourse.controller.mohr;

import io.swagger.annotations.Api;
import ma.sorec.gcecourse.data.mohr.StatutChevalM;
import ma.sorec.gcecourse.exceptions.ObjectNotFoundException;
import ma.sorec.gcecourse.repository.mohr.StatutChevalMRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mohr-statutChevals")
@Api("MOHR Statut Cheval Controller")
public class StatutChevalMController {

    @Autowired
    StatutChevalMRepository repository;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<StatutChevalM> getAll() {
        return repository.findAll();
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public StatutChevalM get(@PathVariable("id") String id) throws ObjectNotFoundException {

        StatutChevalM entity = repository.findById(id).get();
        if (entity != null) {
            return entity;
        } else {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
    }
}
