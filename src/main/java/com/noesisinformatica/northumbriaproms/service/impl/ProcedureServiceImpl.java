package com.noesisinformatica.northumbriaproms.service.impl;

import com.noesisinformatica.northumbriaproms.service.ProcedureService;
import com.noesisinformatica.northumbriaproms.domain.Procedure;
import com.noesisinformatica.northumbriaproms.repository.ProcedureRepository;
import com.noesisinformatica.northumbriaproms.repository.search.ProcedureSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Procedure.
 */
@Service
@Transactional
public class ProcedureServiceImpl implements ProcedureService{

    private final Logger log = LoggerFactory.getLogger(ProcedureServiceImpl.class);

    private final ProcedureRepository procedureRepository;

    private final ProcedureSearchRepository procedureSearchRepository;

    public ProcedureServiceImpl(ProcedureRepository procedureRepository, ProcedureSearchRepository procedureSearchRepository) {
        this.procedureRepository = procedureRepository;
        this.procedureSearchRepository = procedureSearchRepository;
    }

    /**
     * Save a procedure.
     *
     * @param procedure the entity to save
     * @return the persisted entity
     */
    @Override
    public Procedure save(Procedure procedure) {
        log.debug("Request to save Procedure : {}", procedure);
        Procedure result = procedureRepository.save(procedure);
        procedureSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the procedures.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Procedure> findAll(Pageable pageable) {
        log.debug("Request to get all Procedures");
        return procedureRepository.findAll(pageable);
    }

    /**
     * Get one procedure by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Procedure findOne(Long id) {
        log.debug("Request to get Procedure : {}", id);
        return procedureRepository.findOne(id);
    }

    /**
     * Delete the procedure by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Procedure : {}", id);
        procedureRepository.delete(id);
        procedureSearchRepository.delete(id);
    }

    /**
     * Search for the procedure corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Procedure> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Procedures for query {}", query);
        Page<Procedure> result = procedureSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
