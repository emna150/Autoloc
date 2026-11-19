package tn.esprit.autoloc.service.strategy;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

@Component
public class TarifStandardStrategy implements ITarificationStrategy {

    @Override
    public BigDecimal calculerTarif(Vehicule vehicule, Reservation reservation) {
        long nombreJours = ChronoUnit.DAYS.between(
                reservation.getDateDebut(),
                reservation.getDateFin()
        );

        return vehicule.getTarifJournalier()
                .multiply(BigDecimal.valueOf(nombreJours));
    }

    @Override
    public String getCode() {
        return "STANDARD";
    }
}