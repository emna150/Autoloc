package tn.esprit.autoloc.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.service.IEquipementService;
import tn.esprit.autoloc.web.dto.EquipementDTO;
import tn.esprit.autoloc.web.mapper.EquipementMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class EquipementServiceImpl implements IEquipementService {

    private final IEquipementRepository equipementRepository;
    private final EquipementMapper equipementMapper;

    @Override
    public EquipementDTO create(EquipementDTO dto) {
        Equipement equipement = equipementMapper.toEntity(dto);
        if (equipement.getId() != null) {
            throw new IllegalArgumentException(
                    "Un nouvel équipement ne doit pas avoir d'identifiant");
        }
        verifierLibelle(equipement);
        return equipementMapper.toDto(equipementRepository.save(equipement));
    }

    @Override
    @Transactional(readOnly = true)
    public EquipementDTO findById(Long id) {
        return equipementMapper.toDto(charger(id));
    }

    private Equipement charger(Long id) {
        return equipementRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Equipement", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipementDTO> findAll() {
        return equipementRepository.findAll().stream().map(equipementMapper::toDto).toList();
    }

    @Override
    public EquipementDTO update(Long id, EquipementDTO dto) {
        Equipement existant = charger(id);
        Equipement equipement = equipementMapper.toEntity(dto);
        verifierLibelle(equipement);

        existant.setLibelle(equipement.getLibelle());

        return equipementMapper.toDto(equipementRepository.save(existant));
    }

    @Override
    public void deleteById(Long id) {
        if (!equipementRepository.existsById(id)) {
            throw new ResourceNotFoundException("Equipement", id);
        }
        equipementRepository.deleteById(id);
    }

    private void verifierLibelle(Equipement equipement) {
        if (equipement.getLibelle() == null
                || equipement.getLibelle().isBlank()) {
            throw new IllegalArgumentException(
                    "Le libellé de l'équipement est obligatoire");
        }
    }
}
