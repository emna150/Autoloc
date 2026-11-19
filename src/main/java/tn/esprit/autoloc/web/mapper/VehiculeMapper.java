package tn.esprit.autoloc.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.web.dto.VehiculeDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface VehiculeMapper {

    @Mapping(target = "id", source = "idVehicule")
    @Mapping(target = "agenceId", source = "agence.id")
    VehiculeDTO toDto(Vehicule entity);

    @Mapping(target = "idVehicule", source = "id")
    @Mapping(target = "agence", ignore = true)
    @Mapping(target = "reservations", ignore = true)
    @Mapping(target = "maintenances", ignore = true)
    @Mapping(target = "equipements", ignore = true)
    Vehicule toEntity(VehiculeDTO dto);
}
