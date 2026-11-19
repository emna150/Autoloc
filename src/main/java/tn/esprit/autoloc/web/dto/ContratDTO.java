package tn.esprit.autoloc.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.groups.ConvertGroup;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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
public class ContratDTO {
    private Long id;
    private LocalDate dateSignature;

    @PositiveOrZero(message = "Le montant total doit être positif ou nul")
    private BigDecimal montantTotal;
    private Boolean valide;
    private Long reservationId;
    private List<@NotNull(message = "Le paiement est obligatoire") @Valid
            @ConvertGroup(to = PaiementDTO.DansContrat.class) PaiementDTO> paiements;
}
