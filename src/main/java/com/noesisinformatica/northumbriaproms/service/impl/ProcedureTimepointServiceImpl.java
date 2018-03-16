package com.noesisinformatica.northumbriaproms.service.impl;

/*-
 * #%L
 * Proms Platform
 * %%
 * Copyright (C) 2017 - 2018 Termlex
 * %%
 * This software is Copyright and Intellectual Property of Termlex Inc Limited.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation as version 3 of the
 * License.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public
 * License along with this program.  If not, see
 * <https://www.gnu.org/licenses/agpl-3.0.en.html>.
 * #L%
 */

import com.noesisinformatica.northumbriaproms.domain.Timepoint;
import com.noesisinformatica.northumbriaproms.service.ProcedureTimepointService;
import com.noesisinformatica.northumbriaproms.domain.ProcedureTimepoint;
import com.noesisinformatica.northumbriaproms.repository.ProcedureTimepointRepository;
import com.noesisinformatica.northumbriaproms.repository.search.ProcedureTimepointSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ProcedureTimepoint.
 */
@Service
@Transactional
public class ProcedureTimepointServiceImpl implements ProcedureTimepointService{

    private final Logger log = LoggerFactory.getLogger(ProcedureTimepointServiceImpl.class);

    private final ProcedureTimepointRepository procedureTimepointRepository;

    private final ProcedureTimepointSearchRepository procedureTimepointSearchRepository;

    public ProcedureTimepointServiceImpl(ProcedureTimepointRepository procedureTimepointRepository, ProcedureTimepointSearchRepository procedureTimepointSearchRepository) {
        this.procedureTimepointRepository = procedureTimepointRepository;
        this.procedureTimepointSearchRepository = procedureTimepointSearchRepository;
    }

    /**
     * Save a procedureTimepoint.
     *
     * @param procedureTimepoint the entity to save
     * @return the persisted entity
     */
    @Override
    public ProcedureTimepoint save(ProcedureTimepoint procedureTimepoint) {
        log.debug("Request to save ProcedureTimepoint : {}", procedureTimepoint);
        ProcedureTimepoint result = procedureTimepointRepository.save(procedureTimepoint);
        procedureTimepointSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the procedureTimepoints.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProcedureTimepoint> findAll(Pageable pageable) {
        log.debug("Request to get all ProcedureTimepoints");
        return procedureTimepointRepository.findAll(pageable);
    }

    /**
     * Get one procedureTimepoint by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ProcedureTimepoint findOne(Long id) {
        log.debug("Request to get ProcedureTimepoint : {}", id);
        return procedureTimepointRepository.findOne(id);
    }

    /**
     * Delete the procedureTimepoint by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProcedureTimepoint : {}", id);
        procedureTimepointRepository.delete(id);
        procedureTimepointSearchRepository.delete(id);
    }

    /**
     * Search for the procedureTimepoint corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProcedureTimepoint> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ProcedureTimepoints for query {}", query);
        Page<ProcedureTimepoint> result = procedureTimepointSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }

    @Override
    public List<Timepoint> findAllTimepointsByProcedureLocalCode(Integer localCode) {
        log.debug("Request to get Timepoints by Procedure local code : {}", localCode);
        return procedureTimepointRepository.findAllTimepointsByProcedureLocalCode(localCode);
    }
}
