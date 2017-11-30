package com.noesisinformatica.northumbriaproms.service.impl;

import com.noesisinformatica.northumbriaproms.service.FollowupActionService;
import com.noesisinformatica.northumbriaproms.domain.FollowupAction;
import com.noesisinformatica.northumbriaproms.repository.FollowupActionRepository;
import com.noesisinformatica.northumbriaproms.repository.search.FollowupActionSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing FollowupAction.
 */
@Service
@Transactional
public class FollowupActionServiceImpl implements FollowupActionService{

    private final Logger log = LoggerFactory.getLogger(FollowupActionServiceImpl.class);

    private final FollowupActionRepository followupActionRepository;

    private final FollowupActionSearchRepository followupActionSearchRepository;

    public FollowupActionServiceImpl(FollowupActionRepository followupActionRepository, FollowupActionSearchRepository followupActionSearchRepository) {
        this.followupActionRepository = followupActionRepository;
        this.followupActionSearchRepository = followupActionSearchRepository;
    }

    /**
     * Save a followupAction.
     *
     * @param followupAction the entity to save
     * @return the persisted entity
     */
    @Override
    public FollowupAction save(FollowupAction followupAction) {
        log.debug("Request to save FollowupAction : {}", followupAction);
        FollowupAction result = followupActionRepository.save(followupAction);
        followupActionSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the followupActions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FollowupAction> findAll(Pageable pageable) {
        log.debug("Request to get all FollowupActions");
        return followupActionRepository.findAll(pageable);
    }

    /**
     * Get one followupAction by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public FollowupAction findOne(Long id) {
        log.debug("Request to get FollowupAction : {}", id);
        return followupActionRepository.findOne(id);
    }

    /**
     * Delete the followupAction by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FollowupAction : {}", id);
        followupActionRepository.delete(id);
        followupActionSearchRepository.delete(id);
    }

    /**
     * Search for the followupAction corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FollowupAction> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of FollowupActions for query {}", query);
        Page<FollowupAction> result = followupActionSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
