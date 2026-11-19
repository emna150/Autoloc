package tn.esprit.autoloc.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.autoloc.service.IReservationService;
import tn.esprit.autoloc.web.dto.ReservationRequestDTO;
import tn.esprit.autoloc.web.dto.ReservationResponseDTO;
import tn.esprit.autoloc.web.validator.ReservationRequestValidator;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservations", description = "Gestion : reservations")
public class ReservationController {

    private final IReservationService service;
    private final ReservationRequestValidator reservationRequestValidator;

    @Operation(summary = "Lister : reservations")
    @ApiResponse(responseCode = "200", description = "Liste renvoyée")
    @GetMapping
    public List<ReservationResponseDTO> findAll() {
        return service.findAll();
    }

    @Operation(summary = "Consulter : Reservation")
    @ApiResponse(responseCode = "200", description = "Ressource trouvée")
    @ApiResponse(responseCode = "404", description = "Ressource introuvable")
    @GetMapping("/{id}")
    public ReservationResponseDTO findById(
            @Parameter(description = "Identifiant de la ressource")
            @PathVariable("id") Long id) {
        return service.findById(id);
    }

    @Operation(summary = "Créer : Reservation")
    @ApiResponse(responseCode = "201", description = "Ressource créée")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @ApiResponse(responseCode = "404", description = "Association introuvable")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponseDTO create(
            @Valid @RequestBody ReservationRequestDTO dto, BindingResult result) throws BindException {
        verifierDates(dto, result);
        return service.create(dto);
    }

    @Operation(summary = "Modifier : Reservation")
    @ApiResponse(responseCode = "200", description = "Ressource modifiée")
    @ApiResponse(responseCode = "400", description = "Données invalides")
    @ApiResponse(responseCode = "404", description = "Ressource introuvable")
    @PutMapping("/{id}")
    public ReservationResponseDTO update(
            @Parameter(description = "Identifiant de la ressource")
            @PathVariable("id") Long id,
            @Valid @RequestBody ReservationRequestDTO dto, BindingResult result) throws BindException {
        verifierDates(dto, result);
        return service.update(id, dto);
    }

    @Operation(summary = "Supprimer : Reservation")
    @ApiResponse(responseCode = "204", description = "Ressource supprimée, corps vide")
    @ApiResponse(responseCode = "404", description = "Ressource introuvable")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(
            @Parameter(description = "Identifiant de la ressource")
            @PathVariable("id") Long id) {
        service.deleteById(id);
    }

    private void verifierDates(ReservationRequestDTO dto, BindingResult result) throws BindException {
        reservationRequestValidator.validate(dto, result);
        if (result.hasErrors()) {
            throw new BindException(result);
        }
    }
}
