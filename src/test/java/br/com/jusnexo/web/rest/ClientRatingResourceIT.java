package br.com.jusnexo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.jusnexo.IntegrationTest;
import br.com.jusnexo.domain.ClientRating;
import br.com.jusnexo.repository.ClientRatingRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ClientRatingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClientRatingResourceIT {

    private static final Integer DEFAULT_SCORE = 1;
    private static final Integer UPDATED_SCORE = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/client-ratings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClientRatingRepository clientRatingRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClientRatingMockMvc;

    private ClientRating clientRating;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClientRating createEntity(EntityManager em) {
        ClientRating clientRating = new ClientRating().score(DEFAULT_SCORE).description(DEFAULT_DESCRIPTION);
        return clientRating;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClientRating createUpdatedEntity(EntityManager em) {
        ClientRating clientRating = new ClientRating().score(UPDATED_SCORE).description(UPDATED_DESCRIPTION);
        return clientRating;
    }

    @BeforeEach
    public void initTest() {
        clientRating = createEntity(em);
    }

    @Test
    @Transactional
    void createClientRating() throws Exception {
        int databaseSizeBeforeCreate = clientRatingRepository.findAll().size();
        // Create the ClientRating
        restClientRatingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clientRating)))
            .andExpect(status().isCreated());

        // Validate the ClientRating in the database
        List<ClientRating> clientRatingList = clientRatingRepository.findAll();
        assertThat(clientRatingList).hasSize(databaseSizeBeforeCreate + 1);
        ClientRating testClientRating = clientRatingList.get(clientRatingList.size() - 1);
        assertThat(testClientRating.getScore()).isEqualTo(DEFAULT_SCORE);
        assertThat(testClientRating.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createClientRatingWithExistingId() throws Exception {
        // Create the ClientRating with an existing ID
        clientRating.setId(1L);

        int databaseSizeBeforeCreate = clientRatingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClientRatingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clientRating)))
            .andExpect(status().isBadRequest());

        // Validate the ClientRating in the database
        List<ClientRating> clientRatingList = clientRatingRepository.findAll();
        assertThat(clientRatingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkScoreIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRatingRepository.findAll().size();
        // set the field null
        clientRating.setScore(null);

        // Create the ClientRating, which fails.

        restClientRatingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clientRating)))
            .andExpect(status().isBadRequest());

        List<ClientRating> clientRatingList = clientRatingRepository.findAll();
        assertThat(clientRatingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRatingRepository.findAll().size();
        // set the field null
        clientRating.setDescription(null);

        // Create the ClientRating, which fails.

        restClientRatingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clientRating)))
            .andExpect(status().isBadRequest());

        List<ClientRating> clientRatingList = clientRatingRepository.findAll();
        assertThat(clientRatingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClientRatings() throws Exception {
        // Initialize the database
        clientRatingRepository.saveAndFlush(clientRating);

        // Get all the clientRatingList
        restClientRatingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clientRating.getId().intValue())))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getClientRating() throws Exception {
        // Initialize the database
        clientRatingRepository.saveAndFlush(clientRating);

        // Get the clientRating
        restClientRatingMockMvc
            .perform(get(ENTITY_API_URL_ID, clientRating.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(clientRating.getId().intValue()))
            .andExpect(jsonPath("$.score").value(DEFAULT_SCORE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingClientRating() throws Exception {
        // Get the clientRating
        restClientRatingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewClientRating() throws Exception {
        // Initialize the database
        clientRatingRepository.saveAndFlush(clientRating);

        int databaseSizeBeforeUpdate = clientRatingRepository.findAll().size();

        // Update the clientRating
        ClientRating updatedClientRating = clientRatingRepository.findById(clientRating.getId()).get();
        // Disconnect from session so that the updates on updatedClientRating are not directly saved in db
        em.detach(updatedClientRating);
        updatedClientRating.score(UPDATED_SCORE).description(UPDATED_DESCRIPTION);

        restClientRatingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedClientRating.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedClientRating))
            )
            .andExpect(status().isOk());

        // Validate the ClientRating in the database
        List<ClientRating> clientRatingList = clientRatingRepository.findAll();
        assertThat(clientRatingList).hasSize(databaseSizeBeforeUpdate);
        ClientRating testClientRating = clientRatingList.get(clientRatingList.size() - 1);
        assertThat(testClientRating.getScore()).isEqualTo(UPDATED_SCORE);
        assertThat(testClientRating.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingClientRating() throws Exception {
        int databaseSizeBeforeUpdate = clientRatingRepository.findAll().size();
        clientRating.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientRatingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clientRating.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clientRating))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientRating in the database
        List<ClientRating> clientRatingList = clientRatingRepository.findAll();
        assertThat(clientRatingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClientRating() throws Exception {
        int databaseSizeBeforeUpdate = clientRatingRepository.findAll().size();
        clientRating.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientRatingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clientRating))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientRating in the database
        List<ClientRating> clientRatingList = clientRatingRepository.findAll();
        assertThat(clientRatingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClientRating() throws Exception {
        int databaseSizeBeforeUpdate = clientRatingRepository.findAll().size();
        clientRating.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientRatingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clientRating)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClientRating in the database
        List<ClientRating> clientRatingList = clientRatingRepository.findAll();
        assertThat(clientRatingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClientRatingWithPatch() throws Exception {
        // Initialize the database
        clientRatingRepository.saveAndFlush(clientRating);

        int databaseSizeBeforeUpdate = clientRatingRepository.findAll().size();

        // Update the clientRating using partial update
        ClientRating partialUpdatedClientRating = new ClientRating();
        partialUpdatedClientRating.setId(clientRating.getId());

        partialUpdatedClientRating.score(UPDATED_SCORE).description(UPDATED_DESCRIPTION);

        restClientRatingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClientRating.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClientRating))
            )
            .andExpect(status().isOk());

        // Validate the ClientRating in the database
        List<ClientRating> clientRatingList = clientRatingRepository.findAll();
        assertThat(clientRatingList).hasSize(databaseSizeBeforeUpdate);
        ClientRating testClientRating = clientRatingList.get(clientRatingList.size() - 1);
        assertThat(testClientRating.getScore()).isEqualTo(UPDATED_SCORE);
        assertThat(testClientRating.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateClientRatingWithPatch() throws Exception {
        // Initialize the database
        clientRatingRepository.saveAndFlush(clientRating);

        int databaseSizeBeforeUpdate = clientRatingRepository.findAll().size();

        // Update the clientRating using partial update
        ClientRating partialUpdatedClientRating = new ClientRating();
        partialUpdatedClientRating.setId(clientRating.getId());

        partialUpdatedClientRating.score(UPDATED_SCORE).description(UPDATED_DESCRIPTION);

        restClientRatingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClientRating.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClientRating))
            )
            .andExpect(status().isOk());

        // Validate the ClientRating in the database
        List<ClientRating> clientRatingList = clientRatingRepository.findAll();
        assertThat(clientRatingList).hasSize(databaseSizeBeforeUpdate);
        ClientRating testClientRating = clientRatingList.get(clientRatingList.size() - 1);
        assertThat(testClientRating.getScore()).isEqualTo(UPDATED_SCORE);
        assertThat(testClientRating.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingClientRating() throws Exception {
        int databaseSizeBeforeUpdate = clientRatingRepository.findAll().size();
        clientRating.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientRatingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clientRating.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clientRating))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientRating in the database
        List<ClientRating> clientRatingList = clientRatingRepository.findAll();
        assertThat(clientRatingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClientRating() throws Exception {
        int databaseSizeBeforeUpdate = clientRatingRepository.findAll().size();
        clientRating.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientRatingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clientRating))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientRating in the database
        List<ClientRating> clientRatingList = clientRatingRepository.findAll();
        assertThat(clientRatingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClientRating() throws Exception {
        int databaseSizeBeforeUpdate = clientRatingRepository.findAll().size();
        clientRating.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientRatingMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(clientRating))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClientRating in the database
        List<ClientRating> clientRatingList = clientRatingRepository.findAll();
        assertThat(clientRatingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClientRating() throws Exception {
        // Initialize the database
        clientRatingRepository.saveAndFlush(clientRating);

        int databaseSizeBeforeDelete = clientRatingRepository.findAll().size();

        // Delete the clientRating
        restClientRatingMockMvc
            .perform(delete(ENTITY_API_URL_ID, clientRating.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ClientRating> clientRatingList = clientRatingRepository.findAll();
        assertThat(clientRatingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
