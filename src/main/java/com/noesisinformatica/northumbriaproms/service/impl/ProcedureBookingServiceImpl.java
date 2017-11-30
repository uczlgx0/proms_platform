package com.noesisinformatica.northumbriaproms.service.impl;

import com.noesisinformatica.northumbriaproms.domain.Patient;
import com.noesisinformatica.northumbriaproms.service.ProcedureBookingService;
import com.noesisinformatica.northumbriaproms.domain.ProcedureBooking;
import com.noesisinformatica.northumbriaproms.repository.ProcedureBookingRepository;
import com.noesisinformatica.northumbriaproms.repository.search.ProcedureBookingSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ProcedureBooking.
 */
@Service
@Transactional
public class ProcedureBookingServiceImpl implements ProcedureBookingService{

    private final Logger log = LoggerFactory.getLogger(ProcedureBookingServiceImpl.class);

    private final ProcedureBookingRepository procedureBookingRepository;

    private final ProcedureBookingSearchRepository procedureBookingSearchRepository;

    public ProcedureBookingServiceImpl(ProcedureBookingRepository procedureBookingRepository, ProcedureBookingSearchRepository procedureBookingSearchRepository) {
        this.procedureBookingRepository = procedureBookingRepository;
        this.procedureBookingSearchRepository = procedureBookingSearchRepository;
    }

    /**
     * Save a procedureBooking.
     *
     * @param procedureBooking the entity to save
     * @return the persisted entity
     */
    @Override
    public ProcedureBooking save(ProcedureBooking procedureBooking) {
        log.debug("Request to save ProcedureBooking : {}", procedureBooking);
        ProcedureBooking result = procedureBookingRepository.save(procedureBooking);
        procedureBookingSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the procedureBookings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProcedureBooking> findAll(Pageable pageable) {
        log.debug("Request to get all ProcedureBookings");
        return procedureBookingRepository.findAll(pageable);
    }

    /**
     * Get one procedureBooking by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ProcedureBooking findOne(Long id) {
        log.debug("Request to get ProcedureBooking : {}", id);
        return procedureBookingRepository.findOne(id);
    }

    /**
     * Get all procedureBookings by patient.
     *
     * @param patient the patient
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProcedureBooking> findAllByPatient(Patient patient, Pageable pageable) {
        log.debug("Request to get ProcedureBookings for patient : {}", patient);
        return procedureBookingRepository.findAllByPatient(patient, pageable);
    }

    /**
     * Delete the procedureBooking by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProcedureBooking : {}", id);
        procedureBookingRepository.delete(id);
        procedureBookingSearchRepository.delete(id);
    }

    /**
     * Search for the procedureBooking corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProcedureBooking> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ProcedureBookings for query {}", query);
        Page<ProcedureBooking> result = procedureBookingSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
