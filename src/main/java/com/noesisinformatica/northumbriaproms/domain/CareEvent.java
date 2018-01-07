package com.noesisinformatica.northumbriaproms.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.noesisinformatica.northumbriaproms.domain.enumeration.ActionStatus;
import com.noesisinformatica.northumbriaproms.domain.enumeration.EventType;
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
 * A CareEvent.
 */
@Entity
@Table(name = "care_event")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "careevent")
public class CareEvent extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type", nullable = false)
    private EventType type;

    @ManyToOne
    private Timepoint timepoint;

    @ManyToOne(optional = false)
    @NotNull
    private Patient patient;

    @OneToMany(mappedBy = "careEvent", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<FollowupAction> followupActions = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private FollowupPlan followupPlan;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ActionStatus status;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventType getType() {
        return type;
    }

    public CareEvent type(EventType type) {
        this.type = type;
        return this;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public ActionStatus getStatus() {
        return status;
    }

    public void setStatus(ActionStatus status) {
        this.status = status;
    }

    public CareEvent status(ActionStatus status) {
        this.status = status;
        return this;
    }

    public Timepoint getTimepoint() {
        return timepoint;
    }

    public CareEvent timepoint(Timepoint timepoint) {
        this.timepoint = timepoint;
        return this;
    }

    public void setTimepoint(Timepoint timepoint) {
        this.timepoint = timepoint;
    }

    public Patient getPatient() {
        return patient;
    }

    public CareEvent patient(Patient patient) {
        this.patient = patient;
        return this;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Set<FollowupAction> getFollowupActions() {
        return followupActions;
    }

    public CareEvent followupActions(Set<FollowupAction> followupActions) {
        this.followupActions = followupActions;
        return this;
    }

    public CareEvent addFollowupAction(FollowupAction followupAction) {
        this.followupActions.add(followupAction);
        followupAction.setCareEvent(this);
        return this;
    }

    public CareEvent removeFollowupAction(FollowupAction followupAction) {
        this.followupActions.remove(followupAction);
        followupAction.setCareEvent(null);
        return this;
    }

    public void setFollowupActions(Set<FollowupAction> followupActions) {
        this.followupActions = followupActions;
    }

    public FollowupPlan getFollowupPlan() {
        return followupPlan;
    }

    public CareEvent followupPlan(FollowupPlan followupPlan) {
        this.followupPlan = followupPlan;
        return this;
    }

    public void setFollowupPlan(FollowupPlan followupPlan) {
        this.followupPlan = followupPlan;
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
        CareEvent careEvent = (CareEvent) o;
        if (careEvent.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), careEvent.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CareEvent{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", status='" + getStatus() + "'" +
            ", patient='" + getPatient() + "'" +
            ", timepoint='" + getTimepoint() + "'" +
            ", actions='" + getFollowupActions() + "'" +
            "}";
    }
}
