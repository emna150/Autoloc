package tn.esprit.autoloc.web.validator;

import java.time.LocalDate;
import java.time.ZoneId;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import tn.esprit.autoloc.web.dto.ReservationRequestDTO;

@Component
public class ReservationRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> type) {
        return ReservationRequestDTO.class.isAssignableFrom(type);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ReservationRequestDTO reservation = (ReservationRequestDTO) target;
        LocalDate debut = reservation.getDateDebut();
        LocalDate fin = reservation.getDateFin();

        if (debut != null && debut.isBefore(LocalDate.now(ZoneId.systemDefault())) && !errors.hasFieldErrors("dateDebut")) {
            errors.rejectValue("dateDebut", "reservation.dateDebut.passee",
                    "La date de début doit être aujourd’hui ou dans le futur");
        }

        if (debut != null && fin != null && !fin.isAfter(debut)) {
            errors.rejectValue("dateFin", "reservation.dates.invalides",
                    "La date de fin doit être postérieure à la date de début");
        }
    }
}
