package com.noesisinformatica.northumbriaproms.web.rest;

import com.noesisinformatica.northumbriaproms.NorthumbriapromsApp;
import com.noesisinformatica.northumbriaproms.domain.*;
import com.noesisinformatica.northumbriaproms.domain.enumeration.ActionPhase;
import com.noesisinformatica.northumbriaproms.domain.enumeration.ActionType;
import com.noesisinformatica.northumbriaproms.repository.FollowupActionRepository;
import com.noesisinformatica.northumbriaproms.repository.search.FollowupActionSearchRepository;
import com.noesisinformatica.northumbriaproms.service.FollowupActionQueryService;
import com.noesisinformatica.northumbriaproms.service.FollowupActionService;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.noesisinformatica.northumbriaproms.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * Test class for the FollowupActionResource REST controller.
 *
 * @see FollowupActionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NorthumbriapromsApp.class)
public class FollowupActionResourceIntTest {

    private static final ActionPhase DEFAULT_PHASE = ActionPhase.PRE_OPERATIVE;
    private static final ActionPhase UPDATED_PHASE = ActionPhase.POST_OPERATIVE;

    private static final LocalDate DEFAULT_SCHEDULED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SCHEDULED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ActionType DEFAULT_TYPE = ActionType.QUESTIONNAIRE;
    private static final ActionType UPDATED_TYPE = ActionType.UKNOWN;

    private static final Integer DEFAULT_OUTCOME_SCORE = 1;
    private static final Integer UPDATED_OUTCOME_SCORE = 2;

    private static final String DEFAULT_OUTCOME_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_OUTCOME_COMMENT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_COMPLETED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_COMPLETED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private FollowupActionRepository followupActionRepository;

    @Autowired
    private FollowupActionService followupActionService;

    @Autowired
    private FollowupActionSearchRepository followupActionSearchRepository;

    @Autowired
    private FollowupActionQueryService followupActionQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFollowupActionMockMvc;

    private FollowupAction followupAction;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FollowupActionResource followupActionResource = new FollowupActionResource(followupActionService, followupActionQueryService);
        this.restFollowupActionMockMvc = MockMvcBuilders.standaloneSetup(followupActionResource)
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
    public static FollowupAction createEntity(EntityManager em) {
        FollowupAction followupAction = new FollowupAction()
            .phase(DEFAULT_PHASE)
            .scheduledDate(DEFAULT_SCHEDULED_DATE)
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .outcomeScore(DEFAULT_OUTCOME_SCORE)
            .outcomeComment(DEFAULT_OUTCOME_COMMENT)
            .completedDate(DEFAULT_COMPLETED_DATE);
        // Add required entity
        CareEvent careEvent = CareEventResourceIntTest.createEntity(em);
        em.persist(careEvent);
        em.flush();
        followupAction.setCareEvent(careEvent);
        // Add required entity
        Patient patient = PatientResourceIntTest.createEntity(em);
        em.persist(patient);
        em.flush();
        followupAction.setPatient(patient);
        return followupAction;
    }

    @Before
    public void initTest() {
        followupActionSearchRepository.deleteAll();
        followupAction = createEntity(em);
    }

