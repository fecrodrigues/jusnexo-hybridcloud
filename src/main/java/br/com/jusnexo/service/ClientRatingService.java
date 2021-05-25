package br.com.jusnexo.service;

import br.com.jusnexo.domain.ClientRating;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ClientRating}.
 */
public interface ClientRatingService {
    /**
     * Save a clientRating.
     *
     * @param clientRating the entity to save.
     * @return the persisted entity.
     */
    ClientRating save(ClientRating clientRating);

    /**
     * Partially updates a clientRating.
     *
     * @param clientRating the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ClientRating> partialUpdate(ClientRating clientRating);

    /**
     * Get all the clientRatings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ClientRating> findAll(Pageable pageable);

    /**
     * Get the "id" clientRating.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ClientRating> findOne(Long id);

    /**
     * Delete the "id" clientRating.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
