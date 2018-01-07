package com.noesisinformatica.northumbriaproms.web.rest;

import com.noesisinformatica.northumbriaproms.NorthumbriapromsApp;
import com.noesisinformatica.northumbriaproms.domain.*;
import com.noesisinformatica.northumbriaproms.domain.enumeration.EventType;
import com.noesisinformatica.northumbriaproms.repository.CareEventRepository;
import com.noesisinformatica.northumbriaproms.repository.search.CareEventSearchRepository;
import com.noesisinformatica.northumbriaproms.service.CareEventQueryService;
import com.noesisinformatica.northumbriaproms.service.CareEventService;
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
 * Test class for the CareEventResource REST controller.
 *
 * @see CareEventResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NorthumbriapromsApp.class)
public class CareEventResourceIntTest {

    private static final EventType DEFAULT_TYPE = EventType.SCHEDULED;
    private static final EventType UPDATED_TYPE = EventType.AD_HOC;

    @Autowired
    private CareEventRepository careEventRepository;

    @Autowired
    private CareEventService careEventService;

    @Autowired
    private CareEventSearchRepository careEventSearchRepository;

    @Autowired
    private CareEventQueryService careEventQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCareEventMockMvc;

    private CareEvent careEvent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CareEventResource careEventResource = new CareEventResource(careEventService, careEventQueryService);
        this.restCareEventMockMvc = MockMvcBuilders.standaloneSetup(careEventResource)
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
    public static CareEvent createEntity(EntityManager em) {
        CareEvent careEvent = new CareEvent()
            .type(DEFAULT_TYPE);
        // Add required entity
        Patient patient = PatientResourceIntTest.createEntity(em);
        em.persist(patient);
        em.flush();
        careEvent.setPatient(patient);
        // Add required entity
        FollowupPlan followupPlan = FollowupPlanResourceIntTest.createEntity(em);
        em.persist(followupPlan);
        em.flush();
        careEvent.setFollowupPlan(followupPlan);
        return careEvent;
    }

    @Before
    public void initTest() {
        careEventSearchRepository.deleteAll();
        careEvent = createEntity(em);
    }

