package com.noesisinformatica.northumbriaproms.repository;

import com.noesisinformatica.northumbriaproms.domain.Procedure;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.Optional;


/**
 * Spring Data JPA repository for the Procedure entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcedureRepository extends JpaRepository<Procedure, Long> {

    Optional<Procedure> findOneByName(String name);

    Optional<Procedure> findOneByLocalCode(Integer code);

    Optional<Procedure> findOneByExternalCode(String code);
}
