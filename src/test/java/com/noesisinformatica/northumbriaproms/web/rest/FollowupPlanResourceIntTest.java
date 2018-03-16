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

import com.noesisinformatica.northumbriaproms.domain.FollowupPlan;
import com.noesisinformatica.northumbriaproms.domain.Patient;
import com.noesisinformatica.northumbriaproms.repository.FollowupPlanRepository;
import com.noesisinformatica.northumbriaproms.service.FollowupPlanService;
import com.noesisinformatica.northumbriaproms.repository.search.FollowupPlanSearchRepository;
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
 * Test class for the FollowupPlanResource REST controller.
 *
 * @see FollowupPlanResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NorthumbriapromsApp.class)
public class FollowupPlanResourceIntTest {

    @Autowired
    private FollowupPlanRepository followupPlanRepository;

    @Autowired
    private FollowupPlanService followupPlanService;

    @Autowired
    private FollowupPlanSearchRepository followupPlanSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFollowupPlanMockMvc;

    private FollowupPlan followupPlan;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FollowupPlanResource followupPlanResource = new FollowupPlanResource(followupPlanService);
        this.restFollowupPlanMockMvc = MockMvcBuilders.standaloneSetup(followupPlanResource)
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
    public static FollowupPlan createEntity(EntityManager em) {
        FollowupPlan followupPlan = new FollowupPlan();
        // Add required entity
        Patient patient = PatientResourceIntTest.createEntity(em);
        em.persist(patient);
        em.flush();
        followupPlan.setPatient(patient);
        return followupPlan;
    }

    @Before
    public void initTest() {
        followupPlanSearchRepository.deleteAll();
        followupPlan = createEntity(em);
    }

    @Test
    @Transactional
    public void createFollowupPlan() throws Exception {
        int databaseSizeBeforeCreate = followupPlanRepository.findAll().size();

        // Create the FollowupPlan
        restFollowupPlanMockMvc.perform(post("/api/followup-plans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(followupPlan)))
            .andExpect(status().isCreated());

        // Validate the FollowupPlan in the database
        List<FollowupPlan> followupPlanList = followupPlanRepository.findAll();
        assertThat(followupPlanList).hasSize(databaseSizeBeforeCreate + 1);
        FollowupPlan testFollowupPlan = followupPlanList.get(followupPlanList.size() - 1);

        // Validate the FollowupPlan in Elasticsearch
        FollowupPlan followupPlanEs = followupPlanSearchRepository.findOne(testFollowupPlan.getId());
        assertThat(followupPlanEs).isEqualToComparingFieldByField(testFollowupPlan);
    }

    @Test
    @Transactional
    public void createFollowupPlanWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = followupPlanRepository.findAll().size();

        // Create the FollowupPlan with an existing ID
        followupPlan.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFollowupPlanMockMvc.perform(post("/api/followup-plans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(followupPlan)))
            .andExpect(status().isBadRequest());

        // Validate the FollowupPlan in the database
        List<FollowupPlan> followupPlanList = followupPlanRepository.findAll();
        assertThat(followupPlanList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFollowupPlans() throws Exception {
        // Initialize the database
        followupPlanRepository.saveAndFlush(followupPlan);

        // Get all the followupPlanList
        restFollowupPlanMockMvc.perform(get("/api/followup-plans?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(followupPlan.getId().intValue())));
    }

    @Test
    @Transactional
    public void getFollowupPlan() throws Exception {
        // Initialize the database
        followupPlanRepository.saveAndFlush(followupPlan);

        // Get the followupPlan
        restFollowupPlanMockMvc.perform(get("/api/followup-plans/{id}", followupPlan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(followupPlan.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFollowupPlan() throws Exception {
        // Get the followupPlan
        restFollowupPlanMockMvc.perform(get("/api/followup-plans/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFollowupPlan() throws Exception {
        // Initialize the database
        followupPlanService.save(followupPlan);

        int databaseSizeBeforeUpdate = followupPlanRepository.findAll().size();

        // Update the followupPlan
        FollowupPlan updatedFollowupPlan = followupPlanRepository.findOne(followupPlan.getId());
        // Disconnect from session so that the updates on updatedFollowupPlan are not directly saved in db
        em.detach(updatedFollowupPlan);

        restFollowupPlanMockMvc.perform(put("/api/followup-plans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFollowupPlan)))
            .andExpect(status().isOk());

        // Validate the FollowupPlan in the database
        List<FollowupPlan> followupPlanList = followupPlanRepository.findAll();
        assertThat(followupPlanList).hasSize(databaseSizeBeforeUpdate);
        FollowupPlan testFollowupPlan = followupPlanList.get(followupPlanList.size() - 1);

        // Validate the FollowupPlan in Elasticsearch
        FollowupPlan followupPlanEs = followupPlanSearchRepository.findOne(testFollowupPlan.getId());
        assertThat(followupPlanEs).isEqualToComparingFieldByField(testFollowupPlan);
    }

    @Test
    @Transactional
    public void updateNonExistingFollowupPlan() throws Exception {
        int databaseSizeBeforeUpdate = followupPlanRepository.findAll().size();

        // Create the FollowupPlan

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFollowupPlanMockMvc.perform(put("/api/followup-plans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(followupPlan)))
            .andExpect(status().isCreated());

        // Validate the FollowupPlan in the database
        List<FollowupPlan> followupPlanList = followupPlanRepository.findAll();
        assertThat(followupPlanList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFollowupPlan() throws Exception {
        // Initialize the database
        followupPlanService.save(followupPlan);

        int databaseSizeBeforeDelete = followupPlanRepository.findAll().size();

        // Get the followupPlan
        restFollowupPlanMockMvc.perform(delete("/api/followup-plans/{id}", followupPlan.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean followupPlanExistsInEs = followupPlanSearchRepository.exists(followupPlan.getId());
        assertThat(followupPlanExistsInEs).isFalse();

        // Validate the database is empty
        List<FollowupPlan> followupPlanList = followupPlanRepository.findAll();
        assertThat(followupPlanList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFollowupPlan() throws Exception {
        // Initialize the database
        followupPlanService.save(followupPlan);

        // Search the followupPlan
        restFollowupPlanMockMvc.perform(get("/api/_search/followup-plans?query=id:" + followupPlan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(followupPlan.getId().intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FollowupPlan.class);
        FollowupPlan followupPlan1 = new FollowupPlan();
        followupPlan1.setId(1L);
        FollowupPlan followupPlan2 = new FollowupPlan();
        followupPlan2.setId(followupPlan1.getId());
        assertThat(followupPlan1).isEqualTo(followupPlan2);
        followupPlan2.setId(2L);
        assertThat(followupPlan1).isNotEqualTo(followupPlan2);
        followupPlan1.setId(null);
        assertThat(followupPlan1).isNotEqualTo(followupPlan2);
    }
}
