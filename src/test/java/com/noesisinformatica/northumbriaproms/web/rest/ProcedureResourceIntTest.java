package com.noesisinformatica.northumbriaproms.web.rest;

import com.noesisinformatica.northumbriaproms.NorthumbriapromsApp;

import com.noesisinformatica.northumbriaproms.domain.Procedure;
import com.noesisinformatica.northumbriaproms.repository.ProcedureRepository;
import com.noesisinformatica.northumbriaproms.service.ProcedureService;
import com.noesisinformatica.northumbriaproms.repository.search.ProcedureSearchRepository;
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
 * Test class for the ProcedureResource REST controller.
 *
 * @see ProcedureResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NorthumbriapromsApp.class)
public class ProcedureResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EXTERNAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_LOCAL_CODE = 1;
    private static final Integer UPDATED_LOCAL_CODE = 2;

    @Autowired
    private ProcedureRepository procedureRepository;

    @Autowired
    private ProcedureService procedureService;

    @Autowired
    private ProcedureSearchRepository procedureSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProcedureMockMvc;

    private Procedure procedure;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProcedureResource procedureResource = new ProcedureResource(procedureService);
        this.restProcedureMockMvc = MockMvcBuilders.standaloneSetup(procedureResource)
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
    public static Procedure createEntity(EntityManager em) {
        Procedure procedure = new Procedure()
            .name(DEFAULT_NAME)
            .externalCode(DEFAULT_EXTERNAL_CODE)
            .localCode(DEFAULT_LOCAL_CODE);
        return procedure;
    }

    @Before
    public void initTest() {
        procedureSearchRepository.deleteAll();
        procedure = createEntity(em);
    }

    @Test
    @Transactional
    public void createProcedure() throws Exception {
        int databaseSizeBeforeCreate = procedureRepository.findAll().size();

        // Create the Procedure
        restProcedureMockMvc.perform(post("/api/procedures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedure)))
            .andExpect(status().isCreated());

        // Validate the Procedure in the database
        List<Procedure> procedureList = procedureRepository.findAll();
        assertThat(procedureList).hasSize(databaseSizeBeforeCreate + 1);
        Procedure testProcedure = procedureList.get(procedureList.size() - 1);
        assertThat(testProcedure.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProcedure.getExternalCode()).isEqualTo(DEFAULT_EXTERNAL_CODE);
        assertThat(testProcedure.getLocalCode()).isEqualTo(DEFAULT_LOCAL_CODE);

        // Validate the Procedure in Elasticsearch
        Procedure procedureEs = procedureSearchRepository.findOne(testProcedure.getId());
        assertThat(procedureEs).isEqualToComparingFieldByField(testProcedure);
    }

    @Test
    @Transactional
    public void createProcedureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = procedureRepository.findAll().size();

        // Create the Procedure with an existing ID
        procedure.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcedureMockMvc.perform(post("/api/procedures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedure)))
            .andExpect(status().isBadRequest());

        // Validate the Procedure in the database
        List<Procedure> procedureList = procedureRepository.findAll();
        assertThat(procedureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = procedureRepository.findAll().size();
        // set the field null
        procedure.setName(null);

        // Create the Procedure, which fails.

        restProcedureMockMvc.perform(post("/api/procedures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedure)))
            .andExpect(status().isBadRequest());

        List<Procedure> procedureList = procedureRepository.findAll();
        assertThat(procedureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProcedures() throws Exception {
        // Initialize the database
        procedureRepository.saveAndFlush(procedure);

        // Get all the procedureList
        restProcedureMockMvc.perform(get("/api/procedures?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(procedure.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].externalCode").value(hasItem(DEFAULT_EXTERNAL_CODE.toString())))
            .andExpect(jsonPath("$.[*].localCode").value(hasItem(DEFAULT_LOCAL_CODE)));
    }

    @Test
    @Transactional
    public void getProcedure() throws Exception {
        // Initialize the database
        procedureRepository.saveAndFlush(procedure);

        // Get the procedure
        restProcedureMockMvc.perform(get("/api/procedures/{id}", procedure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(procedure.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.externalCode").value(DEFAULT_EXTERNAL_CODE.toString()))
            .andExpect(jsonPath("$.localCode").value(DEFAULT_LOCAL_CODE));
    }

    @Test
    @Transactional
    public void getNonExistingProcedure() throws Exception {
        // Get the procedure
        restProcedureMockMvc.perform(get("/api/procedures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProcedure() throws Exception {
        // Initialize the database
        procedureService.save(procedure);

        int databaseSizeBeforeUpdate = procedureRepository.findAll().size();

        // Update the procedure
        Procedure updatedProcedure = procedureRepository.findOne(procedure.getId());
        // Disconnect from session so that the updates on updatedProcedure are not directly saved in db
        em.detach(updatedProcedure);
        updatedProcedure
            .name(UPDATED_NAME)
            .externalCode(UPDATED_EXTERNAL_CODE)
            .localCode(UPDATED_LOCAL_CODE);

        restProcedureMockMvc.perform(put("/api/procedures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProcedure)))
            .andExpect(status().isOk());

        // Validate the Procedure in the database
        List<Procedure> procedureList = procedureRepository.findAll();
        assertThat(procedureList).hasSize(databaseSizeBeforeUpdate);
        Procedure testProcedure = procedureList.get(procedureList.size() - 1);
        assertThat(testProcedure.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProcedure.getExternalCode()).isEqualTo(UPDATED_EXTERNAL_CODE);
        assertThat(testProcedure.getLocalCode()).isEqualTo(UPDATED_LOCAL_CODE);

        // Validate the Procedure in Elasticsearch
        Procedure procedureEs = procedureSearchRepository.findOne(testProcedure.getId());
        assertThat(procedureEs).isEqualToComparingFieldByField(testProcedure);
    }

    @Test
    @Transactional
    public void updateNonExistingProcedure() throws Exception {
        int databaseSizeBeforeUpdate = procedureRepository.findAll().size();

        // Create the Procedure

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProcedureMockMvc.perform(put("/api/procedures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedure)))
            .andExpect(status().isCreated());

        // Validate the Procedure in the database
        List<Procedure> procedureList = procedureRepository.findAll();
        assertThat(procedureList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProcedure() throws Exception {
        // Initialize the database
        procedureService.save(procedure);

        int databaseSizeBeforeDelete = procedureRepository.findAll().size();

        // Get the procedure
        restProcedureMockMvc.perform(delete("/api/procedures/{id}", procedure.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean procedureExistsInEs = procedureSearchRepository.exists(procedure.getId());
        assertThat(procedureExistsInEs).isFalse();

        // Validate the database is empty
        List<Procedure> procedureList = procedureRepository.findAll();
        assertThat(procedureList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProcedure() throws Exception {
        // Initialize the database
        procedureService.save(procedure);

        // Search the procedure
        restProcedureMockMvc.perform(get("/api/_search/procedures?query=id:" + procedure.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(procedure.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].externalCode").value(hasItem(DEFAULT_EXTERNAL_CODE.toString())))
            .andExpect(jsonPath("$.[*].localCode").value(hasItem(DEFAULT_LOCAL_CODE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Procedure.class);
        Procedure procedure1 = new Procedure();
        procedure1.setId(1L);
        Procedure procedure2 = new Procedure();
        procedure2.setId(procedure1.getId());
        assertThat(procedure1).isEqualTo(procedure2);
        procedure2.setId(2L);
        assertThat(procedure1).isNotEqualTo(procedure2);
        procedure1.setId(null);
        assertThat(procedure1).isNotEqualTo(procedure2);
    }
}
