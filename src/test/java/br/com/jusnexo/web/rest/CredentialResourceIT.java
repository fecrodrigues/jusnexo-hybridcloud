package br.com.jusnexo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.jusnexo.IntegrationTest;
import br.com.jusnexo.domain.Credential;
import br.com.jusnexo.repository.CredentialRepository;
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
 * Integration tests for the {@link CredentialResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CredentialResourceIT {

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/credentials";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCredentialMockMvc;

    private Credential credential;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Credential createEntity(EntityManager em) {
        Credential credential = new Credential().password(DEFAULT_PASSWORD).username(DEFAULT_USERNAME);
        return credential;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Credential createUpdatedEntity(EntityManager em) {
        Credential credential = new Credential().password(UPDATED_PASSWORD).username(UPDATED_USERNAME);
        return credential;
    }

    @BeforeEach
    public void initTest() {
        credential = createEntity(em);
    }

    @Test
    @Transactional
    void createCredential() throws Exception {
        int databaseSizeBeforeCreate = credentialRepository.findAll().size();
        // Create the Credential
        restCredentialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(credential)))
            .andExpect(status().isCreated());

        // Validate the Credential in the database
        List<Credential> credentialList = credentialRepository.findAll();
        assertThat(credentialList).hasSize(databaseSizeBeforeCreate + 1);
        Credential testCredential = credentialList.get(credentialList.size() - 1);
        assertThat(testCredential.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testCredential.getUsername()).isEqualTo(DEFAULT_USERNAME);
    }

    @Test
    @Transactional
    void createCredentialWithExistingId() throws Exception {
        // Create the Credential with an existing ID
        credential.setId(1L);

        int databaseSizeBeforeCreate = credentialRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCredentialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(credential)))
            .andExpect(status().isBadRequest());

        // Validate the Credential in the database
        List<Credential> credentialList = credentialRepository.findAll();
        assertThat(credentialList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = credentialRepository.findAll().size();
        // set the field null
        credential.setPassword(null);

        // Create the Credential, which fails.

        restCredentialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(credential)))
            .andExpect(status().isBadRequest());

        List<Credential> credentialList = credentialRepository.findAll();
        assertThat(credentialList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUsernameIsRequired() throws Exception {
        int databaseSizeBeforeTest = credentialRepository.findAll().size();
        // set the field null
        credential.setUsername(null);

        // Create the Credential, which fails.

        restCredentialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(credential)))
            .andExpect(status().isBadRequest());

        List<Credential> credentialList = credentialRepository.findAll();
        assertThat(credentialList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCredentials() throws Exception {
        // Initialize the database
        credentialRepository.saveAndFlush(credential);

        // Get all the credentialList
        restCredentialMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(credential.getId().intValue())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)));
    }

    @Test
    @Transactional
    void getCredential() throws Exception {
        // Initialize the database
        credentialRepository.saveAndFlush(credential);

        // Get the credential
        restCredentialMockMvc
            .perform(get(ENTITY_API_URL_ID, credential.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(credential.getId().intValue()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME));
    }

    @Test
    @Transactional
    void getNonExistingCredential() throws Exception {
        // Get the credential
        restCredentialMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCredential() throws Exception {
        // Initialize the database
        credentialRepository.saveAndFlush(credential);

        int databaseSizeBeforeUpdate = credentialRepository.findAll().size();

        // Update the credential
        Credential updatedCredential = credentialRepository.findById(credential.getId()).get();
        // Disconnect from session so that the updates on updatedCredential are not directly saved in db
        em.detach(updatedCredential);
        updatedCredential.password(UPDATED_PASSWORD).username(UPDATED_USERNAME);

        restCredentialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCredential.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCredential))
            )
            .andExpect(status().isOk());

        // Validate the Credential in the database
        List<Credential> credentialList = credentialRepository.findAll();
        assertThat(credentialList).hasSize(databaseSizeBeforeUpdate);
        Credential testCredential = credentialList.get(credentialList.size() - 1);
        assertThat(testCredential.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testCredential.getUsername()).isEqualTo(UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void putNonExistingCredential() throws Exception {
        int databaseSizeBeforeUpdate = credentialRepository.findAll().size();
        credential.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCredentialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, credential.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(credential))
            )
            .andExpect(status().isBadRequest());

        // Validate the Credential in the database
        List<Credential> credentialList = credentialRepository.findAll();
        assertThat(credentialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCredential() throws Exception {
        int databaseSizeBeforeUpdate = credentialRepository.findAll().size();
        credential.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCredentialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(credential))
            )
            .andExpect(status().isBadRequest());

        // Validate the Credential in the database
        List<Credential> credentialList = credentialRepository.findAll();
        assertThat(credentialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCredential() throws Exception {
        int databaseSizeBeforeUpdate = credentialRepository.findAll().size();
        credential.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCredentialMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(credential)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Credential in the database
        List<Credential> credentialList = credentialRepository.findAll();
        assertThat(credentialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCredentialWithPatch() throws Exception {
        // Initialize the database
        credentialRepository.saveAndFlush(credential);

        int databaseSizeBeforeUpdate = credentialRepository.findAll().size();

        // Update the credential using partial update
        Credential partialUpdatedCredential = new Credential();
        partialUpdatedCredential.setId(credential.getId());

        restCredentialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCredential.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCredential))
            )
            .andExpect(status().isOk());

        // Validate the Credential in the database
        List<Credential> credentialList = credentialRepository.findAll();
        assertThat(credentialList).hasSize(databaseSizeBeforeUpdate);
        Credential testCredential = credentialList.get(credentialList.size() - 1);
        assertThat(testCredential.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testCredential.getUsername()).isEqualTo(DEFAULT_USERNAME);
    }

    @Test
    @Transactional
    void fullUpdateCredentialWithPatch() throws Exception {
        // Initialize the database
        credentialRepository.saveAndFlush(credential);

        int databaseSizeBeforeUpdate = credentialRepository.findAll().size();

        // Update the credential using partial update
        Credential partialUpdatedCredential = new Credential();
        partialUpdatedCredential.setId(credential.getId());

        partialUpdatedCredential.password(UPDATED_PASSWORD).username(UPDATED_USERNAME);

        restCredentialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCredential.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCredential))
            )
            .andExpect(status().isOk());

        // Validate the Credential in the database
        List<Credential> credentialList = credentialRepository.findAll();
        assertThat(credentialList).hasSize(databaseSizeBeforeUpdate);
        Credential testCredential = credentialList.get(credentialList.size() - 1);
        assertThat(testCredential.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testCredential.getUsername()).isEqualTo(UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void patchNonExistingCredential() throws Exception {
        int databaseSizeBeforeUpdate = credentialRepository.findAll().size();
        credential.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCredentialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, credential.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(credential))
            )
            .andExpect(status().isBadRequest());

        // Validate the Credential in the database
        List<Credential> credentialList = credentialRepository.findAll();
        assertThat(credentialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCredential() throws Exception {
        int databaseSizeBeforeUpdate = credentialRepository.findAll().size();
        credential.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCredentialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(credential))
            )
            .andExpect(status().isBadRequest());

        // Validate the Credential in the database
        List<Credential> credentialList = credentialRepository.findAll();
        assertThat(credentialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCredential() throws Exception {
        int databaseSizeBeforeUpdate = credentialRepository.findAll().size();
        credential.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCredentialMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(credential))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Credential in the database
        List<Credential> credentialList = credentialRepository.findAll();
        assertThat(credentialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCredential() throws Exception {
        // Initialize the database
        credentialRepository.saveAndFlush(credential);

        int databaseSizeBeforeDelete = credentialRepository.findAll().size();

        // Delete the credential
        restCredentialMockMvc
            .perform(delete(ENTITY_API_URL_ID, credential.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Credential> credentialList = credentialRepository.findAll();
        assertThat(credentialList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
