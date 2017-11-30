package com.noesisinformatica.northumbriaproms.service.impl;

import com.noesisinformatica.northumbriaproms.domain.Procedure;
import com.noesisinformatica.northumbriaproms.domain.Questionnaire;
import com.noesisinformatica.northumbriaproms.service.ProcedurelinkService;
import com.noesisinformatica.northumbriaproms.domain.Procedurelink;
import com.noesisinformatica.northumbriaproms.repository.ProcedurelinkRepository;
import com.noesisinformatica.northumbriaproms.repository.search.ProcedurelinkSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Procedurelink.
 */
@Service
@Transactional
public class ProcedurelinkServiceImpl implements ProcedurelinkService{

    private final Logger log = LoggerFactory.getLogger(ProcedurelinkServiceImpl.class);

    private final ProcedurelinkRepository procedurelinkRepository;

    private final ProcedurelinkSearchRepository procedurelinkSearchRepository;

    public ProcedurelinkServiceImpl(ProcedurelinkRepository procedurelinkRepository, ProcedurelinkSearchRepository procedurelinkSearchRepository) {
        this.procedurelinkRepository = procedurelinkRepository;
        this.procedurelinkSearchRepository = procedurelinkSearchRepository;
    }

    /**
     * Save a procedurelink.
     *
     * @param procedurelink the entity to save
     * @return the persisted entity
     */
    @Override
    public Procedurelink save(Procedurelink procedurelink) {
        log.debug("Request to save Procedurelink : {}", procedurelink);
        // verify if there is existing match
        Optional<Procedurelink> existing = procedurelinkRepository.findOneByProcedureAndQuestionnaire(procedurelink.getProcedure(), procedurelink.getQuestionnaire());
        if(existing.isPresent()) {
            return existing.get();
        } else {
            Procedurelink result = procedurelinkRepository.save(procedurelink);
            procedurelinkSearchRepository.save(result);
            return result;
        }
    }

    /**
     * Get all the procedurelinks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Procedurelink> findAll(Pageable pageable) {
        log.debug("Request to get all Procedurelinks");
        return procedurelinkRepository.findAll(pageable);
    }

    /**
     * Get all the {@link Procedurelink}s associated with a procedure.
     *
     * @param procedure the procedure
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Procedurelink> findAllByProcedure(Procedure procedure) {
        log.debug("Request to get all Procedurelinks for Procedure {} ", procedure);
        return procedurelinkRepository.findAllByProcedure(procedure);
    }

    /**
     * Get all the {@link com.noesisinformatica.northumbriaproms.domain.Questionnaire}s associated with a procedure.
     *
     * @param procedureId the id of procedure
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Questionnaire> findAllQuestionnairesByProcedureId(Long procedureId) {
        log.debug("Request to get all Questionnaires for Procedure {} ", procedureId);
        return procedurelinkRepository.findAllQuestionnairesByProcedureId(procedureId);
    }

    /**
     * Get one procedurelink by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Procedurelink findOne(Long id) {
        log.debug("Request to get Procedurelink : {}", id);
        return procedurelinkRepository.findOne(id);
    }

    /**
     * Delete the procedurelink by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Procedurelink : {}", id);
        procedurelinkRepository.delete(id);
        procedurelinkSearchRepository.delete(id);
    }

    /**
     * Search for the procedurelink corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Procedurelink> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Procedurelinks for query {}", query);
        Page<Procedurelink> result = procedurelinkSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
