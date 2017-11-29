package com.noesisinformatica.northumbriaproms.repository;

import com.noesisinformatica.northumbriaproms.domain.Procedure;
import com.noesisinformatica.northumbriaproms.domain.Procedurelink;
import com.noesisinformatica.northumbriaproms.domain.Questionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
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

    Optional<Procedurelink> findOneByProcedureAndQuestionnaire(Procedure procedure, Questionnaire questionnaire);
}
