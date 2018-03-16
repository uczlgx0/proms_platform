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
import com.noesisinformatica.northumbriaproms.domain.Address;
import com.noesisinformatica.northumbriaproms.domain.Patient;
import com.noesisinformatica.northumbriaproms.repository.AddressRepository;
import com.noesisinformatica.northumbriaproms.repository.search.AddressSearchRepository;
import com.noesisinformatica.northumbriaproms.service.AddressQueryService;
import com.noesisinformatica.northumbriaproms.service.AddressService;
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
 * Test class for the AddressResource REST controller.
 *
 * @see AddressResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NorthumbriapromsApp.class)
public class AddressResourceIntTest {

    private static final String DEFAULT_STREET = "AAAAAAAAAA";
    private static final String UPDATED_STREET = "BBBBBBBBBB";

    private static final String DEFAULT_LINE = "AAAAAAAAAA";
    private static final String UPDATED_LINE = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTY = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressService addressService;

    @Autowired
    private AddressSearchRepository addressSearchRepository;

    @Autowired
    private AddressQueryService addressQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAddressMockMvc;

    private Address address;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AddressResource addressResource = new AddressResource(addressService, addressQueryService);
        this.restAddressMockMvc = MockMvcBuilders.standaloneSetup(addressResource)
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
    public static Address createEntity(EntityManager em) {
        Address address = new Address()
            .street(DEFAULT_STREET)
            .line(DEFAULT_LINE)
            .city(DEFAULT_CITY)
            .county(DEFAULT_COUNTY)
            .postalCode(DEFAULT_POSTAL_CODE)
            .country(DEFAULT_COUNTRY);
        return address;
    }

    @Before
    public void initTest() {
        addressSearchRepository.deleteAll();
        address = createEntity(em);
    }

