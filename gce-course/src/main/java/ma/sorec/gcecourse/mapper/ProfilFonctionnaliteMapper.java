package ma.sorec.gcecourse.mapper;

import ma.sorec.gcecourse.data.ProfilFonctionnalite;
import ma.sorec.gcecourse.dto.ProfilFonctionnaliteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProfilFonctionnaliteMapper {

    ProfilFonctionnaliteMapper INSTANCE = Mappers.getMapper(ProfilFonctionnaliteMapper.class);

    ProfilFonctionnaliteDTO entityToDto(ProfilFonctionnalite entity);

    ProfilFonctionnalite dtoToEntity(ProfilFonctionnaliteDTO dto);

    List<ProfilFonctionnaliteDTO> entitiesToDTOs(List<ProfilFonctionnalite> entities);

    List<ProfilFonctionnalite> dtosToEntities(List<ProfilFonctionnaliteDTO> dtos);

}
