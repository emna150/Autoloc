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
import tn.esprit.autoloc.service.IClientService;
import tn.esprit.autoloc.web.dto.ClientDTO;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
@Tag(name = "Clients", description = "Gestion : clients")
public class ClientController {

    private final IClientService service;

    @Operation(summary = "Lister : clients")
    @ApiResponse(responseCode = "200", description = "Liste renvoyée")
    @GetMapping
    public List<ClientDTO> findAll() {
        return service.findAll();
    }

    @Operation(summary = "Consulter : Client")
    @ApiResponse(responseCode = "200", description = "Ressource trouvée")
    @ApiResponse(responseCode = "404", description = "Ressource introuvable")
    @GetMapping("/{id}")
    public ClientDTO findById(
            @Parameter(description = "Identifiant de la ressource")
            @PathVariable("id") Long id) {
        return service.findById(id);
    }

    @Operation(summary = "Créer : Client")
    @ApiResponse(responseCode = "201", description = "Ressource créée")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientDTO create(@Valid @RequestBody ClientDTO dto) {
        return service.create(dto);
    }

    @Operation(summary = "Modifier : Client")
    @ApiResponse(responseCode = "200", description = "Ressource modifiée")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @ApiResponse(responseCode = "404", description = "Ressource introuvable")
    @PutMapping("/{id}")
    public ClientDTO update(
            @Parameter(description = "Identifiant de la ressource")
            @PathVariable("id") Long id, @Valid @RequestBody ClientDTO dto) {
        return service.update(id, dto);
    }

    @Operation(summary = "Supprimer : Client")
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
