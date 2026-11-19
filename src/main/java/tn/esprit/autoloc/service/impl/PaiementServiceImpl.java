package tn.esprit.autoloc.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IPaiementRepository;
import tn.esprit.autoloc.service.IPaiementService;
import tn.esprit.autoloc.web.dto.PaiementDTO;
import tn.esprit.autoloc.web.mapper.PaiementMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class PaiementServiceImpl implements IPaiementService {

    private final IPaiementRepository paiementRepository;
    private final PaiementMapper paiementMapper;

    @Override
    @Transactional(readOnly = true)
    public PaiementDTO findById(Long id) {
        return paiementMapper.toDto(charger(id));
    }

    private Paiement charger(Long id) {
        return paiementRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Paiement", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaiementDTO> findAll() {
        return paiementRepository.findAll().stream().map(paiementMapper::toDto).toList();
    }
}
