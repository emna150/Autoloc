package tn.esprit.autoloc.web.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.service.IAgenceService;

@RestController
@RequestMapping("/agences")
@RequiredArgsConstructor
@Tag(name = "Agences", description = "Gestion : agences")
public class AgenceController {

    private final IAgenceService service;

    @Operation(summary = "Lister : agences")
    @ApiResponse(responseCode = "200", description = "Liste renvoyée")
    @GetMapping
    public List<Agence> findAll() {
        return service.findAll();
    }

    @Operation(summary = "Consulter : Agence")
    @ApiResponse(responseCode = "200", description = "Ressource trouvée")
        @ApiResponse(responseCode = "404", description = "Ressource introuvable")
    @GetMapping("/{id}")
    public Agence findById(
            @Parameter(description = "Identifiant de la ressource")
            @PathVariable("id") Long id) {
        return service.findById(id);
    }

    @Operation(summary = "Créer : Agence")
    @ApiResponse(responseCode = "201", description = "Ressource créée")
        @ApiResponse(responseCode = "400", description = "Données invalides")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Agence create(@RequestBody Agence agence) {
        return service.create(agence);
    }

    @Operation(summary = "Modifier : Agence")
    @ApiResponse(responseCode = "200", description = "Ressource modifiée")
        @ApiResponse(responseCode = "400", description = "Données invalides")
        @ApiResponse(responseCode = "404", description = "Ressource introuvable")
    @PutMapping("/{id}")
    public Agence update(
            @Parameter(description = "Identifiant de la ressource")
            @PathVariable("id") Long id, @RequestBody Agence agence) {
        return service.update(id, agence);
    }

    @Operation(summary = "Supprimer : Agence")
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
