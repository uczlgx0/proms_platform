package com.noesisinformatica.northumbriaproms.service.impl;

import com.noesisinformatica.northumbriaproms.config.Constants;
import com.noesisinformatica.northumbriaproms.domain.*;
import com.noesisinformatica.northumbriaproms.domain.enumeration.ActionPhase;
import com.noesisinformatica.northumbriaproms.domain.enumeration.ActionStatus;
import com.noesisinformatica.northumbriaproms.domain.enumeration.ActionType;
import com.noesisinformatica.northumbriaproms.domain.enumeration.TimeUnit;
import com.noesisinformatica.northumbriaproms.service.CareEventService;
import com.noesisinformatica.northumbriaproms.repository.CareEventRepository;
import com.noesisinformatica.northumbriaproms.repository.search.CareEventSearchRepository;
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

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing CareEvent.
 */
@Service
@Transactional
public class CareEventServiceImpl implements CareEventService {

    private final Logger log = LoggerFactory.getLogger(CareEventServiceImpl.class);
    private final RabbitTemplate rabbitTemplate;
    private final CareEventRepository careEventRepository;
    private final ProcedurelinkService procedurelinkService;
    private final CareEventSearchRepository careEventSearchRepository;

    public CareEventServiceImpl(CareEventRepository careEventRepository,
                                CareEventSearchRepository careEventSearchRepository,
                                RabbitTemplate rabbitTemplate, ProcedurelinkService procedurelinkService) {
        this.careEventRepository = careEventRepository;
        this.careEventSearchRepository = careEventSearchRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.procedurelinkService = procedurelinkService;
    }

    /**
     * Utility method that handles processing of a {@link com.noesisinformatica.northumbriaproms.domain.FollowupPlan}s
     * @param followupPlan the plan to process
     */
    @Override
    @RabbitListener(queues = Constants.PLANS_QUEUE)
    public void processFollowupPlan(FollowupPlan followupPlan) {
        log.debug("Request to process FollowupPlan : {}", followupPlan);
        // get all associated actions in plan
        followupPlan.getCareEvents().forEach(careEvent -> {
            log.info("care event = {}", careEvent);
            this.processCareEvent(careEvent, followupPlan.getProcedureBooking());
        });
    }

    private void processCareEvent(CareEvent careEvent, ProcedureBooking booking) {

        Timepoint timepoint = careEvent.getTimepoint();
        // skip annual events for now - they'll have value of -1
        if(timepoint.getValue() < 0) {
            return;
        }

        // otherwise we process...
        boolean isPreOpTimepoint = false;
        if(timepoint.getValue() == 0 && timepoint.getUnit() == TimeUnit.DAY){
            isPreOpTimepoint = true;
        }

        List<Questionnaire> questionnaires = procedurelinkService.findAllQuestionnairesByProcedureLocalCode(Integer.valueOf(booking.getPrimaryProcedure()));
        for(Questionnaire questionnaire : questionnaires) {
            // create a new follow up action
            FollowupAction action = new FollowupAction();
            action.name(questionnaire.getName())
                .type(ActionType.QUESTIONNAIRE).questionnaire(questionnaire)
                .patient(booking.getPatient()).status(ActionStatus.UNINITIALISED);
            // if procedure booking scheduled date is present, then we process it
            if (isPreOpTimepoint) {
                // update phase
                action.phase(ActionPhase.PRE_OPERATIVE).scheduledDate(booking.getScheduledDate());
                // now status if scheduled date is before today or today - otherwise stays as 'uninitialised'
                if(!booking.getScheduledDate().isAfter(LocalDate.now())) {
                    action.setStatus(ActionStatus.STARTED);
                    careEvent.status(ActionStatus.STARTED);

                    // if procedure performed date is not present, we change action status to UNKNOWN
                    if (booking.getPerformedDate() == null) {
                        action.status(ActionStatus.UNKNOWN);
                        careEvent.status(ActionStatus.UNKNOWN);
                    }
                }
            } else {
                // treat as post-op
                action.setPhase(ActionPhase.POST_OPERATIVE);
                // if procedure performed date is not present, we change action status to UNKNOWN
                if (booking.getPerformedDate() == null) {
                    action.status(ActionStatus.UNKNOWN);
                    careEvent.status(ActionStatus.UNKNOWN);
                } else {
                    // calculate relative date from completed date, using time point
                    LocalDate calculatedDate = this.calculateDateFromTimePoint(timepoint, booking);
                    action.setScheduledDate(calculatedDate);
                    if(! calculatedDate.isAfter(LocalDate.now())) {
                        action.status(ActionStatus.STARTED);
                        careEvent.setStatus(ActionStatus.STARTED);
                    }
                }
            }

            if("OUTCOME".equalsIgnoreCase(questionnaire.getName()) && isPreOpTimepoint) {
                log.debug("Skipping OUTCOME as action type for PRE-OP phase");
            } else {
                // add action to plan
                careEvent.addFollowupAction(action);
            }
        }

        // save event
        this.save(careEvent);
    }

