package com.noesisinformatica.northumbriaproms.repository;

import com.noesisinformatica.northumbriaproms.domain.Questionnaire;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.Optional;


/**
 * Spring Data JPA repository for the Questionnaire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionnaireRepository extends JpaRepository<Questionnaire, Long> {

    Optional<Questionnaire> findOneByName(String name);
}
