package com.noesisinformatica.northumbriaproms.repository.search;

import com.noesisinformatica.northumbriaproms.domain.FollowupAction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the FollowupAction entity.
 */
public interface FollowupActionSearchRepository extends ElasticsearchRepository<FollowupAction, Long> {
}
