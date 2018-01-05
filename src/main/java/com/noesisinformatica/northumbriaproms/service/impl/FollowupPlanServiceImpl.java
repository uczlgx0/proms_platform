package com.noesisinformatica.northumbriaproms.service.impl;

import com.noesisinformatica.northumbriaproms.config.Constants;
import com.noesisinformatica.northumbriaproms.domain.*;
import com.noesisinformatica.northumbriaproms.domain.enumeration.ActionPhase;
import com.noesisinformatica.northumbriaproms.domain.enumeration.ActionStatus;
import com.noesisinformatica.northumbriaproms.domain.enumeration.ActionType;
import com.noesisinformatica.northumbriaproms.repository.FollowupPlanRepository;
import com.noesisinformatica.northumbriaproms.repository.ProcedureBookingRepository;
import com.noesisinformatica.northumbriaproms.repository.search.FollowupPlanSearchRepository;
import com.noesisinformatica.northumbriaproms.repository.search.ProcedureBookingSearchRepository;
import com.noesisinformatica.northumbriaproms.service.FollowupPlanService;
import com.noesisinformatica.northumbriaproms.service.ProcedurelinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing FollowupPlan.
 */
@Service
@Transactional
//@EnableBinding(FollowupPlanService.class)
//@RabbitListener(queues = Constants.BOOKINGS_QUEUE)
//@RabbitListener(bindings = @QueueBinding(value = @Queue(value = Constants.BOOKINGS_QUEUE, durable = "true") , exchange = @Exchange(value = "exch", autoDelete = "true") , key = "key") )
public class FollowupPlanServiceImpl implements FollowupPlanService {

    private final Logger log = LoggerFactory.getLogger(FollowupPlanServiceImpl.class);
    private final RabbitTemplate rabbitTemplate;
    private final FollowupPlanRepository followupPlanRepository;
    private final ProcedureBookingRepository procedureBookingRepository;
    private final ProcedureBookingSearchRepository procedureBookingSearchRepository;
    private final FollowupPlanSearchRepository followupPlanSearchRepository;
    private final ProcedurelinkService procedurelinkService;

