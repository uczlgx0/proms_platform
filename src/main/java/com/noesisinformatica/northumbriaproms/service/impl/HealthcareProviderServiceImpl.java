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

import com.noesisinformatica.northumbriaproms.service.HealthcareProviderService;
import com.noesisinformatica.northumbriaproms.domain.HealthcareProvider;
import com.noesisinformatica.northumbriaproms.repository.HealthcareProviderRepository;
import com.noesisinformatica.northumbriaproms.repository.search.HealthcareProviderSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing HealthcareProvider.
 */
@Service
@Transactional
public class HealthcareProviderServiceImpl implements HealthcareProviderService{

    private final Logger log = LoggerFactory.getLogger(HealthcareProviderServiceImpl.class);

    private final HealthcareProviderRepository healthcareProviderRepository;

    private final HealthcareProviderSearchRepository healthcareProviderSearchRepository;

    public HealthcareProviderServiceImpl(HealthcareProviderRepository healthcareProviderRepository, HealthcareProviderSearchRepository healthcareProviderSearchRepository) {
        this.healthcareProviderRepository = healthcareProviderRepository;
        this.healthcareProviderSearchRepository = healthcareProviderSearchRepository;
    }

    /**
     * Save a healthcareProvider.
     *
     * @param healthcareProvider the entity to save
     * @return the persisted entity
     */
    @Override
    public HealthcareProvider save(HealthcareProvider healthcareProvider) {
        log.debug("Request to save HealthcareProvider : {}", healthcareProvider);
        HealthcareProvider result = healthcareProviderRepository.save(healthcareProvider);
        healthcareProviderSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the healthcareProviders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<HealthcareProvider> findAll(Pageable pageable) {
        log.debug("Request to get all HealthcareProviders");
        return healthcareProviderRepository.findAll(pageable);
    }

    /**
     * Get one healthcareProvider by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public HealthcareProvider findOne(Long id) {
        log.debug("Request to get HealthcareProvider : {}", id);
        return healthcareProviderRepository.findOne(id);
    }

    /**
     * Delete the healthcareProvider by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete HealthcareProvider : {}", id);
        healthcareProviderRepository.delete(id);
        healthcareProviderSearchRepository.delete(id);
    }

    /**
     * Search for the healthcareProvider corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<HealthcareProvider> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of HealthcareProviders for query {}", query);
        Page<HealthcareProvider> result = healthcareProviderSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
