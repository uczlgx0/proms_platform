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

import com.noesisinformatica.northumbriaproms.domain.Timepoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Timepoint.
 */
public interface TimepointService {

    /**
     * Save a timepoint.
     *
     * @param timepoint the entity to save
     * @return the persisted entity
     */
    Timepoint save(Timepoint timepoint);

    /**
     * Get all the timepoints.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Timepoint> findAll(Pageable pageable);

    /**
     * Get the "id" timepoint.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Timepoint findOne(Long id);

    /**
     * Delete the "id" timepoint.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the timepoint corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Timepoint> search(String query, Pageable pageable);
}
