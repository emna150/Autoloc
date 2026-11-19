package tn.esprit.autoloc.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.service.IContratService;
import tn.esprit.autoloc.web.dto.ContratDTO;
import tn.esprit.autoloc.web.mapper.ContratMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class ContratServiceImpl implements IContratService {

    private final IContratRepository contratRepository;
    private final ContratMapper contratMapper;
    private final IReservationRepository reservationRepository;

    @Override
    public ContratDTO create(ContratDTO dto) {
        Contrat contrat = contratMapper.toEntity(dto);
        if (contrat.getId() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau contrat ne doit pas avoir d'identifiant");
        }
        verifierMontant(contrat);
        if (contrat.getPaiements() != null) {
            contrat.getPaiements().forEach(paiement -> paiement.setContrat(contrat));
        }
        Reservation reservation = chercherReservationDisponible(dto.getReservationId(), contrat);
        Contrat sauvegarde = contratRepository.save(contrat);
        rattacherReservation(sauvegarde, reservation);
        return contratMapper.toDto(sauvegarde);
    }

    @Override
    @Transactional(readOnly = true)
    public ContratDTO findById(Long id) {
        return contratMapper.toDto(charger(id));
    }

    private Contrat charger(Long id) {
        return contratRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Contrat", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContratDTO> findAll() {
        return contratRepository.findAll().stream().map(contratMapper::toDto).toList();
    }

    @Override
    public ContratDTO update(Long id, ContratDTO dto) {
        Contrat existant = charger(id);
        Contrat contrat = contratMapper.toEntity(dto);
        verifierMontant(contrat);

        if (dto.getReservationId() != null) {
            rattacherReservation(existant, chercherReservationDisponible(dto.getReservationId(), existant));
        }

        existant.setDateSignature(contrat.getDateSignature());
        existant.setMontantTotal(contrat.getMontantTotal());
        existant.setValide(contrat.isValide());

        return contratMapper.toDto(contratRepository.save(existant));
    }

    @Override
    public void deleteById(Long id) {
        Contrat contrat = charger(id);
        rattacherReservation(contrat, null);
        contratRepository.delete(contrat);
    }

    private Reservation chercherReservationDisponible(Long id, Contrat contrat) {
        if (id == null) {
            return null;
        }
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
        if (reservation.getContrat() != null && reservation.getContrat() != contrat) {
            throw new IllegalArgumentException("La réservation possède déjà un contrat");
        }
        return reservation;
    }

    private void rattacherReservation(Contrat contrat, Reservation reservation) {
        Reservation ancienne = contrat.getReservation();
        if (ancienne == reservation) {
            return;
        }
        if (ancienne != null) {
            ancienne.setContrat(null);
            reservationRepository.saveAndFlush(ancienne);
        }
        if (reservation != null) {
            reservation.setContrat(contrat);
        }
        contrat.setReservation(reservation);
    }

    private void verifierMontant(Contrat contrat) {
        if (contrat.getMontantTotal() != null
                && contrat.getMontantTotal().signum() < 0) {
            throw new IllegalArgumentException(
                    "Le montant total ne doit pas être négatif");
        }
    }
}
