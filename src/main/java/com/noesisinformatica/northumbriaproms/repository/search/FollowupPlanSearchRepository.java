package com.noesisinformatica.northumbriaproms.repository.search;

import com.noesisinformatica.northumbriaproms.domain.FollowupPlan;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the FollowupPlan entity.
 */
public interface FollowupPlanSearchRepository extends ElasticsearchRepository<FollowupPlan, Long> {
}
