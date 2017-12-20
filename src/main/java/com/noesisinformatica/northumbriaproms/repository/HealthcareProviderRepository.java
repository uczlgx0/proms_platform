package com.noesisinformatica.northumbriaproms.repository;

import com.noesisinformatica.northumbriaproms.domain.HealthcareProvider;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the HealthcareProvider entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HealthcareProviderRepository extends JpaRepository<HealthcareProvider, Long>, JpaSpecificationExecutor<HealthcareProvider> {

}
