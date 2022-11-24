package ma.sorec.gcecourse.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.sorec.gcecourse.Utils.SharedUtil;
import ma.sorec.gcecourse.data.Notification;
import ma.sorec.gcecourse.dto.NotificationDTO;
import ma.sorec.gcecourse.exceptions.InternalServerException;
import ma.sorec.gcecourse.exceptions.ObjectNotFoundException;
import ma.sorec.gcecourse.mapper.NotificationMapper;
import ma.sorec.gcecourse.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@Api("Notification Controller")
public class NotificationController {

    @Autowired
    NotificationService service;

    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie une entité")
    public NotificationDTO get(@PathVariable("id") String id) throws ObjectNotFoundException {

        Long idL = SharedUtil.getIdLong(id);
        Notification entity = service.get(idL);
        if (entity != null) {
            return NotificationMapper.INSTANCE.entityToDto(entity);
        } else {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
    }

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées")
    public List<NotificationDTO> getAll() {
        return NotificationMapper.INSTANCE.entitiesToDTOs(service.listAll());
    }

    @PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Enregistre une entité")
    public NotificationDTO save(@RequestBody NotificationDTO dto) throws InternalServerException {

        try {
            Notification entity = service.save(NotificationMapper.INSTANCE.dtoToEntity(dto));
            return NotificationMapper.INSTANCE.entityToDto(entity);
        } catch (Exception e) {
            throw new InternalServerException("IE", "Erreur lors de la création de l'objet");
        }
    }

    @PutMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Met à jour une entité")
    public NotificationDTO update(@PathVariable("id") String id, @RequestBody NotificationDTO dto) {

        Long idL = SharedUtil.getIdLong(id);
        Notification entity = service.get(idL);
        if (entity == null) {
            throw new ObjectNotFoundException("NF", "Elément non trouvé");
        }
        entity = service.save(NotificationMapper.INSTANCE.dtoToEntity(dto));
        return NotificationMapper.INSTANCE.entityToDto(entity);
    }

    @DeleteMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    @ApiOperation(value = "Supprime une entité")
    public void delete(@PathVariable("id") String id) {

        service.delete(SharedUtil.getIdLong(id));

    }

    @GetMapping(value = "/profil/{code}/utilisateur/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste des entitées selon Code Profil")
    public List<NotificationDTO> getAllByCodeProfil(@PathVariable("code") String code, @PathVariable("id") String id) {
        return NotificationMapper.INSTANCE.entitiesToDTOs(service.listAllByCodeProfil(code, new Long(id)));
    }

    @GetMapping(value = "/profil/partiel/{code}/utilisateur/{id}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Renvoie la liste partielle des entitées selong Code Profil")
    public List<NotificationDTO> getAllByCodeProfilPartiel(@PathVariable("code") String code, @PathVariable("id") String id) {
        return NotificationMapper.INSTANCE.entitiesToDTOs(service.listAllByCodeProfilPartiel(code, new Long(id)));
    }

    @GetMapping(value = "/dejaLu/notification/{idNotification}/utilisateur/{idUtilisateur}", produces = { MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "Active déjà lu")
    public void setDejaLu(@PathVariable("idNotification") String idNotification, @PathVariable("idUtilisateur") String idUtilisateur) {
        service.setDejaLu(new Long(idNotification), new Long(idUtilisateur));
    }

}
