package ma.sorec.gcecourse.mapper;

import ma.sorec.gcecourse.data.ProfilFonctionnaliteAction;
import ma.sorec.gcecourse.dto.ProfilFonctionnaliteActionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProfilFonctionnaliteActionMapper {

    ProfilFonctionnaliteActionMapper INSTANCE = Mappers.getMapper(ProfilFonctionnaliteActionMapper.class);

    ProfilFonctionnaliteActionDTO entityToDto(ProfilFonctionnaliteAction entity);

    ProfilFonctionnaliteAction dtoToEntity(ProfilFonctionnaliteActionDTO dto);

    List<ProfilFonctionnaliteActionDTO> entitiesToDTOs(List<ProfilFonctionnaliteAction> entities);

    List<ProfilFonctionnaliteAction> dtosToEntities(List<ProfilFonctionnaliteActionDTO> dtos);

}
