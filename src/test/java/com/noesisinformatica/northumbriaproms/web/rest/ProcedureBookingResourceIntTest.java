package com.noesisinformatica.northumbriaproms.web.rest;

import com.noesisinformatica.northumbriaproms.NorthumbriapromsApp;
import com.noesisinformatica.northumbriaproms.domain.Patient;
import com.noesisinformatica.northumbriaproms.domain.ProcedureBooking;
import com.noesisinformatica.northumbriaproms.repository.ProcedureBookingRepository;
import com.noesisinformatica.northumbriaproms.repository.search.ProcedureBookingSearchRepository;
import com.noesisinformatica.northumbriaproms.service.PatientService;
import com.noesisinformatica.northumbriaproms.service.ProcedureBookingQueryService;
import com.noesisinformatica.northumbriaproms.service.ProcedureBookingService;
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
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static com.noesisinformatica.northumbriaproms.web.rest.TestUtil.createFormattingConversionService;
import static com.noesisinformatica.northumbriaproms.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProcedureBookingResource REST controller.
 *
 * @see ProcedureBookingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NorthumbriapromsApp.class)
public class ProcedureBookingResourceIntTest {

    private static final String DEFAULT_CONSULTANT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONSULTANT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_HOSPITAL_SITE = "AAAAAAAAAA";
    private static final String UPDATED_HOSPITAL_SITE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_SCHEDULED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SCHEDULED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_PERFORMED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PERFORMED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_PRIMARY_PROCEDURE = "AAAAAAAAAA";
    private static final String UPDATED_PRIMARY_PROCEDURE = "BBBBBBBBBB";

    private static final String DEFAULT_OTHER_PROCEDURES = "AAAAAAAAAA";
    private static final String UPDATED_OTHER_PROCEDURES = "BBBBBBBBBB";

    @Autowired
    private ProcedureBookingRepository procedureBookingRepository;

    @Autowired
    private ProcedureBookingService procedureBookingService;

    @Autowired
    private ProcedureBookingSearchRepository procedureBookingSearchRepository;

    @Autowired
    private PatientService patientService;

    @Autowired
    private ProcedureBookingQueryService procedureBookingQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProcedureBookingMockMvc;

    private ProcedureBooking procedureBooking;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProcedureBookingResource procedureBookingResource = new ProcedureBookingResource(procedureBookingService,
            procedureBookingQueryService, patientService);
        this.restProcedureBookingMockMvc = MockMvcBuilders.standaloneSetup(procedureBookingResource)
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
    public static ProcedureBooking createEntity(EntityManager em) {
        ProcedureBooking procedureBooking = new ProcedureBooking()
            .consultantName(DEFAULT_CONSULTANT_NAME)
            .hospitalSite(DEFAULT_HOSPITAL_SITE)
            .scheduledDate(DEFAULT_SCHEDULED_DATE)
            .performedDate(DEFAULT_PERFORMED_DATE)
            .primaryProcedure(DEFAULT_PRIMARY_PROCEDURE)
            .otherProcedures(DEFAULT_OTHER_PROCEDURES);
        // Add required entity
        Patient patient = PatientResourceIntTest.createEntity(em);
        em.persist(patient);
        em.flush();
        procedureBooking.setPatient(patient);
        return procedureBooking;
    }

    @Before
    public void initTest() {
        procedureBookingSearchRepository.deleteAll();
        procedureBooking = createEntity(em);
    }

