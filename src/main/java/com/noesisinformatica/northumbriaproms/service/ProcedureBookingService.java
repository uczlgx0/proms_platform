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

import com.noesisinformatica.northumbriaproms.domain.FollowupPlan;
import com.noesisinformatica.northumbriaproms.domain.Patient;
import com.noesisinformatica.northumbriaproms.domain.ProcedureBooking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

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
     * Get one Followup plan by patient id and primary procedure.
     *
     * @param procedureCode the primary procedure code
     * @param id the id of the patient
     * @return the plan entity associated with matching procedure booking
     */
    Optional<FollowupPlan> findOneByPatientIdAndPrimaryProcedure(Long id, String procedureCode);

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
