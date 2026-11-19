package tn.esprit.autoloc.web.dto;

import java.time.LocalDate;
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
public class MaintenanceDTO {
    private Long id;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String description;
    private Long vehiculeId;
}
