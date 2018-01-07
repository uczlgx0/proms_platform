package com.noesisinformatica.northumbriaproms.repository;

import com.noesisinformatica.northumbriaproms.domain.CareEvent;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the CareEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CareEventRepository extends JpaRepository<CareEvent, Long>, JpaSpecificationExecutor<CareEvent> {

    List<CareEvent> findAllByFollowupPlanId(Long planId);

    List<CareEvent> findAllByPatientId(Long patientId);
}
