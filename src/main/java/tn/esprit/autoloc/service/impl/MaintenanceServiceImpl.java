package tn.esprit.autoloc.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IMaintenanceRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.service.IMaintenanceService;
import tn.esprit.autoloc.web.dto.MaintenanceDTO;
import tn.esprit.autoloc.web.mapper.MaintenanceMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class MaintenanceServiceImpl implements IMaintenanceService {

    private final IMaintenanceRepository maintenanceRepository;
    private final MaintenanceMapper maintenanceMapper;
    private final IVehiculeRepository vehiculeRepository;

    @Override
    public MaintenanceDTO create(MaintenanceDTO dto) {
        Maintenance maintenance = maintenanceMapper.toEntity(dto);
        if (maintenance.getId() != null) {
            throw new IllegalArgumentException(
                    "Une nouvelle maintenance ne doit pas avoir d'identifiant");
        }
        verifierDates(maintenance);
        maintenance.setVehicule(chercherVehicule(dto.getVehiculeId()));
        return maintenanceMapper.toDto(maintenanceRepository.save(maintenance));
    }

    @Override
    @Transactional(readOnly = true)
    public MaintenanceDTO findById(Long id) {
        return maintenanceMapper.toDto(charger(id));
    }

    private Maintenance charger(Long id) {
        return maintenanceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Maintenance", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceDTO> findAll() {
        return maintenanceRepository.findAll().stream().map(maintenanceMapper::toDto).toList();
    }

    @Override
    public MaintenanceDTO update(Long id, MaintenanceDTO dto) {
        Maintenance existante = charger(id);
        Maintenance maintenance = maintenanceMapper.toEntity(dto);
        verifierDates(maintenance);

        existante.setDateDebut(maintenance.getDateDebut());
        existante.setDateFin(maintenance.getDateFin());
        existante.setDescription(maintenance.getDescription());
        existante.setVehicule(chercherVehicule(dto.getVehiculeId()));

        return maintenanceMapper.toDto(maintenanceRepository.save(existante));
    }

    @Override
    public void deleteById(Long id) {
        if (!maintenanceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Maintenance", id);
        }
        maintenanceRepository.deleteById(id);
    }

    private Vehicule chercherVehicule(Long id) {
        if (id == null) {
            return null;
        }
        return vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule", id));
    }

    private void verifierDates(Maintenance maintenance) {
        if (maintenance.getDateDebut() != null
                && maintenance.getDateFin() != null
                && maintenance.getDateFin()
                .isBefore(maintenance.getDateDebut())) {
            throw new IllegalArgumentException(
                    "La date de fin ne doit pas être antérieure à la date de début");
        }
    }
}
