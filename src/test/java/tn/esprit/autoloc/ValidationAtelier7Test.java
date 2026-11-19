package tn.esprit.autoloc;

import jakarta.validation.Validator;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.autoloc.domain.enums.ModePaiement;
import tn.esprit.autoloc.service.IAgenceService;
import tn.esprit.autoloc.service.IClientService;
import tn.esprit.autoloc.service.IContratService;
import tn.esprit.autoloc.service.IReservationService;
import tn.esprit.autoloc.service.IVehiculeService;
import tn.esprit.autoloc.web.controller.AgenceController;
import tn.esprit.autoloc.web.controller.ClientController;
import tn.esprit.autoloc.web.controller.ContratController;
import tn.esprit.autoloc.web.controller.ReservationController;
import tn.esprit.autoloc.web.controller.VehiculeController;
import tn.esprit.autoloc.web.dto.ContratDTO;
import tn.esprit.autoloc.web.dto.PaiementDTO;
import tn.esprit.autoloc.web.dto.ReservationRequestDTO;
import tn.esprit.autoloc.web.dto.ReservationResponseDTO;
import tn.esprit.autoloc.web.validator.ReservationRequestValidator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({AgenceController.class, ClientController.class, ContratController.class,
        ReservationController.class, VehiculeController.class})
@Import(ReservationRequestValidator.class)
class ValidationAtelier7Test {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private Validator validator;

    @MockitoBean
    private IAgenceService agenceService;

    @MockitoBean
    private IClientService clientService;

    @MockitoBean
    private IVehiculeService vehiculeService;

    @MockitoBean
    private IContratService contratService;

    @MockitoBean
    private IReservationService reservationService;

    @Test
    void refuseUneAgenceSansVille() throws Exception {
        mvc.perform(post("/agences").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nom":"Agence de test"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erreurs.ville").exists());
        verifyNoInteractions(agenceService);
    }

    @Test
    void refuseUnEmailInvalide() throws Exception {
        mvc.perform(post("/clients").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nom":"Test","prenom":"Client","email":"email-invalide"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erreurs.email").value("L’adresse email est invalide"));
        verifyNoInteractions(clientService);
    }

    @Test
    void refuseUnVehiculeSansImmatriculation() throws Exception {
        mvc.perform(post("/vehicules").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"marque":"Ford","modele":"Fiesta","categorie":"CITADINE",
                                "tarifJournalier":80,"agenceId":1}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erreurs.immatriculation").exists());
        verifyNoInteractions(vehiculeService);
    }

    @ParameterizedTest
    @ValueSource(strings = {"null", "0", "-1"})
    void refuseUnPaiementInvalideDansUnContrat(String montant) throws Exception {
        mvc.perform(post("/contrats").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"montantTotal":100,"paiements":[{"montant":%s,"modePaiement":"CARTE"}]}
                                """.formatted(montant)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erreurs['paiements[0].montant']").exists());
        verifyNoInteractions(contratService);
    }

    @Test
    void accepteUnPaiementValideAvecUnNouveauContrat() throws Exception {
        when(contratService.create(any(ContratDTO.class))).thenReturn(ContratDTO.builder().id(1L).build());

        mvc.perform(post("/contrats").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"montantTotal":100,"paiements":[{"montant":100,"modePaiement":"CARTE"}]}
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    void exigeLeContratPourUnPaiementValideIndependamment() {
        PaiementDTO paiement = PaiementDTO.builder()
                .montant(BigDecimal.TEN).modePaiement(ModePaiement.CARTE).build();

        assertThat(validator.validate(paiement))
                .extracting(erreur -> erreur.getPropertyPath().toString())
                .containsExactly("contratId");
    }

    @Test
    void refuseLesDatesDeReservationManquantes() throws Exception {
        mvc.perform(post("/reservations").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"clientId":1,"vehiculeId":2}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erreurs.dateDebut").exists())
                .andExpect(jsonPath("$.erreurs.dateFin").exists());
        verifyNoInteractions(reservationService);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void refuseUneDateDeFinAnterieureOuEgaleAuDebut(int decalage) throws Exception {
        LocalDate debut = LocalDate.now().plusDays(3);
        mvc.perform(post("/reservations").contentType(MediaType.APPLICATION_JSON)
                        .content(reservation(debut, debut.plusDays(decalage))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erreurs.dateFin")
                        .value("La date de fin doit être postérieure à la date de début"));
        verifyNoInteractions(reservationService);
    }

    @Test
    void refuseUnDebutPasseEnModification() throws Exception {
        LocalDate aujourdhui = LocalDate.now();
        mvc.perform(put("/reservations/1").contentType(MediaType.APPLICATION_JSON)
                        .content(reservation(aujourdhui.minusDays(1), aujourdhui.plusDays(1))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erreurs.dateDebut").exists());
        verifyNoInteractions(reservationService);
    }

    @Test
    void accepteUneReservationAvecDesDatesCoherentes() throws Exception {
        when(reservationService.create(any(ReservationRequestDTO.class)))
                .thenReturn(ReservationResponseDTO.builder().id(10L).build());
        LocalDate aujourdhui = LocalDate.now();

        mvc.perform(post("/reservations").contentType(MediaType.APPLICATION_JSON)
                        .content(reservation(aujourdhui, aujourdhui.plusDays(2))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10));
    }

    private String reservation(LocalDate debut, LocalDate fin) {
        return """
                {"clientId":1,"vehiculeId":2,"dateDebut":"%s","dateFin":"%s"}
                """.formatted(debut, fin);
    }
}
