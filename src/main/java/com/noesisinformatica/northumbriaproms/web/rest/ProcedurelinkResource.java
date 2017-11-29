package com.noesisinformatica.northumbriaproms.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.noesisinformatica.northumbriaproms.domain.Procedurelink;
import com.noesisinformatica.northumbriaproms.service.ProcedurelinkService;
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
 * REST controller for managing Procedurelink.
 */
@RestController
@RequestMapping("/api")
public class ProcedurelinkResource {

    private final Logger log = LoggerFactory.getLogger(ProcedurelinkResource.class);

    private static final String ENTITY_NAME = "procedurelink";

    private final ProcedurelinkService procedurelinkService;

    public ProcedurelinkResource(ProcedurelinkService procedurelinkService) {
        this.procedurelinkService = procedurelinkService;
    }

    /**
     * POST  /procedurelinks : Create a new procedurelink.
     *
     * @param procedurelink the procedurelink to create
     * @return the ResponseEntity with status 201 (Created) and with body the new procedurelink, or with status 400 (Bad Request) if the procedurelink has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/procedurelinks")
    @Timed
    public ResponseEntity<Procedurelink> createProcedurelink(@Valid @RequestBody Procedurelink procedurelink) throws URISyntaxException {
        log.debug("REST request to save Procedurelink : {}", procedurelink);
        if (procedurelink.getId() != null) {
            throw new BadRequestAlertException("A new procedurelink cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Procedurelink result = procedurelinkService.save(procedurelink);
        return ResponseEntity.created(new URI("/api/procedurelinks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /procedurelinks : Updates an existing procedurelink.
     *
     * @param procedurelink the procedurelink to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated procedurelink,
     * or with status 400 (Bad Request) if the procedurelink is not valid,
     * or with status 500 (Internal Server Error) if the procedurelink couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/procedurelinks")
    @Timed
    public ResponseEntity<Procedurelink> updateProcedurelink(@Valid @RequestBody Procedurelink procedurelink) throws URISyntaxException {
        log.debug("REST request to update Procedurelink : {}", procedurelink);
        if (procedurelink.getId() == null) {
            return createProcedurelink(procedurelink);
        }
        Procedurelink result = procedurelinkService.save(procedurelink);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, procedurelink.getId().toString()))
            .body(result);
    }

    /**
     * GET  /procedurelinks : get all the procedurelinks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of procedurelinks in body
     */
    @GetMapping("/procedurelinks")
    @Timed
    public ResponseEntity<List<Procedurelink>> getAllProcedurelinks(Pageable pageable) {
        log.debug("REST request to get a page of Procedurelinks");
        Page<Procedurelink> page = procedurelinkService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/procedurelinks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /procedurelinks/:id : get the "id" procedurelink.
     *
     * @param id the id of the procedurelink to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the procedurelink, or with status 404 (Not Found)
     */
    @GetMapping("/procedurelinks/{id}")
    @Timed
    public ResponseEntity<Procedurelink> getProcedurelink(@PathVariable Long id) {
        log.debug("REST request to get Procedurelink : {}", id);
        Procedurelink procedurelink = procedurelinkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(procedurelink));
    }

    /**
     * DELETE  /procedurelinks/:id : delete the "id" procedurelink.
     *
     * @param id the id of the procedurelink to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/procedurelinks/{id}")
    @Timed
    public ResponseEntity<Void> deleteProcedurelink(@PathVariable Long id) {
        log.debug("REST request to delete Procedurelink : {}", id);
        procedurelinkService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/procedurelinks?query=:query : search for the procedurelink corresponding
     * to the query.
     *
     * @param query the query of the procedurelink search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/procedurelinks")
    @Timed
    public ResponseEntity<List<Procedurelink>> searchProcedurelinks(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Procedurelinks for query {}", query);
        Page<Procedurelink> page = procedurelinkService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/procedurelinks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
