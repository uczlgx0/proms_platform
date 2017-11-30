package com.noesisinformatica.northumbriaproms.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.noesisinformatica.northumbriaproms.domain.Patient;
import com.noesisinformatica.northumbriaproms.domain.ProcedureBooking;
import com.noesisinformatica.northumbriaproms.service.PatientService;
import com.noesisinformatica.northumbriaproms.service.ProcedureBookingQueryService;
import com.noesisinformatica.northumbriaproms.service.ProcedureBookingService;
import com.noesisinformatica.northumbriaproms.service.dto.ProcedureBookingCriteria;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ProcedureBooking.
 */
@RestController
@RequestMapping("/api")
public class ProcedureBookingResource {

    private final Logger log = LoggerFactory.getLogger(ProcedureBookingResource.class);

    private static final String ENTITY_NAME = "procedureBooking";

    private final ProcedureBookingService procedureBookingService;

    private final ProcedureBookingQueryService procedureBookingQueryService;
    private final PatientService patientService;

    public ProcedureBookingResource(ProcedureBookingService procedureBookingService,
                                    ProcedureBookingQueryService procedureBookingQueryService,
                                    PatientService patientService) {
        this.procedureBookingService = procedureBookingService;
        this.procedureBookingQueryService = procedureBookingQueryService;
        this.patientService = patientService;
    }

    /**
     * POST  /patient/:id/procedure-bookings : Create a new procedureBooking.
     *
     * @param procedureBooking the procedureBooking to create
     * @return the ResponseEntity with status 201 (Created) and with body the new procedureBooking, or with status 400 (Bad Request) if the procedureBooking has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/patient/{patientId}/procedure-bookings")
    @Timed
    public ResponseEntity<ProcedureBooking> createProcedureBooking(@PathVariable Long patientId, @RequestBody ProcedureBooking procedureBooking) throws URISyntaxException {
        log.debug("REST request to save ProcedureBooking : {}", procedureBooking);
        if (procedureBooking.getId() != null) {
            throw new BadRequestAlertException("A new procedureBooking cannot already have an ID", ENTITY_NAME, "idexists");
        }
        // check patient with id exists
        Patient existingPatient = patientService.findOne(patientId);
        if(existingPatient != null) {
            procedureBooking.setPatient(existingPatient);
            ProcedureBooking result = procedureBookingService.save(procedureBooking);
            existingPatient.addProcedureBookings(result);
            patientService.save(existingPatient);
            return ResponseEntity.created(new URI("/api/procedure-bookings/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
        } else {
            throw new BadRequestAlertException("No patient with matching id exists", ENTITY_NAME, "notfound");
        }
    }

    /**
     * PUT  /patient/:id/procedure-bookings : Updates an existing procedureBooking.
     *
     * @param procedureBooking the procedureBooking to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated procedureBooking,
     * or with status 400 (Bad Request) if the procedureBooking is not valid,
     * or with status 500 (Internal Server Error) if the procedureBooking couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/patient/{patientId}/procedure-bookings")
    @Timed
    public ResponseEntity<ProcedureBooking> updateProcedureBooking(@PathVariable Long patientId, @RequestBody ProcedureBooking procedureBooking) throws URISyntaxException {
        log.debug("REST request to update ProcedureBooking : {}", procedureBooking);
        if (procedureBooking.getId() == null) {
            return createProcedureBooking(patientId, procedureBooking);
        }
        // check patient with id exists
        Patient existingPatient = patientService.findOne(patientId);
        if(existingPatient != null) {
            ProcedureBooking result = procedureBookingService.save(procedureBooking);
            existingPatient.addProcedureBookings(result);
            patientService.save(existingPatient);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, procedureBooking.getId().toString()))
                .body(result);
        } else {
            throw new BadRequestAlertException("No patient with matching id exists", ENTITY_NAME, "notfound");
        }
    }

    /**
     * GET  /procedure-bookings : get all the procedureBookings.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of procedureBookings in body
     */
    @GetMapping("/procedure-bookings")
    @Timed
    public ResponseEntity<List<ProcedureBooking>> getAllProcedureBookings(ProcedureBookingCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ProcedureBookings by criteria: {}", criteria);
        Page<ProcedureBooking> page = procedureBookingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/procedure-bookings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /procedure-bookings/:id : get the "id" procedureBooking.
     *
     * @param id the id of the procedureBooking to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the procedureBooking, or with status 404 (Not Found)
     */
    @GetMapping("/procedure-bookings/{id}")
    @Timed
    public ResponseEntity<ProcedureBooking> getProcedureBooking(@PathVariable Long id) {
        log.debug("REST request to get ProcedureBooking : {}", id);
        ProcedureBooking procedureBooking = procedureBookingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(procedureBooking));
    }

    /**
     * GET  /patient/:id/procedure-bookings/ : get the procedureBookings for patient with given id.
     *
     * @param patientId the id of the patient to retrieve procedureBooking for
     * @return the ResponseEntity with status 200 (OK) and with body the procedureBooking, or with status 404 (Not Found)
     */
    @GetMapping("/patient/{patientId}/procedure-bookings")
    @Timed
    public ResponseEntity<List<ProcedureBooking>> getProcedureBooking(@PathVariable Long patientId, Pageable pageable) {
        log.debug("REST request to get ProcedureBooking for patient : {}", patientId);
        // check patient with id exists
        Patient existingPatient = patientService.findOne(patientId);
        if(existingPatient != null) {
            Page<ProcedureBooking> page = procedureBookingService.findAllByPatient(existingPatient, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/patient/" + patientId + "/procedure-bookings");
            return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
        } else {
            throw new BadRequestAlertException("No patient with matching id exists", ENTITY_NAME, "notfound");
        }
    }

    /**
     * DELETE  /procedure-bookings/:id : delete the "id" procedureBooking.
     *
     * @param id the id of the procedureBooking to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/procedure-bookings/{id}")
    @Timed
    public ResponseEntity<Void> deleteProcedureBooking(@PathVariable Long id) {
        log.debug("REST request to delete ProcedureBooking : {}", id);
        procedureBookingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/procedure-bookings?query=:query : search for the procedureBooking corresponding
     * to the query.
     *
     * @param query the query of the procedureBooking search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/procedure-bookings")
    @Timed
    public ResponseEntity<List<ProcedureBooking>> searchProcedureBookings(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ProcedureBookings for query {}", query);
        Page<ProcedureBooking> page = procedureBookingService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/procedure-bookings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
