package com.noesisinformatica.northumbriaproms.repository;

import com.noesisinformatica.northumbriaproms.domain.FollowupPlan;
import com.noesisinformatica.northumbriaproms.domain.Patient;
import com.noesisinformatica.northumbriaproms.domain.ProcedureBooking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.Optional;


/**
 * Spring Data JPA repository for the ProcedureBooking entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcedureBookingRepository extends JpaRepository<ProcedureBooking, Long>, JpaSpecificationExecutor<ProcedureBooking> {

    Page<ProcedureBooking> findAllByPatient(Patient patient, Pageable pageable);

    @Query("select procedurebooking.followupPlan from ProcedureBooking procedurebooking where procedurebooking.primaryProcedure = :code and procedurebooking.patient.id = : patientId")
    Optional<FollowupPlan> findOneByPatientIdAndPrimaryProcedure(@Param("patientId") Long patientId, @Param("code") String primaryProcedureCode);

}
