package ma.sorec.gcecourse.mapper;

import ma.sorec.gcecourse.data.UtilisateurSite;
import ma.sorec.gcecourse.dto.UtilisateurSiteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UtilisateurSiteMapper {

    UtilisateurSiteMapper INSTANCE = Mappers.getMapper(UtilisateurSiteMapper.class);

    UtilisateurSiteDTO entityToDto(UtilisateurSite entity);

    UtilisateurSite dtoToEntity(UtilisateurSiteDTO dto);

    List<UtilisateurSiteDTO> entitiesToDTOs(List<UtilisateurSite> entities);

    List<UtilisateurSite> dtosToEntities(List<UtilisateurSiteDTO> dtos);

}
