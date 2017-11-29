package com.noesisinformatica.northumbriaproms.repository.search;

import com.noesisinformatica.northumbriaproms.domain.Procedure;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Procedure entity.
 */
public interface ProcedureSearchRepository extends ElasticsearchRepository<Procedure, Long> {
}
