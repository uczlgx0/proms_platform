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
import com.noesisinformatica.northumbriaproms.domain.Questionnaire;
import com.noesisinformatica.northumbriaproms.service.QuestionnaireService;
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
 * REST controller for managing Questionnaire.
 */
@RestController
@RequestMapping("/api")
public class QuestionnaireResource {

    private final Logger log = LoggerFactory.getLogger(QuestionnaireResource.class);

    private static final String ENTITY_NAME = "questionnaire";

    private final QuestionnaireService questionnaireService;

    public QuestionnaireResource(QuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    /**
     * POST  /questionnaires : Create a new questionnaire.
     *
     * @param questionnaire the questionnaire to create
     * @return the ResponseEntity with status 201 (Created) and with body the new questionnaire, or with status 400 (Bad Request) if the questionnaire has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/questionnaires")
    @Timed
    public ResponseEntity<Questionnaire> createQuestionnaire(@Valid @RequestBody Questionnaire questionnaire) throws URISyntaxException {
        log.debug("REST request to save Questionnaire : {}", questionnaire);
        if (questionnaire.getId() != null) {
            throw new BadRequestAlertException("A new questionnaire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Questionnaire result = questionnaireService.save(questionnaire);
        return ResponseEntity.created(new URI("/api/questionnaires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /questionnaires : Updates an existing questionnaire.
     *
     * @param questionnaire the questionnaire to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated questionnaire,
     * or with status 400 (Bad Request) if the questionnaire is not valid,
     * or with status 500 (Internal Server Error) if the questionnaire couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/questionnaires")
    @Timed
    public ResponseEntity<Questionnaire> updateQuestionnaire(@Valid @RequestBody Questionnaire questionnaire) throws URISyntaxException {
        log.debug("REST request to update Questionnaire : {}", questionnaire);
        if (questionnaire.getId() == null) {
            return createQuestionnaire(questionnaire);
        }
        Questionnaire result = questionnaireService.save(questionnaire);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, questionnaire.getId().toString()))
            .body(result);
    }

    /**
     * GET  /questionnaires : get all the questionnaires.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of questionnaires in body
     */
    @GetMapping("/questionnaires")
    @Timed
    public ResponseEntity<List<Questionnaire>> getAllQuestionnaires(Pageable pageable) {
        log.debug("REST request to get a page of Questionnaires");
        Page<Questionnaire> page = questionnaireService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/questionnaires");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /questionnaires/:id : get the "id" questionnaire.
     *
     * @param id the id of the questionnaire to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the questionnaire, or with status 404 (Not Found)
     */
    @GetMapping("/questionnaires/{id}")
    @Timed
    public ResponseEntity<Questionnaire> getQuestionnaire(@PathVariable Long id) {
        log.debug("REST request to get Questionnaire : {}", id);
        Questionnaire questionnaire = questionnaireService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(questionnaire));
    }

    /**
     * DELETE  /questionnaires/:id : delete the "id" questionnaire.
     *
     * @param id the id of the questionnaire to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/questionnaires/{id}")
    @Timed
    public ResponseEntity<Void> deleteQuestionnaire(@PathVariable Long id) {
        log.debug("REST request to delete Questionnaire : {}", id);
        questionnaireService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/questionnaires?query=:query : search for the questionnaire corresponding
     * to the query.
     *
     * @param query the query of the questionnaire search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/questionnaires")
    @Timed
    public ResponseEntity<List<Questionnaire>> searchQuestionnaires(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Questionnaires for query {}", query);
        Page<Questionnaire> page = questionnaireService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/questionnaires");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
