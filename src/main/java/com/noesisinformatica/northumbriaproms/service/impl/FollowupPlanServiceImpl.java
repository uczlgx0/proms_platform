package com.noesisinformatica.northumbriaproms.service.impl;

import com.noesisinformatica.northumbriaproms.config.Constants;
import com.noesisinformatica.northumbriaproms.domain.FollowupPlan;
import com.noesisinformatica.northumbriaproms.domain.Patient;
import com.noesisinformatica.northumbriaproms.domain.ProcedureBooking;
import com.noesisinformatica.northumbriaproms.repository.FollowupPlanRepository;
import com.noesisinformatica.northumbriaproms.repository.search.FollowupPlanSearchRepository;
import com.noesisinformatica.northumbriaproms.service.FollowupPlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing FollowupPlan.
 */
@Service
@Transactional
public class FollowupPlanServiceImpl implements FollowupPlanService{

    private final Logger log = LoggerFactory.getLogger(FollowupPlanServiceImpl.class);

    private final FollowupPlanRepository followupPlanRepository;

    private final FollowupPlanSearchRepository followupPlanSearchRepository;

    public FollowupPlanServiceImpl(FollowupPlanRepository followupPlanRepository, FollowupPlanSearchRepository followupPlanSearchRepository) {
        this.followupPlanRepository = followupPlanRepository;
        this.followupPlanSearchRepository = followupPlanSearchRepository;
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
