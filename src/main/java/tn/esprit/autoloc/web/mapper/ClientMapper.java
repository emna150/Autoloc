package tn.esprit.autoloc.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.web.dto.ClientDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ClientMapper {

    ClientDTO toDto(Client entity);

    @Mapping(target = "reservations", ignore = true)
    Client toEntity(ClientDTO dto);
}
