package ma.sorec.gcecourse.mapper;

import ma.sorec.gcecourse.data.Cheval;
import ma.sorec.gcecourse.dto.ChevalDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChevalMapper {

    ChevalMapper INSTANCE = Mappers.getMapper(ChevalMapper.class);

    ChevalDTO entityToDto(Cheval entity);

    Cheval dtoToEntity(ChevalDTO dto);

    List<ChevalDTO> entitiesToDTOs(List<Cheval> entities);

    List<Cheval> dtosToEntities(List<ChevalDTO> dtos);

}
