package ma.sorec.gcecourse.mapper;

import ma.sorec.gcecourse.data.FonctionnaliteAction;
import ma.sorec.gcecourse.dto.FonctionnaliteActionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FonctionnaliteActionMapper {

    FonctionnaliteActionMapper INSTANCE = Mappers.getMapper(FonctionnaliteActionMapper.class);

    FonctionnaliteActionDTO entityToDto(FonctionnaliteAction entity);

    FonctionnaliteAction dtoToEntity(FonctionnaliteActionDTO dto);

    List<FonctionnaliteActionDTO> entitiesToDTOs(List<FonctionnaliteAction> entities);

    List<FonctionnaliteAction> dtosToEntities(List<FonctionnaliteActionDTO> dtos);

}
