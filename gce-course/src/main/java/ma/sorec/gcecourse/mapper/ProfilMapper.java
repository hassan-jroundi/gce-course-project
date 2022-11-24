package ma.sorec.gcecourse.mapper;

import ma.sorec.gcecourse.data.Profil;
import ma.sorec.gcecourse.data.UtilisateurProfil;
import ma.sorec.gcecourse.dto.ProfilDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProfilMapper {

    ProfilMapper INSTANCE = Mappers.getMapper(ProfilMapper.class);

    ProfilDTO entityToDto(Profil entity);

    Profil dtoToEntity(ProfilDTO dto);

    List<ProfilDTO> entitiesToDTOs(List<Profil> entities);

    List<Profil> dtosToEntities(List<ProfilDTO> dtos);

}
