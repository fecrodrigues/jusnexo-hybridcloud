package br.com.jusnexo.service.impl;

import br.com.jusnexo.domain.Credential;
import br.com.jusnexo.repository.CredentialRepository;
import br.com.jusnexo.service.CredentialService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Credential}.
 */
@Service
@Transactional
public class CredentialServiceImpl implements CredentialService {

    private final Logger log = LoggerFactory.getLogger(CredentialServiceImpl.class);

    private final CredentialRepository credentialRepository;

    public CredentialServiceImpl(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }

    @Override
    public Credential save(Credential credential) {
        log.debug("Request to save Credential : {}", credential);
        return credentialRepository.save(credential);
    }

    @Override
    public Optional<Credential> partialUpdate(Credential credential) {
        log.debug("Request to partially update Credential : {}", credential);

        return credentialRepository
            .findById(credential.getId())
            .map(
                existingCredential -> {
                    if (credential.getPassword() != null) {
                        existingCredential.setPassword(credential.getPassword());
                    }
                    if (credential.getUsername() != null) {
                        existingCredential.setUsername(credential.getUsername());
                    }

                    return existingCredential;
                }
            )
            .map(credentialRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Credential> findAll(Pageable pageable) {
        log.debug("Request to get all Credentials");
        return credentialRepository.findAll(pageable);
    }

    /**
     *  Get all the credentials where Client is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Credential> findAllWhereClientIsNull() {
        log.debug("Request to get all credentials where Client is null");
        return StreamSupport
            .stream(credentialRepository.findAll().spliterator(), false)
            .filter(credential -> credential.getClient() == null)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Credential> findOne(Long id) {
        log.debug("Request to get Credential : {}", id);
        return credentialRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Credential : {}", id);
        credentialRepository.deleteById(id);
    }
}
