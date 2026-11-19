package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.NotBlank;
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
public class AgenceDTO {
    private Long id;

    @NotBlank(message = "Le nom de l’agence est obligatoire")
    private String nom;

    @NotBlank(message = "La ville est obligatoire")
    private String ville;
    private String adresse;
    private String telephone;
}
