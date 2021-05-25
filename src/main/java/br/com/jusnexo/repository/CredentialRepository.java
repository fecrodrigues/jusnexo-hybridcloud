package br.com.jusnexo.repository;

import br.com.jusnexo.domain.Credential;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Credential entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CredentialRepository extends JpaRepository<Credential, Long> {}
