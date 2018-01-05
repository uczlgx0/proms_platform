package com.noesisinformatica.northumbriaproms.service;

import com.noesisinformatica.northumbriaproms.domain.Timepoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Timepoint.
 */
public interface TimepointService {

    /**
     * Save a timepoint.
     *
     * @param timepoint the entity to save
     * @return the persisted entity
     */
    Timepoint save(Timepoint timepoint);

    /**
     * Get all the timepoints.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Timepoint> findAll(Pageable pageable);

    /**
     * Get the "id" timepoint.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Timepoint findOne(Long id);

    /**
     * Delete the "id" timepoint.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the timepoint corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Timepoint> search(String query, Pageable pageable);
}
