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

import com.noesisinformatica.northumbriaproms.domain.CareEvent;
import com.noesisinformatica.northumbriaproms.domain.FollowupPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing CareEvent.
 */
public interface CareEventService {

    /**
     * Utility method that handles processing of a {@link com.noesisinformatica.northumbriaproms.domain.FollowupPlan}s
     * @param followupPlan the plan to process
     */
    void processFollowupPlan(FollowupPlan followupPlan);

    /**
     * Save a careEvent.
     *
     * @param careEvent the entity to save
     * @return the persisted entity
     */
    CareEvent save(CareEvent careEvent);

    /**
     * Get all the careEvents.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CareEvent> findAll(Pageable pageable);

    /**
     * Get the "id" careEvent.
     *
     * @param id the id of the entity
     * @return the entity
     */
    CareEvent findOne(Long id);

    /**
     * Delete the "id" careEvent.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the careEvent corresponding to the query.
     *
     * @param query the query of the search
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CareEvent> search(String query, Pageable pageable);

    /**
     * Get all the care events for given {@link com.noesisinformatica.northumbriaproms.domain.FollowupPlan} "id"
     * @param planId the id of followup plan
     * @return the list of care events
     */
    List<CareEvent> findAllByFollowupPlanId(Long planId);

    /**
     * Get all the care events for given {@link com.noesisinformatica.northumbriaproms.domain.Patient} "id"
     * @param patientId the id of the patient
     * @return the list of all care events
     */
    List<CareEvent> findAllByPatientId(Long patientId);
}
