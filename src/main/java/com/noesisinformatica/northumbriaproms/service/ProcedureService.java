package com.noesisinformatica.northumbriaproms.service;

import com.noesisinformatica.northumbriaproms.domain.Procedure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Procedure.
 */
public interface ProcedureService {

    /**
     * Save a procedure.
     *
     * @param procedure the entity to save
     * @return the persisted entity
     */
    Procedure save(Procedure procedure);

    /**
     * Get all the procedures.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Procedure> findAll(Pageable pageable);

    /**
     * Get the "id" procedure.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Procedure findOne(Long id);

    /**
     * Delete the "id" procedure.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the procedure corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Procedure> search(String query, Pageable pageable);
}
