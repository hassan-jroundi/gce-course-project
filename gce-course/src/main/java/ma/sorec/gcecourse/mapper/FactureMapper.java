package ma.sorec.gcecourse.mapper;

import ma.sorec.gcecourse.data.Facture;
import ma.sorec.gcecourse.dto.FactureDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FactureMapper {

    FactureMapper INSTANCE = Mappers.getMapper(FactureMapper.class);

    FactureDTO entityToDto(Facture entity);

    Facture dtoToEntity(FactureDTO dto);

    List<FactureDTO> entitiesToDTOs(List<Facture> entities);

    List<Facture> dtosToEntities(List<FactureDTO> dtos);

}
