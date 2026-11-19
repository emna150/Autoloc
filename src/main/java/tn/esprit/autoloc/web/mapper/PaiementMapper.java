package tn.esprit.autoloc.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.web.dto.PaiementDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PaiementMapper {

    @Mapping(target = "contratId", source = "contrat.id")
    PaiementDTO toDto(Paiement entity);

    @Mapping(target = "contrat", ignore = true)
    Paiement toEntity(PaiementDTO dto);
}
