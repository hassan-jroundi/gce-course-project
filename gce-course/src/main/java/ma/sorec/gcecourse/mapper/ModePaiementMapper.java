package ma.sorec.gcecourse.mapper;

import ma.sorec.gcecourse.data.ModePaiement;
import ma.sorec.gcecourse.dto.ModePaiementDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ModePaiementMapper {

    ModePaiementMapper INSTANCE = Mappers.getMapper(ModePaiementMapper.class);

    ModePaiementDTO entityToDto(ModePaiement entity);

    ModePaiement dtoToEntity(ModePaiementDTO dto);

    List<ModePaiementDTO> entitiesToDTOs(List<ModePaiement> entities);

    List<ModePaiement> dtosToEntities(List<ModePaiementDTO> dtos);

}
