package br.com.jusnexo.service;

import br.com.jusnexo.domain.AreaOfExpertise;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link AreaOfExpertise}.
 */
public interface AreaOfExpertiseService {
    /**
     * Save a areaOfExpertise.
     *
     * @param areaOfExpertise the entity to save.
     * @return the persisted entity.
     */
    AreaOfExpertise save(AreaOfExpertise areaOfExpertise);

    /**
     * Partially updates a areaOfExpertise.
     *
     * @param areaOfExpertise the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AreaOfExpertise> partialUpdate(AreaOfExpertise areaOfExpertise);

    /**
     * Get all the areaOfExpertises.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AreaOfExpertise> findAll(Pageable pageable);

    /**
     * Get all the areaOfExpertises with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AreaOfExpertise> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" areaOfExpertise.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AreaOfExpertise> findOne(Long id);

    /**
     * Delete the "id" areaOfExpertise.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
