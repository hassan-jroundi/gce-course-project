package ma.sorec.gcecourse.mapper;

import ma.sorec.gcecourse.data.Box;
import ma.sorec.gcecourse.dto.BoxDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BoxMapper {

    BoxMapper INSTANCE = Mappers.getMapper(BoxMapper.class);

    BoxDTO entityToDto(Box entity);

    Box dtoToEntity(BoxDTO dto);

    List<BoxDTO> entitiesToDTOs(List<Box> entities);

    List<Box> dtosToEntities(List<BoxDTO> dtos);

}
