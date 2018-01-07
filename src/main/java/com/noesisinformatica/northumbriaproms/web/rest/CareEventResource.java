package com.noesisinformatica.northumbriaproms.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.noesisinformatica.northumbriaproms.domain.CareEvent;
import com.noesisinformatica.northumbriaproms.service.CareEventQueryService;
import com.noesisinformatica.northumbriaproms.service.CareEventService;
import com.noesisinformatica.northumbriaproms.service.dto.CareEventCriteria;
import com.noesisinformatica.northumbriaproms.web.rest.errors.BadRequestAlertException;
import com.noesisinformatica.northumbriaproms.web.rest.util.HeaderUtil;
import com.noesisinformatica.northumbriaproms.web.rest.util.PaginationUtil;
import io.github.jhipster.service.filter.LongFilter;
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
 * REST controller for managing CareEvent.
 */
@RestController
@RequestMapping("/api")
public class CareEventResource {

    private final Logger log = LoggerFactory.getLogger(CareEventResource.class);

    private static final String ENTITY_NAME = "careEvent";

    private final CareEventService careEventService;

    private final CareEventQueryService careEventQueryService;

    public CareEventResource(CareEventService careEventService, CareEventQueryService careEventQueryService) {
        this.careEventService = careEventService;
        this.careEventQueryService = careEventQueryService;
    }

    /**
     * POST  /care-events : Create a new careEvent.
     *
     * @param careEvent the careEvent to create
     * @return the ResponseEntity with status 201 (Created) and with body the new careEvent, or with status 400 (Bad Request) if the careEvent has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/care-events")
    @Timed
    public ResponseEntity<CareEvent> createCareEvent(@Valid @RequestBody CareEvent careEvent) throws URISyntaxException {
        log.debug("REST request to save CareEvent : {}", careEvent);
        if (careEvent.getId() != null) {
            throw new BadRequestAlertException("A new careEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CareEvent result = careEventService.save(careEvent);
        return ResponseEntity.created(new URI("/api/care-events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /care-events : Updates an existing careEvent.
     *
     * @param careEvent the careEvent to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated careEvent,
     * or with status 400 (Bad Request) if the careEvent is not valid,
     * or with status 500 (Internal Server Error) if the careEvent couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/care-events")
    @Timed
    public ResponseEntity<CareEvent> updateCareEvent(@Valid @RequestBody CareEvent careEvent) throws URISyntaxException {
        log.debug("REST request to update CareEvent : {}", careEvent);
        if (careEvent.getId() == null) {
            return createCareEvent(careEvent);
        }
        CareEvent result = careEventService.save(careEvent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, careEvent.getId().toString()))
            .body(result);
    }

    /**
     * GET  /care-events : get all the careEvents.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of careEvents in body
     */
    @GetMapping("/care-events")
    @Timed
    public ResponseEntity<List<CareEvent>> getAllCareEvents(CareEventCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CareEvents by criteria: {}", criteria);
        Page<CareEvent> page = careEventQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/care-events");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /care-events/:id : get the "id" careEvent.
     *
     * @param id the id of the careEvent to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the careEvent, or with status 404 (Not Found)
     */
    @GetMapping("/care-events/{id}")
    @Timed
    public ResponseEntity<CareEvent> getCareEvent(@PathVariable Long id) {
        log.debug("REST request to get CareEvent : {}", id);
        CareEvent careEvent = careEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(careEvent));
    }

    /**
     * GET  /patient/:id/care-events : get all the care events for patient id
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of care-events in body
     */
    @GetMapping("/patient/{id}/care-events")
    @Timed
    public ResponseEntity<List<CareEvent>> getAllCareEventForPatient(@PathVariable Long id, Pageable pageable) {
        log.debug("REST request to get care-events for patient id: {}", id);
        CareEventCriteria criteria = new CareEventCriteria();
        LongFilter filter = new LongFilter();
        filter.setEquals(id);
        criteria.setPatientId(filter);
        Page<CareEvent> page = careEventQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/patient/" + id + "/care-events");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /followup-plan/:id/care-events : get all the care events for followup-plan id
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of care-events in body
     */
    @GetMapping("/followup-plan/{id}/care-events")
    @Timed
    public ResponseEntity<List<CareEvent>> getAllCareEventForFollowupPlan(@PathVariable Long id, Pageable pageable) {
        log.debug("REST request to get care-events for followup-plan id: {}", id);
        CareEventCriteria criteria = new CareEventCriteria();
        LongFilter filter = new LongFilter();
        filter.setEquals(id);
        criteria.setFollowupPlanId(filter);
        Page<CareEvent> page = careEventQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/followup-plan/" + id + "/care-events");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * DELETE  /care-events/:id : delete the "id" careEvent.
     *
     * @param id the id of the careEvent to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/care-events/{id}")
    @Timed
    public ResponseEntity<Void> deleteCareEvent(@PathVariable Long id) {
        log.debug("REST request to delete CareEvent : {}", id);
        careEventService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/care-events?query=:query : search for the careEvent corresponding
     * to the query.
     *
     * @param query the query of the careEvent search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/care-events")
    @Timed
    public ResponseEntity<List<CareEvent>> searchCareEvents(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CareEvents for query {}", query);
        Page<CareEvent> page = careEventService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/care-events");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
