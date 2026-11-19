package tn.esprit.autoloc.web.dto;

import tn.esprit.autoloc.domain.enums.RoleEmploye;
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
public class EmployeDTO {
    private Long id;
    private String nom;
    private String prenom;
    private RoleEmploye role;
    private Long agenceId;
}
