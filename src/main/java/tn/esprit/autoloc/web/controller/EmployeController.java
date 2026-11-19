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
import tn.esprit.autoloc.web.dto.EmployeDTO;
import tn.esprit.autoloc.service.IEmployeService;

@RestController
@RequestMapping("/employes")
@RequiredArgsConstructor
@Tag(name = "Employes", description = "Gestion : employes")
public class EmployeController {

    private final IEmployeService service;

    @Operation(summary = "Lister : employes")
    @ApiResponse(responseCode = "200", description = "Liste renvoyée")
    @GetMapping
    public List<EmployeDTO> findAll() {
        return service.findAll();
    }

    @Operation(summary = "Consulter : Employe")
    @ApiResponse(responseCode = "200", description = "Ressource trouvée")
    @ApiResponse(responseCode = "404", description = "Ressource introuvable")
    @GetMapping("/{id}")
    public EmployeDTO findById(
            @Parameter(description = "Identifiant de la ressource")
            @PathVariable("id") Long id) {
        return service.findById(id);
    }

    @Operation(summary = "Créer : Employe")
    @ApiResponse(responseCode = "201", description = "Ressource créée")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @ApiResponse(responseCode = "404", description = "Association introuvable")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeDTO create(@RequestBody EmployeDTO dto) {
        return service.create(dto);
    }

    @Operation(summary = "Modifier : Employe")
    @ApiResponse(responseCode = "200", description = "Ressource modifiée")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @ApiResponse(responseCode = "404", description = "Ressource introuvable")
    @PutMapping("/{id}")
    public EmployeDTO update(
            @Parameter(description = "Identifiant de la ressource")
            @PathVariable("id") Long id, @RequestBody EmployeDTO dto) {
        return service.update(id, dto);
    }

    @Operation(summary = "Affecter un employé à une agence")
    @ApiResponse(responseCode = "204", description = "Affectation enregistrée")
    @ApiResponse(responseCode = "404", description = "Employe ou agence introuvable")
    @PutMapping("/{employeId}/agence/{agenceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void affecterEmployeAAgence(
            @PathVariable("employeId") Long employeId, @PathVariable("agenceId") Long agenceId) {
        service.affecterEmployeAAgence(employeId, agenceId);
    }

    @Operation(summary = "Désaffecter un employé de son agence")
    @ApiResponse(responseCode = "204", description = "Désaffectation enregistrée")
    @ApiResponse(responseCode = "404", description = "Employe introuvable")
    @PutMapping("/{employeId}/desaffectation")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desaffecterEmployeDeAgence(@PathVariable("employeId") Long employeId) {
        service.desaffecterEmployeDeAgence(employeId);
    }

    @Operation(summary = "Supprimer : Employe")
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
