package br.com.jusnexo.repository;

import br.com.jusnexo.domain.AreaOfExpertise;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AreaOfExpertise entity.
 */
@Repository
public interface AreaOfExpertiseRepository extends JpaRepository<AreaOfExpertise, Long> {
    @Query(
        value = "select distinct areaOfExpertise from AreaOfExpertise areaOfExpertise left join fetch areaOfExpertise.clients",
        countQuery = "select count(distinct areaOfExpertise) from AreaOfExpertise areaOfExpertise"
    )
    Page<AreaOfExpertise> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct areaOfExpertise from AreaOfExpertise areaOfExpertise left join fetch areaOfExpertise.clients")
    List<AreaOfExpertise> findAllWithEagerRelationships();

    @Query(
        "select areaOfExpertise from AreaOfExpertise areaOfExpertise left join fetch areaOfExpertise.clients where areaOfExpertise.id =:id"
    )
    Optional<AreaOfExpertise> findOneWithEagerRelationships(@Param("id") Long id);
}
