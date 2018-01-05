package com.noesisinformatica.northumbriaproms.service;

import com.noesisinformatica.northumbriaproms.domain.ProcedureTimepoint;
import com.noesisinformatica.northumbriaproms.domain.Timepoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing ProcedureTimepoint.
 */
public interface ProcedureTimepointService {

    /**
     * Save a procedureTimepoint.
     *
     * @param procedureTimepoint the entity to save
     * @return the persisted entity
     */
    ProcedureTimepoint save(ProcedureTimepoint procedureTimepoint);

    /**
     * Get all the procedureTimepoints.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ProcedureTimepoint> findAll(Pageable pageable);

    /**
     * Get the "id" procedureTimepoint.
     *
     * @param id the id of the entity
     * @return the entity
     */
    ProcedureTimepoint findOne(Long id);

    /**
     * Delete the "id" procedureTimepoint.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the procedureTimepoint corresponding to the query.
     *
     * @param query the query of the search
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ProcedureTimepoint> search(String query, Pageable pageable);

    /**
     * Get all the {@link Timepoint}s associated with a procedure.
     *
     * @param localCode the local code of procedure
     * @return the list of entities
     */
    List<Timepoint> findAllTimepointsByProcedureLocalCode(Integer localCode);
}