    @Test
    @Transactional
    public void createProcedureBooking() throws Exception {
        int databaseSizeBeforeCreate = procedureBookingRepository.findAll().size();

        // Create the ProcedureBooking
        restProcedureBookingMockMvc.perform(post("/api/procedure-bookings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedureBooking)))
            .andExpect(status().isCreated());

        // Validate the ProcedureBooking in the database
        List<ProcedureBooking> procedureBookingList = procedureBookingRepository.findAll();
        assertThat(procedureBookingList).hasSize(databaseSizeBeforeCreate + 1);
        ProcedureBooking testProcedureBooking = procedureBookingList.get(procedureBookingList.size() - 1);
        assertThat(testProcedureBooking.getConsultantName()).isEqualTo(DEFAULT_CONSULTANT_NAME);
        assertThat(testProcedureBooking.getHospitalSite()).isEqualTo(DEFAULT_HOSPITAL_SITE);
        assertThat(testProcedureBooking.getScheduledDate()).isEqualTo(DEFAULT_SCHEDULED_DATE);
        assertThat(testProcedureBooking.getPerformedDate()).isEqualTo(DEFAULT_PERFORMED_DATE);
        assertThat(testProcedureBooking.getPrimaryProcedure()).isEqualTo(DEFAULT_PRIMARY_PROCEDURE);
        assertThat(testProcedureBooking.getOtherProcedures()).isEqualTo(DEFAULT_OTHER_PROCEDURES);

        // Validate the ProcedureBooking in Elasticsearch
        ProcedureBooking procedureBookingEs = procedureBookingSearchRepository.findOne(testProcedureBooking.getId());
        assertThat(procedureBookingEs).isEqualToComparingFieldByField(testProcedureBooking);
    }

    @Test
    @Transactional
    public void createProcedureBookingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = procedureBookingRepository.findAll().size();

        // Create the ProcedureBooking with an existing ID
        procedureBooking.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcedureBookingMockMvc.perform(post("/api/procedure-bookings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedureBooking)))
            .andExpect(status().isBadRequest());

        // Validate the ProcedureBooking in the database
        List<ProcedureBooking> procedureBookingList = procedureBookingRepository.findAll();
        assertThat(procedureBookingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkConsultantNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = procedureBookingRepository.findAll().size();
        // set the field null
        procedureBooking.setConsultantName(null);

        // Create the ProcedureBooking, which fails.

        restProcedureBookingMockMvc.perform(post("/api/procedure-bookings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedureBooking)))
            .andExpect(status().isBadRequest());

        List<ProcedureBooking> procedureBookingList = procedureBookingRepository.findAll();
        assertThat(procedureBookingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHospitalSiteIsRequired() throws Exception {
        int databaseSizeBeforeTest = procedureBookingRepository.findAll().size();
        // set the field null
        procedureBooking.setHospitalSite(null);

        // Create the ProcedureBooking, which fails.

        restProcedureBookingMockMvc.perform(post("/api/procedure-bookings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedureBooking)))
            .andExpect(status().isBadRequest());

        List<ProcedureBooking> procedureBookingList = procedureBookingRepository.findAll();
        assertThat(procedureBookingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrimaryProcedureIsRequired() throws Exception {
        int databaseSizeBeforeTest = procedureBookingRepository.findAll().size();
        // set the field null
        procedureBooking.setPrimaryProcedure(null);

        // Create the ProcedureBooking, which fails.

        restProcedureBookingMockMvc.perform(post("/api/procedure-bookings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedureBooking)))
            .andExpect(status().isBadRequest());

        List<ProcedureBooking> procedureBookingList = procedureBookingRepository.findAll();
        assertThat(procedureBookingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProcedureBookings() throws Exception {
        // Initialize the database
        procedureBookingRepository.saveAndFlush(procedureBooking);

        // Get all the procedureBookingList
        restProcedureBookingMockMvc.perform(get("/api/procedure-bookings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(procedureBooking.getId().intValue())))
            .andExpect(jsonPath("$.[*].consultantName").value(hasItem(DEFAULT_CONSULTANT_NAME.toString())))
            .andExpect(jsonPath("$.[*].hospitalSite").value(hasItem(DEFAULT_HOSPITAL_SITE.toString())))
            .andExpect(jsonPath("$.[*].scheduledDate").value(hasItem(sameInstant(DEFAULT_SCHEDULED_DATE))))
            .andExpect(jsonPath("$.[*].performedDate").value(hasItem(sameInstant(DEFAULT_PERFORMED_DATE))))
            .andExpect(jsonPath("$.[*].primaryProcedure").value(hasItem(DEFAULT_PRIMARY_PROCEDURE.toString())))
            .andExpect(jsonPath("$.[*].otherProcedures").value(hasItem(DEFAULT_OTHER_PROCEDURES.toString())));
    }

    @Test
    @Transactional
    public void getProcedureBooking() throws Exception {
        // Initialize the database
        procedureBookingRepository.saveAndFlush(procedureBooking);

        // Get the procedureBooking
        restProcedureBookingMockMvc.perform(get("/api/procedure-bookings/{id}", procedureBooking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(procedureBooking.getId().intValue()))
            .andExpect(jsonPath("$.consultantName").value(DEFAULT_CONSULTANT_NAME.toString()))
            .andExpect(jsonPath("$.hospitalSite").value(DEFAULT_HOSPITAL_SITE.toString()))
            .andExpect(jsonPath("$.scheduledDate").value(sameInstant(DEFAULT_SCHEDULED_DATE)))
            .andExpect(jsonPath("$.performedDate").value(sameInstant(DEFAULT_PERFORMED_DATE)))
            .andExpect(jsonPath("$.primaryProcedure").value(DEFAULT_PRIMARY_PROCEDURE.toString()))
            .andExpect(jsonPath("$.otherProcedures").value(DEFAULT_OTHER_PROCEDURES.toString()));
    }

    @Test
    @Transactional
    public void getAllProcedureBookingsByConsultantNameIsEqualToSomething() throws Exception {
        // Initialize the database
        procedureBookingRepository.saveAndFlush(procedureBooking);

        // Get all the procedureBookingList where consultantName equals to DEFAULT_CONSULTANT_NAME
        defaultProcedureBookingShouldBeFound("consultantName.equals=" + DEFAULT_CONSULTANT_NAME);

        // Get all the procedureBookingList where consultantName equals to UPDATED_CONSULTANT_NAME
        defaultProcedureBookingShouldNotBeFound("consultantName.equals=" + UPDATED_CONSULTANT_NAME);
    }

    @Test
    @Transactional
    public void getAllProcedureBookingsByConsultantNameIsInShouldWork() throws Exception {
        // Initialize the database
        procedureBookingRepository.saveAndFlush(procedureBooking);

        // Get all the procedureBookingList where consultantName in DEFAULT_CONSULTANT_NAME or UPDATED_CONSULTANT_NAME
        defaultProcedureBookingShouldBeFound("consultantName.in=" + DEFAULT_CONSULTANT_NAME + "," + UPDATED_CONSULTANT_NAME);

        // Get all the procedureBookingList where consultantName equals to UPDATED_CONSULTANT_NAME
        defaultProcedureBookingShouldNotBeFound("consultantName.in=" + UPDATED_CONSULTANT_NAME);
    }

    @Test
    @Transactional
    public void getAllProcedureBookingsByConsultantNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        procedureBookingRepository.saveAndFlush(procedureBooking);

        // Get all the procedureBookingList where consultantName is not null
        defaultProcedureBookingShouldBeFound("consultantName.specified=true");

        // Get all the procedureBookingList where consultantName is null
        defaultProcedureBookingShouldNotBeFound("consultantName.specified=false");
    }

    @Test
    @Transactional
    public void getAllProcedureBookingsByHospitalSiteIsEqualToSomething() throws Exception {
        // Initialize the database
        procedureBookingRepository.saveAndFlush(procedureBooking);

        // Get all the procedureBookingList where hospitalSite equals to DEFAULT_HOSPITAL_SITE
        defaultProcedureBookingShouldBeFound("hospitalSite.equals=" + DEFAULT_HOSPITAL_SITE);

        // Get all the procedureBookingList where hospitalSite equals to UPDATED_HOSPITAL_SITE
        defaultProcedureBookingShouldNotBeFound("hospitalSite.equals=" + UPDATED_HOSPITAL_SITE);
    }

    @Test
    @Transactional
    public void getAllProcedureBookingsByHospitalSiteIsInShouldWork() throws Exception {
        // Initialize the database
        procedureBookingRepository.saveAndFlush(procedureBooking);

        // Get all the procedureBookingList where hospitalSite in DEFAULT_HOSPITAL_SITE or UPDATED_HOSPITAL_SITE
        defaultProcedureBookingShouldBeFound("hospitalSite.in=" + DEFAULT_HOSPITAL_SITE + "," + UPDATED_HOSPITAL_SITE);

        // Get all the procedureBookingList where hospitalSite equals to UPDATED_HOSPITAL_SITE
        defaultProcedureBookingShouldNotBeFound("hospitalSite.in=" + UPDATED_HOSPITAL_SITE);
    }

    @Test
    @Transactional
    public void getAllProcedureBookingsByHospitalSiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        procedureBookingRepository.saveAndFlush(procedureBooking);

        // Get all the procedureBookingList where hospitalSite is not null
        defaultProcedureBookingShouldBeFound("hospitalSite.specified=true");

        // Get all the procedureBookingList where hospitalSite is null
        defaultProcedureBookingShouldNotBeFound("hospitalSite.specified=false");
    }

    @Test
    @Transactional
    public void getAllProcedureBookingsByScheduledDateIsEqualToSomething() throws Exception {
        // Initialize the database
        procedureBookingRepository.saveAndFlush(procedureBooking);

        // Get all the procedureBookingList where scheduledDate equals to DEFAULT_SCHEDULED_DATE
        defaultProcedureBookingShouldBeFound("scheduledDate.equals=" + DEFAULT_SCHEDULED_DATE);

        // Get all the procedureBookingList where scheduledDate equals to UPDATED_SCHEDULED_DATE
        defaultProcedureBookingShouldNotBeFound("scheduledDate.equals=" + UPDATED_SCHEDULED_DATE);
    }

    @Test
    @Transactional
    public void getAllProcedureBookingsByScheduledDateIsInShouldWork() throws Exception {
        // Initialize the database
        procedureBookingRepository.saveAndFlush(procedureBooking);

        // Get all the procedureBookingList where scheduledDate in DEFAULT_SCHEDULED_DATE or UPDATED_SCHEDULED_DATE
        defaultProcedureBookingShouldBeFound("scheduledDate.in=" + DEFAULT_SCHEDULED_DATE + "," + UPDATED_SCHEDULED_DATE);

        // Get all the procedureBookingList where scheduledDate equals to UPDATED_SCHEDULED_DATE
        defaultProcedureBookingShouldNotBeFound("scheduledDate.in=" + UPDATED_SCHEDULED_DATE);
    }

    @Test
    @Transactional
    public void getAllProcedureBookingsByScheduledDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        procedureBookingRepository.saveAndFlush(procedureBooking);

        // Get all the procedureBookingList where scheduledDate is not null
        defaultProcedureBookingShouldBeFound("scheduledDate.specified=true");

        // Get all the procedureBookingList where scheduledDate is null
        defaultProcedureBookingShouldNotBeFound("scheduledDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllProcedureBookingsByScheduledDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        procedureBookingRepository.saveAndFlush(procedureBooking);

        // Get all the procedureBookingList where scheduledDate greater than or equals to DEFAULT_SCHEDULED_DATE
        defaultProcedureBookingShouldBeFound("scheduledDate.greaterOrEqualThan=" + DEFAULT_SCHEDULED_DATE);

        // Get all the procedureBookingList where scheduledDate greater than or equals to UPDATED_SCHEDULED_DATE
        defaultProcedureBookingShouldNotBeFound("scheduledDate.greaterOrEqualThan=" + UPDATED_SCHEDULED_DATE);
    }

    @Test
    @Transactional
    public void getAllProcedureBookingsByScheduledDateIsLessThanSomething() throws Exception {
        // Initialize the database
        procedureBookingRepository.saveAndFlush(procedureBooking);

        // Get all the procedureBookingList where scheduledDate less than or equals to DEFAULT_SCHEDULED_DATE
        defaultProcedureBookingShouldNotBeFound("scheduledDate.lessThan=" + DEFAULT_SCHEDULED_DATE);

        // Get all the procedureBookingList where scheduledDate less than or equals to UPDATED_SCHEDULED_DATE
        defaultProcedureBookingShouldBeFound("scheduledDate.lessThan=" + UPDATED_SCHEDULED_DATE);
    }


    @Test
    @Transactional
    public void getAllProcedureBookingsByPerformedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        procedureBookingRepository.saveAndFlush(procedureBooking);

        // Get all the procedureBookingList where performedDate equals to DEFAULT_PERFORMED_DATE
        defaultProcedureBookingShouldBeFound("performedDate.equals=" + DEFAULT_PERFORMED_DATE);

        // Get all the procedureBookingList where performedDate equals to UPDATED_PERFORMED_DATE
        defaultProcedureBookingShouldNotBeFound("performedDate.equals=" + UPDATED_PERFORMED_DATE);
    }

    @Test
    @Transactional
    public void getAllProcedureBookingsByPerformedDateIsInShouldWork() throws Exception {
        // Initialize the database
        procedureBookingRepository.saveAndFlush(procedureBooking);

        // Get all the procedureBookingList where performedDate in DEFAULT_PERFORMED_DATE or UPDATED_PERFORMED_DATE
        defaultProcedureBookingShouldBeFound("performedDate.in=" + DEFAULT_PERFORMED_DATE + "," + UPDATED_PERFORMED_DATE);

        // Get all the procedureBookingList where performedDate equals to UPDATED_PERFORMED_DATE
        defaultProcedureBookingShouldNotBeFound("performedDate.in=" + UPDATED_PERFORMED_DATE);
    }

    @Test
    @Transactional
    public void getAllProcedureBookingsByPerformedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        procedureBookingRepository.saveAndFlush(procedureBooking);

        // Get all the procedureBookingList where performedDate is not null
        defaultProcedureBookingShouldBeFound("performedDate.specified=true");

        // Get all the procedureBookingList where performedDate is null
        defaultProcedureBookingShouldNotBeFound("performedDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllProcedureBookingsByPerformedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        procedureBookingRepository.saveAndFlush(procedureBooking);

        // Get all the procedureBookingList where performedDate greater than or equals to DEFAULT_PERFORMED_DATE
        defaultProcedureBookingShouldBeFound("performedDate.greaterOrEqualThan=" + DEFAULT_PERFORMED_DATE);

        // Get all the procedureBookingList where performedDate greater than or equals to UPDATED_PERFORMED_DATE
        defaultProcedureBookingShouldNotBeFound("performedDate.greaterOrEqualThan=" + UPDATED_PERFORMED_DATE);
    }

    @Test
    @Transactional
    public void getAllProcedureBookingsByPerformedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        procedureBookingRepository.saveAndFlush(procedureBooking);

        // Get all the procedureBookingList where performedDate less than or equals to DEFAULT_PERFORMED_DATE
        defaultProcedureBookingShouldNotBeFound("performedDate.lessThan=" + DEFAULT_PERFORMED_DATE);

        // Get all the procedureBookingList where performedDate less than or equals to UPDATED_PERFORMED_DATE
        defaultProcedureBookingShouldBeFound("performedDate.lessThan=" + UPDATED_PERFORMED_DATE);
    }


    @Test
    @Transactional
    public void getAllProcedureBookingsByPrimaryProcedureIsEqualToSomething() throws Exception {
        // Initialize the database
        procedureBookingRepository.saveAndFlush(procedureBooking);

        // Get all the procedureBookingList where primaryProcedure equals to DEFAULT_PRIMARY_PROCEDURE
        defaultProcedureBookingShouldBeFound("primaryProcedure.equals=" + DEFAULT_PRIMARY_PROCEDURE);

        // Get all the procedureBookingList where primaryProcedure equals to UPDATED_PRIMARY_PROCEDURE
        defaultProcedureBookingShouldNotBeFound("primaryProcedure.equals=" + UPDATED_PRIMARY_PROCEDURE);
    }

    @Test
    @Transactional
    public void getAllProcedureBookingsByPrimaryProcedureIsInShouldWork() throws Exception {
        // Initialize the database
        procedureBookingRepository.saveAndFlush(procedureBooking);

        // Get all the procedureBookingList where primaryProcedure in DEFAULT_PRIMARY_PROCEDURE or UPDATED_PRIMARY_PROCEDURE
        defaultProcedureBookingShouldBeFound("primaryProcedure.in=" + DEFAULT_PRIMARY_PROCEDURE + "," + UPDATED_PRIMARY_PROCEDURE);

        // Get all the procedureBookingList where primaryProcedure equals to UPDATED_PRIMARY_PROCEDURE
        defaultProcedureBookingShouldNotBeFound("primaryProcedure.in=" + UPDATED_PRIMARY_PROCEDURE);
    }

    @Test
    @Transactional
    public void getAllProcedureBookingsByPrimaryProcedureIsNullOrNotNull() throws Exception {
        // Initialize the database
        procedureBookingRepository.saveAndFlush(procedureBooking);

        // Get all the procedureBookingList where primaryProcedure is not null
        defaultProcedureBookingShouldBeFound("primaryProcedure.specified=true");

        // Get all the procedureBookingList where primaryProcedure is null
        defaultProcedureBookingShouldNotBeFound("primaryProcedure.specified=false");
    }

    @Test
    @Transactional
    public void getAllProcedureBookingsByOtherProceduresIsEqualToSomething() throws Exception {
        // Initialize the database
        procedureBookingRepository.saveAndFlush(procedureBooking);

        // Get all the procedureBookingList where otherProcedures equals to DEFAULT_OTHER_PROCEDURES
        defaultProcedureBookingShouldBeFound("otherProcedures.equals=" + DEFAULT_OTHER_PROCEDURES);

        // Get all the procedureBookingList where otherProcedures equals to UPDATED_OTHER_PROCEDURES
        defaultProcedureBookingShouldNotBeFound("otherProcedures.equals=" + UPDATED_OTHER_PROCEDURES);
    }

    @Test
    @Transactional
    public void getAllProcedureBookingsByOtherProceduresIsInShouldWork() throws Exception {
        // Initialize the database
        procedureBookingRepository.saveAndFlush(procedureBooking);

        // Get all the procedureBookingList where otherProcedures in DEFAULT_OTHER_PROCEDURES or UPDATED_OTHER_PROCEDURES
        defaultProcedureBookingShouldBeFound("otherProcedures.in=" + DEFAULT_OTHER_PROCEDURES + "," + UPDATED_OTHER_PROCEDURES);

        // Get all the procedureBookingList where otherProcedures equals to UPDATED_OTHER_PROCEDURES
        defaultProcedureBookingShouldNotBeFound("otherProcedures.in=" + UPDATED_OTHER_PROCEDURES);
    }

    @Test
    @Transactional
    public void getAllProcedureBookingsByOtherProceduresIsNullOrNotNull() throws Exception {
        // Initialize the database
        procedureBookingRepository.saveAndFlush(procedureBooking);

        // Get all the procedureBookingList where otherProcedures is not null
        defaultProcedureBookingShouldBeFound("otherProcedures.specified=true");

        // Get all the procedureBookingList where otherProcedures is null
        defaultProcedureBookingShouldNotBeFound("otherProcedures.specified=false");
    }

    @Test
    @Transactional
    public void getAllProcedureBookingsByPatientIsEqualToSomething() throws Exception {
        // Initialize the database
        Patient patient = PatientResourceIntTest.createEntity(em);
        em.persist(patient);
        em.flush();
        procedureBooking.setPatient(patient);
        procedureBookingRepository.saveAndFlush(procedureBooking);
        Long patientId = patient.getId();

        // Get all the procedureBookingList where patient equals to patientId
        defaultProcedureBookingShouldBeFound("patientId.equals=" + patientId);

        // Get all the procedureBookingList where patient equals to patientId + 1
        defaultProcedureBookingShouldNotBeFound("patientId.equals=" + (patientId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultProcedureBookingShouldBeFound(String filter) throws Exception {
        restProcedureBookingMockMvc.perform(get("/api/procedure-bookings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(procedureBooking.getId().intValue())))
            .andExpect(jsonPath("$.[*].consultantName").value(hasItem(DEFAULT_CONSULTANT_NAME.toString())))
            .andExpect(jsonPath("$.[*].hospitalSite").value(hasItem(DEFAULT_HOSPITAL_SITE.toString())))
            .andExpect(jsonPath("$.[*].scheduledDate").value(hasItem(sameInstant(DEFAULT_SCHEDULED_DATE))))
            .andExpect(jsonPath("$.[*].performedDate").value(hasItem(sameInstant(DEFAULT_PERFORMED_DATE))))
            .andExpect(jsonPath("$.[*].primaryProcedure").value(hasItem(DEFAULT_PRIMARY_PROCEDURE.toString())))
            .andExpect(jsonPath("$.[*].otherProcedures").value(hasItem(DEFAULT_OTHER_PROCEDURES.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultProcedureBookingShouldNotBeFound(String filter) throws Exception {
        restProcedureBookingMockMvc.perform(get("/api/procedure-bookings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingProcedureBooking() throws Exception {
        // Get the procedureBooking
        restProcedureBookingMockMvc.perform(get("/api/procedure-bookings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProcedureBooking() throws Exception {
        // Initialize the database
        procedureBookingService.save(procedureBooking);

        int databaseSizeBeforeUpdate = procedureBookingRepository.findAll().size();

        // Update the procedureBooking
        ProcedureBooking updatedProcedureBooking = procedureBookingRepository.findOne(procedureBooking.getId());
        // Disconnect from session so that the updates on updatedProcedureBooking are not directly saved in db
        em.detach(updatedProcedureBooking);
        updatedProcedureBooking
            .consultantName(UPDATED_CONSULTANT_NAME)
            .hospitalSite(UPDATED_HOSPITAL_SITE)
            .scheduledDate(UPDATED_SCHEDULED_DATE)
            .performedDate(UPDATED_PERFORMED_DATE)
            .primaryProcedure(UPDATED_PRIMARY_PROCEDURE)
            .otherProcedures(UPDATED_OTHER_PROCEDURES);

        restProcedureBookingMockMvc.perform(put("/api/procedure-bookings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProcedureBooking)))
            .andExpect(status().isOk());

        // Validate the ProcedureBooking in the database
        List<ProcedureBooking> procedureBookingList = procedureBookingRepository.findAll();
        assertThat(procedureBookingList).hasSize(databaseSizeBeforeUpdate);
        ProcedureBooking testProcedureBooking = procedureBookingList.get(procedureBookingList.size() - 1);
        assertThat(testProcedureBooking.getConsultantName()).isEqualTo(UPDATED_CONSULTANT_NAME);
        assertThat(testProcedureBooking.getHospitalSite()).isEqualTo(UPDATED_HOSPITAL_SITE);
        assertThat(testProcedureBooking.getScheduledDate()).isEqualTo(UPDATED_SCHEDULED_DATE);
        assertThat(testProcedureBooking.getPerformedDate()).isEqualTo(UPDATED_PERFORMED_DATE);
        assertThat(testProcedureBooking.getPrimaryProcedure()).isEqualTo(UPDATED_PRIMARY_PROCEDURE);
        assertThat(testProcedureBooking.getOtherProcedures()).isEqualTo(UPDATED_OTHER_PROCEDURES);

        // Validate the ProcedureBooking in Elasticsearch
        ProcedureBooking procedureBookingEs = procedureBookingSearchRepository.findOne(testProcedureBooking.getId());
        assertThat(procedureBookingEs).isEqualToComparingFieldByField(testProcedureBooking);
    }

    @Test
    @Transactional
    public void updateNonExistingProcedureBooking() throws Exception {
        int databaseSizeBeforeUpdate = procedureBookingRepository.findAll().size();

        // Create the ProcedureBooking

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProcedureBookingMockMvc.perform(put("/api/procedure-bookings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedureBooking)))
            .andExpect(status().isCreated());

        // Validate the ProcedureBooking in the database
        List<ProcedureBooking> procedureBookingList = procedureBookingRepository.findAll();
        assertThat(procedureBookingList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProcedureBooking() throws Exception {
        // Initialize the database
        procedureBookingService.save(procedureBooking);

        int databaseSizeBeforeDelete = procedureBookingRepository.findAll().size();

        // Get the procedureBooking
        restProcedureBookingMockMvc.perform(delete("/api/procedure-bookings/{id}", procedureBooking.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean procedureBookingExistsInEs = procedureBookingSearchRepository.exists(procedureBooking.getId());
        assertThat(procedureBookingExistsInEs).isFalse();

        // Validate the database is empty
        List<ProcedureBooking> procedureBookingList = procedureBookingRepository.findAll();
        assertThat(procedureBookingList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProcedureBooking() throws Exception {
        // Initialize the database
        procedureBookingService.save(procedureBooking);

        // Search the procedureBooking
        restProcedureBookingMockMvc.perform(get("/api/_search/procedure-bookings?query=id:" + procedureBooking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(procedureBooking.getId().intValue())))
            .andExpect(jsonPath("$.[*].consultantName").value(hasItem(DEFAULT_CONSULTANT_NAME.toString())))
            .andExpect(jsonPath("$.[*].hospitalSite").value(hasItem(DEFAULT_HOSPITAL_SITE.toString())))
            .andExpect(jsonPath("$.[*].scheduledDate").value(hasItem(sameInstant(DEFAULT_SCHEDULED_DATE))))
            .andExpect(jsonPath("$.[*].performedDate").value(hasItem(sameInstant(DEFAULT_PERFORMED_DATE))))
            .andExpect(jsonPath("$.[*].primaryProcedure").value(hasItem(DEFAULT_PRIMARY_PROCEDURE.toString())))
            .andExpect(jsonPath("$.[*].otherProcedures").value(hasItem(DEFAULT_OTHER_PROCEDURES.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcedureBooking.class);
        ProcedureBooking procedureBooking1 = new ProcedureBooking();
        procedureBooking1.setId(1L);
        ProcedureBooking procedureBooking2 = new ProcedureBooking();
        procedureBooking2.setId(procedureBooking1.getId());
        assertThat(procedureBooking1).isEqualTo(procedureBooking2);
        procedureBooking2.setId(2L);
        assertThat(procedureBooking1).isNotEqualTo(procedureBooking2);
        procedureBooking1.setId(null);
        assertThat(procedureBooking1).isNotEqualTo(procedureBooking2);
    }
}
