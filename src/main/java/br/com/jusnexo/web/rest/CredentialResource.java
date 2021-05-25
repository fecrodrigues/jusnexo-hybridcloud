package br.com.jusnexo.web.rest;

import br.com.jusnexo.domain.Credential;
import br.com.jusnexo.repository.CredentialRepository;
import br.com.jusnexo.service.CredentialService;
import br.com.jusnexo.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.jusnexo.domain.Credential}.
 */
@RestController
@RequestMapping("/api")
public class CredentialResource {

    private final Logger log = LoggerFactory.getLogger(CredentialResource.class);

    private static final String ENTITY_NAME = "credential";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CredentialService credentialService;

    private final CredentialRepository credentialRepository;

    public CredentialResource(CredentialService credentialService, CredentialRepository credentialRepository) {
        this.credentialService = credentialService;
        this.credentialRepository = credentialRepository;
    }

    /**
     * {@code POST  /credentials} : Create a new credential.
     *
     * @param credential the credential to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new credential, or with status {@code 400 (Bad Request)} if the credential has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/credentials")
    public ResponseEntity<Credential> createCredential(@Valid @RequestBody Credential credential) throws URISyntaxException {
        log.debug("REST request to save Credential : {}", credential);
        if (credential.getId() != null) {
            throw new BadRequestAlertException("A new credential cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Credential result = credentialService.save(credential);
        return ResponseEntity
            .created(new URI("/api/credentials/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /credentials/:id} : Updates an existing credential.
     *
     * @param id the id of the credential to save.
     * @param credential the credential to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated credential,
     * or with status {@code 400 (Bad Request)} if the credential is not valid,
     * or with status {@code 500 (Internal Server Error)} if the credential couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/credentials/{id}")
    public ResponseEntity<Credential> updateCredential(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Credential credential
    ) throws URISyntaxException {
        log.debug("REST request to update Credential : {}, {}", id, credential);
        if (credential.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, credential.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!credentialRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Credential result = credentialService.save(credential);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, credential.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /credentials/:id} : Partial updates given fields of an existing credential, field will ignore if it is null
     *
     * @param id the id of the credential to save.
     * @param credential the credential to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated credential,
     * or with status {@code 400 (Bad Request)} if the credential is not valid,
     * or with status {@code 404 (Not Found)} if the credential is not found,
     * or with status {@code 500 (Internal Server Error)} if the credential couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/credentials/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Credential> partialUpdateCredential(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Credential credential
    ) throws URISyntaxException {
        log.debug("REST request to partial update Credential partially : {}, {}", id, credential);
        if (credential.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, credential.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!credentialRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Credential> result = credentialService.partialUpdate(credential);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, credential.getId().toString())
        );
    }

    /**
     * {@code GET  /credentials} : get all the credentials.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of credentials in body.
     */
    @GetMapping("/credentials")
    public ResponseEntity<List<Credential>> getAllCredentials(Pageable pageable, @RequestParam(required = false) String filter) {
        if ("client-is-null".equals(filter)) {
            log.debug("REST request to get all Credentials where client is null");
            return new ResponseEntity<>(credentialService.findAllWhereClientIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Credentials");
        Page<Credential> page = credentialService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /credentials/:id} : get the "id" credential.
     *
     * @param id the id of the credential to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the credential, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/credentials/{id}")
    public ResponseEntity<Credential> getCredential(@PathVariable Long id) {
        log.debug("REST request to get Credential : {}", id);
        Optional<Credential> credential = credentialService.findOne(id);
        return ResponseUtil.wrapOrNotFound(credential);
    }

    /**
     * {@code DELETE  /credentials/:id} : delete the "id" credential.
     *
     * @param id the id of the credential to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/credentials/{id}")
    public ResponseEntity<Void> deleteCredential(@PathVariable Long id) {
        log.debug("REST request to delete Credential : {}", id);
        credentialService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
