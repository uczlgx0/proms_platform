package com.noesisinformatica.northumbriaproms.repository;

import com.noesisinformatica.northumbriaproms.domain.FollowupAction;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the FollowupAction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FollowupActionRepository extends JpaRepository<FollowupAction, Long>, JpaSpecificationExecutor<FollowupAction> {

}
