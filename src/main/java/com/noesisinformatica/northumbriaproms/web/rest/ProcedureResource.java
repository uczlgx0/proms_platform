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
import com.noesisinformatica.northumbriaproms.domain.Procedure;
import com.noesisinformatica.northumbriaproms.service.ProcedureService;
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
 * REST controller for managing Procedure.
 */
@RestController
@RequestMapping("/api")
public class ProcedureResource {

    private final Logger log = LoggerFactory.getLogger(ProcedureResource.class);

    private static final String ENTITY_NAME = "procedure";

    private final ProcedureService procedureService;

    public ProcedureResource(ProcedureService procedureService) {
        this.procedureService = procedureService;
    }

    /**
     * POST  /procedures : Create a new procedure.
     *
     * @param procedure the procedure to create
     * @return the ResponseEntity with status 201 (Created) and with body the new procedure, or with status 400 (Bad Request) if the procedure has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/procedures")
    @Timed
    public ResponseEntity<Procedure> createProcedure(@Valid @RequestBody Procedure procedure) throws URISyntaxException {
        log.debug("REST request to save Procedure : {}", procedure);
        if (procedure.getId() != null) {
            throw new BadRequestAlertException("A new procedure cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Procedure result = procedureService.save(procedure);
        return ResponseEntity.created(new URI("/api/procedures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /procedures : Updates an existing procedure.
     *
     * @param procedure the procedure to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated procedure,
     * or with status 400 (Bad Request) if the procedure is not valid,
     * or with status 500 (Internal Server Error) if the procedure couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/procedures")
    @Timed
    public ResponseEntity<Procedure> updateProcedure(@Valid @RequestBody Procedure procedure) throws URISyntaxException {
        log.debug("REST request to update Procedure : {}", procedure);
        if (procedure.getId() == null) {
            return createProcedure(procedure);
        }
        Procedure result = procedureService.save(procedure);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, procedure.getId().toString()))
            .body(result);
    }

    /**
     * GET  /procedures : get all the procedures.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of procedures in body
     */
    @GetMapping("/procedures")
    @Timed
    public ResponseEntity<List<Procedure>> getAllProcedures(Pageable pageable) {
        log.debug("REST request to get a page of Procedures");
        Page<Procedure> page = procedureService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/procedures");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /procedures/:id : get the "id" procedure.
     *
     * @param id the id of the procedure to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the procedure, or with status 404 (Not Found)
     */
    @GetMapping("/procedures/{id}")
    @Timed
    public ResponseEntity<Procedure> getProcedure(@PathVariable Long id) {
        log.debug("REST request to get Procedure : {}", id);
        Procedure procedure = procedureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(procedure));
    }

    /**
     * DELETE  /procedures/:id : delete the "id" procedure.
     *
     * @param id the id of the procedure to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/procedures/{id}")
    @Timed
    public ResponseEntity<Void> deleteProcedure(@PathVariable Long id) {
        log.debug("REST request to delete Procedure : {}", id);
        procedureService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/procedures?query=:query : search for the procedure corresponding
     * to the query.
     *
     * @param query the query of the procedure search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/procedures")
    @Timed
    public ResponseEntity<List<Procedure>> searchProcedures(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Procedures for query {}", query);
        Page<Procedure> page = procedureService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/procedures");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
