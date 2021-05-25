package br.com.jusnexo.web.rest;

import br.com.jusnexo.domain.AreaOfExpertise;
import br.com.jusnexo.repository.AreaOfExpertiseRepository;
import br.com.jusnexo.service.AreaOfExpertiseService;
import br.com.jusnexo.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link br.com.jusnexo.domain.AreaOfExpertise}.
 */
@RestController
@RequestMapping("/api")
public class AreaOfExpertiseResource {

    private final Logger log = LoggerFactory.getLogger(AreaOfExpertiseResource.class);

    private static final String ENTITY_NAME = "areaOfExpertise";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AreaOfExpertiseService areaOfExpertiseService;

    private final AreaOfExpertiseRepository areaOfExpertiseRepository;

    public AreaOfExpertiseResource(AreaOfExpertiseService areaOfExpertiseService, AreaOfExpertiseRepository areaOfExpertiseRepository) {
        this.areaOfExpertiseService = areaOfExpertiseService;
        this.areaOfExpertiseRepository = areaOfExpertiseRepository;
    }

    /**
     * {@code POST  /area-of-expertises} : Create a new areaOfExpertise.
     *
     * @param areaOfExpertise the areaOfExpertise to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new areaOfExpertise, or with status {@code 400 (Bad Request)} if the areaOfExpertise has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/area-of-expertises")
    public ResponseEntity<AreaOfExpertise> createAreaOfExpertise(@RequestBody AreaOfExpertise areaOfExpertise) throws URISyntaxException {
        log.debug("REST request to save AreaOfExpertise : {}", areaOfExpertise);
        if (areaOfExpertise.getId() != null) {
            throw new BadRequestAlertException("A new areaOfExpertise cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AreaOfExpertise result = areaOfExpertiseService.save(areaOfExpertise);
        return ResponseEntity
            .created(new URI("/api/area-of-expertises/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /area-of-expertises/:id} : Updates an existing areaOfExpertise.
     *
     * @param id the id of the areaOfExpertise to save.
     * @param areaOfExpertise the areaOfExpertise to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated areaOfExpertise,
     * or with status {@code 400 (Bad Request)} if the areaOfExpertise is not valid,
     * or with status {@code 500 (Internal Server Error)} if the areaOfExpertise couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/area-of-expertises/{id}")
    public ResponseEntity<AreaOfExpertise> updateAreaOfExpertise(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AreaOfExpertise areaOfExpertise
    ) throws URISyntaxException {
        log.debug("REST request to update AreaOfExpertise : {}, {}", id, areaOfExpertise);
        if (areaOfExpertise.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, areaOfExpertise.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!areaOfExpertiseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AreaOfExpertise result = areaOfExpertiseService.save(areaOfExpertise);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, areaOfExpertise.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /area-of-expertises/:id} : Partial updates given fields of an existing areaOfExpertise, field will ignore if it is null
     *
     * @param id the id of the areaOfExpertise to save.
     * @param areaOfExpertise the areaOfExpertise to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated areaOfExpertise,
     * or with status {@code 400 (Bad Request)} if the areaOfExpertise is not valid,
     * or with status {@code 404 (Not Found)} if the areaOfExpertise is not found,
     * or with status {@code 500 (Internal Server Error)} if the areaOfExpertise couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/area-of-expertises/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AreaOfExpertise> partialUpdateAreaOfExpertise(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AreaOfExpertise areaOfExpertise
    ) throws URISyntaxException {
        log.debug("REST request to partial update AreaOfExpertise partially : {}, {}", id, areaOfExpertise);
        if (areaOfExpertise.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, areaOfExpertise.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!areaOfExpertiseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AreaOfExpertise> result = areaOfExpertiseService.partialUpdate(areaOfExpertise);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, areaOfExpertise.getId().toString())
        );
    }

    /**
     * {@code GET  /area-of-expertises} : get all the areaOfExpertises.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of areaOfExpertises in body.
     */
    @GetMapping("/area-of-expertises")
    public ResponseEntity<List<AreaOfExpertise>> getAllAreaOfExpertises(
        Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of AreaOfExpertises");
        Page<AreaOfExpertise> page;
        if (eagerload) {
            page = areaOfExpertiseService.findAllWithEagerRelationships(pageable);
        } else {
            page = areaOfExpertiseService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /area-of-expertises/:id} : get the "id" areaOfExpertise.
     *
     * @param id the id of the areaOfExpertise to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the areaOfExpertise, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/area-of-expertises/{id}")
    public ResponseEntity<AreaOfExpertise> getAreaOfExpertise(@PathVariable Long id) {
        log.debug("REST request to get AreaOfExpertise : {}", id);
        Optional<AreaOfExpertise> areaOfExpertise = areaOfExpertiseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(areaOfExpertise);
    }

    /**
     * {@code DELETE  /area-of-expertises/:id} : delete the "id" areaOfExpertise.
     *
     * @param id the id of the areaOfExpertise to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/area-of-expertises/{id}")
    public ResponseEntity<Void> deleteAreaOfExpertise(@PathVariable Long id) {
        log.debug("REST request to delete AreaOfExpertise : {}", id);
        areaOfExpertiseService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
