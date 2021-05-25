package br.com.jusnexo.service;

import br.com.jusnexo.domain.Chat;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Chat}.
 */
public interface ChatService {
    /**
     * Save a chat.
     *
     * @param chat the entity to save.
     * @return the persisted entity.
     */
    Chat save(Chat chat);

    /**
     * Partially updates a chat.
     *
     * @param chat the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Chat> partialUpdate(Chat chat);

    /**
     * Get all the chats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Chat> findAll(Pageable pageable);

    /**
     * Get all the chats with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Chat> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" chat.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Chat> findOne(Long id);

    /**
     * Delete the "id" chat.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
