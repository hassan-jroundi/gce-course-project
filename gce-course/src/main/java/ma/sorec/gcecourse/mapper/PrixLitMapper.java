package ma.sorec.gcecourse.mapper;

import ma.sorec.gcecourse.data.PrixLit;
import ma.sorec.gcecourse.dto.PrixLitDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PrixLitMapper {

    PrixLitMapper INSTANCE = Mappers.getMapper(PrixLitMapper.class);

    PrixLitDTO entityToDto(PrixLit entity);

    PrixLit dtoToEntity(PrixLitDTO dto);

    List<PrixLitDTO> entitiesToDTOs(List<PrixLit> entities);

    List<PrixLit> dtosToEntities(List<PrixLitDTO> dtos);

}
