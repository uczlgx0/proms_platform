package com.noesisinformatica.northumbriaproms.service;

import com.noesisinformatica.northumbriaproms.domain.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Address.
 */
public interface AddressService {

    /**
     * Save a address.
     *
     * @param address the entity to save
     * @return the persisted entity
     */
    Address save(Address address);

    /**
     * Get all the addresses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Address> findAll(Pageable pageable);

    /**
     * Get all the addresses for patient.
     *
     * @param patientId the id of patient
     * @return the list of addresses
     */
    List<Address> findAllForPatientId(Long patientId);

    /**
     * Get the "id" address.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Address findOne(Long id);

    /**
     * Delete the "id" address.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the address corresponding to the query.
     *
     * @param query the query of the search
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Address> search(String query, Pageable pageable);
}
