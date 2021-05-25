package br.com.jusnexo.service.impl;

import br.com.jusnexo.domain.Client;
import br.com.jusnexo.repository.ClientRepository;
import br.com.jusnexo.service.ClientService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Client}.
 */
@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    private final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client save(Client client) {
        log.debug("Request to save Client : {}", client);
        return clientRepository.save(client);
    }

    @Override
    public Optional<Client> partialUpdate(Client client) {
        log.debug("Request to partially update Client : {}", client);

        return clientRepository
            .findById(client.getId())
            .map(
                existingClient -> {
                    if (client.getAdvocate() != null) {
                        existingClient.setAdvocate(client.getAdvocate());
                    }
                    if (client.getBiography() != null) {
                        existingClient.setBiography(client.getBiography());
                    }
                    if (client.getBirthdate() != null) {
                        existingClient.setBirthdate(client.getBirthdate());
                    }
                    if (client.getFirstname() != null) {
                        existingClient.setFirstname(client.getFirstname());
                    }
                    if (client.getLastname() != null) {
                        existingClient.setLastname(client.getLastname());
                    }
                    if (client.getOabnumber() != null) {
                        existingClient.setOabnumber(client.getOabnumber());
                    }
                    if (client.getPhone() != null) {
                        existingClient.setPhone(client.getPhone());
                    }
                    if (client.getCreatedAt() != null) {
                        existingClient.setCreatedAt(client.getCreatedAt());
                    }
                    if (client.getPicture() != null) {
                        existingClient.setPicture(client.getPicture());
                    }
                    if (client.getPictureContentType() != null) {
                        existingClient.setPictureContentType(client.getPictureContentType());
                    }

                    return existingClient;
                }
            )
            .map(clientRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Client> findAll(Pageable pageable) {
        log.debug("Request to get all Clients");
        return clientRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Client> findOne(Long id) {
        log.debug("Request to get Client : {}", id);
        return clientRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Client : {}", id);
        clientRepository.deleteById(id);
    }
}
