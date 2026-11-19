package tn.esprit.autoloc.exception;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException exception) {
        return error(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(IllegalArgumentException exception) {
        return error(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidJson(HttpMessageNotReadableException exception) {
        return error(HttpStatus.BAD_REQUEST, "JSON illisible : vérifier la syntaxe, les dates et les énumérations");
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(BindException exception) {
        Map<String, String> erreurs = new LinkedHashMap<>();
        exception.getFieldErrors().forEach(erreur ->
                erreurs.putIfAbsent(erreur.getField(), erreur.getDefaultMessage()));
        Map<String, Object> reponse = new LinkedHashMap<>(corpsErreur(
                HttpStatus.BAD_REQUEST, "Des données sont invalides"));
        reponse.put("erreurs", erreurs);
        return ResponseEntity.badRequest().body(reponse);
    }

    private ResponseEntity<Map<String, Object>> error(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(corpsErreur(status, message));
    }

    private Map<String, Object> corpsErreur(HttpStatus status, String message) {
        return Map.of(
                "statut", status.value(),
                "message", message == null ? status.getReasonPhrase() : message,
                "horodatage", Instant.now().toString());
    }
}
