package tn.esprit.autoloc.service;

import java.util.List;
import tn.esprit.autoloc.domain.Reservation;

public interface IReservationService {

    Reservation create(Reservation reservation);

    Reservation findById(Long id);

    List<Reservation> findAll();

    Reservation update(Long id, Reservation reservation);

    Reservation annulerReservation(Long id);

    void deleteById(Long id);
}