package ma.sorec.gcecourse.controller;

import io.swagger.annotations.ApiOperation;
import ma.sorec.gcecourse.data.RelationLAD;
import ma.sorec.gcecourse.dto.RelationLADDTO;
import ma.sorec.gcecourse.exceptions.ObjectNotFoundException;
import ma.sorec.gcecourse.mapper.RelationLADMapper;
import ma.sorec.gcecourse.service.RelationLADService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/relationsLADs")
public class RelationLADController {

    @Autowired
    RelationLADService service;

    @GetMapping(value = "/personne/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la relation LAD selon Id Personne")
    public RelationLADDTO getByPersonneId(@PathVariable("id") String id) throws ObjectNotFoundException {

        RelationLAD entity = service.getByPersonneIdAndDateFinContratIsNull(id);
        if (entity != null) {
            return RelationLADMapper.INSTANCE.entityToDto(entity);
        } else {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
    }
}
