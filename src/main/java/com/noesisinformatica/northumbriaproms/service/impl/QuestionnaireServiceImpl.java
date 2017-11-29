package com.noesisinformatica.northumbriaproms.service.impl;

import com.noesisinformatica.northumbriaproms.service.QuestionnaireService;
import com.noesisinformatica.northumbriaproms.domain.Questionnaire;
import com.noesisinformatica.northumbriaproms.repository.QuestionnaireRepository;
import com.noesisinformatica.northumbriaproms.repository.search.QuestionnaireSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Questionnaire.
 */
@Service
@Transactional
public class QuestionnaireServiceImpl implements QuestionnaireService{

    private final Logger log = LoggerFactory.getLogger(QuestionnaireServiceImpl.class);

    private final QuestionnaireRepository questionnaireRepository;

    private final QuestionnaireSearchRepository questionnaireSearchRepository;

    public QuestionnaireServiceImpl(QuestionnaireRepository questionnaireRepository, QuestionnaireSearchRepository questionnaireSearchRepository) {
        this.questionnaireRepository = questionnaireRepository;
        this.questionnaireSearchRepository = questionnaireSearchRepository;
    }

    /**
     * Save a questionnaire.
     *
     * @param questionnaire the entity to save
     * @return the persisted entity
     */
    @Override
    public Questionnaire save(Questionnaire questionnaire) {
        log.debug("Request to save Questionnaire : {}", questionnaire);
        Questionnaire result = questionnaireRepository.save(questionnaire);
        questionnaireSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the questionnaires.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Questionnaire> findAll(Pageable pageable) {
        log.debug("Request to get all Questionnaires");
        return questionnaireRepository.findAll(pageable);
    }

    /**
     * Get one questionnaire by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Questionnaire findOne(Long id) {
        log.debug("Request to get Questionnaire : {}", id);
        return questionnaireRepository.findOne(id);
    }

    /**
     * Delete the questionnaire by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Questionnaire : {}", id);
        questionnaireRepository.delete(id);
        questionnaireSearchRepository.delete(id);
    }

    /**
     * Search for the questionnaire corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Questionnaire> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Questionnaires for query {}", query);
        Page<Questionnaire> result = questionnaireSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
