package com.noesisinformatica.northumbriaproms.repository.search;

import com.noesisinformatica.northumbriaproms.domain.HealthcareProvider;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the HealthcareProvider entity.
 */
public interface HealthcareProviderSearchRepository extends ElasticsearchRepository<HealthcareProvider, Long> {
}
