package ma.sorec.gcecourse.mapper;

import ma.sorec.gcecourse.data.Notification;
import ma.sorec.gcecourse.dto.NotificationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

    NotificationDTO entityToDto(Notification entity);

    Notification dtoToEntity(NotificationDTO dto);

    List<NotificationDTO> entitiesToDTOs(List<Notification> entities);

    List<Notification> dtosToEntities(List<NotificationDTO> dtos);

}