    @Test
    @Transactional
    public void createFollowupAction() throws Exception {
        int databaseSizeBeforeCreate = followupActionRepository.findAll().size();

        // Create the FollowupAction
        restFollowupActionMockMvc.perform(post("/api/followup-actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(followupAction)))
            .andExpect(status().isCreated());

        // Validate the FollowupAction in the database
        List<FollowupAction> followupActionList = followupActionRepository.findAll();
        assertThat(followupActionList).hasSize(databaseSizeBeforeCreate + 1);
        FollowupAction testFollowupAction = followupActionList.get(followupActionList.size() - 1);
        assertThat(testFollowupAction.getPhase()).isEqualTo(DEFAULT_PHASE);
        assertThat(testFollowupAction.getScheduledDate()).isEqualTo(DEFAULT_SCHEDULED_DATE);
        assertThat(testFollowupAction.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFollowupAction.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testFollowupAction.getOutcomeScore()).isEqualTo(DEFAULT_OUTCOME_SCORE);
        assertThat(testFollowupAction.getOutcomeComment()).isEqualTo(DEFAULT_OUTCOME_COMMENT);
        assertThat(testFollowupAction.getCompletedDate()).isEqualTo(DEFAULT_COMPLETED_DATE);

        // Validate the FollowupAction in Elasticsearch
        FollowupAction followupActionEs = followupActionSearchRepository.findOne(testFollowupAction.getId());
        assertThat(followupActionEs).isEqualToComparingFieldByField(testFollowupAction);
    }

    @Test
    @Transactional
    public void createFollowupActionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = followupActionRepository.findAll().size();

        // Create the FollowupAction with an existing ID
        followupAction.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFollowupActionMockMvc.perform(post("/api/followup-actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(followupAction)))
            .andExpect(status().isBadRequest());

        // Validate the FollowupAction in the database
        List<FollowupAction> followupActionList = followupActionRepository.findAll();
        assertThat(followupActionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = followupActionRepository.findAll().size();
        // set the field null
        followupAction.setName(null);

        // Create the FollowupAction, which fails.

        restFollowupActionMockMvc.perform(post("/api/followup-actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(followupAction)))
            .andExpect(status().isBadRequest());

        List<FollowupAction> followupActionList = followupActionRepository.findAll();
        assertThat(followupActionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFollowupActions() throws Exception {
        // Initialize the database
        followupActionRepository.saveAndFlush(followupAction);

        // Get all the followupActionList
        restFollowupActionMockMvc.perform(get("/api/followup-actions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(followupAction.getId().intValue())))
            .andExpect(jsonPath("$.[*].phase").value(hasItem(DEFAULT_PHASE.toString())))
            .andExpect(jsonPath("$.[*].scheduledDate").value(hasItem(DEFAULT_SCHEDULED_DATE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].outcomeScore").value(hasItem(DEFAULT_OUTCOME_SCORE)))
            .andExpect(jsonPath("$.[*].outcomeComment").value(hasItem(DEFAULT_OUTCOME_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].completedDate").value(hasItem(DEFAULT_COMPLETED_DATE)));
    }

    @Test
    @Transactional
    public void getFollowupAction() throws Exception {
        // Initialize the database
        followupActionRepository.saveAndFlush(followupAction);

        // Get the followupAction
        restFollowupActionMockMvc.perform(get("/api/followup-actions/{id}", followupAction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(followupAction.getId().intValue()))
            .andExpect(jsonPath("$.phase").value(DEFAULT_PHASE.toString()))
            .andExpect(jsonPath("$.scheduledDate").value(DEFAULT_SCHEDULED_DATE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.outcomeScore").value(DEFAULT_OUTCOME_SCORE))
            .andExpect(jsonPath("$.outcomeComment").value(DEFAULT_OUTCOME_COMMENT.toString()))
            .andExpect(jsonPath("$.completedDate").value(DEFAULT_COMPLETED_DATE));
    }

    @Test
    @Transactional
    public void getAllFollowupActionsByPhaseIsEqualToSomething() throws Exception {
        // Initialize the database
        followupActionRepository.saveAndFlush(followupAction);

        // Get all the followupActionList where phase equals to DEFAULT_PHASE
        defaultFollowupActionShouldBeFound("phase.equals=" + DEFAULT_PHASE);

        // Get all the followupActionList where phase equals to UPDATED_PHASE
        defaultFollowupActionShouldNotBeFound("phase.equals=" + UPDATED_PHASE);
    }

    @Test
    @Transactional
    public void getAllFollowupActionsByPhaseIsInShouldWork() throws Exception {
        // Initialize the database
        followupActionRepository.saveAndFlush(followupAction);

        // Get all the followupActionList where phase in DEFAULT_PHASE or UPDATED_PHASE
        defaultFollowupActionShouldBeFound("phase.in=" + DEFAULT_PHASE + "," + UPDATED_PHASE);

        // Get all the followupActionList where phase equals to UPDATED_PHASE
        defaultFollowupActionShouldNotBeFound("phase.in=" + UPDATED_PHASE);
    }

    @Test
    @Transactional
    public void getAllFollowupActionsByPhaseIsNullOrNotNull() throws Exception {
        // Initialize the database
        followupActionRepository.saveAndFlush(followupAction);

        // Get all the followupActionList where phase is not null
        defaultFollowupActionShouldBeFound("phase.specified=true");

        // Get all the followupActionList where phase is null
        defaultFollowupActionShouldNotBeFound("phase.specified=false");
    }

    @Test
    @Transactional
    public void getAllFollowupActionsByScheduledDateIsEqualToSomething() throws Exception {
        // Initialize the database
        followupActionRepository.saveAndFlush(followupAction);

        // Get all the followupActionList where scheduledDate equals to DEFAULT_SCHEDULED_DATE
        defaultFollowupActionShouldBeFound("scheduledDate.equals=" + DEFAULT_SCHEDULED_DATE);

        // Get all the followupActionList where scheduledDate equals to UPDATED_SCHEDULED_DATE
        defaultFollowupActionShouldNotBeFound("scheduledDate.equals=" + UPDATED_SCHEDULED_DATE);
    }

    @Test
    @Transactional
    public void getAllFollowupActionsByScheduledDateIsInShouldWork() throws Exception {
        // Initialize the database
        followupActionRepository.saveAndFlush(followupAction);

        // Get all the followupActionList where scheduledDate in DEFAULT_SCHEDULED_DATE or UPDATED_SCHEDULED_DATE
        defaultFollowupActionShouldBeFound("scheduledDate.in=" + DEFAULT_SCHEDULED_DATE + "," + UPDATED_SCHEDULED_DATE);

        // Get all the followupActionList where scheduledDate equals to UPDATED_SCHEDULED_DATE
        defaultFollowupActionShouldNotBeFound("scheduledDate.in=" + UPDATED_SCHEDULED_DATE);
    }

    @Test
    @Transactional
    public void getAllFollowupActionsByScheduledDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        followupActionRepository.saveAndFlush(followupAction);

        // Get all the followupActionList where scheduledDate is not null
        defaultFollowupActionShouldBeFound("scheduledDate.specified=true");

        // Get all the followupActionList where scheduledDate is null
        defaultFollowupActionShouldNotBeFound("scheduledDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllFollowupActionsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        followupActionRepository.saveAndFlush(followupAction);

        // Get all the followupActionList where name equals to DEFAULT_NAME
        defaultFollowupActionShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the followupActionList where name equals to UPDATED_NAME
        defaultFollowupActionShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllFollowupActionsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        followupActionRepository.saveAndFlush(followupAction);

        // Get all the followupActionList where name in DEFAULT_NAME or UPDATED_NAME
        defaultFollowupActionShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the followupActionList where name equals to UPDATED_NAME
        defaultFollowupActionShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllFollowupActionsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        followupActionRepository.saveAndFlush(followupAction);

        // Get all the followupActionList where name is not null
        defaultFollowupActionShouldBeFound("name.specified=true");

        // Get all the followupActionList where name is null
        defaultFollowupActionShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllFollowupActionsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        followupActionRepository.saveAndFlush(followupAction);

        // Get all the followupActionList where type equals to DEFAULT_TYPE
        defaultFollowupActionShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the followupActionList where type equals to UPDATED_TYPE
        defaultFollowupActionShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllFollowupActionsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        followupActionRepository.saveAndFlush(followupAction);

        // Get all the followupActionList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultFollowupActionShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the followupActionList where type equals to UPDATED_TYPE
        defaultFollowupActionShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllFollowupActionsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        followupActionRepository.saveAndFlush(followupAction);

        // Get all the followupActionList where type is not null
        defaultFollowupActionShouldBeFound("type.specified=true");

        // Get all the followupActionList where type is null
        defaultFollowupActionShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllFollowupActionsByOutcomeScoreIsEqualToSomething() throws Exception {
        // Initialize the database
        followupActionRepository.saveAndFlush(followupAction);

        // Get all the followupActionList where outcomeScore equals to DEFAULT_OUTCOME_SCORE
        defaultFollowupActionShouldBeFound("outcomeScore.equals=" + DEFAULT_OUTCOME_SCORE);

        // Get all the followupActionList where outcomeScore equals to UPDATED_OUTCOME_SCORE
        defaultFollowupActionShouldNotBeFound("outcomeScore.equals=" + UPDATED_OUTCOME_SCORE);
    }

    @Test
    @Transactional
    public void getAllFollowupActionsByOutcomeScoreIsInShouldWork() throws Exception {
        // Initialize the database
        followupActionRepository.saveAndFlush(followupAction);

        // Get all the followupActionList where outcomeScore in DEFAULT_OUTCOME_SCORE or UPDATED_OUTCOME_SCORE
        defaultFollowupActionShouldBeFound("outcomeScore.in=" + DEFAULT_OUTCOME_SCORE + "," + UPDATED_OUTCOME_SCORE);

        // Get all the followupActionList where outcomeScore equals to UPDATED_OUTCOME_SCORE
        defaultFollowupActionShouldNotBeFound("outcomeScore.in=" + UPDATED_OUTCOME_SCORE);
    }

    @Test
    @Transactional
    public void getAllFollowupActionsByOutcomeScoreIsNullOrNotNull() throws Exception {
        // Initialize the database
        followupActionRepository.saveAndFlush(followupAction);

        // Get all the followupActionList where outcomeScore is not null
        defaultFollowupActionShouldBeFound("outcomeScore.specified=true");

        // Get all the followupActionList where outcomeScore is null
        defaultFollowupActionShouldNotBeFound("outcomeScore.specified=false");
    }

    @Test
    @Transactional
    public void getAllFollowupActionsByOutcomeScoreIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        followupActionRepository.saveAndFlush(followupAction);

        // Get all the followupActionList where outcomeScore greater than or equals to DEFAULT_OUTCOME_SCORE
        defaultFollowupActionShouldBeFound("outcomeScore.greaterOrEqualThan=" + DEFAULT_OUTCOME_SCORE);

        // Get all the followupActionList where outcomeScore greater than or equals to UPDATED_OUTCOME_SCORE
        defaultFollowupActionShouldNotBeFound("outcomeScore.greaterOrEqualThan=" + UPDATED_OUTCOME_SCORE);
    }

    @Test
    @Transactional
    public void getAllFollowupActionsByOutcomeScoreIsLessThanSomething() throws Exception {
        // Initialize the database
        followupActionRepository.saveAndFlush(followupAction);

        // Get all the followupActionList where outcomeScore less than or equals to DEFAULT_OUTCOME_SCORE
        defaultFollowupActionShouldNotBeFound("outcomeScore.lessThan=" + DEFAULT_OUTCOME_SCORE);

        // Get all the followupActionList where outcomeScore less than or equals to UPDATED_OUTCOME_SCORE
        defaultFollowupActionShouldBeFound("outcomeScore.lessThan=" + UPDATED_OUTCOME_SCORE);
    }


    @Test
    @Transactional
    public void getAllFollowupActionsByOutcomeCommentIsEqualToSomething() throws Exception {
        // Initialize the database
        followupActionRepository.saveAndFlush(followupAction);

        // Get all the followupActionList where outcomeComment equals to DEFAULT_OUTCOME_COMMENT
        defaultFollowupActionShouldBeFound("outcomeComment.equals=" + DEFAULT_OUTCOME_COMMENT);

        // Get all the followupActionList where outcomeComment equals to UPDATED_OUTCOME_COMMENT
        defaultFollowupActionShouldNotBeFound("outcomeComment.equals=" + UPDATED_OUTCOME_COMMENT);
    }

    @Test
    @Transactional
    public void getAllFollowupActionsByOutcomeCommentIsInShouldWork() throws Exception {
        // Initialize the database
        followupActionRepository.saveAndFlush(followupAction);

        // Get all the followupActionList where outcomeComment in DEFAULT_OUTCOME_COMMENT or UPDATED_OUTCOME_COMMENT
        defaultFollowupActionShouldBeFound("outcomeComment.in=" + DEFAULT_OUTCOME_COMMENT + "," + UPDATED_OUTCOME_COMMENT);

        // Get all the followupActionList where outcomeComment equals to UPDATED_OUTCOME_COMMENT
        defaultFollowupActionShouldNotBeFound("outcomeComment.in=" + UPDATED_OUTCOME_COMMENT);
    }

    @Test
    @Transactional
    public void getAllFollowupActionsByOutcomeCommentIsNullOrNotNull() throws Exception {
        // Initialize the database
        followupActionRepository.saveAndFlush(followupAction);

        // Get all the followupActionList where outcomeComment is not null
        defaultFollowupActionShouldBeFound("outcomeComment.specified=true");

        // Get all the followupActionList where outcomeComment is null
        defaultFollowupActionShouldNotBeFound("outcomeComment.specified=false");
    }

    @Test
    @Transactional
    public void getAllFollowupActionsByCompletedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        followupActionRepository.saveAndFlush(followupAction);

        // Get all the followupActionList where completedDate equals to DEFAULT_COMPLETED_DATE
        defaultFollowupActionShouldBeFound("completedDate.equals=" + DEFAULT_COMPLETED_DATE);

        // Get all the followupActionList where completedDate equals to UPDATED_COMPLETED_DATE
        defaultFollowupActionShouldNotBeFound("completedDate.equals=" + UPDATED_COMPLETED_DATE);
    }

    @Test
    @Transactional
    public void getAllFollowupActionsByCompletedDateIsInShouldWork() throws Exception {
        // Initialize the database
        followupActionRepository.saveAndFlush(followupAction);

        // Get all the followupActionList where completedDate in DEFAULT_COMPLETED_DATE or UPDATED_COMPLETED_DATE
        defaultFollowupActionShouldBeFound("completedDate.in=" + DEFAULT_COMPLETED_DATE + "," + UPDATED_COMPLETED_DATE);

        // Get all the followupActionList where completedDate equals to UPDATED_COMPLETED_DATE
        defaultFollowupActionShouldNotBeFound("completedDate.in=" + UPDATED_COMPLETED_DATE);
    }

    @Test
    @Transactional
    public void getAllFollowupActionsByCompletedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        followupActionRepository.saveAndFlush(followupAction);

        // Get all the followupActionList where completedDate is not null
        defaultFollowupActionShouldBeFound("completedDate.specified=true");

        // Get all the followupActionList where completedDate is null
        defaultFollowupActionShouldNotBeFound("completedDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllFollowupActionsByCompletedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        followupActionRepository.saveAndFlush(followupAction);

        // Get all the followupActionList where completedDate greater than or equals to DEFAULT_COMPLETED_DATE
        defaultFollowupActionShouldBeFound("completedDate.greaterOrEqualThan=" + DEFAULT_COMPLETED_DATE);

        // Get all the followupActionList where completedDate greater than or equals to UPDATED_COMPLETED_DATE
        defaultFollowupActionShouldNotBeFound("completedDate.greaterOrEqualThan=" + UPDATED_COMPLETED_DATE);
    }

    @Test
    @Transactional
    public void getAllFollowupActionsByCompletedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        followupActionRepository.saveAndFlush(followupAction);

        // Get all the followupActionList where completedDate less than or equals to DEFAULT_COMPLETED_DATE
        defaultFollowupActionShouldNotBeFound("completedDate.lessThan=" + DEFAULT_COMPLETED_DATE);

        // Get all the followupActionList where completedDate less than or equals to UPDATED_COMPLETED_DATE
        defaultFollowupActionShouldBeFound("completedDate.lessThan=" + UPDATED_COMPLETED_DATE);
    }


    @Test
    @Transactional
    public void getAllFollowupActionsByFollowupPlanIsEqualToSomething() throws Exception {
        // Initialize the database
        CareEvent careEvent = CareEventResourceIntTest.createEntity(em);
        em.persist(careEvent);
        em.flush();
        followupAction.setCareEvent(careEvent);
        followupActionRepository.saveAndFlush(followupAction);
        Long careEventId = careEvent.getId();

        // Get all the followupActionList where careEvent equals to careEventId
        defaultFollowupActionShouldBeFound("careEventId.equals=" + careEventId);

        // Get all the followupActionList where careEvent equals to careEventId + 1
        defaultFollowupActionShouldNotBeFound("careEventId.equals=" + (careEventId + 1));
    }


    @Test
    @Transactional
    public void getAllFollowupActionsByPatientIsEqualToSomething() throws Exception {
        // Initialize the database
        Patient patient = PatientResourceIntTest.createEntity(em);
        em.persist(patient);
        em.flush();
        followupAction.setPatient(patient);
        followupActionRepository.saveAndFlush(followupAction);
        Long patientId = patient.getId();

        // Get all the followupActionList where patient equals to patientId
        defaultFollowupActionShouldBeFound("patientId.equals=" + patientId);

        // Get all the followupActionList where patient equals to patientId + 1
        defaultFollowupActionShouldNotBeFound("patientId.equals=" + (patientId + 1));
    }


    @Test
    @Transactional
    public void getAllFollowupActionsByQuestionnaireIsEqualToSomething() throws Exception {
        // Initialize the database
        Questionnaire questionnaire = QuestionnaireResourceIntTest.createEntity(em);
        em.persist(questionnaire);
        em.flush();
        followupAction.setQuestionnaire(questionnaire);
        followupActionRepository.saveAndFlush(followupAction);
        Long questionnaireId = questionnaire.getId();

        // Get all the followupActionList where questionnaire equals to questionnaireId
        defaultFollowupActionShouldBeFound("questionnaireId.equals=" + questionnaireId);

        // Get all the followupActionList where questionnaire equals to questionnaireId + 1
        defaultFollowupActionShouldNotBeFound("questionnaireId.equals=" + (questionnaireId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultFollowupActionShouldBeFound(String filter) throws Exception {
        restFollowupActionMockMvc.perform(get("/api/followup-actions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(followupAction.getId().intValue())))
            .andExpect(jsonPath("$.[*].phase").value(hasItem(DEFAULT_PHASE.toString())))
            .andExpect(jsonPath("$.[*].scheduledDate").value(hasItem(DEFAULT_SCHEDULED_DATE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].outcomeScore").value(hasItem(DEFAULT_OUTCOME_SCORE)))
            .andExpect(jsonPath("$.[*].outcomeComment").value(hasItem(DEFAULT_OUTCOME_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].completedDate").value(hasItem(DEFAULT_COMPLETED_DATE)));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultFollowupActionShouldNotBeFound(String filter) throws Exception {
        restFollowupActionMockMvc.perform(get("/api/followup-actions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingFollowupAction() throws Exception {
        // Get the followupAction
        restFollowupActionMockMvc.perform(get("/api/followup-actions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFollowupAction() throws Exception {
        // Initialize the database
        followupActionService.save(followupAction);

        int databaseSizeBeforeUpdate = followupActionRepository.findAll().size();

        // Update the followupAction
        FollowupAction updatedFollowupAction = followupActionRepository.findOne(followupAction.getId());
        // Disconnect from session so that the updates on updatedFollowupAction are not directly saved in db
        em.detach(updatedFollowupAction);
        updatedFollowupAction
            .phase(UPDATED_PHASE)
            .scheduledDate(UPDATED_SCHEDULED_DATE)
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .outcomeScore(UPDATED_OUTCOME_SCORE)
            .outcomeComment(UPDATED_OUTCOME_COMMENT)
            .completedDate(UPDATED_COMPLETED_DATE);

        restFollowupActionMockMvc.perform(put("/api/followup-actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFollowupAction)))
            .andExpect(status().isOk());

        // Validate the FollowupAction in the database
        List<FollowupAction> followupActionList = followupActionRepository.findAll();
        assertThat(followupActionList).hasSize(databaseSizeBeforeUpdate);
        FollowupAction testFollowupAction = followupActionList.get(followupActionList.size() - 1);
        assertThat(testFollowupAction.getPhase()).isEqualTo(UPDATED_PHASE);
        assertThat(testFollowupAction.getScheduledDate()).isEqualTo(UPDATED_SCHEDULED_DATE);
        assertThat(testFollowupAction.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFollowupAction.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testFollowupAction.getOutcomeScore()).isEqualTo(UPDATED_OUTCOME_SCORE);
        assertThat(testFollowupAction.getOutcomeComment()).isEqualTo(UPDATED_OUTCOME_COMMENT);
        assertThat(testFollowupAction.getCompletedDate()).isEqualTo(UPDATED_COMPLETED_DATE);

        // Validate the FollowupAction in Elasticsearch
        FollowupAction followupActionEs = followupActionSearchRepository.findOne(testFollowupAction.getId());
        assertThat(followupActionEs).isEqualToComparingFieldByField(testFollowupAction);
    }

    @Test
    @Transactional
    public void updateNonExistingFollowupAction() throws Exception {
        int databaseSizeBeforeUpdate = followupActionRepository.findAll().size();

        // Create the FollowupAction

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFollowupActionMockMvc.perform(put("/api/followup-actions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(followupAction)))
            .andExpect(status().isCreated());

        // Validate the FollowupAction in the database
        List<FollowupAction> followupActionList = followupActionRepository.findAll();
        assertThat(followupActionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFollowupAction() throws Exception {
        // Initialize the database
        followupActionService.save(followupAction);

        int databaseSizeBeforeDelete = followupActionRepository.findAll().size();

        // Get the followupAction
        restFollowupActionMockMvc.perform(delete("/api/followup-actions/{id}", followupAction.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean followupActionExistsInEs = followupActionSearchRepository.exists(followupAction.getId());
        assertThat(followupActionExistsInEs).isFalse();

        // Validate the database is empty
        List<FollowupAction> followupActionList = followupActionRepository.findAll();
        assertThat(followupActionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFollowupAction() throws Exception {
        // Initialize the database
        followupActionService.save(followupAction);

        // Search the followupAction
        restFollowupActionMockMvc.perform(get("/api/_search/followup-actions?query=id:" + followupAction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(followupAction.getId().intValue())))
            .andExpect(jsonPath("$.[*].phase").value(hasItem(DEFAULT_PHASE.toString())))
            .andExpect(jsonPath("$.[*].scheduledDate").value(hasItem(DEFAULT_SCHEDULED_DATE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].outcomeScore").value(hasItem(DEFAULT_OUTCOME_SCORE)))
            .andExpect(jsonPath("$.[*].outcomeComment").value(hasItem(DEFAULT_OUTCOME_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].completedDate").value(hasItem(DEFAULT_COMPLETED_DATE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FollowupAction.class);
        FollowupAction followupAction1 = new FollowupAction();
        followupAction1.setId(1L);
        FollowupAction followupAction2 = new FollowupAction();
        followupAction2.setId(followupAction1.getId());
        assertThat(followupAction1).isEqualTo(followupAction2);
        followupAction2.setId(2L);
        assertThat(followupAction1).isNotEqualTo(followupAction2);
        followupAction1.setId(null);
        assertThat(followupAction1).isNotEqualTo(followupAction2);
    }
}
