package tn.esprit.autoloc.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.service.IClientService;
import tn.esprit.autoloc.web.dto.ClientDTO;
import tn.esprit.autoloc.web.mapper.ClientMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientServiceImpl implements IClientService {

    private final IClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    public ClientDTO create(ClientDTO dto) {
        Client client = clientMapper.toEntity(dto);
        if (client.getId() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau client ne doit pas avoir d'identifiant");
        }
        verifierClient(client);
        return clientMapper.toDto(clientRepository.save(client));
    }

    @Override
    @Transactional(readOnly = true)
    public ClientDTO findById(Long id) {
        return clientMapper.toDto(charger(id));
    }

    private Client charger(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Client", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientDTO> findAll() {
        return clientRepository.findAll().stream().map(clientMapper::toDto).toList();
    }

    @Override
    public ClientDTO update(Long id, ClientDTO dto) {
        Client existant = charger(id);
        Client client = clientMapper.toEntity(dto);
        verifierClient(client);

        existant.setNom(client.getNom());
        existant.setPrenom(client.getPrenom());
        existant.setEmail(client.getEmail());
        existant.setTelephone(client.getTelephone());
        existant.setNumPermis(client.getNumPermis());
        existant.setDateInscription(client.getDateInscription());

        return clientMapper.toDto(clientRepository.save(existant));
    }

    @Override
    public void deleteById(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Client", id);
        }
        clientRepository.deleteById(id);
    }

    private void verifierClient(Client client) {
        if (client.getNom() == null || client.getNom().isBlank()) {
            throw new IllegalArgumentException(
                    "Le nom du client est obligatoire");
        }
        if (client.getEmail() == null || client.getEmail().isBlank()) {
            throw new IllegalArgumentException(
                    "L'email du client est obligatoire");
        }
    }
}
