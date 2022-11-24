package ma.sorec.gcecourse.mapper;

import ma.sorec.gcecourse.data.ChevalReservationEtat;
import ma.sorec.gcecourse.dto.ChevalReservationEtatDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChevalReservationEtatMapper {

    ChevalReservationEtatMapper INSTANCE = Mappers.getMapper(ChevalReservationEtatMapper.class);

    ChevalReservationEtatDTO entityToDto(ChevalReservationEtat entity);

    ChevalReservationEtat dtoToEntity(ChevalReservationEtatDTO dto);

    List<ChevalReservationEtatDTO> entitiesToDTOs(List<ChevalReservationEtat> entities);

    List<ChevalReservationEtat> dtosToEntities(List<ChevalReservationEtatDTO> dtos);

}
