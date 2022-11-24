package ma.sorec.gcecourse.mapper;

import ma.sorec.gcecourse.data.Site;
import ma.sorec.gcecourse.dto.SiteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SiteMapper {

    SiteMapper INSTANCE = Mappers.getMapper(SiteMapper.class);

    SiteDTO entityToDto(Site entity);

    Site dtoToEntity(SiteDTO dto);

    List<SiteDTO> entitiesToDTOs(List<Site> entities);

    List<Site> dtosToEntities(List<SiteDTO> dtos);

}
