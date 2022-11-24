package ma.sorec.gcecourse.mapper;

import ma.sorec.gcecourse.data.PersonneLit;
import ma.sorec.gcecourse.dto.PersonneLitDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersonneLitMapper {

    PersonneLitMapper INSTANCE = Mappers.getMapper(PersonneLitMapper.class);

    PersonneLitDTO entityToDto(PersonneLit entity);

    PersonneLit dtoToEntity(PersonneLitDTO dto);

    List<PersonneLitDTO> entitiesToDTOs(List<PersonneLit> entities);

    List<PersonneLit> dtosToEntities(List<PersonneLitDTO> dtos);

}
