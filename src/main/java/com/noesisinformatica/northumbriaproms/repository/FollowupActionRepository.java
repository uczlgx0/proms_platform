package com.noesisinformatica.northumbriaproms.repository;

import com.noesisinformatica.northumbriaproms.domain.FollowupAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the FollowupAction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FollowupActionRepository extends JpaRepository<FollowupAction, Long>, JpaSpecificationExecutor<FollowupAction> {

//    Page<FollowupAction> findAllByPatientId(Long patientId);
}
