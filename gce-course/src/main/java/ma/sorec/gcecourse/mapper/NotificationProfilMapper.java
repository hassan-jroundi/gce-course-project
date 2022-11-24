package ma.sorec.gcecourse.mapper;

import ma.sorec.gcecourse.data.NotificationProfil;
import ma.sorec.gcecourse.dto.NotificationProfilDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationProfilMapper {

    NotificationProfilMapper INSTANCE = Mappers.getMapper(NotificationProfilMapper.class);

    NotificationProfilDTO entityToDto(NotificationProfil entity);

    NotificationProfil dtoToEntity(NotificationProfilDTO dto);

    List<NotificationProfilDTO> entitiesToDTOs(List<NotificationProfil> entities);

    List<NotificationProfil> dtosToEntities(List<NotificationProfilDTO> dtos);

}