    @Test
    @Transactional
    public void createAddress() throws Exception {
        int databaseSizeBeforeCreate = addressRepository.findAll().size();

        // Create the Address
        restAddressMockMvc.perform(post("/api/addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(address)))
            .andExpect(status().isCreated());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeCreate + 1);
        Address testAddress = addressList.get(addressList.size() - 1);
        assertThat(testAddress.getStreet()).isEqualTo(DEFAULT_STREET);
        assertThat(testAddress.getLines()).contains(DEFAULT_LINE);
        assertThat(testAddress.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testAddress.getCounty()).isEqualTo(DEFAULT_COUNTY);
        assertThat(testAddress.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testAddress.getCountry()).isEqualTo(DEFAULT_COUNTRY);

        // Validate the Address in Elasticsearch
        Address addressEs = addressSearchRepository.findOne(testAddress.getId());
        assertThat(addressEs).isEqualToComparingFieldByField(testAddress);
    }

    @Test
    @Transactional
    public void createAddressWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = addressRepository.findAll().size();

        // Create the Address with an existing ID
        address.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAddressMockMvc.perform(post("/api/addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(address)))
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAddresses() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList
        restAddressMockMvc.perform(get("/api/addresses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(address.getId().intValue())))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET.toString())))
            .andExpect(jsonPath("$.[*].line").value(hasItem(DEFAULT_LINE.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].county").value(hasItem(DEFAULT_COUNTY.toString())))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())));
    }

    @Test
    @Transactional
    public void getAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get the address
        restAddressMockMvc.perform(get("/api/addresses/{id}", address.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(address.getId().intValue()))
            .andExpect(jsonPath("$.street").value(DEFAULT_STREET.toString()))
            .andExpect(jsonPath("$.line").value(DEFAULT_LINE.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.county").value(DEFAULT_COUNTY.toString()))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()));
    }

    @Test
    @Transactional
    public void getAllAddressesByStreetIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where street equals to DEFAULT_STREET
        defaultAddressShouldBeFound("street.equals=" + DEFAULT_STREET);

        // Get all the addressList where street equals to UPDATED_STREET
        defaultAddressShouldNotBeFound("street.equals=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    public void getAllAddressesByStreetIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where street in DEFAULT_STREET or UPDATED_STREET
        defaultAddressShouldBeFound("street.in=" + DEFAULT_STREET + "," + UPDATED_STREET);

        // Get all the addressList where street equals to UPDATED_STREET
        defaultAddressShouldNotBeFound("street.in=" + UPDATED_STREET);
    }

    @Test
    @Transactional
    public void getAllAddressesByStreetIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where street is not null
        defaultAddressShouldBeFound("street.specified=true");

        // Get all the addressList where street is null
        defaultAddressShouldNotBeFound("street.specified=false");
    }

    @Test
    @Transactional
    public void getAllAddressesByLineIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where line equals to DEFAULT_LINE
        defaultAddressShouldBeFound("line.equals=" + DEFAULT_LINE);

        // Get all the addressList where line equals to UPDATED_LINE
        defaultAddressShouldNotBeFound("line.equals=" + UPDATED_LINE);
    }

    @Test
    @Transactional
    public void getAllAddressesByLineIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where line in DEFAULT_LINE or UPDATED_LINE
        defaultAddressShouldBeFound("line.in=" + DEFAULT_LINE + "," + UPDATED_LINE);

        // Get all the addressList where line equals to UPDATED_LINE
        defaultAddressShouldNotBeFound("line.in=" + UPDATED_LINE);
    }

    @Test
    @Transactional
    public void getAllAddressesByLineIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where line is not null
        defaultAddressShouldBeFound("line.specified=true");

        // Get all the addressList where line is null
        defaultAddressShouldNotBeFound("line.specified=false");
    }

    @Test
    @Transactional
    public void getAllAddressesByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where city equals to DEFAULT_CITY
        defaultAddressShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the addressList where city equals to UPDATED_CITY
        defaultAddressShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllAddressesByCityIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where city in DEFAULT_CITY or UPDATED_CITY
        defaultAddressShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the addressList where city equals to UPDATED_CITY
        defaultAddressShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllAddressesByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where city is not null
        defaultAddressShouldBeFound("city.specified=true");

        // Get all the addressList where city is null
        defaultAddressShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    public void getAllAddressesByCountyIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where county equals to DEFAULT_COUNTY
        defaultAddressShouldBeFound("county.equals=" + DEFAULT_COUNTY);

        // Get all the addressList where county equals to UPDATED_COUNTY
        defaultAddressShouldNotBeFound("county.equals=" + UPDATED_COUNTY);
    }

    @Test
    @Transactional
    public void getAllAddressesByCountyIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where county in DEFAULT_COUNTY or UPDATED_COUNTY
        defaultAddressShouldBeFound("county.in=" + DEFAULT_COUNTY + "," + UPDATED_COUNTY);

        // Get all the addressList where county equals to UPDATED_COUNTY
        defaultAddressShouldNotBeFound("county.in=" + UPDATED_COUNTY);
    }

    @Test
    @Transactional
    public void getAllAddressesByCountyIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where county is not null
        defaultAddressShouldBeFound("county.specified=true");

        // Get all the addressList where county is null
        defaultAddressShouldNotBeFound("county.specified=false");
    }

    @Test
    @Transactional
    public void getAllAddressesByPostalCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where postalCode equals to DEFAULT_POSTAL_CODE
        defaultAddressShouldBeFound("postalCode.equals=" + DEFAULT_POSTAL_CODE);

        // Get all the addressList where postalCode equals to UPDATED_POSTAL_CODE
        defaultAddressShouldNotBeFound("postalCode.equals=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    public void getAllAddressesByPostalCodeIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where postalCode in DEFAULT_POSTAL_CODE or UPDATED_POSTAL_CODE
        defaultAddressShouldBeFound("postalCode.in=" + DEFAULT_POSTAL_CODE + "," + UPDATED_POSTAL_CODE);

        // Get all the addressList where postalCode equals to UPDATED_POSTAL_CODE
        defaultAddressShouldNotBeFound("postalCode.in=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    public void getAllAddressesByPostalCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where postalCode is not null
        defaultAddressShouldBeFound("postalCode.specified=true");

        // Get all the addressList where postalCode is null
        defaultAddressShouldNotBeFound("postalCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllAddressesByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where country equals to DEFAULT_COUNTRY
        defaultAddressShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the addressList where country equals to UPDATED_COUNTRY
        defaultAddressShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllAddressesByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultAddressShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the addressList where country equals to UPDATED_COUNTRY
        defaultAddressShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    public void getAllAddressesByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where country is not null
        defaultAddressShouldBeFound("country.specified=true");

        // Get all the addressList where country is null
        defaultAddressShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    public void getAllAddressesByPatientIsEqualToSomething() throws Exception {
        // Initialize the database
        Patient patient = PatientResourceIntTest.createEntity(em);
        em.persist(patient);
        em.flush();
        address.setPatient(patient);
        addressRepository.saveAndFlush(address);
        Long patientId = patient.getId();

        // Get all the addressList where patient equals to patientId
        defaultAddressShouldBeFound("patientId.equals=" + patientId);

        // Get all the addressList where patient equals to patientId + 1
        defaultAddressShouldNotBeFound("patientId.equals=" + (patientId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultAddressShouldBeFound(String filter) throws Exception {
        restAddressMockMvc.perform(get("/api/addresses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(address.getId().intValue())))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET.toString())))
            .andExpect(jsonPath("$.[*].line").value(hasItem(DEFAULT_LINE.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].county").value(hasItem(DEFAULT_COUNTY.toString())))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultAddressShouldNotBeFound(String filter) throws Exception {
        restAddressMockMvc.perform(get("/api/addresses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingAddress() throws Exception {
        // Get the address
        restAddressMockMvc.perform(get("/api/addresses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAddress() throws Exception {
        // Initialize the database
        addressService.save(address);

        int databaseSizeBeforeUpdate = addressRepository.findAll().size();

        // Update the address
        Address updatedAddress = addressRepository.findOne(address.getId());
        // Disconnect from session so that the updates on updatedAddress are not directly saved in db
        em.detach(updatedAddress);
        updatedAddress
            .street(UPDATED_STREET)
            .city(UPDATED_CITY)
            .county(UPDATED_COUNTY)
            .postalCode(UPDATED_POSTAL_CODE)
            .country(UPDATED_COUNTRY);
        updatedAddress.addLine(UPDATED_LINE);

        restAddressMockMvc.perform(put("/api/addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAddress)))
            .andExpect(status().isOk());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
        Address testAddress = addressList.get(addressList.size() - 1);
        assertThat(testAddress.getStreet()).isEqualTo(UPDATED_STREET);
        assertThat(testAddress.getLines()).contains(UPDATED_LINE);
        assertThat(testAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testAddress.getCounty()).isEqualTo(UPDATED_COUNTY);
        assertThat(testAddress.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testAddress.getCountry()).isEqualTo(UPDATED_COUNTRY);

        // Validate the Address in Elasticsearch
        Address addressEs = addressSearchRepository.findOne(testAddress.getId());
        assertThat(addressEs).isEqualToComparingFieldByField(testAddress);
    }

    @Test
    @Transactional
    public void updateNonExistingAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();

        // Create the Address

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAddressMockMvc.perform(put("/api/addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(address)))
            .andExpect(status().isCreated());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAddress() throws Exception {
        // Initialize the database
        addressService.save(address);

        int databaseSizeBeforeDelete = addressRepository.findAll().size();

        // Get the address
        restAddressMockMvc.perform(delete("/api/addresses/{id}", address.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean addressExistsInEs = addressSearchRepository.exists(address.getId());
        assertThat(addressExistsInEs).isFalse();

        // Validate the database is empty
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAddress() throws Exception {
        // Initialize the database
        addressService.save(address);

        // Search the address
        restAddressMockMvc.perform(get("/api/_search/addresses?query=id:" + address.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(address.getId().intValue())))
            .andExpect(jsonPath("$.[*].street").value(hasItem(DEFAULT_STREET.toString())))
            .andExpect(jsonPath("$.[*].line").value(hasItem(DEFAULT_LINE.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
            .andExpect(jsonPath("$.[*].county").value(hasItem(DEFAULT_COUNTY.toString())))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Address.class);
        Address address1 = new Address();
        address1.setId(1L);
        Address address2 = new Address();
        address2.setId(address1.getId());
        assertThat(address1).isEqualTo(address2);
        address2.setId(2L);
        assertThat(address1).isNotEqualTo(address2);
        address1.setId(null);
        assertThat(address1).isNotEqualTo(address2);
    }
}
