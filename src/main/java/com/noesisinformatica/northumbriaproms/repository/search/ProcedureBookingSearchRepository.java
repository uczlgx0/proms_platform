package com.noesisinformatica.northumbriaproms.repository.search;

import com.noesisinformatica.northumbriaproms.domain.ProcedureBooking;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ProcedureBooking entity.
 */
public interface ProcedureBookingSearchRepository extends ElasticsearchRepository<ProcedureBooking, Long> {
}
