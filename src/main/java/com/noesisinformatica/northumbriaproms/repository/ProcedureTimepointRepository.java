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
