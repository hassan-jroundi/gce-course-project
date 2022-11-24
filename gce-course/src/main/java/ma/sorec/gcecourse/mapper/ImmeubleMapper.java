package ma.sorec.gcecourse.mapper;

import ma.sorec.gcecourse.data.Immeuble;
import ma.sorec.gcecourse.dto.ImmeubleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImmeubleMapper {

    ImmeubleMapper INSTANCE = Mappers.getMapper(ImmeubleMapper.class);

    ImmeubleDTO entityToDto(Immeuble entity);

    Immeuble dtoToEntity(ImmeubleDTO dto);

    List<ImmeubleDTO> entitiesToDTOs(List<Immeuble> entities);

    List<Immeuble> dtosToEntities(List<ImmeubleDTO> dtos);

}
