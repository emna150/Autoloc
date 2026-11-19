package tn.esprit.autoloc.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.service.IAgenceService;
import tn.esprit.autoloc.web.dto.AgenceDTO;
import tn.esprit.autoloc.web.mapper.AgenceMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class AgenceServiceImpl implements IAgenceService {

    private final IAgenceRepository agenceRepository;
    private final AgenceMapper agenceMapper;

    @Override
    public AgenceDTO create(AgenceDTO dto) {
        Agence agence = agenceMapper.toEntity(dto);
        if (agence.getId() != null) {
            throw new IllegalArgumentException(
                    "Une nouvelle agence ne doit pas avoir d'identifiant");
        }
        verifierNom(agence);
        return agenceMapper.toDto(agenceRepository.save(agence));
    }

    @Override
    @Transactional(readOnly = true)
    public AgenceDTO findById(Long id) {
        return agenceMapper.toDto(charger(id));
    }

    private Agence charger(Long id) {
        return agenceRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Agence", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgenceDTO> findAll() {
        return agenceRepository.findAll().stream().map(agenceMapper::toDto).toList();
    }

    @Override
    public AgenceDTO update(Long id, AgenceDTO dto) {
        Agence existante = charger(id);
        Agence agence = agenceMapper.toEntity(dto);
        verifierNom(agence);

        existante.setNom(agence.getNom());
        existante.setVille(agence.getVille());
        existante.setAdresse(agence.getAdresse());
        existante.setTelephone(agence.getTelephone());

        return agenceMapper.toDto(agenceRepository.save(existante));
    }

    @Override
    public void deleteById(Long id) {
        if (!agenceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Agence", id);
        }
        agenceRepository.deleteById(id);
    }

    private void verifierNom(Agence agence) {
        if (agence.getNom() == null || agence.getNom().isBlank()) {
            throw new IllegalArgumentException(
                    "Le nom de l'agence est obligatoire");
        }
    }
}
