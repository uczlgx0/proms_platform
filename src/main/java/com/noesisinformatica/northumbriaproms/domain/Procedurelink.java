package com.noesisinformatica.northumbriaproms.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A Procedurelink.
 */
@Entity
@Table(name = "procedurelink")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "procedurelink")
public class Procedurelink implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    private Procedure procedure;

    @ManyToOne(optional = false)
    @NotNull
    private Questionnaire questionnaire;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Procedure getProcedure() {
        return procedure;
    }

    public Procedurelink procedure(Procedure procedure) {
        this.procedure = procedure;
        return this;
    }

    public void setProcedure(Procedure procedure) {
        this.procedure = procedure;
    }

    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    public Procedurelink questionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
        return this;
    }

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
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
        Procedurelink procedurelink = (Procedurelink) o;
        if (procedurelink.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), procedurelink.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Procedurelink{" +
            "id=" + getId() +
            "}";
    }
}
