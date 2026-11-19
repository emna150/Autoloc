package tn.esprit.autoloc.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.web.dto.AgenceDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AgenceMapper {

    AgenceDTO toDto(Agence entity);

    @Mapping(target = "employes", ignore = true)
    @Mapping(target = "vehicules", ignore = true)
    Agence toEntity(AgenceDTO dto);
}
