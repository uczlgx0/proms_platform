package com.noesisinformatica.northumbriaproms.repository;

/*-
 * #%L
 * Proms Platform
 * %%
 * Copyright (C) 2017 - 2018 Termlex
 * %%
 * This software is Copyright and Intellectual Property of Termlex Inc Limited.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation as version 3 of the
 * License.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public
 * License along with this program.  If not, see
 * <https://www.gnu.org/licenses/agpl-3.0.en.html>.
 * #L%
 */

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
