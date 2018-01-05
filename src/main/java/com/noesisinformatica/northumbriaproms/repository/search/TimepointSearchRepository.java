package com.noesisinformatica.northumbriaproms.repository.search;

import com.noesisinformatica.northumbriaproms.domain.Timepoint;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Timepoint entity.
 */
public interface TimepointSearchRepository extends ElasticsearchRepository<Timepoint, Long> {
}
