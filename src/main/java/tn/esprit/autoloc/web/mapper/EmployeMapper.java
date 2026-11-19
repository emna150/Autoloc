package tn.esprit.autoloc.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.web.dto.EmployeDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface EmployeMapper {

    @Mapping(target = "agenceId", source = "agence.id")
    EmployeDTO toDto(Employe entity);

    @Mapping(target = "agence", ignore = true)
    Employe toEntity(EmployeDTO dto);
}
