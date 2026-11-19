package tn.esprit.autoloc.web.dto;

import java.time.LocalDate;
import java.math.BigDecimal;
import tn.esprit.autoloc.domain.enums.StatutReservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponseDTO {
    private Long id;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private StatutReservation statut;
    private Long clientId;
    private String clientNomComplet;
    private Long vehiculeId;
    private String vehiculeImmatriculation;
    private BigDecimal tarifEstime;
}
