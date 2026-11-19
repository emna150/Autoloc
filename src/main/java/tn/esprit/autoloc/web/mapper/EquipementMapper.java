package tn.esprit.autoloc.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.web.dto.EquipementDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface EquipementMapper {

    EquipementDTO toDto(Equipement entity);

    @Mapping(target = "vehicules", ignore = true)
    Equipement toEntity(EquipementDTO dto);
}
