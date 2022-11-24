package ma.sorec.gcecourse.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.sorec.gcecourse.Utils.SharedUtil;
import ma.sorec.gcecourse.data.Utilisateur;
import ma.sorec.gcecourse.dto.UtilisateurDTO;
import ma.sorec.gcecourse.exceptions.ObjectNotFoundException;
import ma.sorec.gcecourse.mapper.UtilisateurMapper;
import ma.sorec.gcecourse.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/utilisateurs")
@Api("Utilisateur Controller")
public class UtilisateurController {

    @Autowired
    UtilisateurService service;


    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie de l'entité identifiée par son id")
    public UtilisateurDTO get(@PathVariable("id") String id) throws ObjectNotFoundException {

        Long idL = SharedUtil.getIdLong(id);
        Utilisateur entity = service.get(idL);
        if (entity != null) {
            return UtilisateurMapper.INSTANCE.entityToDto(entity);
        } else {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées")
    public List<UtilisateurDTO> getAll() {
        return UtilisateurMapper.INSTANCE.entitiesToDTOs(service.chargerListeUtilisateurs());
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Enregistre une nouvelle'entité")
    public UtilisateurDTO save(@RequestBody UtilisateurDTO dto) throws Exception {

        Utilisateur entity = service.creerUtilisateur(UtilisateurMapper.INSTANCE.dtoToEntity(dto));
        return UtilisateurMapper.INSTANCE.entityToDto(entity);
    }

    @PutMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "met à jour une entité")
    public UtilisateurDTO update(@PathVariable("id") String id, @RequestBody UtilisateurDTO dto) {

        return UtilisateurMapper.INSTANCE.entityToDto(service.modifierUtilisateur(UtilisateurMapper.INSTANCE.dtoToEntity(dto)));
    }

    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    @ApiOperation(value = "Supprime une entité")
    public void delete(@PathVariable("id") String id) {

        service.supprimerUtilisateur(SharedUtil.getIdLong(id));

    }

    @GetMapping(value = "changerMotDePasse", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Change le mot de passe d'un utilisateur")
    public void changerMotDePasse(@RequestParam(required = false, name = "id") String id,
                                  @RequestParam(required = false, name = "motDePasse") String motDePasse) throws Exception {
        service.changerMotDePasse(new Long(id), motDePasse);
    }

}
