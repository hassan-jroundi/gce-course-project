package ma.sorec.gcecourse.mapper;

import ma.sorec.gcecourse.data.UtilisateurSession;
import ma.sorec.gcecourse.dto.UtilisateurSessionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UtilisateurSessionMapper {

    UtilisateurSessionMapper INSTANCE = Mappers.getMapper(UtilisateurSessionMapper.class);

    UtilisateurSessionDTO entityToDto(UtilisateurSession entity);

    UtilisateurSession dtoToEntity(UtilisateurSessionDTO dto);

    List<UtilisateurSessionDTO> entitiesToDTOs(List<UtilisateurSession> entities);

    List<UtilisateurSession> dtosToEntities(List<UtilisateurSessionDTO> dtos);

}
