package tn.esprit.autoloc;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.enums.StatutReservation;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.IReservationService;
import tn.esprit.autoloc.service.impl.ReservationServiceImpl;
import tn.esprit.autoloc.service.observer.EmailNotificationObserver;
import tn.esprit.autoloc.service.observer.ReservationSubject;
import tn.esprit.autoloc.service.observer.SmsNotificationObserver;
import tn.esprit.autoloc.web.dto.ReservationRequestDTO;
import tn.esprit.autoloc.web.dto.ReservationResponseDTO;
import tn.esprit.autoloc.web.mapper.ReservationMapperImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringJUnitConfig(classes = {
        ReservationServiceImpl.class,
        ReservationMapperImpl.class,
        ReservationSubject.class,
        EmailNotificationObserver.class,
        SmsNotificationObserver.class
})
@ExtendWith(OutputCaptureExtension.class)
class ReservationServiceTest {

    @Autowired
    private IReservationService reservationService;

    @MockitoBean
    private IReservationRepository reservationRepository;

    @MockitoBean
    private IClientRepository clientRepository;

    @MockitoBean
    private IVehiculeRepository vehiculeRepository;

    @Test
    void doitNotifierLaConfirmationParEmailEtSms(CapturedOutput output) {
        Reservation reservation = preparerReservationConfirmee();
        when(clientRepository.findById(1L)).thenReturn(Optional.of(reservation.getClient()));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> {
            Reservation creee = invocation.getArgument(0);
            creee.setId(1L);
            return creee;
        });

        ReservationResponseDTO creee = reservationService.create(ReservationRequestDTO.builder()
                .clientId(1L)
                .dateDebut(reservation.getDateDebut())
                .dateFin(reservation.getDateFin())
                .statut(reservation.getStatut())
                .build());

        assertEquals(StatutReservation.CONFIRMEE, creee.getStatut());
        assertTrue(output.getAll().contains("[EMAIL] Confirmation"));
        assertTrue(output.getAll().contains("[SMS] Confirmation"));
    }

    @Test
    void doitNotifierLAnnulationParEmailEtSms(CapturedOutput output) {
        Reservation reservation = preparerReservationConfirmee();
        reservation.setId(2L);
        when(reservationRepository.findById(2L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        ReservationResponseDTO annulee = reservationService.annulerReservation(2L);

        assertEquals(StatutReservation.ANNULEE, annulee.getStatut());
        assertTrue(output.getAll().contains("[EMAIL] Annulation"));
        assertTrue(output.getAll().contains("[SMS] Annulation"));
    }

    private Reservation preparerReservationConfirmee() {
        Client client = new Client();
        client.setEmail("client@example.com");
        client.setTelephone("22123456");

        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setDateDebut(LocalDate.of(2026, 11, 20));
        reservation.setDateFin(LocalDate.of(2026, 11, 22));
        reservation.setStatut(StatutReservation.CONFIRMEE);

        return reservation;
    }
}
