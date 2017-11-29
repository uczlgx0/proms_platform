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

import com.noesisinformatica.northumbriaproms.domain.ProcedureBooking;
import com.noesisinformatica.northumbriaproms.domain.*; // for static metamodels
import com.noesisinformatica.northumbriaproms.repository.ProcedureBookingRepository;
import com.noesisinformatica.northumbriaproms.repository.search.ProcedureBookingSearchRepository;
import com.noesisinformatica.northumbriaproms.service.dto.ProcedureBookingCriteria;


/**
 * Service for executing complex queries for ProcedureBooking entities in the database.
 * The main input is a {@link ProcedureBookingCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProcedureBooking} or a {@link Page} of {@link ProcedureBooking} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProcedureBookingQueryService extends QueryService<ProcedureBooking> {

    private final Logger log = LoggerFactory.getLogger(ProcedureBookingQueryService.class);


    private final ProcedureBookingRepository procedureBookingRepository;

    private final ProcedureBookingSearchRepository procedureBookingSearchRepository;

    public ProcedureBookingQueryService(ProcedureBookingRepository procedureBookingRepository, ProcedureBookingSearchRepository procedureBookingSearchRepository) {
        this.procedureBookingRepository = procedureBookingRepository;
        this.procedureBookingSearchRepository = procedureBookingSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ProcedureBooking} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProcedureBooking> findByCriteria(ProcedureBookingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<ProcedureBooking> specification = createSpecification(criteria);
        return procedureBookingRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ProcedureBooking} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProcedureBooking> findByCriteria(ProcedureBookingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<ProcedureBooking> specification = createSpecification(criteria);
        return procedureBookingRepository.findAll(specification, page);
    }

    /**
     * Function to convert ProcedureBookingCriteria to a {@link Specifications}
     */
    private Specifications<ProcedureBooking> createSpecification(ProcedureBookingCriteria criteria) {
        Specifications<ProcedureBooking> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ProcedureBooking_.id));
            }
            if (criteria.getConsultantName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getConsultantName(), ProcedureBooking_.consultantName));
            }
            if (criteria.getHospitalSite() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHospitalSite(), ProcedureBooking_.hospitalSite));
            }
            if (criteria.getScheduledDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getScheduledDate(), ProcedureBooking_.scheduledDate));
            }
            if (criteria.getPerformedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPerformedDate(), ProcedureBooking_.performedDate));
            }
            if (criteria.getPrimaryProcedure() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrimaryProcedure(), ProcedureBooking_.primaryProcedure));
            }
            if (criteria.getOtherProcedures() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOtherProcedures(), ProcedureBooking_.otherProcedures));
            }
            if (criteria.getPatientId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getPatientId(), ProcedureBooking_.patient, Patient_.id));
            }
        }
        return specification;
    }

}
