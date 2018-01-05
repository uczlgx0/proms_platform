package com.noesisinformatica.northumbriaproms.repository;

import com.noesisinformatica.northumbriaproms.domain.Timepoint;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Timepoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TimepointRepository extends JpaRepository<Timepoint, Long> {

}
