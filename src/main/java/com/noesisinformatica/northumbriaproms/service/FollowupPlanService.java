package com.noesisinformatica.northumbriaproms.service;

import com.noesisinformatica.northumbriaproms.domain.FollowupPlan;
import com.noesisinformatica.northumbriaproms.domain.ProcedureBooking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing FollowupPlan.
 */
public interface FollowupPlanService {

    /**
     * Utility method for generating {@link FollowupPlan} from a {@link ProcedureBooking}
     *
     * @param booking the ProcedureBooking to process
     */
    void processBooking(ProcedureBooking booking);

    /**
     * Save a followupPlan.
     *
     * @param followupPlan the entity to save
     * @return the persisted entity
     */
    FollowupPlan save(FollowupPlan followupPlan);

    /**
     * Get all the followupPlans.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<FollowupPlan> findAll(Pageable pageable);
    /**
     * Get all the FollowupPlanDTO where ProcedureBooking is null.
     *
     * @return the list of entities
     */
    List<FollowupPlan> findAllWhereProcedureBookingIsNull();

    /**
     * Get the "id" followupPlan.
     *
     * @param id the id of the entity
     * @return the entity
     */
    FollowupPlan findOne(Long id);

    /**
     * Delete the "id" followupPlan.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the followupPlan corresponding to the query.
     *
     * @param query the query of the search
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<FollowupPlan> search(String query, Pageable pageable);
}
