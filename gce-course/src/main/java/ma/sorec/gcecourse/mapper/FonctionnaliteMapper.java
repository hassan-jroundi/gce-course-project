package ma.sorec.gcecourse.mapper;

import ma.sorec.gcecourse.data.Fonctionnalite;
import ma.sorec.gcecourse.dto.FonctionnaliteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FonctionnaliteMapper {

    FonctionnaliteMapper INSTANCE = Mappers.getMapper(FonctionnaliteMapper.class);

    FonctionnaliteDTO entityToDto(Fonctionnalite entity);

    Fonctionnalite dtoToEntity(FonctionnaliteDTO dto);

    List<FonctionnaliteDTO> entitiesToDTOs(List<Fonctionnalite> entities);

    List<Fonctionnalite> dtosToEntities(List<FonctionnaliteDTO> dtos);

}
