package com.noesisinformatica.northumbriaproms.service;

import com.noesisinformatica.northumbriaproms.domain.Procedure;
import com.noesisinformatica.northumbriaproms.domain.Procedurelink;
import com.noesisinformatica.northumbriaproms.domain.Questionnaire;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Procedurelink.
 */
public interface ProcedurelinkService {

    /**
     * Save a procedurelink.
     *
     * @param procedurelink the entity to save
     * @return the persisted entity
     */
    Procedurelink save(Procedurelink procedurelink);

    /**
     * Get all the procedurelinks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Procedurelink> findAll(Pageable pageable);

    /**
     * Get all the {@link Procedurelink}s associated with a procedure.
     *
     * @param procedure the procedure
     * @return the list of entities
     */
    List<Procedurelink> findAllByProcedure(Procedure procedure);

    /**
     * Get all the {@link com.noesisinformatica.northumbriaproms.domain.Questionnaire}s associated with a procedure.
     *
     * @param procedureId the id of the procedure
     * @return the list of entities
     */
    List<Questionnaire> findAllQuestionnairesByProcedureId(Long procedureId);

    /**
     * Get all the {@link com.noesisinformatica.northumbriaproms.domain.Questionnaire}s associated with a procedure.
     *
     * @param localCode the local code of procedure
     * @return the list of entities
     */
    List<Questionnaire> findAllQuestionnairesByProcedureLocalCode(Integer localCode);

    /**
     * Get the "id" procedurelink.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Procedurelink findOne(Long id);

    /**
     * Delete the "id" procedurelink.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the procedurelink corresponding to the query.
     *
     * @param query the query of the search
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Procedurelink> search(String query, Pageable pageable);
}
