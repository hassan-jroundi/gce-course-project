package ma.sorec.gcecourse.mapper;

import ma.sorec.gcecourse.data.Box;
import ma.sorec.gcecourse.data.RelationLAD;
import ma.sorec.gcecourse.dto.BoxDTO;
import ma.sorec.gcecourse.dto.RelationLADDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RelationLADMapper {

    RelationLADMapper INSTANCE = Mappers.getMapper(RelationLADMapper.class);

    RelationLADDTO entityToDto(RelationLAD entity);

    RelationLAD dtoToEntity(RelationLADDTO dto);

    List<RelationLADDTO> entitiesToDTOs(List<RelationLAD> entities);

    List<RelationLAD> dtosToEntities(List<RelationLADDTO> dtos);

}
