package com.noesisinformatica.northumbriaproms.service.impl;

import com.noesisinformatica.northumbriaproms.config.Constants;
import com.noesisinformatica.northumbriaproms.domain.*;
import com.noesisinformatica.northumbriaproms.domain.enumeration.ActionStatus;
import com.noesisinformatica.northumbriaproms.domain.enumeration.EventType;
import com.noesisinformatica.northumbriaproms.repository.FollowupPlanRepository;
import com.noesisinformatica.northumbriaproms.repository.ProcedureBookingRepository;
import com.noesisinformatica.northumbriaproms.repository.search.FollowupPlanSearchRepository;
import com.noesisinformatica.northumbriaproms.repository.search.ProcedureBookingSearchRepository;
import com.noesisinformatica.northumbriaproms.service.FollowupPlanService;
import com.noesisinformatica.northumbriaproms.service.ProcedureTimepointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ProcedureTimepointService procedureTimepointService;

    public FollowupPlanServiceImpl(FollowupPlanRepository followupPlanRepository,
                                   ProcedureBookingRepository procedureBookingRepository,
                                   ProcedureBookingSearchRepository procedureBookingSearchRepository,
                                   FollowupPlanSearchRepository followupPlanSearchRepository,
                                   ProcedureTimepointService procedureTimepointService,
                                   RabbitTemplate rabbitTemplate) {
        this.followupPlanRepository = followupPlanRepository;
        this.procedureBookingRepository = procedureBookingRepository;
        this.procedureBookingSearchRepository = procedureBookingSearchRepository;
        this.followupPlanSearchRepository = followupPlanSearchRepository;
        this.procedureTimepointService = procedureTimepointService;
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
        Patient patient = booking.getPatient();
        // see if booking has already been processed - we know this by seeing if follow-up plan is set
        Optional<FollowupPlan> existing = findOneByProcedureBookingId(booking.getId());
        FollowupPlan plan = new FollowupPlan();
        if(existing.isPresent()) {
            plan = existing.get();
        } else {
            log.info("Found no existing plan. Assuming new procedure booking and creating plan and actions.");
            List<Timepoint> timepoints = procedureTimepointService.findAllTimepointsByProcedureLocalCode(Integer.valueOf(booking.getPrimaryProcedure()));
            for(Timepoint timepoint: timepoints) {
                // add all time points to plan as scheduled care events
                CareEvent careEvent = new CareEvent().timepoint(timepoint).patient(patient).type(EventType.SCHEDULED).status(ActionStatus.UNINITIALISED);
                // add event to plan
                plan.addCareEvent(careEvent);
            }
        }

        // save plan
        plan.setProcedureBooking(booking);
        plan.setPatient(patient);
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
        // now send to message queue for further processing
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
        log.debug("Request to get FollowupPlan for Procedure Booking : {}", id);
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
