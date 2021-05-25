package br.com.jusnexo.repository;

import br.com.jusnexo.domain.ClientRating;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ClientRating entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClientRatingRepository extends JpaRepository<ClientRating, Long> {}
