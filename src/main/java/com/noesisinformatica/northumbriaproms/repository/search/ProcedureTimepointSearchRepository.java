package com.noesisinformatica.northumbriaproms.repository.search;

import com.noesisinformatica.northumbriaproms.domain.ProcedureTimepoint;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ProcedureTimepoint entity.
 */
public interface ProcedureTimepointSearchRepository extends ElasticsearchRepository<ProcedureTimepoint, Long> {
}
