package ma.sorec.gcecourse.mapper;

import ma.sorec.gcecourse.data.Reservation;
import ma.sorec.gcecourse.dto.ReservationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    ReservationMapper INSTANCE = Mappers.getMapper(ReservationMapper.class);

    ReservationDTO entityToDto(Reservation entity);

    Reservation dtoToEntity(ReservationDTO dto);

    List<ReservationDTO> entitiesToDTOs(List<Reservation> entities);

    List<Reservation> dtosToEntities(List<ReservationDTO> dtos);

}
