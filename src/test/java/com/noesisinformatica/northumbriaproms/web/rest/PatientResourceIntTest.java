package com.noesisinformatica.northumbriaproms.web.rest;

import com.noesisinformatica.northumbriaproms.NorthumbriapromsApp;
import com.noesisinformatica.northumbriaproms.domain.Address;
import com.noesisinformatica.northumbriaproms.domain.Patient;
import com.noesisinformatica.northumbriaproms.domain.ProcedureBooking;
import com.noesisinformatica.northumbriaproms.domain.enumeration.GenderType;
import com.noesisinformatica.northumbriaproms.repository.PatientRepository;
import com.noesisinformatica.northumbriaproms.repository.search.PatientSearchRepository;
import com.noesisinformatica.northumbriaproms.service.PatientQueryService;
import com.noesisinformatica.northumbriaproms.service.PatientService;
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
 * Test class for the PatientResource REST controller.
 *
 * @see PatientResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NorthumbriapromsApp.class)
public class PatientResourceIntTest {

    private static final String DEFAULT_FAMILY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FAMILY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GIVEN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GIVEN_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_BIRTH_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_BIRTH_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final GenderType DEFAULT_GENDER = GenderType.MALE;
    private static final GenderType UPDATED_GENDER = GenderType.FEMALE;

    private static final Long DEFAULT_NHS_NUMBER = 1L;
    private static final Long UPDATED_NHS_NUMBER = 2L;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientSearchRepository patientSearchRepository;

    @Autowired
    private PatientQueryService patientQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPatientMockMvc;

    private Patient patient;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PatientResource patientResource = new PatientResource(patientService, patientQueryService);
        this.restPatientMockMvc = MockMvcBuilders.standaloneSetup(patientResource)
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
    public static Patient createEntity(EntityManager em) {
        Patient patient = new Patient()
            .familyName(DEFAULT_FAMILY_NAME)
            .givenName(DEFAULT_GIVEN_NAME)
            .birthDate(DEFAULT_BIRTH_DATE)
            .gender(DEFAULT_GENDER)
            .nhsNumber(DEFAULT_NHS_NUMBER)
            .email(DEFAULT_EMAIL);
        return patient;
    }

    @Before
    public void initTest() {
        patientSearchRepository.deleteAll();
        patient = createEntity(em);
    }

