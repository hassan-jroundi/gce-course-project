package ma.sorec.gcecourse.mapper;

import ma.sorec.gcecourse.data.Lit;
import ma.sorec.gcecourse.dto.LitDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LitMapper {

    LitMapper INSTANCE = Mappers.getMapper(LitMapper.class);

    LitDTO entityToDto(Lit entity);

    Lit dtoToEntity(LitDTO dto);

    List<LitDTO> entitiesToDTOs(List<Lit> entities);

    List<Lit> dtosToEntities(List<LitDTO> dtos);

}
