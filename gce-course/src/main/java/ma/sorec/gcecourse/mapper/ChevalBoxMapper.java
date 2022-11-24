package ma.sorec.gcecourse.mapper;

import ma.sorec.gcecourse.data.ChevalBox;
import ma.sorec.gcecourse.dto.ChevalBoxDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChevalBoxMapper {

    ChevalBoxMapper INSTANCE = Mappers.getMapper(ChevalBoxMapper.class);

    ChevalBoxDTO entityToDto(ChevalBox entity);

    ChevalBox dtoToEntity(ChevalBoxDTO dto);

    List<ChevalBoxDTO> entitiesToDTOs(List<ChevalBox> entities);

    List<ChevalBox> dtosToEntities(List<ChevalBoxDTO> dtos);

}
