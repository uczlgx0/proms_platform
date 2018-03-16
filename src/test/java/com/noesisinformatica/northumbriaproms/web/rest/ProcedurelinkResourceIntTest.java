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

import com.noesisinformatica.northumbriaproms.NorthumbriapromsApp;

import com.noesisinformatica.northumbriaproms.domain.Procedurelink;
import com.noesisinformatica.northumbriaproms.domain.Procedure;
import com.noesisinformatica.northumbriaproms.domain.Questionnaire;
import com.noesisinformatica.northumbriaproms.repository.ProcedurelinkRepository;
import com.noesisinformatica.northumbriaproms.service.ProcedurelinkService;
import com.noesisinformatica.northumbriaproms.repository.search.ProcedurelinkSearchRepository;
import com.noesisinformatica.northumbriaproms.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.noesisinformatica.northumbriaproms.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProcedurelinkResource REST controller.
 *
 * @see ProcedurelinkResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NorthumbriapromsApp.class)
public class ProcedurelinkResourceIntTest {

    @Autowired
    private ProcedurelinkRepository procedurelinkRepository;

    @Autowired
    private ProcedurelinkService procedurelinkService;

    @Autowired
    private ProcedurelinkSearchRepository procedurelinkSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProcedurelinkMockMvc;

    private Procedurelink procedurelink;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProcedurelinkResource procedurelinkResource = new ProcedurelinkResource(procedurelinkService);
        this.restProcedurelinkMockMvc = MockMvcBuilders.standaloneSetup(procedurelinkResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Procedurelink createEntity(EntityManager em) {
        Procedurelink procedurelink = new Procedurelink();
        // Add required entity
        Procedure procedure = ProcedureResourceIntTest.createEntity(em);
        em.persist(procedure);
        em.flush();
        procedurelink.setProcedure(procedure);
        // Add required entity
        Questionnaire questionnaire = QuestionnaireResourceIntTest.createEntity(em);
        em.persist(questionnaire);
        em.flush();
        procedurelink.setQuestionnaire(questionnaire);
        return procedurelink;
    }

    @Before
    public void initTest() {
        procedurelinkSearchRepository.deleteAll();
        procedurelink = createEntity(em);
    }

    @Test
    @Transactional
    public void createProcedurelink() throws Exception {
        int databaseSizeBeforeCreate = procedurelinkRepository.findAll().size();

        // Create the Procedurelink
        restProcedurelinkMockMvc.perform(post("/api/procedurelinks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedurelink)))
            .andExpect(status().isCreated());

        // Validate the Procedurelink in the database
        List<Procedurelink> procedurelinkList = procedurelinkRepository.findAll();
        assertThat(procedurelinkList).hasSize(databaseSizeBeforeCreate + 1);
        Procedurelink testProcedurelink = procedurelinkList.get(procedurelinkList.size() - 1);

        // Validate the Procedurelink in Elasticsearch
        Procedurelink procedurelinkEs = procedurelinkSearchRepository.findOne(testProcedurelink.getId());
        assertThat(procedurelinkEs).isEqualToComparingFieldByField(testProcedurelink);
    }

    @Test
    @Transactional
    public void createProcedurelinkWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = procedurelinkRepository.findAll().size();

        // Create the Procedurelink with an existing ID
        procedurelink.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcedurelinkMockMvc.perform(post("/api/procedurelinks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedurelink)))
            .andExpect(status().isBadRequest());

        // Validate the Procedurelink in the database
        List<Procedurelink> procedurelinkList = procedurelinkRepository.findAll();
        assertThat(procedurelinkList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProcedurelinks() throws Exception {
        // Initialize the database
        procedurelinkRepository.saveAndFlush(procedurelink);

        // Get all the procedurelinkList
        restProcedurelinkMockMvc.perform(get("/api/procedurelinks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(procedurelink.getId().intValue())));
    }

    @Test
    @Transactional
    public void getProcedurelink() throws Exception {
        // Initialize the database
        procedurelinkRepository.saveAndFlush(procedurelink);

        // Get the procedurelink
        restProcedurelinkMockMvc.perform(get("/api/procedurelinks/{id}", procedurelink.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(procedurelink.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingProcedurelink() throws Exception {
        // Get the procedurelink
        restProcedurelinkMockMvc.perform(get("/api/procedurelinks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProcedurelink() throws Exception {
        // Initialize the database
        procedurelinkService.save(procedurelink);

        int databaseSizeBeforeUpdate = procedurelinkRepository.findAll().size();

        // Update the procedurelink
        Procedurelink updatedProcedurelink = procedurelinkRepository.findOne(procedurelink.getId());
        // Disconnect from session so that the updates on updatedProcedurelink are not directly saved in db
        em.detach(updatedProcedurelink);

        restProcedurelinkMockMvc.perform(put("/api/procedurelinks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProcedurelink)))
            .andExpect(status().isOk());

        // Validate the Procedurelink in the database
        List<Procedurelink> procedurelinkList = procedurelinkRepository.findAll();
        assertThat(procedurelinkList).hasSize(databaseSizeBeforeUpdate);
        Procedurelink testProcedurelink = procedurelinkList.get(procedurelinkList.size() - 1);

        // Validate the Procedurelink in Elasticsearch
        Procedurelink procedurelinkEs = procedurelinkSearchRepository.findOne(testProcedurelink.getId());
        assertThat(procedurelinkEs).isEqualToComparingFieldByField(testProcedurelink);
    }

    @Test
    @Transactional
    public void updateNonExistingProcedurelink() throws Exception {
        int databaseSizeBeforeUpdate = procedurelinkRepository.findAll().size();

        // Create the Procedurelink

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProcedurelinkMockMvc.perform(put("/api/procedurelinks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(procedurelink)))
            .andExpect(status().isCreated());

        // Validate the Procedurelink in the database
        List<Procedurelink> procedurelinkList = procedurelinkRepository.findAll();
        assertThat(procedurelinkList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProcedurelink() throws Exception {
        // Initialize the database
        procedurelinkService.save(procedurelink);

        int databaseSizeBeforeDelete = procedurelinkRepository.findAll().size();

        // Get the procedurelink
        restProcedurelinkMockMvc.perform(delete("/api/procedurelinks/{id}", procedurelink.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean procedurelinkExistsInEs = procedurelinkSearchRepository.exists(procedurelink.getId());
        assertThat(procedurelinkExistsInEs).isFalse();

        // Validate the database is empty
        List<Procedurelink> procedurelinkList = procedurelinkRepository.findAll();
        assertThat(procedurelinkList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProcedurelink() throws Exception {
        // Initialize the database
        procedurelinkService.save(procedurelink);

        // Search the procedurelink
        restProcedurelinkMockMvc.perform(get("/api/_search/procedurelinks?query=id:" + procedurelink.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(procedurelink.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Procedurelink.class);
        Procedurelink procedurelink1 = new Procedurelink();
        procedurelink1.setId(1L);
        Procedurelink procedurelink2 = new Procedurelink();
        procedurelink2.setId(procedurelink1.getId());
        assertThat(procedurelink1).isEqualTo(procedurelink2);
        procedurelink2.setId(2L);
        assertThat(procedurelink1).isNotEqualTo(procedurelink2);
        procedurelink1.setId(null);
        assertThat(procedurelink1).isNotEqualTo(procedurelink2);
    }
}
