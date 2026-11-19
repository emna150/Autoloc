package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class ClientDTO {
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotBlank(message = "L’adresse email est obligatoire")
    @Email(message = "L’adresse email est invalide")
    private String email;
    private String telephone;
    private String numPermis;
    private LocalDate dateInscription;
}
