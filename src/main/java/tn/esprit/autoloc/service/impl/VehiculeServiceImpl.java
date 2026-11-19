package tn.esprit.autoloc.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.IVehiculeService;
import tn.esprit.autoloc.web.dto.VehiculeDTO;
import tn.esprit.autoloc.web.mapper.VehiculeMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class VehiculeServiceImpl implements IVehiculeService {

    private final IVehiculeRepository vehiculeRepository;
    private final VehiculeMapper vehiculeMapper;
    private final IAgenceRepository agenceRepository;

    @Override
    public VehiculeDTO create(VehiculeDTO dto) {
        Vehicule vehicule = vehiculeMapper.toEntity(dto);
        if (vehicule.getIdVehicule() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau véhicule ne doit pas avoir d'identifiant");
        }
        verifierTarif(vehicule);
        vehicule.setAgence(chercherAgence(dto.getAgenceId()));
        return vehiculeMapper.toDto(vehiculeRepository.save(vehicule));
    }

    @Override
    @Transactional(readOnly = true)
    public VehiculeDTO findById(Long id) {
        return vehiculeMapper.toDto(charger(id));
    }

    private Vehicule charger(Long id) {
        return vehiculeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Vehicule", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehiculeDTO> findAll() {
        return vehiculeRepository.findAll().stream().map(vehiculeMapper::toDto).toList();
    }

    @Override
    public VehiculeDTO update(Long id, VehiculeDTO dto) {
        Vehicule existant = charger(id);
        Vehicule vehicule = vehiculeMapper.toEntity(dto);
        verifierTarif(vehicule);

        existant.setImmatriculation(vehicule.getImmatriculation());
        existant.setMarque(vehicule.getMarque());
        existant.setModele(vehicule.getModele());
        existant.setCategorie(vehicule.getCategorie());
        existant.setTarifJournalier(vehicule.getTarifJournalier());
        existant.setStatut(vehicule.getStatut());
        existant.setAgence(chercherAgence(dto.getAgenceId()));

        return vehiculeMapper.toDto(vehiculeRepository.save(existant));
    }

    @Override
    public void deleteById(Long id) {
        if (!vehiculeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicule", id);
        }
        vehiculeRepository.deleteById(id);
    }

    @Override
    public void affecterVehiculeAAgence(Long vehiculeId, Long agenceId) {
        Vehicule vehicule = charger(vehiculeId);
        Agence agence = agenceRepository.findById(agenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", agenceId));
        vehicule.setAgence(agence);
        vehiculeRepository.save(vehicule);
    }

    @Override
    public void desaffecterVehiculeDeAgence(Long vehiculeId) {
        Vehicule vehicule = charger(vehiculeId);
        if (vehicule.getAgence() != null) {
            vehicule.setAgence(null);
            vehiculeRepository.save(vehicule);
        }
    }

    private Agence chercherAgence(Long id) {
        if (id == null) {
            return null;
        }
        return agenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", id));
    }

    private void verifierTarif(Vehicule vehicule) {
        if (vehicule.getTarifJournalier() != null
                && vehicule.getTarifJournalier().signum() < 0) {
            throw new IllegalArgumentException(
                    "Le tarif journalier ne doit pas être négatif");
        }
    }
}