    private LocalDate calculateDateFromTimePoint(Timepoint timepoint, ProcedureBooking booking) {
        LocalDate date = LocalDate.from(booking.getPerformedDate());
        TimeUnit unit = timepoint.getUnit();
        if(TimeUnit.MONTH == unit) {
            date = date.plusMonths(Long.valueOf(timepoint.getValue()));
        } else if(TimeUnit.YEAR == unit) {
            date = date.plusYears(Long.valueOf(timepoint.getValue()));
        } else if(TimeUnit.WEEK == unit) {
            date = date.plusWeeks(Long.valueOf(timepoint.getValue()));
        } else  {
            log.error("Unknown time unit passed : {}. Assuming day ", unit);
            date = date.plusDays(Long.valueOf(timepoint.getValue()));
        }

        return date;
    }

    /**
     * Save a careEvent.
     *
     * @param careEvent the entity to save
     * @return the persisted entity
     */
    @Override
    public CareEvent save(CareEvent careEvent) {
        log.debug("Request to save CareEvent : {}", careEvent);
        CareEvent result = careEventRepository.save(careEvent);
        careEventSearchRepository.save(result);
        log.info("Sending care event to message queue");
        rabbitTemplate.convertAndSend(Constants.CARE_EVENTS_QUEUE, result);
        return result;
    }

    /**
     * Get all the careEvents.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CareEvent> findAll(Pageable pageable) {
        log.debug("Request to get all CareEvents");
        return careEventRepository.findAll(pageable);
    }

    /**
     * Get one careEvent by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CareEvent findOne(Long id) {
        log.debug("Request to get CareEvent : {}", id);
        return careEventRepository.findOne(id);
    }

    /**
     * Delete the careEvent by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CareEvent : {}", id);
        careEventRepository.delete(id);
        careEventSearchRepository.delete(id);
    }

    /**
     * Search for the careEvent corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CareEvent> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CareEvents for query {}", query);
        Page<CareEvent> result = careEventSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }

    /**
     * Get all the care events for given {@link com.noesisinformatica.northumbriaproms.domain.FollowupPlan} "id"
     * @param planId the id of followup plan
     * @return the list of care events
     */
    @Override
    public List<CareEvent> findAllByFollowupPlanId(Long planId) {
        log.debug("Request to get CareEvent for Plan id : {}", planId);
        return careEventRepository.findAllByFollowupPlanId(planId);
    }

    /**
     * Get all the care events for given {@link com.noesisinformatica.northumbriaproms.domain.Patient} "id"
     * @param patientId the id of the patient
     * @return the list of all care events
     */
    @Override
    public List<CareEvent> findAllByPatientId(Long patientId) {
        log.debug("Request to get CareEvent for Patient id : {}", patientId);
        return careEventRepository.findAllByPatientId(patientId);
    }
}
