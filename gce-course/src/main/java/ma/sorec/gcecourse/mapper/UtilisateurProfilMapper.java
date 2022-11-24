package ma.sorec.gcecourse.mapper;

import ma.sorec.gcecourse.data.UtilisateurProfil;
import ma.sorec.gcecourse.dto.UtilisateurProfilDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UtilisateurProfilMapper {

    UtilisateurProfilMapper INSTANCE = Mappers.getMapper(UtilisateurProfilMapper.class);

    UtilisateurProfilDTO entityToDto(UtilisateurProfil entity);

    UtilisateurProfil dtoToEntity(UtilisateurProfilDTO dto);

    List<UtilisateurProfilDTO> entitiesToDTOs(List<UtilisateurProfil> entities);

    List<UtilisateurProfil> dtosToEntities(List<UtilisateurProfilDTO> dtos);

}
