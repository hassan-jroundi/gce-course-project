package ma.sorec.gcecourse.mapper;

import ma.sorec.gcecourse.data.Personne;
import ma.sorec.gcecourse.dto.PersonneDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersonneMapper {

    PersonneMapper INSTANCE = Mappers.getMapper(PersonneMapper.class);

    PersonneDTO entityToDto(Personne entity);

    Personne dtoToEntity(PersonneDTO dto);

    List<PersonneDTO> entitiesToDTOs(List<Personne> entities);

    List<Personne> dtosToEntities(List<PersonneDTO> dtos);

}
