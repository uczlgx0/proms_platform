package com.noesisinformatica.northumbriaproms.service;

import com.noesisinformatica.northumbriaproms.domain.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Patient.
 */
public interface PatientService {

    /**
     * Save a patient.
     *
     * @param patient the entity to save
     * @return the persisted entity
     */
    Patient save(Patient patient);

    /**
     * Get all the patients.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Patient> findAll(Pageable pageable);

    /**
     * Get the "id" patient.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Patient findOne(Long id);

    /**
     * Delete the "id" patient.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the patient corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Patient> search(String query, Pageable pageable);
}
