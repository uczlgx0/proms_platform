package com.noesisinformatica.northumbriaproms.repository.search;

import com.noesisinformatica.northumbriaproms.domain.CareEvent;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CareEvent entity.
 */
public interface CareEventSearchRepository extends ElasticsearchRepository<CareEvent, Long> {
}
