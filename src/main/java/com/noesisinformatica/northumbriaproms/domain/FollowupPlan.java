package com.noesisinformatica.northumbriaproms.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A FollowupPlan.
 */
@Entity
@Table(name = "followup_plan")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "followupplan")
public class FollowupPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "followupPlan")
    @JsonIgnore
    private ProcedureBooking procedureBooking;

    @ManyToOne(optional = false)
    @NotNull
    private Patient patient;

    @OneToMany(mappedBy = "followupPlan", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<FollowupAction> followupActions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProcedureBooking getProcedureBooking() {
        return procedureBooking;
    }

    public FollowupPlan procedureBooking(ProcedureBooking procedureBooking) {
        this.procedureBooking = procedureBooking;
        return this;
    }

    public void setProcedureBooking(ProcedureBooking procedureBooking) {
        this.procedureBooking = procedureBooking;
    }

    public Patient getPatient() {
        return patient;
    }

    public FollowupPlan patient(Patient patient) {
        this.patient = patient;
        return this;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Set<FollowupAction> getFollowupActions() {
        return followupActions;
    }

    public FollowupPlan followupActions(Set<FollowupAction> followupActions) {
        this.followupActions = followupActions;
        return this;
    }

    public FollowupPlan addFollowupAction(FollowupAction followupAction) {
        this.followupActions.add(followupAction);
        followupAction.setFollowupPlan(this);
        return this;
    }

    public FollowupPlan removeFollowupAction(FollowupAction followupAction) {
        this.followupActions.remove(followupAction);
        followupAction.setFollowupPlan(null);
        return this;
    }

    public void setFollowupActions(Set<FollowupAction> followupActions) {
        this.followupActions = followupActions;
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
        FollowupPlan followupPlan = (FollowupPlan) o;
        if (followupPlan.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), followupPlan.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FollowupPlan{" +
            "id=" + getId() +
            "}";
    }
}
