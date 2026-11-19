package tn.esprit.autoloc.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.web.dto.MaintenanceDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MaintenanceMapper {

    @Mapping(target = "vehiculeId", source = "vehicule.idVehicule")
    MaintenanceDTO toDto(Maintenance entity);

    @Mapping(target = "vehicule", ignore = true)
    Maintenance toEntity(MaintenanceDTO dto);
}
