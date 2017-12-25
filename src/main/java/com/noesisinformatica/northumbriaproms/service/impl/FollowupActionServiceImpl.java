package com.noesisinformatica.northumbriaproms.service.impl;

import com.noesisinformatica.northumbriaproms.domain.FollowupAction;
import com.noesisinformatica.northumbriaproms.repository.FollowupActionRepository;
import com.noesisinformatica.northumbriaproms.repository.search.FollowupActionSearchRepository;
import com.noesisinformatica.northumbriaproms.service.FollowupActionService;
import com.noesisinformatica.northumbriaproms.web.rest.util.QueryModel;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service Implementation for managing FollowupAction.
 */
@Service
@Transactional
public class FollowupActionServiceImpl implements FollowupActionService{

    private final Logger log = LoggerFactory.getLogger(FollowupActionServiceImpl.class);

    private final FollowupActionRepository followupActionRepository;
    private final ElasticsearchTemplate elasticsearchTemplate;
    private final FollowupActionSearchRepository followupActionSearchRepository;

    public FollowupActionServiceImpl(FollowupActionRepository followupActionRepository,
                                     FollowupActionSearchRepository followupActionSearchRepository,
                                     ElasticsearchTemplate elasticsearchTemplate) {
        this.followupActionRepository = followupActionRepository;
        this.followupActionSearchRepository = followupActionSearchRepository;
        this.elasticsearchTemplate = elasticsearchTemplate;
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
     * Return all followup actions with all categories.
     *
     *  @return the page of followup actions
     */
    @Override
    @Transactional(readOnly = true)
    public FacetedPage<FollowupAction> findAllWithCategories(Pageable pageable) {
        log.debug("Request to search for a page of Trials for page {}", pageable);
        // build and return match all query
        return getFacetedPageForQuery(QueryBuilders.matchAllQuery(), pageable);
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
    public FacetedPage<FollowupAction> search(QueryModel query, Pageable pageable) {
        log.debug("Request to search for a page of Followup Actions for query {}", query);

        // if empty query, then just return all follow up actions
        if(query == null || query.isEmpty()) {
            // build and return match all query
            return getFacetedPageForQuery(QueryBuilders.matchAllQuery(), pageable);
        }

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        BoolQueryBuilder proceduresQueryBuilder = QueryBuilders.boolQuery();
        for(String condition : query.getProcedures()) {
            proceduresQueryBuilder.should(QueryBuilders.matchQuery("followupPlan.procedureBooking.primaryProcedure", condition));
        }

        BoolQueryBuilder sourcesQueryBuilder = QueryBuilders.boolQuery();
        for(String location : query.getLocations()) {
            sourcesQueryBuilder.should(QueryBuilders.matchPhraseQuery("followupPlan.procedureBooking.hospitalSite", location));
        }

        BoolQueryBuilder consultantsQueryBuilder = QueryBuilders.boolQuery();
        for(String consultant : query.getConsultants()) {
            consultantsQueryBuilder.should(QueryBuilders.matchPhraseQuery("followupPlan.procedureBooking.consultantName", consultant));
        }

        BoolQueryBuilder patientIdsQueryBuilder = QueryBuilders.boolQuery();
        for(String id : query.getPatientIds()) {
            patientIdsQueryBuilder.should(QueryBuilders.matchQuery("patient.id", id));
        }

        BoolQueryBuilder phaseQueryBuilder = QueryBuilders.boolQuery();
        for(String phase : query.getPhases()) {
            phaseQueryBuilder.should(QueryBuilders.matchQuery("phases", phase));
        }

        BoolQueryBuilder typeQueryBuilder = QueryBuilders.boolQuery();
        for(String type : query.getTypes()) {
            typeQueryBuilder.should(QueryBuilders.matchQuery("type", type));
        }

        BoolQueryBuilder genderQueryBuilder = QueryBuilders.boolQuery();
        for(String gender : query.getGenders()) {
            genderQueryBuilder.should(QueryBuilders.matchQuery("followupPlan.patient.gender", gender));
        }

        if(query.getMinAge() != null) {
            boolQueryBuilder.must(
                QueryBuilders.boolQuery().should(QueryBuilders.rangeQuery("followupPlan.procedureBooking.patientAge").gte(query.getMinAge()))
                    .should(QueryBuilders.boolQuery().mustNot(QueryBuilders.existsQuery("followupPlan.procedureBooking.patientAge")))
            );
        }

        if(query.getMaxAge() != null) {
            boolQueryBuilder.must(
                QueryBuilders.boolQuery().should(QueryBuilders.rangeQuery("followupPlan.procedureBooking.patientAge").lte(query.getMaxAge()))
                    .should(QueryBuilders.boolQuery().mustNot(QueryBuilders.existsQuery("followupPlan.procedureBooking.patientAge")))
            );
        }

        // we only add procedures clause if there are more than 1 procedure specified
        if(query.getProcedures().size() > 0){
            boolQueryBuilder.must(proceduresQueryBuilder);
        }

        // we only add locations clause if there are more than 1 locations specified
        if(query.getLocations().size() > 0){
            boolQueryBuilder.must(sourcesQueryBuilder);
        }

        // we only add phases clause if there are more than 1 phase specified
        if (query.getPhases().size() > 0){
            boolQueryBuilder.must(phaseQueryBuilder);
        }

        // we only add consultants clause if there are more than 1 consultant specified
        if(query.getConsultants().size() > 0){
            boolQueryBuilder.must(consultantsQueryBuilder);
        }

        // we only add patient ids clause if there are more than 1 id specified
        if(query.getPatientIds().size() > 0){
            boolQueryBuilder.must(patientIdsQueryBuilder);
        }

        // we only add types clause if there are more than 1 type specified
        if(query.getTypes().size() > 0){
            boolQueryBuilder.must(typeQueryBuilder);
        }

        // we only add gender clause if there are more than 1 gender specified
        if (query.getGenders().size() > 0){
            boolQueryBuilder.must(genderQueryBuilder);
        }

        // we only add token clause if there is a token specified
        if (query.getToken() != null && query.getToken().length() > 2){
            BoolQueryBuilder tokenBuilder = QueryBuilders.boolQuery();
            tokenBuilder.should(QueryBuilders.multiMatchQuery(query.getToken(), "patient.address.*", "patient.nhsNumber", "patient.id").type(MultiMatchQueryBuilder.Type.PHRASE_PREFIX));

            boolQueryBuilder.must(tokenBuilder);
        }
        log.debug("boolQueryBuilder = " + boolQueryBuilder);
        // build and return boolean query
        return getFacetedPageForQuery(boolQueryBuilder, pageable);
    }

    private FacetedPage<FollowupAction> getFacetedPageForQuery(QueryBuilder queryBuilder, Pageable pageable) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
            .withQuery(queryBuilder)
            .withSort(getSortParameters(pageable))
            .withPageable(pageable)
            .addAggregation(new TermsBuilder("types").field("type").size(5).order(Terms.Order.term(true)))
            .addAggregation(new TermsBuilder("procedures").field("followupPlan.procedureBooking.primaryProcedure").size(100).order(Terms.Order.term(true)))
            .addAggregation(new TermsBuilder("consultants").field("followupPlan.procedureBooking.consultantName").size(100).order(Terms.Order.term(true)))
            .addAggregation(new TermsBuilder("locations").field("followupPlan.procedureBooking.hospitalSite").size(100).order(Terms.Order.term(true)))
            .addAggregation(new TermsBuilder("genders").field("followupPlan.patient.gender").size(5).order(Terms.Order.term(true)))
            .addAggregation(new TermsBuilder("phases").field("phase").size(10).order(Terms.Order.term(true)))
            .build();

        FacetedPage<FollowupAction> page = elasticsearchTemplate.queryForPage(searchQuery, FollowupAction.class);

        return page;
    }


    private FieldSortBuilder getSortParameters(Pageable pageable) {
        List<Sort.Order> orders = new ArrayList<>() ;
        if (pageable != null) {
            for (Sort.Order order : pageable.getSort()) {
                String encapsulatedProperty = "("+order.getProperty() + ")";
                orders.add(new Sort.Order(order.getDirection(), encapsulatedProperty));
            }
        }

        SortOrder sortOrder = SortOrder.ASC;
        String sortField = "name";

        if (!orders.isEmpty()) {
            Sort.Order order = orders.get(0);
            if(order != null) {
                if(order.getProperty() != null) {
                    sortField = order.getProperty();
                }
                if(order.isDescending()) {
                    sortOrder = SortOrder.DESC;
                }
            }
        }

        return SortBuilders.fieldSort(sortField).order(sortOrder).unmappedType("string");
    }
}
