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
import com.noesisinformatica.northumbriaproms.domain.FollowupPlan;
import com.noesisinformatica.northumbriaproms.service.FollowupPlanService;
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
 * REST controller for managing FollowupPlan.
 */
@RestController
@RequestMapping("/api")
public class FollowupPlanResource {

    private final Logger log = LoggerFactory.getLogger(FollowupPlanResource.class);

    private static final String ENTITY_NAME = "followupPlan";

    private final FollowupPlanService followupPlanService;

    public FollowupPlanResource(FollowupPlanService followupPlanService) {
        this.followupPlanService = followupPlanService;
    }

    /**
     * POST  /followup-plans : Create a new followupPlan.
     *
     * @param followupPlan the followupPlan to create
     * @return the ResponseEntity with status 201 (Created) and with body the new followupPlan, or with status 400 (Bad Request) if the followupPlan has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/followup-plans")
    @Timed
    public ResponseEntity<FollowupPlan> createFollowupPlan(@Valid @RequestBody FollowupPlan followupPlan) throws URISyntaxException {
        log.debug("REST request to save FollowupPlan : {}", followupPlan);
        if (followupPlan.getId() != null) {
            throw new BadRequestAlertException("A new followupPlan cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FollowupPlan result = followupPlanService.save(followupPlan);
        return ResponseEntity.created(new URI("/api/followup-plans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /followup-plans : Updates an existing followupPlan.
     *
     * @param followupPlan the followupPlan to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated followupPlan,
     * or with status 400 (Bad Request) if the followupPlan is not valid,
     * or with status 500 (Internal Server Error) if the followupPlan couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/followup-plans")
    @Timed
    public ResponseEntity<FollowupPlan> updateFollowupPlan(@Valid @RequestBody FollowupPlan followupPlan) throws URISyntaxException {
        log.debug("REST request to update FollowupPlan : {}", followupPlan);
        if (followupPlan.getId() == null) {
            return createFollowupPlan(followupPlan);
        }
        FollowupPlan result = followupPlanService.save(followupPlan);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, followupPlan.getId().toString()))
            .body(result);
    }

    /**
     * GET  /followup-plans : get all the followupPlans.
     *
     * @param pageable the pagination information
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of followupPlans in body
     */
    @GetMapping("/followup-plans")
    @Timed
    public ResponseEntity<List<FollowupPlan>> getAllFollowupPlans(Pageable pageable, @RequestParam(required = false) String filter) {
        if ("procedurebooking-is-null".equals(filter)) {
            log.debug("REST request to get all FollowupPlans where procedureBooking is null");
            return new ResponseEntity<>(followupPlanService.findAllWhereProcedureBookingIsNull(),
                    HttpStatus.OK);
        }
        log.debug("REST request to get a page of FollowupPlans");
        Page<FollowupPlan> page = followupPlanService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/followup-plans");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /followup-plans/:id : get the "id" followupPlan.
     *
     * @param id the id of the followupPlan to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the followupPlan, or with status 404 (Not Found)
     */
    @GetMapping("/followup-plans/{id}")
    @Timed
    public ResponseEntity<FollowupPlan> getFollowupPlan(@PathVariable Long id) {
        log.debug("REST request to get FollowupPlan : {}", id);
        FollowupPlan followupPlan = followupPlanService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(followupPlan));
    }

    /**
     * GET  /procedure-bookings/:id/followup-plan : get the followupPlan for "id" procedure booking
     *
     * @param id the id of the procedureBooking to retrieve followupPlan for
     * @return the ResponseEntity with status 200 (OK) and with body the followupPlan, or with status 404 (Not Found)
     */
    @GetMapping("/procedure-bookings/{id}/followup-plan")
    @Timed
    public ResponseEntity<FollowupPlan> getFollowupPlanForBookingId(@PathVariable Long id) {
        log.debug("REST request to get FollowupPlan for ProcedureBooking : {}", id);
        Optional<FollowupPlan> followupPlan = followupPlanService.findOneByProcedureBookingId(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(followupPlan.get()));
    }

    /**
     * DELETE  /followup-plans/:id : delete the "id" followupPlan.
     *
     * @param id the id of the followupPlan to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/followup-plans/{id}")
    @Timed
    public ResponseEntity<Void> deleteFollowupPlan(@PathVariable Long id) {
        log.debug("REST request to delete FollowupPlan : {}", id);
        followupPlanService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/followup-plans?query=:query : search for the followupPlan corresponding
     * to the query.
     *
     * @param query the query of the followupPlan search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/followup-plans")
    @Timed
    public ResponseEntity<List<FollowupPlan>> searchFollowupPlans(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of FollowupPlans for query {}", query);
        Page<FollowupPlan> page = followupPlanService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/followup-plans");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
