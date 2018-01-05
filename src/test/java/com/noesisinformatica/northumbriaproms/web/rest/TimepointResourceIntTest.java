package com.noesisinformatica.northumbriaproms.web.rest;

import com.noesisinformatica.northumbriaproms.NorthumbriapromsApp;

import com.noesisinformatica.northumbriaproms.domain.Timepoint;
import com.noesisinformatica.northumbriaproms.repository.TimepointRepository;
import com.noesisinformatica.northumbriaproms.service.TimepointService;
import com.noesisinformatica.northumbriaproms.repository.search.TimepointSearchRepository;
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

import com.noesisinformatica.northumbriaproms.domain.enumeration.TimeUnit;
/**
 * Test class for the TimepointResource REST controller.
 *
 * @see TimepointResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NorthumbriapromsApp.class)
public class TimepointResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_VALUE = 1;
    private static final Integer UPDATED_VALUE = 2;

    private static final TimeUnit DEFAULT_UNIT = TimeUnit.DAY;
    private static final TimeUnit UPDATED_UNIT = TimeUnit.WEEK;

    @Autowired
    private TimepointRepository timepointRepository;

    @Autowired
    private TimepointService timepointService;

    @Autowired
    private TimepointSearchRepository timepointSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTimepointMockMvc;

    private Timepoint timepoint;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TimepointResource timepointResource = new TimepointResource(timepointService);
        this.restTimepointMockMvc = MockMvcBuilders.standaloneSetup(timepointResource)
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
    public static Timepoint createEntity(EntityManager em) {
        Timepoint timepoint = new Timepoint()
            .name(DEFAULT_NAME)
            .value(DEFAULT_VALUE)
            .unit(DEFAULT_UNIT);
        return timepoint;
    }

    @Before
    public void initTest() {
        timepointSearchRepository.deleteAll();
        timepoint = createEntity(em);
    }

    @Test
    @Transactional
    public void createTimepoint() throws Exception {
        int databaseSizeBeforeCreate = timepointRepository.findAll().size();

        // Create the Timepoint
        restTimepointMockMvc.perform(post("/api/timepoints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timepoint)))
            .andExpect(status().isCreated());

        // Validate the Timepoint in the database
        List<Timepoint> timepointList = timepointRepository.findAll();
        assertThat(timepointList).hasSize(databaseSizeBeforeCreate + 1);
        Timepoint testTimepoint = timepointList.get(timepointList.size() - 1);
        assertThat(testTimepoint.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTimepoint.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testTimepoint.getUnit()).isEqualTo(DEFAULT_UNIT);

        // Validate the Timepoint in Elasticsearch
        Timepoint timepointEs = timepointSearchRepository.findOne(testTimepoint.getId());
        assertThat(timepointEs).isEqualToComparingFieldByField(testTimepoint);
    }

    @Test
    @Transactional
    public void createTimepointWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = timepointRepository.findAll().size();

        // Create the Timepoint with an existing ID
        timepoint.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTimepointMockMvc.perform(post("/api/timepoints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timepoint)))
            .andExpect(status().isBadRequest());

        // Validate the Timepoint in the database
        List<Timepoint> timepointList = timepointRepository.findAll();
        assertThat(timepointList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = timepointRepository.findAll().size();
        // set the field null
        timepoint.setName(null);

        // Create the Timepoint, which fails.

        restTimepointMockMvc.perform(post("/api/timepoints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timepoint)))
            .andExpect(status().isBadRequest());

        List<Timepoint> timepointList = timepointRepository.findAll();
        assertThat(timepointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = timepointRepository.findAll().size();
        // set the field null
        timepoint.setValue(null);

        // Create the Timepoint, which fails.

        restTimepointMockMvc.perform(post("/api/timepoints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timepoint)))
            .andExpect(status().isBadRequest());

        List<Timepoint> timepointList = timepointRepository.findAll();
        assertThat(timepointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUnitIsRequired() throws Exception {
        int databaseSizeBeforeTest = timepointRepository.findAll().size();
        // set the field null
        timepoint.setUnit(null);

        // Create the Timepoint, which fails.

        restTimepointMockMvc.perform(post("/api/timepoints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timepoint)))
            .andExpect(status().isBadRequest());

        List<Timepoint> timepointList = timepointRepository.findAll();
        assertThat(timepointList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTimepoints() throws Exception {
        // Initialize the database
        timepointRepository.saveAndFlush(timepoint);

        // Get all the timepointList
        restTimepointMockMvc.perform(get("/api/timepoints?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timepoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT.toString())));
    }

    @Test
    @Transactional
    public void getTimepoint() throws Exception {
        // Initialize the database
        timepointRepository.saveAndFlush(timepoint);

        // Get the timepoint
        restTimepointMockMvc.perform(get("/api/timepoints/{id}", timepoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(timepoint.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTimepoint() throws Exception {
        // Get the timepoint
        restTimepointMockMvc.perform(get("/api/timepoints/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTimepoint() throws Exception {
        // Initialize the database
        timepointService.save(timepoint);

        int databaseSizeBeforeUpdate = timepointRepository.findAll().size();

        // Update the timepoint
        Timepoint updatedTimepoint = timepointRepository.findOne(timepoint.getId());
        // Disconnect from session so that the updates on updatedTimepoint are not directly saved in db
        em.detach(updatedTimepoint);
        updatedTimepoint
            .name(UPDATED_NAME)
            .value(UPDATED_VALUE)
            .unit(UPDATED_UNIT);

        restTimepointMockMvc.perform(put("/api/timepoints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTimepoint)))
            .andExpect(status().isOk());

        // Validate the Timepoint in the database
        List<Timepoint> timepointList = timepointRepository.findAll();
        assertThat(timepointList).hasSize(databaseSizeBeforeUpdate);
        Timepoint testTimepoint = timepointList.get(timepointList.size() - 1);
        assertThat(testTimepoint.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTimepoint.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testTimepoint.getUnit()).isEqualTo(UPDATED_UNIT);

        // Validate the Timepoint in Elasticsearch
        Timepoint timepointEs = timepointSearchRepository.findOne(testTimepoint.getId());
        assertThat(timepointEs).isEqualToComparingFieldByField(testTimepoint);
    }

    @Test
    @Transactional
    public void updateNonExistingTimepoint() throws Exception {
        int databaseSizeBeforeUpdate = timepointRepository.findAll().size();

        // Create the Timepoint

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTimepointMockMvc.perform(put("/api/timepoints")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timepoint)))
            .andExpect(status().isCreated());

        // Validate the Timepoint in the database
        List<Timepoint> timepointList = timepointRepository.findAll();
        assertThat(timepointList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTimepoint() throws Exception {
        // Initialize the database
        timepointService.save(timepoint);

        int databaseSizeBeforeDelete = timepointRepository.findAll().size();

        // Get the timepoint
        restTimepointMockMvc.perform(delete("/api/timepoints/{id}", timepoint.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean timepointExistsInEs = timepointSearchRepository.exists(timepoint.getId());
        assertThat(timepointExistsInEs).isFalse();

        // Validate the database is empty
        List<Timepoint> timepointList = timepointRepository.findAll();
        assertThat(timepointList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTimepoint() throws Exception {
        // Initialize the database
        timepointService.save(timepoint);

        // Search the timepoint
        restTimepointMockMvc.perform(get("/api/_search/timepoints?query=id:" + timepoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timepoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Timepoint.class);
        Timepoint timepoint1 = new Timepoint();
        timepoint1.setId(1L);
        Timepoint timepoint2 = new Timepoint();
        timepoint2.setId(timepoint1.getId());
        assertThat(timepoint1).isEqualTo(timepoint2);
        timepoint2.setId(2L);
        assertThat(timepoint1).isNotEqualTo(timepoint2);
        timepoint1.setId(null);
        assertThat(timepoint1).isNotEqualTo(timepoint2);
    }
}
