package com.noesisinformatica.northumbriaproms.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


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

    @OneToOne(mappedBy = "followupPlan", fetch = FetchType.EAGER)
    private ProcedureBooking procedureBooking;

    @ManyToOne(optional = false)
    @NotNull
    private Patient patient;

    @OneToMany(mappedBy = "followupPlan", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CareEvent> careEvents = new HashSet<>();

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

    public Set<CareEvent> getCareEvents() {
        return careEvents;
    }

    public FollowupPlan careEvents(Set<CareEvent> careEvents) {
        this.careEvents = careEvents;
        return this;
    }

    public FollowupPlan addCareEvent(CareEvent careEvent) {
        this.careEvents.add(careEvent);
        careEvent.setFollowupPlan(this);
        return this;
    }

    public FollowupPlan removeCareEvent(CareEvent careEvent) {
        this.careEvents.remove(careEvent);
        careEvent.setFollowupPlan(null);
        return this;
    }

    public void setCareEvents(Set<CareEvent> careEvents) {
        this.careEvents = careEvents;
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
        final StringBuffer sb = new StringBuffer("FollowupPlan{");
        sb.append("id=").append(id);
        sb.append(", procedureBooking=").append(procedureBooking);
        sb.append(", patient=").append(patient);
        sb.append(", careEvents=").append(careEvents);
        sb.append('}');
        return sb.toString();
    }
}
