package com.noesisinformatica.northumbriaproms.service;

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


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.noesisinformatica.northumbriaproms.domain.HealthcareProvider;
import com.noesisinformatica.northumbriaproms.domain.*; // for static metamodels
import com.noesisinformatica.northumbriaproms.repository.HealthcareProviderRepository;
import com.noesisinformatica.northumbriaproms.repository.search.HealthcareProviderSearchRepository;
import com.noesisinformatica.northumbriaproms.service.dto.HealthcareProviderCriteria;


/**
 * Service for executing complex queries for HealthcareProvider entities in the database.
 * The main input is a {@link HealthcareProviderCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link HealthcareProvider} or a {@link Page} of {@link HealthcareProvider} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HealthcareProviderQueryService extends QueryService<HealthcareProvider> {

    private final Logger log = LoggerFactory.getLogger(HealthcareProviderQueryService.class);


    private final HealthcareProviderRepository healthcareProviderRepository;

    private final HealthcareProviderSearchRepository healthcareProviderSearchRepository;

    public HealthcareProviderQueryService(HealthcareProviderRepository healthcareProviderRepository, HealthcareProviderSearchRepository healthcareProviderSearchRepository) {
        this.healthcareProviderRepository = healthcareProviderRepository;
        this.healthcareProviderSearchRepository = healthcareProviderSearchRepository;
    }

    /**
     * Return a {@link List} of {@link HealthcareProvider} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HealthcareProvider> findByCriteria(HealthcareProviderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<HealthcareProvider> specification = createSpecification(criteria);
        return healthcareProviderRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link HealthcareProvider} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HealthcareProvider> findByCriteria(HealthcareProviderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<HealthcareProvider> specification = createSpecification(criteria);
        return healthcareProviderRepository.findAll(specification, page);
    }

    /**
     * Function to convert HealthcareProviderCriteria to a {@link Specifications}
     */
    private Specifications<HealthcareProvider> createSpecification(HealthcareProviderCriteria criteria) {
        Specifications<HealthcareProvider> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), HealthcareProvider_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), HealthcareProvider_.name));
            }
        }
        return specification;
    }

}
