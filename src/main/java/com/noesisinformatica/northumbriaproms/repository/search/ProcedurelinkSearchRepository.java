package com.noesisinformatica.northumbriaproms.repository.search;

import com.noesisinformatica.northumbriaproms.domain.Procedurelink;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Procedurelink entity.
 */
public interface ProcedurelinkSearchRepository extends ElasticsearchRepository<Procedurelink, Long> {
}
