package br.com.jusnexo.service;

import br.com.jusnexo.domain.Credential;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Credential}.
 */
public interface CredentialService {
    /**
     * Save a credential.
     *
     * @param credential the entity to save.
     * @return the persisted entity.
     */
    Credential save(Credential credential);

    /**
     * Partially updates a credential.
     *
     * @param credential the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Credential> partialUpdate(Credential credential);

    /**
     * Get all the credentials.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Credential> findAll(Pageable pageable);
    /**
     * Get all the Credential where Client is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Credential> findAllWhereClientIsNull();

    /**
     * Get the "id" credential.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Credential> findOne(Long id);

    /**
     * Delete the "id" credential.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
