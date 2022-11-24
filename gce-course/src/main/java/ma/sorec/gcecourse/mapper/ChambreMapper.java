package ma.sorec.gcecourse.mapper;

import ma.sorec.gcecourse.data.Chambre;
import ma.sorec.gcecourse.dto.ChambreDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChambreMapper {

    ChambreMapper INSTANCE = Mappers.getMapper(ChambreMapper.class);

    ChambreDTO entityToDto(Chambre entity);

    Chambre dtoToEntity(ChambreDTO dto);

    List<ChambreDTO> entitiesToDTOs(List<Chambre> entities);

    List<Chambre> dtosToEntities(List<ChambreDTO> dtos);

}
