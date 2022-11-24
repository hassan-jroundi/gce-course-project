package ma.sorec.gcecourse.mapper;

import ma.sorec.gcecourse.data.PrixPiste;
import ma.sorec.gcecourse.dto.PrixPisteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PrixPisteMapper {

    PrixPisteMapper INSTANCE = Mappers.getMapper(PrixPisteMapper.class);

    PrixPisteDTO entityToDto(PrixPiste entity);

    PrixPiste dtoToEntity(PrixPisteDTO dto);

    List<PrixPisteDTO> entitiesToDTOs(List<PrixPiste> entities);

    List<PrixPiste> dtosToEntities(List<PrixPisteDTO> dtos);

}
