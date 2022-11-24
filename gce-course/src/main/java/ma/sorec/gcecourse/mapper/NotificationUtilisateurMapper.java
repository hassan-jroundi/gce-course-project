package ma.sorec.gcecourse.mapper;

import ma.sorec.gcecourse.data.NotificationUtilisateur;
import ma.sorec.gcecourse.dto.NotificationUtilisateurDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationUtilisateurMapper {

    NotificationUtilisateurMapper INSTANCE = Mappers.getMapper(NotificationUtilisateurMapper.class);

    NotificationUtilisateurDTO entityToDto(NotificationUtilisateur entity);

    NotificationUtilisateur dtoToEntity(NotificationUtilisateurDTO dto);

    List<NotificationUtilisateurDTO> entitiesToDTOs(List<NotificationUtilisateur> entities);

    List<NotificationUtilisateur> dtosToEntities(List<NotificationUtilisateurDTO> dtos);

}
