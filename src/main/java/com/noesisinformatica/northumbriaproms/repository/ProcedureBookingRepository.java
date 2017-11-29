package com.noesisinformatica.northumbriaproms.repository;

import com.noesisinformatica.northumbriaproms.domain.ProcedureBooking;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ProcedureBooking entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcedureBookingRepository extends JpaRepository<ProcedureBooking, Long>, JpaSpecificationExecutor<ProcedureBooking> {

}
