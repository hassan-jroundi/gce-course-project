package ma.sorec.gcecourse.mapper;

import ma.sorec.gcecourse.data.Ecurie;
import ma.sorec.gcecourse.dto.EcurieDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EcurieMapper {

    EcurieMapper INSTANCE = Mappers.getMapper(EcurieMapper.class);

    EcurieDTO entityToDto(Ecurie entity);

    Ecurie dtoToEntity(EcurieDTO dto);

    List<EcurieDTO> entitiesToDTOs(List<Ecurie> entities);

    List<Ecurie> dtosToEntities(List<EcurieDTO> dtos);

}
