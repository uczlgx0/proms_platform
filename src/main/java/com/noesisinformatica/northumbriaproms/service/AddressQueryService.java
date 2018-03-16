package com.noesisinformatica.northumbriaproms.service;

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


import com.noesisinformatica.northumbriaproms.domain.Address;
import com.noesisinformatica.northumbriaproms.domain.Address_;
import com.noesisinformatica.northumbriaproms.domain.Patient_;
import com.noesisinformatica.northumbriaproms.repository.AddressRepository;
import com.noesisinformatica.northumbriaproms.repository.search.AddressSearchRepository;
import com.noesisinformatica.northumbriaproms.service.dto.AddressCriteria;
import io.github.jhipster.service.QueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Service for executing complex queries for Address entities in the database.
 * The main input is a {@link AddressCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Address} or a {@link Page} of {@link Address} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AddressQueryService extends QueryService<Address> {

    private final Logger log = LoggerFactory.getLogger(AddressQueryService.class);


    private final AddressRepository addressRepository;

    private final AddressSearchRepository addressSearchRepository;

    public AddressQueryService(AddressRepository addressRepository, AddressSearchRepository addressSearchRepository) {
        this.addressRepository = addressRepository;
        this.addressSearchRepository = addressSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Address} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Address> findByCriteria(AddressCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Address> specification = createSpecification(criteria);
        return addressRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Address} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Address> findByCriteria(AddressCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Address> specification = createSpecification(criteria);
        return addressRepository.findAll(specification, page);
    }

    /**
     * Function to convert AddressCriteria to a {@link Specifications}
     */
    private Specifications<Address> createSpecification(AddressCriteria criteria) {
        Specifications<Address> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Address_.id));
            }
            if (criteria.getStreet() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStreet(), Address_.street));
            }
//            if (criteria.getLine() != null) {
//                specification = specification.and(buildStringSpecification(criteria.getLine(), Address_.lines));
//            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), Address_.city));
            }
            if (criteria.getCounty() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCounty(), Address_.county));
            }
            if (criteria.getPostalCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPostalCode(), Address_.postalCode));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountry(), Address_.country));
            }
            if (criteria.getPatientId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getPatientId(), Address_.patient, Patient_.id));
            }
        }
        return specification;
    }

}
