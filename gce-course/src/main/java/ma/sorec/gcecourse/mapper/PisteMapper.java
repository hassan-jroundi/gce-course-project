package ma.sorec.gcecourse.mapper;

import ma.sorec.gcecourse.data.Piste;
import ma.sorec.gcecourse.dto.PisteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PisteMapper {

    PisteMapper INSTANCE = Mappers.getMapper(PisteMapper.class);

    PisteDTO entityToDto(Piste entity);

    Piste dtoToEntity(PisteDTO dto);

    List<PisteDTO> entitiesToDTOs(List<Piste> entities);

    List<Piste> dtosToEntities(List<PisteDTO> dtos);

}
