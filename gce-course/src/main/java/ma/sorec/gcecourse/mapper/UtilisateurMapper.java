package ma.sorec.gcecourse.mapper;

import ma.sorec.gcecourse.data.Utilisateur;
import ma.sorec.gcecourse.dto.UtilisateurDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UtilisateurMapper {

    UtilisateurMapper INSTANCE = Mappers.getMapper(UtilisateurMapper.class);

    UtilisateurDTO entityToDto(Utilisateur entity);

    Utilisateur dtoToEntity(UtilisateurDTO dto);

    List<UtilisateurDTO> entitiesToDTOs(List<Utilisateur> entities);

    List<Utilisateur> dtosToEntities(List<UtilisateurDTO> dtos);

}
