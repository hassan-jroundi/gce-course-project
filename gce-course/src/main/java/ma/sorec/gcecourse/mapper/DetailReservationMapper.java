package ma.sorec.gcecourse.mapper;

import ma.sorec.gcecourse.data.DetailReservation;
import ma.sorec.gcecourse.dto.DetailReservationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DetailReservationMapper {

    DetailReservationMapper INSTANCE = Mappers.getMapper(DetailReservationMapper.class);

    DetailReservationDTO entityToDto(DetailReservation entity);

    DetailReservation dtoToEntity(DetailReservationDTO dto);

    List<DetailReservationDTO> entitiesToDTOs(List<DetailReservation> entities);

    List<DetailReservation> dtosToEntities(List<DetailReservationDTO> dtos);

}
