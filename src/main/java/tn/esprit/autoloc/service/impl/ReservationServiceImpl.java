package tn.esprit.autoloc.service.impl;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.enums.StatutReservation;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.service.IReservationService;
import tn.esprit.autoloc.service.observer.ReservationSubject;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository reservationRepository;
    private final ReservationSubject reservationSubject;

    @Override
    public Reservation create(Reservation reservation) {
        if (reservation.getId() != null) {
            throw new IllegalArgumentException(
                    "Une nouvelle réservation ne doit pas avoir d'identifiant");
        }

        verifierDates(reservation);
        Reservation sauvegardee = reservationRepository.save(reservation);

        if (sauvegardee.getStatut() == StatutReservation.CONFIRMEE
                && sauvegardee.getClient() != null) {
            reservationSubject.notifierConfirmation(sauvegardee);
        }

        return sauvegardee;
    }

    @Override
    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reservation", id));
    }

    @Override
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation update(Long id, Reservation reservation) {
        Reservation existante = findById(id);
        verifierDates(reservation);

        StatutReservation ancienStatut = existante.getStatut();

        existante.setDateDebut(reservation.getDateDebut());
        existante.setDateFin(reservation.getDateFin());
        existante.setStatut(reservation.getStatut());
        existante.setClient(reservation.getClient());
        existante.setVehicule(reservation.getVehicule());
        existante.setContrat(reservation.getContrat());

        return sauvegarderEtNotifierAnnulation(existante, ancienStatut);
    }

    @Override
    public Reservation annulerReservation(Long id) {
        Reservation reservation = findById(id);
        StatutReservation ancienStatut = reservation.getStatut();

        if (ancienStatut == StatutReservation.ANNULEE) {
            return reservation;
        }

        reservation.setStatut(StatutReservation.ANNULEE);
        return sauvegarderEtNotifierAnnulation(reservation, ancienStatut);
    }

    @Override
    public void deleteById(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reservation", id);
        }
        reservationRepository.deleteById(id);
    }

    private Reservation sauvegarderEtNotifierAnnulation(
            Reservation reservation, StatutReservation ancienStatut) {
        Reservation sauvegardee = reservationRepository.save(reservation);

        if (ancienStatut != StatutReservation.ANNULEE
                && sauvegardee.getStatut() == StatutReservation.ANNULEE
                && sauvegardee.getClient() != null) {
            reservationSubject.notifierAnnulation(sauvegardee);
        }

        return sauvegardee;
    }

    private void verifierDates(Reservation reservation) {
        if (reservation.getDateDebut() != null
                && reservation.getDateFin() != null
                && reservation.getDateFin().isBefore(reservation.getDateDebut())) {
            throw new IllegalArgumentException(
                    "La date de fin ne doit pas être antérieure à la date de début");
        }
    }
}