    public FollowupPlanServiceImpl(FollowupPlanRepository followupPlanRepository,
                                   ProcedureBookingRepository procedureBookingRepository,
                                   ProcedureBookingSearchRepository procedureBookingSearchRepository,
                                   FollowupPlanSearchRepository followupPlanSearchRepository,
                                   ProcedurelinkService procedurelinkService,
                                   RabbitTemplate rabbitTemplate) {
        this.followupPlanRepository = followupPlanRepository;
        this.procedureBookingRepository = procedureBookingRepository;
        this.procedureBookingSearchRepository = procedureBookingSearchRepository;
        this.followupPlanSearchRepository = followupPlanSearchRepository;
        this.procedurelinkService = procedurelinkService;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Utility method for generating {@link FollowupPlan} from a {@link ProcedureBooking}
     *
     * @param booking the ProcedureBooking to process
     */
    @Override
//    @RabbitHandler
    @RabbitListener(queues = Constants.BOOKINGS_QUEUE)
//    @SendTo(value = Constants.ACTIONS_QUEUE)
    public void processBooking(ProcedureBooking booking) {
        log.debug("Request to process ProcedureBooking : {}", booking);
        // see if booking has already been processed - we know this by seeing if follow-up plan is set
        Optional<FollowupPlan> existing = findOneByProcedureBookingId(booking.getId());
        FollowupPlan plan = new FollowupPlan();
        if(existing.isPresent()) {
            plan = existing.get();
            // see if procedures have changed
            log.info("Found existing plan. Assuming procedure booking has already been processed.");
            // if procedure booking scheduled date is present and action is not completed/pending, then we can update date
            if(booking.getScheduledDate() != null) {
                plan.getFollowupActions().forEach(action -> {
                    if (ActionStatus.COMPLETED != action.getStatus() && ActionStatus.PENDING != action.getStatus()) {
                        action.setScheduledDate(booking.getScheduledDate());
                    }
                });
            }
        } else {
            log.info("Found no existing plan. Assuming new procedure booking and creating plan and actions.");
            // get procedure code from booking and add follow up activities to a new follow up plan
            Patient patient = booking.getPatient();
            List<Questionnaire> questionnaires = procedurelinkService.findAllQuestionnairesByProcedureLocalCode(Integer.valueOf(booking.getPrimaryProcedure()));
            plan.setProcedureBooking(booking);
            plan.setPatient(patient);
            for(Questionnaire questionnaire : questionnaires) {
                // create a new follow up action
                FollowupAction action = new FollowupAction();
                action.name(questionnaire.getName())
                    .type(ActionType.QUESTIONNAIRE).questionnaire(questionnaire).phase(ActionPhase.PRE_OPERATIVE)
                    .patient(patient).status(ActionStatus.UNINITIALISED);
                // if procedure booking scheduled date is present, then we can assign tentative date
                if (booking.getScheduledDate() != null && booking.getScheduledDate().isBefore(LocalDate.now())) {
                    action.status(ActionStatus.STARTED).setScheduledDate(booking.getScheduledDate());
                }
                // if procedure performed date is not present, we have change action status to PENDING
                if (booking.getPerformedDate() == null) {
                    action.status(ActionStatus.PENDING);
                }

                if ("OUTCOME".equalsIgnoreCase(questionnaire.getName())) {
                    action.setPhase(ActionPhase.POST_OPERATIVE);
                }
                // add action to plan
                plan.addFollowupAction(action);
            }
        }

        // save plan
        this.save(plan);
    }

    /**
     * Save a followupPlan.
     *
     * @param followupPlan the entity to save
     * @return the persisted entity
     */
    @Override
    public FollowupPlan save(FollowupPlan followupPlan) {
        log.debug("Request to save FollowupPlan : {}", followupPlan);
        FollowupPlan result = followupPlanRepository.save(followupPlan);
        followupPlanSearchRepository.save(result);
        // now send off all associated actions to message queue
        rabbitTemplate.convertAndSend(Constants.PLANS_QUEUE, result);
        // now update procedure booking with plan
        ProcedureBooking booking = result.getProcedureBooking();
        booking.setFollowupPlan(followupPlan);
        procedureBookingRepository.save(booking);
        procedureBookingSearchRepository.save(booking);
        return result;
    }

    /**
     * Get all the followupPlans.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FollowupPlan> findAll(Pageable pageable) {
        log.debug("Request to get all FollowupPlans");
        return followupPlanRepository.findAll(pageable);
    }


    /**
     *  get all the followupPlans where ProcedureBooking is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<FollowupPlan> findAllWhereProcedureBookingIsNull() {
        log.debug("Request to get all followupPlans where ProcedureBooking is null");
        return StreamSupport
            .stream(followupPlanRepository.findAll().spliterator(), false)
            .filter(followupPlan -> followupPlan.getProcedureBooking() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one followupPlan by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public FollowupPlan findOne(Long id) {
        log.debug("Request to get FollowupPlan : {}", id);
        return followupPlanRepository.findOne(id);
    }

    /**
     * Get one followupPlan by {@link ProcedureBooking} id.
     *
     * @param id the id of the procedure booking
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FollowupPlan> findOneByProcedureBookingId(Long id) {
        log.debug("Request to get FollowupPlan : {}", id);
        return followupPlanRepository.findOneByProcedureBookingId(id);
    }

    /**
     * Delete the followupPlan by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FollowupPlan : {}", id);
        followupPlanRepository.delete(id);
        followupPlanSearchRepository.delete(id);
    }

    /**
     * Search for the followupPlan corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FollowupPlan> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FollowupPlans for query {}", query);
        Page<FollowupPlan> result = followupPlanSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
