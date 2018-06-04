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

import com.noesisinformatica.northumbriaproms.domain.CareEvent;
import com.noesisinformatica.northumbriaproms.domain.*; // for static metamodels
import com.noesisinformatica.northumbriaproms.repository.CareEventRepository;
import com.noesisinformatica.northumbriaproms.repository.search.CareEventSearchRepository;
import com.noesisinformatica.northumbriaproms.service.dto.CareEventCriteria;

import com.noesisinformatica.northumbriaproms.domain.enumeration.EventType;

/**
 * Service for executing complex queries for CareEvent entities in the database.
 * The main input is a {@link CareEventCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CareEvent} or a {@link Page} of {@link CareEvent} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CareEventQueryService extends QueryService<CareEvent> {

    private final Logger log = LoggerFactory.getLogger(CareEventQueryService.class);


    private final CareEventRepository careEventRepository;

    private final CareEventSearchRepository careEventSearchRepository;

    public CareEventQueryService(CareEventRepository careEventRepository, CareEventSearchRepository careEventSearchRepository) {
        this.careEventRepository = careEventRepository;
        this.careEventSearchRepository = careEventSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CareEvent} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CareEvent> findByCriteria(CareEventCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<CareEvent> specification = createSpecification(criteria);
        return careEventRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CareEvent} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CareEvent> findByCriteria(CareEventCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<CareEvent> specification = createSpecification(criteria);
        return careEventRepository.findAll(specification, page);
    }

    /**
     * Function to convert CareEventCriteria to a {@link Specifications}
     */
    private Specifications<CareEvent> createSpecification(CareEventCriteria criteria) {
        Specifications<CareEvent> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), CareEvent_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), CareEvent_.type));
            }
            if (criteria.getTimepointId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getTimepointId(), CareEvent_.timepoint, Timepoint_.id));
            }
            if (criteria.getPatientId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getPatientId(), CareEvent_.patient, Patient_.id));
            }
            if (criteria.getFollowupActionsId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getFollowupActionsId(), CareEvent_.followupActions, FollowupAction_.id));
            }
            if (criteria.getFollowupPlanId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getFollowupPlanId(), CareEvent_.followupPlan, FollowupPlan_.id));
            }
        }
        return specification;
    }

}
