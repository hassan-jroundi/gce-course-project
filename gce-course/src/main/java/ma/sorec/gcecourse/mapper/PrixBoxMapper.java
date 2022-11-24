package ma.sorec.gcecourse.mapper;

import ma.sorec.gcecourse.data.PrixBox;
import ma.sorec.gcecourse.dto.PrixBoxDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PrixBoxMapper {

    PrixBoxMapper INSTANCE = Mappers.getMapper(PrixBoxMapper.class);

    PrixBoxDTO entityToDto(PrixBox entity);

    PrixBox dtoToEntity(PrixBoxDTO dto);

    List<PrixBoxDTO> entitiesToDTOs(List<PrixBox> entities);

    List<PrixBox> dtosToEntities(List<PrixBoxDTO> dtos);

}
