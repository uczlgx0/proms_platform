package com.noesisinformatica.northumbriaproms.service;

import com.noesisinformatica.northumbriaproms.domain.HealthcareProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing HealthcareProvider.
 */
public interface HealthcareProviderService {

    /**
     * Save a healthcareProvider.
     *
     * @param healthcareProvider the entity to save
     * @return the persisted entity
     */
    HealthcareProvider save(HealthcareProvider healthcareProvider);

    /**
     * Get all the healthcareProviders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<HealthcareProvider> findAll(Pageable pageable);

    /**
     * Get the "id" healthcareProvider.
     *
     * @param id the id of the entity
     * @return the entity
     */
    HealthcareProvider findOne(Long id);

    /**
     * Delete the "id" healthcareProvider.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the healthcareProvider corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<HealthcareProvider> search(String query, Pageable pageable);
}
