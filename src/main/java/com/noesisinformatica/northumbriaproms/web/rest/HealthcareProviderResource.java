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
import com.noesisinformatica.northumbriaproms.domain.HealthcareProvider;
import com.noesisinformatica.northumbriaproms.service.HealthcareProviderService;
import com.noesisinformatica.northumbriaproms.web.rest.errors.BadRequestAlertException;
import com.noesisinformatica.northumbriaproms.web.rest.util.HeaderUtil;
import com.noesisinformatica.northumbriaproms.web.rest.util.PaginationUtil;
import com.noesisinformatica.northumbriaproms.service.dto.HealthcareProviderCriteria;
import com.noesisinformatica.northumbriaproms.service.HealthcareProviderQueryService;
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
 * REST controller for managing HealthcareProvider.
 */
@RestController
@RequestMapping("/api")
public class HealthcareProviderResource {

    private final Logger log = LoggerFactory.getLogger(HealthcareProviderResource.class);

    private static final String ENTITY_NAME = "healthcareProvider";

    private final HealthcareProviderService healthcareProviderService;

    private final HealthcareProviderQueryService healthcareProviderQueryService;

    public HealthcareProviderResource(HealthcareProviderService healthcareProviderService, HealthcareProviderQueryService healthcareProviderQueryService) {
        this.healthcareProviderService = healthcareProviderService;
        this.healthcareProviderQueryService = healthcareProviderQueryService;
    }

    /**
     * POST  /healthcare-providers : Create a new healthcareProvider.
     *
     * @param healthcareProvider the healthcareProvider to create
     * @return the ResponseEntity with status 201 (Created) and with body the new healthcareProvider, or with status 400 (Bad Request) if the healthcareProvider has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/healthcare-providers")
    @Timed
    public ResponseEntity<HealthcareProvider> createHealthcareProvider(@Valid @RequestBody HealthcareProvider healthcareProvider) throws URISyntaxException {
        log.debug("REST request to save HealthcareProvider : {}", healthcareProvider);
        if (healthcareProvider.getId() != null) {
            throw new BadRequestAlertException("A new healthcareProvider cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HealthcareProvider result = healthcareProviderService.save(healthcareProvider);
        return ResponseEntity.created(new URI("/api/healthcare-providers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /healthcare-providers : Updates an existing healthcareProvider.
     *
     * @param healthcareProvider the healthcareProvider to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated healthcareProvider,
     * or with status 400 (Bad Request) if the healthcareProvider is not valid,
     * or with status 500 (Internal Server Error) if the healthcareProvider couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/healthcare-providers")
    @Timed
    public ResponseEntity<HealthcareProvider> updateHealthcareProvider(@Valid @RequestBody HealthcareProvider healthcareProvider) throws URISyntaxException {
        log.debug("REST request to update HealthcareProvider : {}", healthcareProvider);
        if (healthcareProvider.getId() == null) {
            return createHealthcareProvider(healthcareProvider);
        }
        HealthcareProvider result = healthcareProviderService.save(healthcareProvider);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, healthcareProvider.getId().toString()))
            .body(result);
    }

    /**
     * GET  /healthcare-providers : get all the healthcareProviders.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of healthcareProviders in body
     */
    @GetMapping("/healthcare-providers")
    @Timed
    public ResponseEntity<List<HealthcareProvider>> getAllHealthcareProviders(HealthcareProviderCriteria criteria, Pageable pageable) {
        log.debug("REST request to get HealthcareProviders by criteria: {}", criteria);
        Page<HealthcareProvider> page = healthcareProviderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/healthcare-providers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /healthcare-providers/:id : get the "id" healthcareProvider.
     *
     * @param id the id of the healthcareProvider to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the healthcareProvider, or with status 404 (Not Found)
     */
    @GetMapping("/healthcare-providers/{id}")
    @Timed
    public ResponseEntity<HealthcareProvider> getHealthcareProvider(@PathVariable Long id) {
        log.debug("REST request to get HealthcareProvider : {}", id);
        HealthcareProvider healthcareProvider = healthcareProviderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(healthcareProvider));
    }

    /**
     * DELETE  /healthcare-providers/:id : delete the "id" healthcareProvider.
     *
     * @param id the id of the healthcareProvider to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/healthcare-providers/{id}")
    @Timed
    public ResponseEntity<Void> deleteHealthcareProvider(@PathVariable Long id) {
        log.debug("REST request to delete HealthcareProvider : {}", id);
        healthcareProviderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/healthcare-providers?query=:query : search for the healthcareProvider corresponding
     * to the query.
     *
     * @param query the query of the healthcareProvider search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/healthcare-providers")
    @Timed
    public ResponseEntity<List<HealthcareProvider>> searchHealthcareProviders(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of HealthcareProviders for query {}", query);
        Page<HealthcareProvider> page = healthcareProviderService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/healthcare-providers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
