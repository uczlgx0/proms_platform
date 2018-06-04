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
import com.noesisinformatica.northumbriaproms.domain.Timepoint;
import com.noesisinformatica.northumbriaproms.service.TimepointService;
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
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Timepoint.
 */
@RestController
@RequestMapping("/api")
public class TimepointResource {

    private final Logger log = LoggerFactory.getLogger(TimepointResource.class);

    private static final String ENTITY_NAME = "timepoint";

    private final TimepointService timepointService;

    public TimepointResource(TimepointService timepointService) {
        this.timepointService = timepointService;
    }

    /**
     * POST  /timepoints : Create a new timepoint.
     *
     * @param timepoint the timepoint to create
     * @return the ResponseEntity with status 201 (Created) and with body the new timepoint, or with status 400 (Bad Request) if the timepoint has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/timepoints")
    @Timed
    public ResponseEntity<Timepoint> createTimepoint(@Valid @RequestBody Timepoint timepoint) throws URISyntaxException {
        log.debug("REST request to save Timepoint : {}", timepoint);
        if (timepoint.getId() != null) {
            throw new BadRequestAlertException("A new timepoint cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Timepoint result = timepointService.save(timepoint);
        return ResponseEntity.created(new URI("/api/timepoints/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /timepoints : Updates an existing timepoint.
     *
     * @param timepoint the timepoint to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated timepoint,
     * or with status 400 (Bad Request) if the timepoint is not valid,
     * or with status 500 (Internal Server Error) if the timepoint couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/timepoints")
    @Timed
    public ResponseEntity<Timepoint> updateTimepoint(@Valid @RequestBody Timepoint timepoint) throws URISyntaxException {
        log.debug("REST request to update Timepoint : {}", timepoint);
        if (timepoint.getId() == null) {
            return createTimepoint(timepoint);
        }
        Timepoint result = timepointService.save(timepoint);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, timepoint.getId().toString()))
            .body(result);
    }

    /**
     * GET  /timepoints : get all the timepoints.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of timepoints in body
     */
    @GetMapping("/timepoints")
    @Timed
    public ResponseEntity<List<Timepoint>> getAllTimepoints(Pageable pageable) {
        log.debug("REST request to get a page of Timepoints");
        Page<Timepoint> page = timepointService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/timepoints");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /timepoints/:id : get the "id" timepoint.
     *
     * @param id the id of the timepoint to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the timepoint, or with status 404 (Not Found)
     */
    @GetMapping("/timepoints/{id}")
    @Timed
    public ResponseEntity<Timepoint> getTimepoint(@PathVariable Long id) {
        log.debug("REST request to get Timepoint : {}", id);
        Timepoint timepoint = timepointService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(timepoint));
    }

    /**
     * DELETE  /timepoints/:id : delete the "id" timepoint.
     *
     * @param id the id of the timepoint to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/timepoints/{id}")
    @Timed
    public ResponseEntity<Void> deleteTimepoint(@PathVariable Long id) {
        log.debug("REST request to delete Timepoint : {}", id);
        timepointService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/timepoints?query=:query : search for the timepoint corresponding
     * to the query.
     *
     * @param query the query of the timepoint search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/timepoints")
    @Timed
    public ResponseEntity<List<Timepoint>> searchTimepoints(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Timepoints for query {}", query);
        Page<Timepoint> page = timepointService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/timepoints");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
