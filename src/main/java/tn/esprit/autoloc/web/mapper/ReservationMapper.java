package tn.esprit.autoloc.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.web.dto.ReservationRequestDTO;
import tn.esprit.autoloc.web.dto.ReservationResponseDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ReservationMapper {

    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "clientNomComplet", source = "client", qualifiedByName = "nomComplet")
    @Mapping(target = "vehiculeId", source = "vehicule.idVehicule")
    @Mapping(target = "vehiculeImmatriculation", source = "vehicule.immatriculation")
    @Mapping(target = "tarifEstime", ignore = true)
    ReservationResponseDTO toDto(Reservation reservation);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "vehicule", ignore = true)
    @Mapping(target = "contrat", ignore = true)
    Reservation toEntity(ReservationRequestDTO dto);

    @Named("nomComplet")
    default String nomComplet(Client client) {
        if (client == null) {
            return null;
        }
        String prenom = client.getPrenom() == null ? "" : client.getPrenom();
        String nom = client.getNom() == null ? "" : client.getNom();
        return (prenom + " " + nom).strip();
    }
}
