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
import tn.esprit.autoloc.service.IContratService;
import tn.esprit.autoloc.web.dto.ContratDTO;

@RestController
@RequestMapping("/contrats")
@RequiredArgsConstructor
@Tag(name = "Contrats", description = "Gestion : contrats")
public class ContratController {

    private final IContratService service;

    @Operation(summary = "Lister : contrats")
    @ApiResponse(responseCode = "200", description = "Liste renvoyée")
    @GetMapping
    public List<ContratDTO> findAll() {
        return service.findAll();
    }

    @Operation(summary = "Consulter : Contrat")
    @ApiResponse(responseCode = "200", description = "Ressource trouvée")
    @ApiResponse(responseCode = "404", description = "Ressource introuvable")
    @GetMapping("/{id}")
    public ContratDTO findById(
            @Parameter(description = "Identifiant de la ressource")
            @PathVariable("id") Long id) {
        return service.findById(id);
    }

    @Operation(summary = "Créer : Contrat")
    @ApiResponse(responseCode = "201", description = "Ressource créée")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @ApiResponse(responseCode = "404", description = "Association introuvable")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContratDTO create(@Valid @RequestBody ContratDTO dto) {
        return service.create(dto);
    }

    @Operation(summary = "Modifier : Contrat")
    @ApiResponse(responseCode = "200", description = "Ressource modifiée")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @ApiResponse(responseCode = "404", description = "Ressource introuvable")
    @PutMapping("/{id}")
    public ContratDTO update(
            @Parameter(description = "Identifiant de la ressource")
            @PathVariable("id") Long id, @Valid @RequestBody ContratDTO dto) {
        return service.update(id, dto);
    }

    @Operation(summary = "Supprimer : Contrat")
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
