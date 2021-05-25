package br.com.jusnexo.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.jusnexo.IntegrationTest;
import br.com.jusnexo.domain.AreaOfExpertise;
import br.com.jusnexo.repository.AreaOfExpertiseRepository;
import br.com.jusnexo.service.AreaOfExpertiseService;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AreaOfExpertiseResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AreaOfExpertiseResourceIT {

    private static final String DEFAULT_AREA_NAME = "AAAAAAAAAA";
    private static final String UPDATED_AREA_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_SELECTED = false;
    private static final Boolean UPDATED_IS_SELECTED = true;

    private static final String ENTITY_API_URL = "/api/area-of-expertises";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AreaOfExpertiseRepository areaOfExpertiseRepository;

    @Mock
    private AreaOfExpertiseRepository areaOfExpertiseRepositoryMock;

    @Mock
    private AreaOfExpertiseService areaOfExpertiseServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAreaOfExpertiseMockMvc;

    private AreaOfExpertise areaOfExpertise;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AreaOfExpertise createEntity(EntityManager em) {
        AreaOfExpertise areaOfExpertise = new AreaOfExpertise().areaName(DEFAULT_AREA_NAME).isSelected(DEFAULT_IS_SELECTED);
        return areaOfExpertise;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AreaOfExpertise createUpdatedEntity(EntityManager em) {
        AreaOfExpertise areaOfExpertise = new AreaOfExpertise().areaName(UPDATED_AREA_NAME).isSelected(UPDATED_IS_SELECTED);
        return areaOfExpertise;
    }

    @BeforeEach
    public void initTest() {
        areaOfExpertise = createEntity(em);
    }

    @Test
    @Transactional
    void createAreaOfExpertise() throws Exception {
        int databaseSizeBeforeCreate = areaOfExpertiseRepository.findAll().size();
        // Create the AreaOfExpertise
        restAreaOfExpertiseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(areaOfExpertise))
            )
            .andExpect(status().isCreated());

        // Validate the AreaOfExpertise in the database
        List<AreaOfExpertise> areaOfExpertiseList = areaOfExpertiseRepository.findAll();
        assertThat(areaOfExpertiseList).hasSize(databaseSizeBeforeCreate + 1);
        AreaOfExpertise testAreaOfExpertise = areaOfExpertiseList.get(areaOfExpertiseList.size() - 1);
        assertThat(testAreaOfExpertise.getAreaName()).isEqualTo(DEFAULT_AREA_NAME);
        assertThat(testAreaOfExpertise.getIsSelected()).isEqualTo(DEFAULT_IS_SELECTED);
    }

    @Test
    @Transactional
    void createAreaOfExpertiseWithExistingId() throws Exception {
        // Create the AreaOfExpertise with an existing ID
        areaOfExpertise.setId(1L);

        int databaseSizeBeforeCreate = areaOfExpertiseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAreaOfExpertiseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(areaOfExpertise))
            )
            .andExpect(status().isBadRequest());

        // Validate the AreaOfExpertise in the database
        List<AreaOfExpertise> areaOfExpertiseList = areaOfExpertiseRepository.findAll();
        assertThat(areaOfExpertiseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAreaOfExpertises() throws Exception {
        // Initialize the database
        areaOfExpertiseRepository.saveAndFlush(areaOfExpertise);

        // Get all the areaOfExpertiseList
        restAreaOfExpertiseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(areaOfExpertise.getId().intValue())))
            .andExpect(jsonPath("$.[*].areaName").value(hasItem(DEFAULT_AREA_NAME)))
            .andExpect(jsonPath("$.[*].isSelected").value(hasItem(DEFAULT_IS_SELECTED.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAreaOfExpertisesWithEagerRelationshipsIsEnabled() throws Exception {
        when(areaOfExpertiseServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAreaOfExpertiseMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(areaOfExpertiseServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAreaOfExpertisesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(areaOfExpertiseServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAreaOfExpertiseMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(areaOfExpertiseServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getAreaOfExpertise() throws Exception {
        // Initialize the database
        areaOfExpertiseRepository.saveAndFlush(areaOfExpertise);

        // Get the areaOfExpertise
        restAreaOfExpertiseMockMvc
            .perform(get(ENTITY_API_URL_ID, areaOfExpertise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(areaOfExpertise.getId().intValue()))
            .andExpect(jsonPath("$.areaName").value(DEFAULT_AREA_NAME))
            .andExpect(jsonPath("$.isSelected").value(DEFAULT_IS_SELECTED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingAreaOfExpertise() throws Exception {
        // Get the areaOfExpertise
        restAreaOfExpertiseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAreaOfExpertise() throws Exception {
        // Initialize the database
        areaOfExpertiseRepository.saveAndFlush(areaOfExpertise);

        int databaseSizeBeforeUpdate = areaOfExpertiseRepository.findAll().size();

        // Update the areaOfExpertise
        AreaOfExpertise updatedAreaOfExpertise = areaOfExpertiseRepository.findById(areaOfExpertise.getId()).get();
        // Disconnect from session so that the updates on updatedAreaOfExpertise are not directly saved in db
        em.detach(updatedAreaOfExpertise);
        updatedAreaOfExpertise.areaName(UPDATED_AREA_NAME).isSelected(UPDATED_IS_SELECTED);

        restAreaOfExpertiseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAreaOfExpertise.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAreaOfExpertise))
            )
            .andExpect(status().isOk());

        // Validate the AreaOfExpertise in the database
        List<AreaOfExpertise> areaOfExpertiseList = areaOfExpertiseRepository.findAll();
        assertThat(areaOfExpertiseList).hasSize(databaseSizeBeforeUpdate);
        AreaOfExpertise testAreaOfExpertise = areaOfExpertiseList.get(areaOfExpertiseList.size() - 1);
        assertThat(testAreaOfExpertise.getAreaName()).isEqualTo(UPDATED_AREA_NAME);
        assertThat(testAreaOfExpertise.getIsSelected()).isEqualTo(UPDATED_IS_SELECTED);
    }

    @Test
    @Transactional
    void putNonExistingAreaOfExpertise() throws Exception {
        int databaseSizeBeforeUpdate = areaOfExpertiseRepository.findAll().size();
        areaOfExpertise.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAreaOfExpertiseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, areaOfExpertise.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(areaOfExpertise))
            )
            .andExpect(status().isBadRequest());

        // Validate the AreaOfExpertise in the database
        List<AreaOfExpertise> areaOfExpertiseList = areaOfExpertiseRepository.findAll();
        assertThat(areaOfExpertiseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAreaOfExpertise() throws Exception {
        int databaseSizeBeforeUpdate = areaOfExpertiseRepository.findAll().size();
        areaOfExpertise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAreaOfExpertiseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(areaOfExpertise))
            )
            .andExpect(status().isBadRequest());

        // Validate the AreaOfExpertise in the database
        List<AreaOfExpertise> areaOfExpertiseList = areaOfExpertiseRepository.findAll();
        assertThat(areaOfExpertiseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAreaOfExpertise() throws Exception {
        int databaseSizeBeforeUpdate = areaOfExpertiseRepository.findAll().size();
        areaOfExpertise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAreaOfExpertiseMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(areaOfExpertise))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AreaOfExpertise in the database
        List<AreaOfExpertise> areaOfExpertiseList = areaOfExpertiseRepository.findAll();
        assertThat(areaOfExpertiseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAreaOfExpertiseWithPatch() throws Exception {
        // Initialize the database
        areaOfExpertiseRepository.saveAndFlush(areaOfExpertise);

        int databaseSizeBeforeUpdate = areaOfExpertiseRepository.findAll().size();

        // Update the areaOfExpertise using partial update
        AreaOfExpertise partialUpdatedAreaOfExpertise = new AreaOfExpertise();
        partialUpdatedAreaOfExpertise.setId(areaOfExpertise.getId());

        partialUpdatedAreaOfExpertise.areaName(UPDATED_AREA_NAME);

        restAreaOfExpertiseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAreaOfExpertise.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAreaOfExpertise))
            )
            .andExpect(status().isOk());

        // Validate the AreaOfExpertise in the database
        List<AreaOfExpertise> areaOfExpertiseList = areaOfExpertiseRepository.findAll();
        assertThat(areaOfExpertiseList).hasSize(databaseSizeBeforeUpdate);
        AreaOfExpertise testAreaOfExpertise = areaOfExpertiseList.get(areaOfExpertiseList.size() - 1);
        assertThat(testAreaOfExpertise.getAreaName()).isEqualTo(UPDATED_AREA_NAME);
        assertThat(testAreaOfExpertise.getIsSelected()).isEqualTo(DEFAULT_IS_SELECTED);
    }

    @Test
    @Transactional
    void fullUpdateAreaOfExpertiseWithPatch() throws Exception {
        // Initialize the database
        areaOfExpertiseRepository.saveAndFlush(areaOfExpertise);

        int databaseSizeBeforeUpdate = areaOfExpertiseRepository.findAll().size();

        // Update the areaOfExpertise using partial update
        AreaOfExpertise partialUpdatedAreaOfExpertise = new AreaOfExpertise();
        partialUpdatedAreaOfExpertise.setId(areaOfExpertise.getId());

        partialUpdatedAreaOfExpertise.areaName(UPDATED_AREA_NAME).isSelected(UPDATED_IS_SELECTED);

        restAreaOfExpertiseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAreaOfExpertise.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAreaOfExpertise))
            )
            .andExpect(status().isOk());

        // Validate the AreaOfExpertise in the database
        List<AreaOfExpertise> areaOfExpertiseList = areaOfExpertiseRepository.findAll();
        assertThat(areaOfExpertiseList).hasSize(databaseSizeBeforeUpdate);
        AreaOfExpertise testAreaOfExpertise = areaOfExpertiseList.get(areaOfExpertiseList.size() - 1);
        assertThat(testAreaOfExpertise.getAreaName()).isEqualTo(UPDATED_AREA_NAME);
        assertThat(testAreaOfExpertise.getIsSelected()).isEqualTo(UPDATED_IS_SELECTED);
    }

    @Test
    @Transactional
    void patchNonExistingAreaOfExpertise() throws Exception {
        int databaseSizeBeforeUpdate = areaOfExpertiseRepository.findAll().size();
        areaOfExpertise.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAreaOfExpertiseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, areaOfExpertise.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(areaOfExpertise))
            )
            .andExpect(status().isBadRequest());

        // Validate the AreaOfExpertise in the database
        List<AreaOfExpertise> areaOfExpertiseList = areaOfExpertiseRepository.findAll();
        assertThat(areaOfExpertiseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAreaOfExpertise() throws Exception {
        int databaseSizeBeforeUpdate = areaOfExpertiseRepository.findAll().size();
        areaOfExpertise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAreaOfExpertiseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(areaOfExpertise))
            )
            .andExpect(status().isBadRequest());

        // Validate the AreaOfExpertise in the database
        List<AreaOfExpertise> areaOfExpertiseList = areaOfExpertiseRepository.findAll();
        assertThat(areaOfExpertiseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAreaOfExpertise() throws Exception {
        int databaseSizeBeforeUpdate = areaOfExpertiseRepository.findAll().size();
        areaOfExpertise.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAreaOfExpertiseMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(areaOfExpertise))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AreaOfExpertise in the database
        List<AreaOfExpertise> areaOfExpertiseList = areaOfExpertiseRepository.findAll();
        assertThat(areaOfExpertiseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAreaOfExpertise() throws Exception {
        // Initialize the database
        areaOfExpertiseRepository.saveAndFlush(areaOfExpertise);

        int databaseSizeBeforeDelete = areaOfExpertiseRepository.findAll().size();

        // Delete the areaOfExpertise
        restAreaOfExpertiseMockMvc
            .perform(delete(ENTITY_API_URL_ID, areaOfExpertise.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AreaOfExpertise> areaOfExpertiseList = areaOfExpertiseRepository.findAll();
        assertThat(areaOfExpertiseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
