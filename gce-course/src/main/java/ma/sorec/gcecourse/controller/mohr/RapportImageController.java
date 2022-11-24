package ma.sorec.gcecourse.controller.mohr;

import ma.sorec.gcecourse.data.mohr.RapportImage;
import ma.sorec.gcecourse.exceptions.ObjectNotFoundException;
import ma.sorec.gcecourse.repository.RapportImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mohr-rapportImage")
public class RapportImageController {

    @Autowired
    private RapportImageRepository repository;

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public RapportImage get(@PathVariable("id") String id) throws ObjectNotFoundException {

        return repository.findById(id).orElse(null);
    }


}
