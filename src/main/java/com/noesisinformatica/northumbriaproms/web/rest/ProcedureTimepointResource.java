package com.noesisinformatica.northumbriaproms.web.rest;

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

import com.codahale.metrics.annotation.Timed;
import com.noesisinformatica.northumbriaproms.domain.ProcedureTimepoint;
import com.noesisinformatica.northumbriaproms.domain.Timepoint;
import com.noesisinformatica.northumbriaproms.service.ProcedureTimepointService;
import com.noesisinformatica.northumbriaproms.web.rest.errors.BadRequestAlertException;
import com.noesisinformatica.northumbriaproms.web.rest.util.HeaderUtil;
import com.noesisinformatica.northumbriaproms.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ProcedureTimepoint.
 */
@RestController
@RequestMapping("/api")
public class ProcedureTimepointResource {

    private final Logger log = LoggerFactory.getLogger(ProcedureTimepointResource.class);

    private static final String ENTITY_NAME = "procedureTimepoint";

    private final ProcedureTimepointService procedureTimepointService;

    public ProcedureTimepointResource(ProcedureTimepointService procedureTimepointService) {
        this.procedureTimepointService = procedureTimepointService;
    }

    /**
     * POST  /procedure-timepoints : Create a new procedureTimepoint.
     *
     * @param procedureTimepoint the procedureTimepoint to create
     * @return the ResponseEntity with status 201 (Created) and with body the new procedureTimepoint, or with status 400 (Bad Request) if the procedureTimepoint has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/procedure-timepoints")
    @Timed
    public ResponseEntity<ProcedureTimepoint> createProcedureTimepoint(@Valid @RequestBody ProcedureTimepoint procedureTimepoint) throws URISyntaxException {
        log.debug("REST request to save ProcedureTimepoint : {}", procedureTimepoint);
        if (procedureTimepoint.getId() != null) {
            throw new BadRequestAlertException("A new procedureTimepoint cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProcedureTimepoint result = procedureTimepointService.save(procedureTimepoint);
        return ResponseEntity.created(new URI("/api/procedure-timepoints/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /procedure-timepoints : Updates an existing procedureTimepoint.
     *
     * @param procedureTimepoint the procedureTimepoint to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated procedureTimepoint,
     * or with status 400 (Bad Request) if the procedureTimepoint is not valid,
     * or with status 500 (Internal Server Error) if the procedureTimepoint couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/procedure-timepoints")
    @Timed
    public ResponseEntity<ProcedureTimepoint> updateProcedureTimepoint(@Valid @RequestBody ProcedureTimepoint procedureTimepoint) throws URISyntaxException {
        log.debug("REST request to update ProcedureTimepoint : {}", procedureTimepoint);
        if (procedureTimepoint.getId() == null) {
            return createProcedureTimepoint(procedureTimepoint);
        }
        ProcedureTimepoint result = procedureTimepointService.save(procedureTimepoint);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, procedureTimepoint.getId().toString()))
            .body(result);
    }

    /**
     * GET  /procedure-timepoints : get all the procedureTimepoints.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of procedureTimepoints in body
     */
    @GetMapping("/procedure-timepoints")
    @Timed
    public ResponseEntity<List<ProcedureTimepoint>> getAllProcedureTimepoints(Pageable pageable) {
        log.debug("REST request to get a page of ProcedureTimepoints");
        Page<ProcedureTimepoint> page = procedureTimepointService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/procedure-timepoints");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /procedures/:id/timepoints : get timepoints for procedure with localCode "id".
     *
     * @param id the localCode of the procedure to retrieve timepoints for
     * @return the ResponseEntity with status 200 (OK) and with body as list of timepoints, or with status 404 (Not Found)
     */
    @GetMapping("/procedures/{id}/timepoints")
    @Timed
    public ResponseEntity<List<Timepoint>> getTimepointsForProcedure(@PathVariable Integer id) {
        log.debug("REST request to get timepoints for Procedure : {}", id);
        List<Timepoint> list = procedureTimepointService.findAllTimepointsByProcedureLocalCode(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(list));
    }

    /**
     * GET  /procedure-timepoints/:id : get the "id" procedureTimepoint.
     *
     * @param id the id of the procedureTimepoint to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the procedureTimepoint, or with status 404 (Not Found)
     */
    @GetMapping("/procedure-timepoints/{id}")
    @Timed
    public ResponseEntity<ProcedureTimepoint> getProcedureTimepoint(@PathVariable Long id) {
        log.debug("REST request to get ProcedureTimepoint : {}", id);
        ProcedureTimepoint procedureTimepoint = procedureTimepointService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(procedureTimepoint));
    }

    /**
     * DELETE  /procedure-timepoints/:id : delete the "id" procedureTimepoint.
     *
     * @param id the id of the procedureTimepoint to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/procedure-timepoints/{id}")
    @Timed
    public ResponseEntity<Void> deleteProcedureTimepoint(@PathVariable Long id) {
        log.debug("REST request to delete ProcedureTimepoint : {}", id);
        procedureTimepointService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/procedure-timepoints?query=:query : search for the procedureTimepoint corresponding
     * to the query.
     *
     * @param query the query of the procedureTimepoint search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/procedure-timepoints")
    @Timed
    public ResponseEntity<List<ProcedureTimepoint>> searchProcedureTimepoints(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ProcedureTimepoints for query {}", query);
        Page<ProcedureTimepoint> page = procedureTimepointService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/procedure-timepoints");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
