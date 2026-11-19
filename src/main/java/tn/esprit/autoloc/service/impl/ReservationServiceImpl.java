package tn.esprit.autoloc.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.enums.StatutReservation;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.IReservationService;
import tn.esprit.autoloc.service.observer.ReservationSubject;
import tn.esprit.autoloc.web.dto.ReservationRequestDTO;
import tn.esprit.autoloc.web.dto.ReservationResponseDTO;
import tn.esprit.autoloc.web.mapper.ReservationMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository reservationRepository;
    private final ReservationSubject reservationSubject;
    private final ReservationMapper reservationMapper;
    private final IClientRepository clientRepository;
    private final IVehiculeRepository vehiculeRepository;

    @Override
    public ReservationResponseDTO create(ReservationRequestDTO dto) {
        Reservation reservation = reservationMapper.toEntity(dto);
        verifierDates(reservation);
        remplirAssociations(reservation, dto);
        if (reservation.getStatut() == null) {
            reservation.setStatut(StatutReservation.EN_ATTENTE);
        }
        Reservation sauvegardee = reservationRepository.save(reservation);

        if (sauvegardee.getStatut() == StatutReservation.CONFIRMEE
                && sauvegardee.getClient() != null) {
            reservationSubject.notifierConfirmation(sauvegardee);
        }

        return reservationMapper.toDto(sauvegardee);
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationResponseDTO findById(Long id) {
        return reservationMapper.toDto(charger(id));
    }

    private Reservation charger(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reservation", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> findAll() {
        return reservationRepository.findAll().stream().map(reservationMapper::toDto).toList();
    }

    @Override
    public ReservationResponseDTO update(Long id, ReservationRequestDTO dto) {
        Reservation existante = charger(id);
        Reservation reservation = reservationMapper.toEntity(dto);
        verifierDates(reservation);

        StatutReservation ancienStatut = existante.getStatut();

        existante.setDateDebut(reservation.getDateDebut());
        existante.setDateFin(reservation.getDateFin());
        if (dto.getStatut() != null) {
            existante.setStatut(dto.getStatut());
        }
        remplirAssociations(existante, dto);

        return reservationMapper.toDto(sauvegarderEtNotifierAnnulation(existante, ancienStatut));
    }

    @Override
    public ReservationResponseDTO annulerReservation(Long id) {
        Reservation reservation = charger(id);
        StatutReservation ancienStatut = reservation.getStatut();

        if (ancienStatut == StatutReservation.ANNULEE) {
            return reservationMapper.toDto(reservation);
        }

        reservation.setStatut(StatutReservation.ANNULEE);
        return reservationMapper.toDto(sauvegarderEtNotifierAnnulation(reservation, ancienStatut));
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

    private void remplirAssociations(Reservation reservation, ReservationRequestDTO dto) {
        if (dto.getClientId() != null) {
            reservation.setClient(clientRepository.findById(dto.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Client", dto.getClientId())));
        }
        if (dto.getVehiculeId() != null) {
            reservation.setVehicule(vehiculeRepository.findById(dto.getVehiculeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Vehicule", dto.getVehiculeId())));
        }
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
