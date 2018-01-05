package com.noesisinformatica.northumbriaproms.web.rest;

import com.noesisinformatica.northumbriaproms.NorthumbriapromsApp;

import com.noesisinformatica.northumbriaproms.domain.ProcedureTimepoint;
import com.noesisinformatica.northumbriaproms.domain.Procedure;
import com.noesisinformatica.northumbriaproms.domain.Timepoint;
import com.noesisinformatica.northumbriaproms.repository.ProcedureTimepointRepository;
import com.noesisinformatica.northumbriaproms.service.ProcedureTimepointService;
import com.noesisinformatica.northumbriaproms.repository.search.ProcedureTimepointSearchRepository;
import com.noesisinformatica.northumbriaproms.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.noesisinformatica.northumbriaproms.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProcedureTimepointResource REST controller.
 *
 * @see ProcedureTimepointResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NorthumbriapromsApp.class)
public class ProcedureTimepointResourceIntTest {

    @Autowired
    private ProcedureTimepointRepository procedureTimepointRepository;

    @Autowired
    private ProcedureTimepointService procedureTimepointService;

    @Autowired
    private ProcedureTimepointSearchRepository procedureTimepointSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProcedureTimepointMockMvc;

    private ProcedureTimepoint procedureTimepoint;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProcedureTimepointResource procedureTimepointResource = new ProcedureTimepointResource(procedureTimepointService);
        this.restProcedureTimepointMockMvc = MockMvcBuilders.standaloneSetup(procedureTimepointResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProcedureTimepoint createEntity(EntityManager em) {
        ProcedureTimepoint procedureTimepoint = new ProcedureTimepoint();
        // Add required entity
        Procedure procedure = ProcedureResourceIntTest.createEntity(em);
        em.persist(procedure);
        em.flush();
        procedureTimepoint.setProcedure(procedure);
        // Add required entity
        Timepoint timepoint = TimepointResourceIntTest.createEntity(em);
        em.persist(timepoint);
        em.flush();
        procedureTimepoint.setTimepoint(timepoint);
        return procedureTimepoint;
    }

    @Before
    public void initTest() {
        procedureTimepointSearchRepository.deleteAll();
        procedureTimepoint = createEntity(em);
    }

    @Test
    @Transactional
    public void createProcedureTimepoint() throws Exception {
        int databaseSizeBeforeCreate = procedureTimepointRepository.findAll().size();

        // Create the ProcedureTimepoint
        restProcedureTimepointMockMvc.perform(post("/api/procedure-timepoints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedureTimepoint)))
            .andExpect(status().isCreated());

        // Validate the ProcedureTimepoint in the database
        List<ProcedureTimepoint> procedureTimepointList = procedureTimepointRepository.findAll();
        assertThat(procedureTimepointList).hasSize(databaseSizeBeforeCreate + 1);
        ProcedureTimepoint testProcedureTimepoint = procedureTimepointList.get(procedureTimepointList.size() - 1);

        // Validate the ProcedureTimepoint in Elasticsearch
        ProcedureTimepoint procedureTimepointEs = procedureTimepointSearchRepository.findOne(testProcedureTimepoint.getId());
        assertThat(procedureTimepointEs).isEqualToComparingFieldByField(testProcedureTimepoint);
    }

    @Test
    @Transactional
    public void createProcedureTimepointWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = procedureTimepointRepository.findAll().size();

        // Create the ProcedureTimepoint with an existing ID
        procedureTimepoint.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcedureTimepointMockMvc.perform(post("/api/procedure-timepoints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedureTimepoint)))
            .andExpect(status().isBadRequest());

        // Validate the ProcedureTimepoint in the database
        List<ProcedureTimepoint> procedureTimepointList = procedureTimepointRepository.findAll();
        assertThat(procedureTimepointList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProcedureTimepoints() throws Exception {
        // Initialize the database
        procedureTimepointRepository.saveAndFlush(procedureTimepoint);

        // Get all the procedureTimepointList
        restProcedureTimepointMockMvc.perform(get("/api/procedure-timepoints?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(procedureTimepoint.getId().intValue())));
    }

    @Test
    @Transactional
    public void getProcedureTimepoint() throws Exception {
        // Initialize the database
        procedureTimepointRepository.saveAndFlush(procedureTimepoint);

        // Get the procedureTimepoint
        restProcedureTimepointMockMvc.perform(get("/api/procedure-timepoints/{id}", procedureTimepoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(procedureTimepoint.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingProcedureTimepoint() throws Exception {
        // Get the procedureTimepoint
        restProcedureTimepointMockMvc.perform(get("/api/procedure-timepoints/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProcedureTimepoint() throws Exception {
        // Initialize the database
        procedureTimepointService.save(procedureTimepoint);

        int databaseSizeBeforeUpdate = procedureTimepointRepository.findAll().size();

        // Update the procedureTimepoint
        ProcedureTimepoint updatedProcedureTimepoint = procedureTimepointRepository.findOne(procedureTimepoint.getId());
        // Disconnect from session so that the updates on updatedProcedureTimepoint are not directly saved in db
        em.detach(updatedProcedureTimepoint);

        restProcedureTimepointMockMvc.perform(put("/api/procedure-timepoints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProcedureTimepoint)))
            .andExpect(status().isOk());

        // Validate the ProcedureTimepoint in the database
        List<ProcedureTimepoint> procedureTimepointList = procedureTimepointRepository.findAll();
        assertThat(procedureTimepointList).hasSize(databaseSizeBeforeUpdate);
        ProcedureTimepoint testProcedureTimepoint = procedureTimepointList.get(procedureTimepointList.size() - 1);

        // Validate the ProcedureTimepoint in Elasticsearch
        ProcedureTimepoint procedureTimepointEs = procedureTimepointSearchRepository.findOne(testProcedureTimepoint.getId());
        assertThat(procedureTimepointEs).isEqualToComparingFieldByField(testProcedureTimepoint);
    }

    @Test
    @Transactional
    public void updateNonExistingProcedureTimepoint() throws Exception {
        int databaseSizeBeforeUpdate = procedureTimepointRepository.findAll().size();

        // Create the ProcedureTimepoint

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProcedureTimepointMockMvc.perform(put("/api/procedure-timepoints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedureTimepoint)))
            .andExpect(status().isCreated());

        // Validate the ProcedureTimepoint in the database
        List<ProcedureTimepoint> procedureTimepointList = procedureTimepointRepository.findAll();
        assertThat(procedureTimepointList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProcedureTimepoint() throws Exception {
        // Initialize the database
        procedureTimepointService.save(procedureTimepoint);

        int databaseSizeBeforeDelete = procedureTimepointRepository.findAll().size();

        // Get the procedureTimepoint
        restProcedureTimepointMockMvc.perform(delete("/api/procedure-timepoints/{id}", procedureTimepoint.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean procedureTimepointExistsInEs = procedureTimepointSearchRepository.exists(procedureTimepoint.getId());
        assertThat(procedureTimepointExistsInEs).isFalse();

        // Validate the database is empty
        List<ProcedureTimepoint> procedureTimepointList = procedureTimepointRepository.findAll();
        assertThat(procedureTimepointList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProcedureTimepoint() throws Exception {
        // Initialize the database
        procedureTimepointService.save(procedureTimepoint);

        // Search the procedureTimepoint
        restProcedureTimepointMockMvc.perform(get("/api/_search/procedure-timepoints?query=id:" + procedureTimepoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(procedureTimepoint.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcedureTimepoint.class);
        ProcedureTimepoint procedureTimepoint1 = new ProcedureTimepoint();
        procedureTimepoint1.setId(1L);
        ProcedureTimepoint procedureTimepoint2 = new ProcedureTimepoint();
        procedureTimepoint2.setId(procedureTimepoint1.getId());
        assertThat(procedureTimepoint1).isEqualTo(procedureTimepoint2);
        procedureTimepoint2.setId(2L);
        assertThat(procedureTimepoint1).isNotEqualTo(procedureTimepoint2);
        procedureTimepoint1.setId(null);
        assertThat(procedureTimepoint1).isNotEqualTo(procedureTimepoint2);
    }
}
