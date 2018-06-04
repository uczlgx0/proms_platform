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

import com.noesisinformatica.northumbriaproms.domain.HealthcareProvider;
import com.noesisinformatica.northumbriaproms.repository.HealthcareProviderRepository;
import com.noesisinformatica.northumbriaproms.service.HealthcareProviderService;
import com.noesisinformatica.northumbriaproms.repository.search.HealthcareProviderSearchRepository;
import com.noesisinformatica.northumbriaproms.web.rest.errors.ExceptionTranslator;
import com.noesisinformatica.northumbriaproms.service.dto.HealthcareProviderCriteria;
import com.noesisinformatica.northumbriaproms.service.HealthcareProviderQueryService;

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
 * Test class for the HealthcareProviderResource REST controller.
 *
 * @see HealthcareProviderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NorthumbriapromsApp.class)
public class HealthcareProviderResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private HealthcareProviderRepository healthcareProviderRepository;

    @Autowired
    private HealthcareProviderService healthcareProviderService;

    @Autowired
    private HealthcareProviderSearchRepository healthcareProviderSearchRepository;

    @Autowired
    private HealthcareProviderQueryService healthcareProviderQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restHealthcareProviderMockMvc;

    private HealthcareProvider healthcareProvider;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HealthcareProviderResource healthcareProviderResource = new HealthcareProviderResource(healthcareProviderService, healthcareProviderQueryService);
        this.restHealthcareProviderMockMvc = MockMvcBuilders.standaloneSetup(healthcareProviderResource)
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
    public static HealthcareProvider createEntity(EntityManager em) {
        HealthcareProvider healthcareProvider = new HealthcareProvider()
            .name(DEFAULT_NAME);
        return healthcareProvider;
    }

    @Before
    public void initTest() {
        healthcareProviderSearchRepository.deleteAll();
        healthcareProvider = createEntity(em);
    }

    @Test
    @Transactional
    public void createHealthcareProvider() throws Exception {
        int databaseSizeBeforeCreate = healthcareProviderRepository.findAll().size();

        // Create the HealthcareProvider
        restHealthcareProviderMockMvc.perform(post("/api/healthcare-providers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(healthcareProvider)))
            .andExpect(status().isCreated());

        // Validate the HealthcareProvider in the database
        List<HealthcareProvider> healthcareProviderList = healthcareProviderRepository.findAll();
        assertThat(healthcareProviderList).hasSize(databaseSizeBeforeCreate + 1);
        HealthcareProvider testHealthcareProvider = healthcareProviderList.get(healthcareProviderList.size() - 1);
        assertThat(testHealthcareProvider.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the HealthcareProvider in Elasticsearch
        HealthcareProvider healthcareProviderEs = healthcareProviderSearchRepository.findOne(testHealthcareProvider.getId());
        assertThat(healthcareProviderEs).isEqualToComparingFieldByField(testHealthcareProvider);
    }

    @Test
    @Transactional
    public void createHealthcareProviderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = healthcareProviderRepository.findAll().size();

        // Create the HealthcareProvider with an existing ID
        healthcareProvider.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHealthcareProviderMockMvc.perform(post("/api/healthcare-providers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(healthcareProvider)))
            .andExpect(status().isBadRequest());

        // Validate the HealthcareProvider in the database
        List<HealthcareProvider> healthcareProviderList = healthcareProviderRepository.findAll();
        assertThat(healthcareProviderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = healthcareProviderRepository.findAll().size();
        // set the field null
        healthcareProvider.setName(null);

        // Create the HealthcareProvider, which fails.

        restHealthcareProviderMockMvc.perform(post("/api/healthcare-providers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(healthcareProvider)))
            .andExpect(status().isBadRequest());

        List<HealthcareProvider> healthcareProviderList = healthcareProviderRepository.findAll();
        assertThat(healthcareProviderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHealthcareProviders() throws Exception {
        // Initialize the database
        healthcareProviderRepository.saveAndFlush(healthcareProvider);

        // Get all the healthcareProviderList
        restHealthcareProviderMockMvc.perform(get("/api/healthcare-providers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(healthcareProvider.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getHealthcareProvider() throws Exception {
        // Initialize the database
        healthcareProviderRepository.saveAndFlush(healthcareProvider);

        // Get the healthcareProvider
        restHealthcareProviderMockMvc.perform(get("/api/healthcare-providers/{id}", healthcareProvider.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(healthcareProvider.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllHealthcareProvidersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        healthcareProviderRepository.saveAndFlush(healthcareProvider);

        // Get all the healthcareProviderList where name equals to DEFAULT_NAME
        defaultHealthcareProviderShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the healthcareProviderList where name equals to UPDATED_NAME
        defaultHealthcareProviderShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllHealthcareProvidersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        healthcareProviderRepository.saveAndFlush(healthcareProvider);

        // Get all the healthcareProviderList where name in DEFAULT_NAME or UPDATED_NAME
        defaultHealthcareProviderShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the healthcareProviderList where name equals to UPDATED_NAME
        defaultHealthcareProviderShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllHealthcareProvidersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        healthcareProviderRepository.saveAndFlush(healthcareProvider);

        // Get all the healthcareProviderList where name is not null
        defaultHealthcareProviderShouldBeFound("name.specified=true");

        // Get all the healthcareProviderList where name is null
        defaultHealthcareProviderShouldNotBeFound("name.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultHealthcareProviderShouldBeFound(String filter) throws Exception {
        restHealthcareProviderMockMvc.perform(get("/api/healthcare-providers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(healthcareProvider.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultHealthcareProviderShouldNotBeFound(String filter) throws Exception {
        restHealthcareProviderMockMvc.perform(get("/api/healthcare-providers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingHealthcareProvider() throws Exception {
        // Get the healthcareProvider
        restHealthcareProviderMockMvc.perform(get("/api/healthcare-providers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHealthcareProvider() throws Exception {
        // Initialize the database
        healthcareProviderService.save(healthcareProvider);

        int databaseSizeBeforeUpdate = healthcareProviderRepository.findAll().size();

        // Update the healthcareProvider
        HealthcareProvider updatedHealthcareProvider = healthcareProviderRepository.findOne(healthcareProvider.getId());
        // Disconnect from session so that the updates on updatedHealthcareProvider are not directly saved in db
        em.detach(updatedHealthcareProvider);
        updatedHealthcareProvider
            .name(UPDATED_NAME);

        restHealthcareProviderMockMvc.perform(put("/api/healthcare-providers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHealthcareProvider)))
            .andExpect(status().isOk());

        // Validate the HealthcareProvider in the database
        List<HealthcareProvider> healthcareProviderList = healthcareProviderRepository.findAll();
        assertThat(healthcareProviderList).hasSize(databaseSizeBeforeUpdate);
        HealthcareProvider testHealthcareProvider = healthcareProviderList.get(healthcareProviderList.size() - 1);
        assertThat(testHealthcareProvider.getName()).isEqualTo(UPDATED_NAME);

        // Validate the HealthcareProvider in Elasticsearch
        HealthcareProvider healthcareProviderEs = healthcareProviderSearchRepository.findOne(testHealthcareProvider.getId());
        assertThat(healthcareProviderEs).isEqualToComparingFieldByField(testHealthcareProvider);
    }

    @Test
    @Transactional
    public void updateNonExistingHealthcareProvider() throws Exception {
        int databaseSizeBeforeUpdate = healthcareProviderRepository.findAll().size();

        // Create the HealthcareProvider

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHealthcareProviderMockMvc.perform(put("/api/healthcare-providers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(healthcareProvider)))
            .andExpect(status().isCreated());

        // Validate the HealthcareProvider in the database
        List<HealthcareProvider> healthcareProviderList = healthcareProviderRepository.findAll();
        assertThat(healthcareProviderList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteHealthcareProvider() throws Exception {
        // Initialize the database
        healthcareProviderService.save(healthcareProvider);

        int databaseSizeBeforeDelete = healthcareProviderRepository.findAll().size();

        // Get the healthcareProvider
        restHealthcareProviderMockMvc.perform(delete("/api/healthcare-providers/{id}", healthcareProvider.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean healthcareProviderExistsInEs = healthcareProviderSearchRepository.exists(healthcareProvider.getId());
        assertThat(healthcareProviderExistsInEs).isFalse();

        // Validate the database is empty
        List<HealthcareProvider> healthcareProviderList = healthcareProviderRepository.findAll();
        assertThat(healthcareProviderList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchHealthcareProvider() throws Exception {
        // Initialize the database
        healthcareProviderService.save(healthcareProvider);

        // Search the healthcareProvider
        restHealthcareProviderMockMvc.perform(get("/api/_search/healthcare-providers?query=id:" + healthcareProvider.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(healthcareProvider.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HealthcareProvider.class);
        HealthcareProvider healthcareProvider1 = new HealthcareProvider();
        healthcareProvider1.setId(1L);
        HealthcareProvider healthcareProvider2 = new HealthcareProvider();
        healthcareProvider2.setId(healthcareProvider1.getId());
        assertThat(healthcareProvider1).isEqualTo(healthcareProvider2);
        healthcareProvider2.setId(2L);
        assertThat(healthcareProvider1).isNotEqualTo(healthcareProvider2);
        healthcareProvider1.setId(null);
        assertThat(healthcareProvider1).isNotEqualTo(healthcareProvider2);
    }
}
