package tn.esprit.autoloc.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IEmployeRepository;
import tn.esprit.autoloc.service.IEmployeService;
import tn.esprit.autoloc.web.dto.EmployeDTO;
import tn.esprit.autoloc.web.mapper.EmployeMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeServiceImpl implements IEmployeService {

    private final IEmployeRepository employeRepository;
    private final EmployeMapper employeMapper;
    private final IAgenceRepository agenceRepository;

    @Override
    public EmployeDTO create(EmployeDTO dto) {
        Employe employe = employeMapper.toEntity(dto);
        if (employe.getId() != null) {
            throw new IllegalArgumentException(
                    "Un nouvel employé ne doit pas avoir d'identifiant");
        }
        verifierEmploye(employe);
        employe.setAgence(chercherAgence(dto.getAgenceId()));
        return employeMapper.toDto(employeRepository.save(employe));
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeDTO findById(Long id) {
        return employeMapper.toDto(charger(id));
    }

    private Employe charger(Long id) {
        return employeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employe", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeDTO> findAll() {
        return employeRepository.findAll().stream().map(employeMapper::toDto).toList();
    }

    @Override
    public EmployeDTO update(Long id, EmployeDTO dto) {
        Employe existant = charger(id);
        Employe employe = employeMapper.toEntity(dto);
        verifierEmploye(employe);

        existant.setNom(employe.getNom());
        existant.setPrenom(employe.getPrenom());
        existant.setRole(employe.getRole());
        existant.setAgence(chercherAgence(dto.getAgenceId()));

        return employeMapper.toDto(employeRepository.save(existant));
    }

    @Override
    public void deleteById(Long id) {
        if (!employeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employe", id);
        }
        employeRepository.deleteById(id);
    }

    @Override
    public void affecterEmployeAAgence(Long employeId, Long agenceId) {
        Employe employe = charger(employeId);
        Agence agence = agenceRepository.findById(agenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", agenceId));
        employe.setAgence(agence);
        employeRepository.save(employe);
    }

    @Override
    public void desaffecterEmployeDeAgence(Long employeId) {
        Employe employe = charger(employeId);
        if (employe.getAgence() != null) {
            employe.setAgence(null);
            employeRepository.save(employe);
        }
    }

    private Agence chercherAgence(Long id) {
        if (id == null) {
            return null;
        }
        return agenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", id));
    }

    private void verifierEmploye(Employe employe) {
        if (employe.getNom() == null || employe.getNom().isBlank()) {
            throw new IllegalArgumentException(
                    "Le nom de l'employé est obligatoire");
        }
        if (employe.getRole() == null) {
            throw new IllegalArgumentException(
                    "Le rôle de l'employé est obligatoire");
        }
    }
}
