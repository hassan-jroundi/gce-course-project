package ma.sorec.gcecourse.mapper;

import ma.sorec.gcecourse.data.StatutReservation;
import ma.sorec.gcecourse.dto.StatutReservationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StatutReservationMapper {

    StatutReservationMapper INSTANCE = Mappers.getMapper(StatutReservationMapper.class);

    StatutReservationDTO entityToDto(StatutReservation entity);

    StatutReservation dtoToEntity(StatutReservationDTO dto);

    List<StatutReservationDTO> entitiesToDTOs(List<StatutReservation> entities);

    List<StatutReservation> dtosToEntities(List<StatutReservationDTO> dtos);

}
