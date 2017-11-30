package com.noesisinformatica.northumbriaproms.service;


import java.time.ZonedDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.noesisinformatica.northumbriaproms.domain.FollowupAction;
import com.noesisinformatica.northumbriaproms.domain.*; // for static metamodels
import com.noesisinformatica.northumbriaproms.repository.FollowupActionRepository;
import com.noesisinformatica.northumbriaproms.repository.search.FollowupActionSearchRepository;
import com.noesisinformatica.northumbriaproms.service.dto.FollowupActionCriteria;

import com.noesisinformatica.northumbriaproms.domain.enumeration.ActionPhase;
import com.noesisinformatica.northumbriaproms.domain.enumeration.ActionType;

/**
 * Service for executing complex queries for FollowupAction entities in the database.
 * The main input is a {@link FollowupActionCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FollowupAction} or a {@link Page} of {@link FollowupAction} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FollowupActionQueryService extends QueryService<FollowupAction> {

    private final Logger log = LoggerFactory.getLogger(FollowupActionQueryService.class);


    private final FollowupActionRepository followupActionRepository;

    private final FollowupActionSearchRepository followupActionSearchRepository;

    public FollowupActionQueryService(FollowupActionRepository followupActionRepository, FollowupActionSearchRepository followupActionSearchRepository) {
        this.followupActionRepository = followupActionRepository;
        this.followupActionSearchRepository = followupActionSearchRepository;
    }

    /**
     * Return a {@link List} of {@link FollowupAction} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FollowupAction> findByCriteria(FollowupActionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<FollowupAction> specification = createSpecification(criteria);
        return followupActionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link FollowupAction} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FollowupAction> findByCriteria(FollowupActionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<FollowupAction> specification = createSpecification(criteria);
        return followupActionRepository.findAll(specification, page);
    }

    /**
     * Function to convert FollowupActionCriteria to a {@link Specifications}
     */
    private Specifications<FollowupAction> createSpecification(FollowupActionCriteria criteria) {
        Specifications<FollowupAction> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), FollowupAction_.id));
            }
            if (criteria.getPhase() != null) {
                specification = specification.and(buildSpecification(criteria.getPhase(), FollowupAction_.phase));
            }
            if (criteria.getScheduledDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getScheduledDate(), FollowupAction_.scheduledDate));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), FollowupAction_.name));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), FollowupAction_.type));
            }
            if (criteria.getOutcomeScore() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOutcomeScore(), FollowupAction_.outcomeScore));
            }
            if (criteria.getOutcomeComment() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOutcomeComment(), FollowupAction_.outcomeComment));
            }
            if (criteria.getCompletedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCompletedDate(), FollowupAction_.completedDate));
            }
            if (criteria.getFollowupPlanId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getFollowupPlanId(), FollowupAction_.followupPlan, FollowupPlan_.id));
            }
            if (criteria.getPatientId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getPatientId(), FollowupAction_.patient, Patient_.id));
            }
            if (criteria.getQuestionnaireId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getQuestionnaireId(), FollowupAction_.questionnaire, Questionnaire_.id));
            }
        }
        return specification;
    }

}
