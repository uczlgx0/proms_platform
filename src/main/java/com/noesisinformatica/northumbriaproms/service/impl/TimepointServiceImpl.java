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

import com.noesisinformatica.northumbriaproms.service.TimepointService;
import com.noesisinformatica.northumbriaproms.domain.Timepoint;
import com.noesisinformatica.northumbriaproms.repository.TimepointRepository;
import com.noesisinformatica.northumbriaproms.repository.search.TimepointSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Timepoint.
 */
@Service
@Transactional
public class TimepointServiceImpl implements TimepointService{

    private final Logger log = LoggerFactory.getLogger(TimepointServiceImpl.class);

    private final TimepointRepository timepointRepository;

    private final TimepointSearchRepository timepointSearchRepository;

    public TimepointServiceImpl(TimepointRepository timepointRepository, TimepointSearchRepository timepointSearchRepository) {
        this.timepointRepository = timepointRepository;
        this.timepointSearchRepository = timepointSearchRepository;
    }

    /**
     * Save a timepoint.
     *
     * @param timepoint the entity to save
     * @return the persisted entity
     */
    @Override
    public Timepoint save(Timepoint timepoint) {
        log.debug("Request to save Timepoint : {}", timepoint);
        Timepoint result = timepointRepository.save(timepoint);
        timepointSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the timepoints.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Timepoint> findAll(Pageable pageable) {
        log.debug("Request to get all Timepoints");
        return timepointRepository.findAll(pageable);
    }

    /**
     * Get one timepoint by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Timepoint findOne(Long id) {
        log.debug("Request to get Timepoint : {}", id);
        return timepointRepository.findOne(id);
    }

    /**
     * Delete the timepoint by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Timepoint : {}", id);
        timepointRepository.delete(id);
        timepointSearchRepository.delete(id);
    }

    /**
     * Search for the timepoint corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Timepoint> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Timepoints for query {}", query);
        Page<Timepoint> result = timepointSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