    @Test
    @Transactional
    public void createCareEvent() throws Exception {
        int databaseSizeBeforeCreate = careEventRepository.findAll().size();

        // Create the CareEvent
        restCareEventMockMvc.perform(post("/api/care-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(careEvent)))
            .andExpect(status().isCreated());

        // Validate the CareEvent in the database
        List<CareEvent> careEventList = careEventRepository.findAll();
        assertThat(careEventList).hasSize(databaseSizeBeforeCreate + 1);
        CareEvent testCareEvent = careEventList.get(careEventList.size() - 1);
        assertThat(testCareEvent.getType()).isEqualTo(DEFAULT_TYPE);

        // Validate the CareEvent in Elasticsearch
        CareEvent careEventEs = careEventSearchRepository.findOne(testCareEvent.getId());
        assertThat(careEventEs).isEqualToComparingFieldByField(testCareEvent);
    }

    @Test
    @Transactional
    public void createCareEventWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = careEventRepository.findAll().size();

        // Create the CareEvent with an existing ID
        careEvent.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCareEventMockMvc.perform(post("/api/care-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(careEvent)))
            .andExpect(status().isBadRequest());

        // Validate the CareEvent in the database
        List<CareEvent> careEventList = careEventRepository.findAll();
        assertThat(careEventList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = careEventRepository.findAll().size();
        // set the field null
        careEvent.setType(null);

        // Create the CareEvent, which fails.

        restCareEventMockMvc.perform(post("/api/care-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(careEvent)))
            .andExpect(status().isBadRequest());

        List<CareEvent> careEventList = careEventRepository.findAll();
        assertThat(careEventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCareEvents() throws Exception {
        // Initialize the database
        careEventRepository.saveAndFlush(careEvent);

        // Get all the careEventList
        restCareEventMockMvc.perform(get("/api/care-events?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(careEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getCareEvent() throws Exception {
        // Initialize the database
        careEventRepository.saveAndFlush(careEvent);

        // Get the careEvent
        restCareEventMockMvc.perform(get("/api/care-events/{id}", careEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(careEvent.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getAllCareEventsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        careEventRepository.saveAndFlush(careEvent);

        // Get all the careEventList where type equals to DEFAULT_TYPE
        defaultCareEventShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the careEventList where type equals to UPDATED_TYPE
        defaultCareEventShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllCareEventsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        careEventRepository.saveAndFlush(careEvent);

        // Get all the careEventList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultCareEventShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the careEventList where type equals to UPDATED_TYPE
        defaultCareEventShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllCareEventsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        careEventRepository.saveAndFlush(careEvent);

        // Get all the careEventList where type is not null
        defaultCareEventShouldBeFound("type.specified=true");

        // Get all the careEventList where type is null
        defaultCareEventShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllCareEventsByTimepointIsEqualToSomething() throws Exception {
        // Initialize the database
        Timepoint timepoint = TimepointResourceIntTest.createEntity(em);
        em.persist(timepoint);
        em.flush();
        careEvent.setTimepoint(timepoint);
        careEventRepository.saveAndFlush(careEvent);
        Long timepointId = timepoint.getId();

        // Get all the careEventList where timepoint equals to timepointId
        defaultCareEventShouldBeFound("timepointId.equals=" + timepointId);

        // Get all the careEventList where timepoint equals to timepointId + 1
        defaultCareEventShouldNotBeFound("timepointId.equals=" + (timepointId + 1));
    }


    @Test
    @Transactional
    public void getAllCareEventsByPatientIsEqualToSomething() throws Exception {
        // Initialize the database
        Patient patient = PatientResourceIntTest.createEntity(em);
        em.persist(patient);
        em.flush();
        careEvent.setPatient(patient);
        careEventRepository.saveAndFlush(careEvent);
        Long patientId = patient.getId();

        // Get all the careEventList where patient equals to patientId
        defaultCareEventShouldBeFound("patientId.equals=" + patientId);

        // Get all the careEventList where patient equals to patientId + 1
        defaultCareEventShouldNotBeFound("patientId.equals=" + (patientId + 1));
    }


    @Test
    @Transactional
    public void getAllCareEventsByFollowupActionsIsEqualToSomething() throws Exception {
        // Initialize the database
        FollowupAction followupActions = FollowupActionResourceIntTest.createEntity(em);
        em.persist(followupActions);
        em.flush();
        careEvent.addFollowupAction(followupActions);
        careEventRepository.saveAndFlush(careEvent);
        Long followupActionsId = followupActions.getId();

        // Get all the careEventList where followupActions equals to followupActionsId
        defaultCareEventShouldBeFound("followupActionsId.equals=" + followupActionsId);

        // Get all the careEventList where followupActions equals to followupActionsId + 1
        defaultCareEventShouldNotBeFound("followupActionsId.equals=" + (followupActionsId + 1));
    }


    @Test
    @Transactional
    public void getAllCareEventsByFollowupPlanIsEqualToSomething() throws Exception {
        // Initialize the database
        FollowupPlan followupPlan = FollowupPlanResourceIntTest.createEntity(em);
        em.persist(followupPlan);
        em.flush();
        careEvent.setFollowupPlan(followupPlan);
        careEventRepository.saveAndFlush(careEvent);
        Long followupPlanId = followupPlan.getId();

        // Get all the careEventList where followupPlan equals to followupPlanId
        defaultCareEventShouldBeFound("followupPlanId.equals=" + followupPlanId);

        // Get all the careEventList where followupPlan equals to followupPlanId + 1
        defaultCareEventShouldNotBeFound("followupPlanId.equals=" + (followupPlanId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCareEventShouldBeFound(String filter) throws Exception {
        restCareEventMockMvc.perform(get("/api/care-events?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(careEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCareEventShouldNotBeFound(String filter) throws Exception {
        restCareEventMockMvc.perform(get("/api/care-events?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingCareEvent() throws Exception {
        // Get the careEvent
        restCareEventMockMvc.perform(get("/api/care-events/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCareEvent() throws Exception {
        // Initialize the database
        careEventService.save(careEvent);

        int databaseSizeBeforeUpdate = careEventRepository.findAll().size();

        // Update the careEvent
        CareEvent updatedCareEvent = careEventRepository.findOne(careEvent.getId());
        // Disconnect from session so that the updates on updatedCareEvent are not directly saved in db
        em.detach(updatedCareEvent);
        updatedCareEvent
            .type(UPDATED_TYPE);

        restCareEventMockMvc.perform(put("/api/care-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCareEvent)))
            .andExpect(status().isOk());

        // Validate the CareEvent in the database
        List<CareEvent> careEventList = careEventRepository.findAll();
        assertThat(careEventList).hasSize(databaseSizeBeforeUpdate);
        CareEvent testCareEvent = careEventList.get(careEventList.size() - 1);
        assertThat(testCareEvent.getType()).isEqualTo(UPDATED_TYPE);

        // Validate the CareEvent in Elasticsearch
        CareEvent careEventEs = careEventSearchRepository.findOne(testCareEvent.getId());
        assertThat(careEventEs).isEqualToComparingFieldByField(testCareEvent);
    }

    @Test
    @Transactional
    public void updateNonExistingCareEvent() throws Exception {
        int databaseSizeBeforeUpdate = careEventRepository.findAll().size();

        // Create the CareEvent

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCareEventMockMvc.perform(put("/api/care-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(careEvent)))
            .andExpect(status().isCreated());

        // Validate the CareEvent in the database
        List<CareEvent> careEventList = careEventRepository.findAll();
        assertThat(careEventList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCareEvent() throws Exception {
        // Initialize the database
        careEventService.save(careEvent);

        int databaseSizeBeforeDelete = careEventRepository.findAll().size();

        // Get the careEvent
        restCareEventMockMvc.perform(delete("/api/care-events/{id}", careEvent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean careEventExistsInEs = careEventSearchRepository.exists(careEvent.getId());
        assertThat(careEventExistsInEs).isFalse();

        // Validate the database is empty
        List<CareEvent> careEventList = careEventRepository.findAll();
        assertThat(careEventList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCareEvent() throws Exception {
        // Initialize the database
        careEventService.save(careEvent);

        // Search the careEvent
        restCareEventMockMvc.perform(get("/api/_search/care-events?query=id:" + careEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(careEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CareEvent.class);
        CareEvent careEvent1 = new CareEvent();
        careEvent1.setId(1L);
        CareEvent careEvent2 = new CareEvent();
        careEvent2.setId(careEvent1.getId());
        assertThat(careEvent1).isEqualTo(careEvent2);
        careEvent2.setId(2L);
        assertThat(careEvent1).isNotEqualTo(careEvent2);
        careEvent1.setId(null);
        assertThat(careEvent1).isNotEqualTo(careEvent2);
    }
}
