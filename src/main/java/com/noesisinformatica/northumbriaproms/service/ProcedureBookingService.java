package com.noesisinformatica.northumbriaproms.service;

import com.noesisinformatica.northumbriaproms.domain.Patient;
import com.noesisinformatica.northumbriaproms.domain.ProcedureBooking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing ProcedureBooking.
 */
public interface ProcedureBookingService {

    /**
     * Save a procedureBooking.
     *
     * @param procedureBooking the entity to save
     * @return the persisted entity
     */
    ProcedureBooking save(ProcedureBooking procedureBooking);

    /**
     * Get all the procedureBookings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ProcedureBooking> findAll(Pageable pageable);

    /**
     * Get the "id" procedureBooking.
     *
     * @param id the id of the entity
     * @return the entity
     */
    ProcedureBooking findOne(Long id);

    /**
     * Get all procedureBookings by patient.
     *
     * @param patient the patient
     * @return the entity
     */
    Page<ProcedureBooking> findAllByPatient(Patient patient, Pageable pageable);

    /**
     * Delete the "id" procedureBooking.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the procedureBooking corresponding to the query.
     *
     * @param query the query of the search
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ProcedureBooking> search(String query, Pageable pageable);
}
