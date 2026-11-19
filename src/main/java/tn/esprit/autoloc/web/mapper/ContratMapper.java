package tn.esprit.autoloc.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.web.dto.ContratDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR, uses = PaiementMapper.class)
public interface ContratMapper {

    @Mapping(target = "reservationId", source = "reservation.id")
    ContratDTO toDto(Contrat entity);

    @Mapping(target = "reservation", ignore = true)
    @Mapping(target = "valide", defaultValue = "false")
    Contrat toEntity(ContratDTO dto);
}
