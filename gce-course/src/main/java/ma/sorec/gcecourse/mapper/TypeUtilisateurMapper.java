package ma.sorec.gcecourse.mapper;

import ma.sorec.gcecourse.data.TypeUtilisateur;
import ma.sorec.gcecourse.dto.TypeUtilisateurDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TypeUtilisateurMapper {

    TypeUtilisateurMapper INSTANCE = Mappers.getMapper(TypeUtilisateurMapper.class);

    TypeUtilisateurDTO entityToDto(TypeUtilisateur entity);

    TypeUtilisateur dtoToEntity(TypeUtilisateurDTO dto);

    List<TypeUtilisateurDTO> entitiesToDTOs(List<TypeUtilisateur> entities);

    List<TypeUtilisateur> dtosToEntities(List<TypeUtilisateurDTO> dtos);

}
