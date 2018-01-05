package com.noesisinformatica.northumbriaproms.repository;

import com.noesisinformatica.northumbriaproms.domain.ProcedureTimepoint;
import com.noesisinformatica.northumbriaproms.domain.Questionnaire;
import com.noesisinformatica.northumbriaproms.domain.Timepoint;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the ProcedureTimepoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcedureTimepointRepository extends JpaRepository<ProcedureTimepoint, Long> {

    @Query("select proceduretimepoint.timepoint from ProcedureTimepoint proceduretimepoint where proceduretimepoint.procedure.localCode = :localCode")
    List<Timepoint> findAllTimepointsByProcedureLocalCode(@Param("localCode") Integer localCode);
}
