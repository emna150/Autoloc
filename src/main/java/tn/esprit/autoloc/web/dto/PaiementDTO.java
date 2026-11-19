package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.groups.Default;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.autoloc.domain.enums.ModePaiement;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaiementDTO {
    /** Le contrat qui contient le paiement reçoit son identifiant à la sauvegarde. */
    public interface DansContrat {
    }

    private Long id;

    @NotNull(message = "Le montant du paiement est obligatoire", groups = {Default.class, DansContrat.class})
    @Positive(message = "Le montant du paiement doit être strictement positif", groups = {Default.class, DansContrat.class})
    private BigDecimal montant;
    private LocalDate datePaiement;

    @NotNull(message = "Le mode de paiement est obligatoire", groups = {Default.class, DansContrat.class})
    private ModePaiement modePaiement;

    @NotNull(message = "L’identifiant du contrat est obligatoire")
    private Long contratId;
}
