package ma.sorec.gcecourse.mapper;

import ma.sorec.gcecourse.data.GroupeUtilisateur;
import ma.sorec.gcecourse.dto.GroupeUtilisateurDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupeUtilisateurMapper {

    GroupeUtilisateurMapper INSTANCE = Mappers.getMapper(GroupeUtilisateurMapper.class);

    GroupeUtilisateurDTO entityToDto(GroupeUtilisateur entity);

    GroupeUtilisateur dtoToEntity(GroupeUtilisateurDTO dto);

    List<GroupeUtilisateurDTO> entitiesToDTOs(List<GroupeUtilisateur> entities);

    List<GroupeUtilisateur> dtosToEntities(List<GroupeUtilisateurDTO> dtos);

}
