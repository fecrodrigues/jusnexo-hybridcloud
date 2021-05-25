package br.com.jusnexo.web.rest;

import br.com.jusnexo.domain.ClientRating;
import br.com.jusnexo.repository.ClientRatingRepository;
import br.com.jusnexo.service.ClientRatingService;
import br.com.jusnexo.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link br.com.jusnexo.domain.ClientRating}.
 */
@RestController
@RequestMapping("/api")
public class ClientRatingResource {

    private final Logger log = LoggerFactory.getLogger(ClientRatingResource.class);

    private static final String ENTITY_NAME = "clientRating";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClientRatingService clientRatingService;

    private final ClientRatingRepository clientRatingRepository;

    public ClientRatingResource(ClientRatingService clientRatingService, ClientRatingRepository clientRatingRepository) {
        this.clientRatingService = clientRatingService;
        this.clientRatingRepository = clientRatingRepository;
    }

    /**
     * {@code POST  /client-ratings} : Create a new clientRating.
     *
     * @param clientRating the clientRating to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clientRating, or with status {@code 400 (Bad Request)} if the clientRating has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/client-ratings")
    public ResponseEntity<ClientRating> createClientRating(@Valid @RequestBody ClientRating clientRating) throws URISyntaxException {
        log.debug("REST request to save ClientRating : {}", clientRating);
        if (clientRating.getId() != null) {
            throw new BadRequestAlertException("A new clientRating cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClientRating result = clientRatingService.save(clientRating);
        return ResponseEntity
            .created(new URI("/api/client-ratings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /client-ratings/:id} : Updates an existing clientRating.
     *
     * @param id the id of the clientRating to save.
     * @param clientRating the clientRating to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clientRating,
     * or with status {@code 400 (Bad Request)} if the clientRating is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clientRating couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/client-ratings/{id}")
    public ResponseEntity<ClientRating> updateClientRating(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ClientRating clientRating
    ) throws URISyntaxException {
        log.debug("REST request to update ClientRating : {}, {}", id, clientRating);
        if (clientRating.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clientRating.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clientRatingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ClientRating result = clientRatingService.save(clientRating);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, clientRating.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /client-ratings/:id} : Partial updates given fields of an existing clientRating, field will ignore if it is null
     *
     * @param id the id of the clientRating to save.
     * @param clientRating the clientRating to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clientRating,
     * or with status {@code 400 (Bad Request)} if the clientRating is not valid,
     * or with status {@code 404 (Not Found)} if the clientRating is not found,
     * or with status {@code 500 (Internal Server Error)} if the clientRating couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/client-ratings/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ClientRating> partialUpdateClientRating(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ClientRating clientRating
    ) throws URISyntaxException {
        log.debug("REST request to partial update ClientRating partially : {}, {}", id, clientRating);
        if (clientRating.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clientRating.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clientRatingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ClientRating> result = clientRatingService.partialUpdate(clientRating);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, clientRating.getId().toString())
        );
    }

    /**
     * {@code GET  /client-ratings} : get all the clientRatings.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clientRatings in body.
     */
    @GetMapping("/client-ratings")
    public ResponseEntity<List<ClientRating>> getAllClientRatings(Pageable pageable) {
        log.debug("REST request to get a page of ClientRatings");
        Page<ClientRating> page = clientRatingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /client-ratings/:id} : get the "id" clientRating.
     *
     * @param id the id of the clientRating to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clientRating, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/client-ratings/{id}")
    public ResponseEntity<ClientRating> getClientRating(@PathVariable Long id) {
        log.debug("REST request to get ClientRating : {}", id);
        Optional<ClientRating> clientRating = clientRatingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(clientRating);
    }

    /**
     * {@code DELETE  /client-ratings/:id} : delete the "id" clientRating.
     *
     * @param id the id of the clientRating to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/client-ratings/{id}")
    public ResponseEntity<Void> deleteClientRating(@PathVariable Long id) {
        log.debug("REST request to delete ClientRating : {}", id);
        clientRatingService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
