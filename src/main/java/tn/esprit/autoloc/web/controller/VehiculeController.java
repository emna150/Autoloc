package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.autoloc.service.IVehiculeService;
import tn.esprit.autoloc.web.dto.VehiculeDTO;

@RestController
@RequestMapping("/vehicules")
@RequiredArgsConstructor
@Tag(name = "Vehicules", description = "Gestion : vehicules")
public class VehiculeController {

    private final IVehiculeService service;

    @Operation(summary = "Lister : vehicules")
    @ApiResponse(responseCode = "200", description = "Liste renvoyée")
    @GetMapping
    public List<VehiculeDTO> findAll() {
        return service.findAll();
    }

    @Operation(summary = "Consulter : Vehicule")
    @ApiResponse(responseCode = "200", description = "Ressource trouvée")
    @ApiResponse(responseCode = "404", description = "Ressource introuvable")
    @GetMapping("/{id}")
    public VehiculeDTO findById(
            @Parameter(description = "Identifiant de la ressource")
            @PathVariable("id") Long id) {
        return service.findById(id);
    }

    @Operation(summary = "Créer : Vehicule")
    @ApiResponse(responseCode = "201", description = "Ressource créée")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @ApiResponse(responseCode = "404", description = "Association introuvable")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VehiculeDTO create(@Valid @RequestBody VehiculeDTO dto) {
        return service.create(dto);
    }

    @Operation(summary = "Modifier : Vehicule")
    @ApiResponse(responseCode = "200", description = "Ressource modifiée")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @ApiResponse(responseCode = "404", description = "Ressource introuvable")
    @PutMapping("/{id}")
    public VehiculeDTO update(
            @Parameter(description = "Identifiant de la ressource")
            @PathVariable("id") Long id, @Valid @RequestBody VehiculeDTO dto) {
        return service.update(id, dto);
    }

    @Operation(summary = "Affecter un véhicule à une agence")
    @ApiResponse(responseCode = "204", description = "Affectation enregistrée")
    @ApiResponse(responseCode = "404", description = "Vehicule ou agence introuvable")
    @PutMapping("/{vehiculeId}/agence/{agenceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void affecterVehiculeAAgence(
            @PathVariable("vehiculeId") Long vehiculeId, @PathVariable("agenceId") Long agenceId) {
        service.affecterVehiculeAAgence(vehiculeId, agenceId);
    }

    @Operation(summary = "Désaffecter un véhicule de son agence")
    @ApiResponse(responseCode = "204", description = "Désaffectation enregistrée")
    @ApiResponse(responseCode = "404", description = "Vehicule introuvable")
    @PutMapping("/{vehiculeId}/desaffectation")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desaffecterVehiculeDeAgence(@PathVariable("vehiculeId") Long vehiculeId) {
        service.desaffecterVehiculeDeAgence(vehiculeId);
    }

    @Operation(summary = "Supprimer : Vehicule")
    @ApiResponse(responseCode = "204", description = "Ressource supprimée, corps vide")
    @ApiResponse(responseCode = "404", description = "Ressource introuvable")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @Parameter(description = "Identifiant de la ressource")
            @PathVariable("id") Long id) {
        service.deleteById(id);
    }
}
