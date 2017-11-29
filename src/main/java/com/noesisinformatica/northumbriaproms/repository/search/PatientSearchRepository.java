package com.noesisinformatica.northumbriaproms.repository.search;

import com.noesisinformatica.northumbriaproms.domain.Patient;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Patient entity.
 */
public interface PatientSearchRepository extends ElasticsearchRepository<Patient, Long> {
}
