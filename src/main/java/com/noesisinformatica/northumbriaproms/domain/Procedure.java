package com.noesisinformatica.northumbriaproms.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A Procedure.
 */
@Entity
@Table(name = "procedures_table")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "procedure")
public class Procedure implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "external_code")
    private String externalCode;

    @Column(name = "local_code")
    private Integer localCode;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Procedure name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExternalCode() {
        return externalCode;
    }

    public Procedure externalCode(String externalCode) {
        this.externalCode = externalCode;
        return this;
    }

    public void setExternalCode(String externalCode) {
        this.externalCode = externalCode;
    }

    public Integer getLocalCode() {
        return localCode;
    }

    public Procedure localCode(Integer localCode) {
        this.localCode = localCode;
        return this;
    }

    public void setLocalCode(Integer localCode) {
        this.localCode = localCode;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Procedure procedure = (Procedure) o;
        if (procedure.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), procedure.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Procedure{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", externalCode='" + getExternalCode() + "'" +
            ", localCode=" + getLocalCode() +
            "}";
    }
}
