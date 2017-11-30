package com.noesisinformatica.northumbriaproms.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Objects;

import com.noesisinformatica.northumbriaproms.domain.enumeration.ActionPhase;

import com.noesisinformatica.northumbriaproms.domain.enumeration.ActionType;


/**
 * A FollowupAction.
 */
@Entity
@Table(name = "followup_action")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "followupaction")
public class FollowupAction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "phase")
    private ActionPhase phase;

    @Column(name = "scheduled_date")
    private Instant scheduledDate;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private ActionType type;

    @Column(name = "outcome_score")
    private Integer outcomeScore;

    @Column(name = "outcome_comment")
    private String outcomeComment;

    @Column(name = "completed_date")
    private ZonedDateTime completedDate;

    @ManyToOne(optional = false)
    @NotNull
    private FollowupPlan followupPlan;

    @ManyToOne(optional = false)
    @NotNull
    private Patient patient;

    @ManyToOne
    private Questionnaire questionnaire;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ActionPhase getPhase() {
        return phase;
    }

    public FollowupAction phase(ActionPhase phase) {
        this.phase = phase;
        return this;
    }

    public void setPhase(ActionPhase phase) {
        this.phase = phase;
    }

    public Instant getScheduledDate() {
        return scheduledDate;
    }

    public FollowupAction scheduledDate(Instant scheduledDate) {
        this.scheduledDate = scheduledDate;
        return this;
    }

    public void setScheduledDate(Instant scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getName() {
        return name;
    }

    public FollowupAction name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ActionType getType() {
        return type;
    }

    public FollowupAction type(ActionType type) {
        this.type = type;
        return this;
    }

    public void setType(ActionType type) {
        this.type = type;
    }

    public Integer getOutcomeScore() {
        return outcomeScore;
    }

    public FollowupAction outcomeScore(Integer outcomeScore) {
        this.outcomeScore = outcomeScore;
        return this;
    }

    public void setOutcomeScore(Integer outcomeScore) {
        this.outcomeScore = outcomeScore;
    }

    public String getOutcomeComment() {
        return outcomeComment;
    }

    public FollowupAction outcomeComment(String outcomeComment) {
        this.outcomeComment = outcomeComment;
        return this;
    }

    public void setOutcomeComment(String outcomeComment) {
        this.outcomeComment = outcomeComment;
    }

    public ZonedDateTime getCompletedDate() {
        return completedDate;
    }

    public FollowupAction completedDate(ZonedDateTime completedDate) {
        this.completedDate = completedDate;
        return this;
    }

    public void setCompletedDate(ZonedDateTime completedDate) {
        this.completedDate = completedDate;
    }

    public FollowupPlan getFollowupPlan() {
        return followupPlan;
    }

    public FollowupAction followupPlan(FollowupPlan followupPlan) {
        this.followupPlan = followupPlan;
        return this;
    }

    public void setFollowupPlan(FollowupPlan followupPlan) {
        this.followupPlan = followupPlan;
    }

    public Patient getPatient() {
        return patient;
    }

    public FollowupAction patient(Patient patient) {
        this.patient = patient;
        return this;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    public FollowupAction questionnaire(Questionnaire questionnaire) {
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
        FollowupAction followupAction = (FollowupAction) o;
        if (followupAction.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), followupAction.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FollowupAction{" +
            "id=" + getId() +
            ", phase='" + getPhase() + "'" +
            ", scheduledDate='" + getScheduledDate() + "'" +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", outcomeScore=" + getOutcomeScore() +
            ", outcomeComment='" + getOutcomeComment() + "'" +
            ", completedDate='" + getCompletedDate() + "'" +
            "}";
    }
}
