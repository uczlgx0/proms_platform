package com.noesisinformatica.northumbriaproms.service;

import com.noesisinformatica.northumbriaproms.domain.FollowupAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing FollowupAction.
 */
public interface FollowupActionService {

    /**
     * Save a followupAction.
     *
     * @param followupAction the entity to save
     * @return the persisted entity
     */
    FollowupAction save(FollowupAction followupAction);

    /**
     * Get all the followupActions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<FollowupAction> findAll(Pageable pageable);

    /**
     * Get the "id" followupAction.
     *
     * @param id the id of the entity
     * @return the entity
     */
    FollowupAction findOne(Long id);

    /**
     * Delete the "id" followupAction.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the followupAction corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<FollowupAction> search(String query, Pageable pageable);
}
