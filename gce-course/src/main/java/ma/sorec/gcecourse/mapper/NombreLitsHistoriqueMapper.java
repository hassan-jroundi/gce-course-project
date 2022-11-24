package ma.sorec.gcecourse.mapper;

import ma.sorec.gcecourse.data.NombreLitsHistorique;
import ma.sorec.gcecourse.dto.NombreLitsHistoriqueDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NombreLitsHistoriqueMapper {

    NombreLitsHistoriqueMapper INSTANCE = Mappers.getMapper(NombreLitsHistoriqueMapper.class);

    NombreLitsHistoriqueDTO entityToDto(NombreLitsHistorique entity);

    NombreLitsHistorique dtoToEntity(NombreLitsHistoriqueDTO dto);

    List<NombreLitsHistoriqueDTO> entitiesToDTOs(List<NombreLitsHistorique> entities);

    List<NombreLitsHistorique> dtosToEntities(List<NombreLitsHistoriqueDTO> dtos);

}