    @Test
    @Transactional
    public void createPatient() throws Exception {
        int databaseSizeBeforeCreate = patientRepository.findAll().size();

        // Create the Patient
        restPatientMockMvc.perform(post("/api/patients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isCreated());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeCreate + 1);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getFamilyName()).isEqualTo(DEFAULT_FAMILY_NAME);
        assertThat(testPatient.getGivenName()).isEqualTo(DEFAULT_GIVEN_NAME);
        assertThat(testPatient.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testPatient.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testPatient.getNhsNumber()).isEqualTo(DEFAULT_NHS_NUMBER);
        assertThat(testPatient.getEmail()).isEqualTo(DEFAULT_EMAIL);

        // Validate the Patient in Elasticsearch
        Patient patientEs = patientSearchRepository.findOne(testPatient.getId());
        assertThat(patientEs).isEqualToComparingFieldByField(testPatient);
    }

    @Test
    @Transactional
    public void createPatientWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = patientRepository.findAll().size();

        // Create the Patient with an existing ID
        patient.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPatientMockMvc.perform(post("/api/patients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFamilyNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setFamilyName(null);

        // Create the Patient, which fails.

        restPatientMockMvc.perform(post("/api/patients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGivenNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setGivenName(null);

        // Create the Patient, which fails.

        restPatientMockMvc.perform(post("/api/patients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBirthDatePassedAsString() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setBirthDate("01/13/2017");

        // Create the Patient

        restPatientMockMvc.perform(post("/api/patients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isOk());

        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeTest + 1);
    }

    @Test
    @Transactional
    public void getAllPatients() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList
        restPatientMockMvc.perform(get("/api/patients?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patient.getId().intValue())))
            .andExpect(jsonPath("$.[*].familyName").value(hasItem(DEFAULT_FAMILY_NAME.toString())))
            .andExpect(jsonPath("$.[*].givenName").value(hasItem(DEFAULT_GIVEN_NAME.toString())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(sameInstant(DEFAULT_BIRTH_DATE))))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].nhsNumber").value(hasItem(DEFAULT_NHS_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }

    @Test
    @Transactional
    public void getPatient() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get the patient
        restPatientMockMvc.perform(get("/api/patients/{id}", patient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(patient.getId().intValue()))
            .andExpect(jsonPath("$.familyName").value(DEFAULT_FAMILY_NAME.toString()))
            .andExpect(jsonPath("$.givenName").value(DEFAULT_GIVEN_NAME.toString()))
            .andExpect(jsonPath("$.birthDate").value(sameInstant(DEFAULT_BIRTH_DATE)))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.nhsNumber").value(DEFAULT_NHS_NUMBER.intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()));
    }

    @Test
    @Transactional
    public void getAllPatientsByFamilyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where familyName equals to DEFAULT_FAMILY_NAME
        defaultPatientShouldBeFound("familyName.equals=" + DEFAULT_FAMILY_NAME);

        // Get all the patientList where familyName equals to UPDATED_FAMILY_NAME
        defaultPatientShouldNotBeFound("familyName.equals=" + UPDATED_FAMILY_NAME);
    }

    @Test
    @Transactional
    public void getAllPatientsByFamilyNameIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where familyName in DEFAULT_FAMILY_NAME or UPDATED_FAMILY_NAME
        defaultPatientShouldBeFound("familyName.in=" + DEFAULT_FAMILY_NAME + "," + UPDATED_FAMILY_NAME);

        // Get all the patientList where familyName equals to UPDATED_FAMILY_NAME
        defaultPatientShouldNotBeFound("familyName.in=" + UPDATED_FAMILY_NAME);
    }

    @Test
    @Transactional
    public void getAllPatientsByFamilyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where familyName is not null
        defaultPatientShouldBeFound("familyName.specified=true");

        // Get all the patientList where familyName is null
        defaultPatientShouldNotBeFound("familyName.specified=false");
    }

    @Test
    @Transactional
    public void getAllPatientsByGivenNameIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where givenName equals to DEFAULT_GIVEN_NAME
        defaultPatientShouldBeFound("givenName.equals=" + DEFAULT_GIVEN_NAME);

        // Get all the patientList where givenName equals to UPDATED_GIVEN_NAME
        defaultPatientShouldNotBeFound("givenName.equals=" + UPDATED_GIVEN_NAME);
    }

    @Test
    @Transactional
    public void getAllPatientsByGivenNameIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where givenName in DEFAULT_GIVEN_NAME or UPDATED_GIVEN_NAME
        defaultPatientShouldBeFound("givenName.in=" + DEFAULT_GIVEN_NAME + "," + UPDATED_GIVEN_NAME);

        // Get all the patientList where givenName equals to UPDATED_GIVEN_NAME
        defaultPatientShouldNotBeFound("givenName.in=" + UPDATED_GIVEN_NAME);
    }

    @Test
    @Transactional
    public void getAllPatientsByGivenNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where givenName is not null
        defaultPatientShouldBeFound("givenName.specified=true");

        // Get all the patientList where givenName is null
        defaultPatientShouldNotBeFound("givenName.specified=false");
    }

    @Test
    @Transactional
    public void getAllPatientsByBirthDateIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where birthDate equals to DEFAULT_BIRTH_DATE
        defaultPatientShouldBeFound("birthDate.equals=" + DEFAULT_BIRTH_DATE);

        // Get all the patientList where birthDate equals to UPDATED_BIRTH_DATE
        defaultPatientShouldNotBeFound("birthDate.equals=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllPatientsByBirthDateIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where birthDate in DEFAULT_BIRTH_DATE or UPDATED_BIRTH_DATE
        defaultPatientShouldBeFound("birthDate.in=" + DEFAULT_BIRTH_DATE + "," + UPDATED_BIRTH_DATE);

        // Get all the patientList where birthDate equals to UPDATED_BIRTH_DATE
        defaultPatientShouldNotBeFound("birthDate.in=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllPatientsByBirthDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where birthDate is not null
        defaultPatientShouldBeFound("birthDate.specified=true");

        // Get all the patientList where birthDate is null
        defaultPatientShouldNotBeFound("birthDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPatientsByBirthDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where birthDate greater than or equals to DEFAULT_BIRTH_DATE
        defaultPatientShouldBeFound("birthDate.greaterOrEqualThan=" + DEFAULT_BIRTH_DATE);

        // Get all the patientList where birthDate greater than or equals to UPDATED_BIRTH_DATE
        defaultPatientShouldNotBeFound("birthDate.greaterOrEqualThan=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    public void getAllPatientsByBirthDateIsLessThanSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where birthDate less than or equals to DEFAULT_BIRTH_DATE
        defaultPatientShouldNotBeFound("birthDate.lessThan=" + DEFAULT_BIRTH_DATE);

        // Get all the patientList where birthDate less than or equals to UPDATED_BIRTH_DATE
        defaultPatientShouldBeFound("birthDate.lessThan=" + UPDATED_BIRTH_DATE);
    }


    @Test
    @Transactional
    public void getAllPatientsByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where gender equals to DEFAULT_GENDER
        defaultPatientShouldBeFound("gender.equals=" + DEFAULT_GENDER);

        // Get all the patientList where gender equals to UPDATED_GENDER
        defaultPatientShouldNotBeFound("gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllPatientsByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where gender in DEFAULT_GENDER or UPDATED_GENDER
        defaultPatientShouldBeFound("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER);

        // Get all the patientList where gender equals to UPDATED_GENDER
        defaultPatientShouldNotBeFound("gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllPatientsByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where gender is not null
        defaultPatientShouldBeFound("gender.specified=true");

        // Get all the patientList where gender is null
        defaultPatientShouldNotBeFound("gender.specified=false");
    }

    @Test
    @Transactional
    public void getAllPatientsByNhsNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where nhsNumber equals to DEFAULT_NHS_NUMBER
        defaultPatientShouldBeFound("nhsNumber.equals=" + DEFAULT_NHS_NUMBER);

        // Get all the patientList where nhsNumber equals to UPDATED_NHS_NUMBER
        defaultPatientShouldNotBeFound("nhsNumber.equals=" + UPDATED_NHS_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPatientsByNhsNumberIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where nhsNumber in DEFAULT_NHS_NUMBER or UPDATED_NHS_NUMBER
        defaultPatientShouldBeFound("nhsNumber.in=" + DEFAULT_NHS_NUMBER + "," + UPDATED_NHS_NUMBER);

        // Get all the patientList where nhsNumber equals to UPDATED_NHS_NUMBER
        defaultPatientShouldNotBeFound("nhsNumber.in=" + UPDATED_NHS_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPatientsByNhsNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where nhsNumber is not null
        defaultPatientShouldBeFound("nhsNumber.specified=true");

        // Get all the patientList where nhsNumber is null
        defaultPatientShouldNotBeFound("nhsNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllPatientsByNhsNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where nhsNumber greater than or equals to DEFAULT_NHS_NUMBER
        defaultPatientShouldBeFound("nhsNumber.greaterOrEqualThan=" + DEFAULT_NHS_NUMBER);

        // Get all the patientList where nhsNumber greater than or equals to UPDATED_NHS_NUMBER
        defaultPatientShouldNotBeFound("nhsNumber.greaterOrEqualThan=" + UPDATED_NHS_NUMBER);
    }

    @Test
    @Transactional
    public void getAllPatientsByNhsNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where nhsNumber less than or equals to DEFAULT_NHS_NUMBER
        defaultPatientShouldNotBeFound("nhsNumber.lessThan=" + DEFAULT_NHS_NUMBER);

        // Get all the patientList where nhsNumber less than or equals to UPDATED_NHS_NUMBER
        defaultPatientShouldBeFound("nhsNumber.lessThan=" + UPDATED_NHS_NUMBER);
    }


    @Test
    @Transactional
    public void getAllPatientsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where email equals to DEFAULT_EMAIL
        defaultPatientShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the patientList where email equals to UPDATED_EMAIL
        defaultPatientShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllPatientsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultPatientShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the patientList where email equals to UPDATED_EMAIL
        defaultPatientShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllPatientsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where email is not null
        defaultPatientShouldBeFound("email.specified=true");

        // Get all the patientList where email is null
        defaultPatientShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    public void getAllPatientsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        Address address = AddressResourceIntTest.createEntity(em);
        em.persist(address);
        em.flush();
        patient.addAddress(address);
        patientRepository.saveAndFlush(patient);
        Long addressId = address.getId();

        // Get all the patientList where address equals to addressId
        defaultPatientShouldBeFound("addressId.equals=" + addressId);

        // Get all the patientList where address equals to addressId + 1
        defaultPatientShouldNotBeFound("addressId.equals=" + (addressId + 1));
    }


    @Test
    @Transactional
    public void getAllPatientsByProcedureBookingsIsEqualToSomething() throws Exception {
        // Initialize the database
        ProcedureBooking procedureBookings = ProcedureBookingResourceIntTest.createEntity(em);
        em.persist(procedureBookings);
        em.flush();
        patient.addProcedureBookings(procedureBookings);
        patientRepository.saveAndFlush(patient);
        Long procedureBookingsId = procedureBookings.getId();

        // Get all the patientList where procedureBookings equals to procedureBookingsId
        defaultPatientShouldBeFound("procedureBookingsId.equals=" + procedureBookingsId);

        // Get all the patientList where procedureBookings equals to procedureBookingsId + 1
        defaultPatientShouldNotBeFound("procedureBookingsId.equals=" + (procedureBookingsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultPatientShouldBeFound(String filter) throws Exception {
        restPatientMockMvc.perform(get("/api/patients?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patient.getId().intValue())))
            .andExpect(jsonPath("$.[*].familyName").value(hasItem(DEFAULT_FAMILY_NAME.toString())))
            .andExpect(jsonPath("$.[*].givenName").value(hasItem(DEFAULT_GIVEN_NAME.toString())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(sameInstant(DEFAULT_BIRTH_DATE))))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].nhsNumber").value(hasItem(DEFAULT_NHS_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultPatientShouldNotBeFound(String filter) throws Exception {
        restPatientMockMvc.perform(get("/api/patients?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingPatient() throws Exception {
        // Get the patient
        restPatientMockMvc.perform(get("/api/patients/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePatient() throws Exception {
        // Initialize the database
        patientService.save(patient);

        int databaseSizeBeforeUpdate = patientRepository.findAll().size();

        // Update the patient
        Patient updatedPatient = patientRepository.findOne(patient.getId());
        // Disconnect from session so that the updates on updatedPatient are not directly saved in db
        em.detach(updatedPatient);
        updatedPatient
            .familyName(UPDATED_FAMILY_NAME)
            .givenName(UPDATED_GIVEN_NAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .gender(UPDATED_GENDER)
            .nhsNumber(UPDATED_NHS_NUMBER)
            .email(UPDATED_EMAIL);

        restPatientMockMvc.perform(put("/api/patients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPatient)))
            .andExpect(status().isOk());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getFamilyName()).isEqualTo(UPDATED_FAMILY_NAME);
        assertThat(testPatient.getGivenName()).isEqualTo(UPDATED_GIVEN_NAME);
        assertThat(testPatient.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testPatient.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testPatient.getNhsNumber()).isEqualTo(UPDATED_NHS_NUMBER);
        assertThat(testPatient.getEmail()).isEqualTo(UPDATED_EMAIL);

        // Validate the Patient in Elasticsearch
        Patient patientEs = patientSearchRepository.findOne(testPatient.getId());
        assertThat(patientEs).isEqualToComparingFieldByField(testPatient);
    }

    @Test
    @Transactional
    public void updateNonExistingPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();

        // Create the Patient

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPatientMockMvc.perform(put("/api/patients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isCreated());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePatient() throws Exception {
        // Initialize the database
        patientService.save(patient);

        int databaseSizeBeforeDelete = patientRepository.findAll().size();

        // Get the patient
        restPatientMockMvc.perform(delete("/api/patients/{id}", patient.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean patientExistsInEs = patientSearchRepository.exists(patient.getId());
        assertThat(patientExistsInEs).isFalse();

        // Validate the database is empty
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPatient() throws Exception {
        // Initialize the database
        patientService.save(patient);

        // Search the patient
        restPatientMockMvc.perform(get("/api/_search/patients?query=id:" + patient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patient.getId().intValue())))
            .andExpect(jsonPath("$.[*].familyName").value(hasItem(DEFAULT_FAMILY_NAME.toString())))
            .andExpect(jsonPath("$.[*].givenName").value(hasItem(DEFAULT_GIVEN_NAME.toString())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(sameInstant(DEFAULT_BIRTH_DATE))))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].nhsNumber").value(hasItem(DEFAULT_NHS_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Patient.class);
        Patient patient1 = new Patient();
        patient1.setId(1L);
        Patient patient2 = new Patient();
        patient2.setId(patient1.getId());
        assertThat(patient1).isEqualTo(patient2);
        patient2.setId(2L);
        assertThat(patient1).isNotEqualTo(patient2);
        patient1.setId(null);
        assertThat(patient1).isNotEqualTo(patient2);
    }
}
