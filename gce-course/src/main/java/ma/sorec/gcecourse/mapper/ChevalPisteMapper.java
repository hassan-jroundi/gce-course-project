package ma.sorec.gcecourse.mapper;

import ma.sorec.gcecourse.data.ChevalPiste;
import ma.sorec.gcecourse.dto.ChevalPisteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChevalPisteMapper {

    ChevalPisteMapper INSTANCE = Mappers.getMapper(ChevalPisteMapper.class);

    ChevalPisteDTO entityToDto(ChevalPiste entity);

    ChevalPiste dtoToEntity(ChevalPisteDTO dto);

    List<ChevalPisteDTO> entitiesToDTOs(List<ChevalPiste> entities);

    List<ChevalPiste> dtosToEntities(List<ChevalPisteDTO> dtos);

}
