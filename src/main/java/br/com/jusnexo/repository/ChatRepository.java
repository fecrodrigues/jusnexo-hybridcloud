package br.com.jusnexo.repository;

import br.com.jusnexo.domain.Chat;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Chat entity.
 */
@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query(
        value = "select distinct chat from Chat chat left join fetch chat.clientReceivers left join fetch chat.clientSenders",
        countQuery = "select count(distinct chat) from Chat chat"
    )
    Page<Chat> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct chat from Chat chat left join fetch chat.clientReceivers left join fetch chat.clientSenders")
    List<Chat> findAllWithEagerRelationships();

    @Query("select chat from Chat chat left join fetch chat.clientReceivers left join fetch chat.clientSenders where chat.id =:id")
    Optional<Chat> findOneWithEagerRelationships(@Param("id") Long id);
}
