package com.noesisinformatica.northumbriaproms.repository;

import com.noesisinformatica.northumbriaproms.domain.Procedure;
import com.noesisinformatica.northumbriaproms.domain.Procedurelink;
import com.noesisinformatica.northumbriaproms.domain.Questionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data JPA repository for the Procedurelink entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcedurelinkRepository extends JpaRepository<Procedurelink, Long> {

    List<Procedurelink> findAllByProcedure(Procedure procedure);

    @Query("select procedurelink.questionnaire from Procedurelink procedurelink where procedurelink.procedure.id = :procedureId")
    List<Questionnaire> findAllQuestionnairesByProcedureId(@Param("procedureId") Long procedureId);

    @Query("select procedurelink.questionnaire from Procedurelink procedurelink where procedurelink.procedure.localCode = :localCode")
    List<Questionnaire> findAllQuestionnairesByProcedureLocalCode(@Param("localCode") Integer localCode);

    Optional<Procedurelink> findOneByProcedureAndQuestionnaire(Procedure procedure, Questionnaire questionnaire);
}
