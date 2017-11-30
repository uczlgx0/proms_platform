package com.noesisinformatica.northumbriaproms.service.dto;

import java.io.Serializable;
import com.noesisinformatica.northumbriaproms.domain.enumeration.ActionPhase;
import com.noesisinformatica.northumbriaproms.domain.enumeration.ActionType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import io.github.jhipster.service.filter.InstantFilter;

import io.github.jhipster.service.filter.ZonedDateTimeFilter;


/**
 * Criteria class for the FollowupAction entity. This class is used in FollowupActionResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /followup-actions?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FollowupActionCriteria implements Serializable {
    /**
     * Class for filtering ActionPhase
     */
    public static class ActionPhaseFilter extends Filter<ActionPhase> {
    }

    /**
     * Class for filtering ActionType
     */
    public static class ActionTypeFilter extends Filter<ActionType> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private ActionPhaseFilter phase;

    private InstantFilter scheduledDate;

    private StringFilter name;

    private ActionTypeFilter type;

    private IntegerFilter outcomeScore;

    private StringFilter outcomeComment;

    private ZonedDateTimeFilter completedDate;

    private LongFilter followupPlanId;

    private LongFilter patientId;

    private LongFilter questionnaireId;

    public FollowupActionCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ActionPhaseFilter getPhase() {
        return phase;
    }

    public void setPhase(ActionPhaseFilter phase) {
        this.phase = phase;
    }

    public InstantFilter getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(InstantFilter scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public ActionTypeFilter getType() {
        return type;
    }

    public void setType(ActionTypeFilter type) {
        this.type = type;
    }

    public IntegerFilter getOutcomeScore() {
        return outcomeScore;
    }

    public void setOutcomeScore(IntegerFilter outcomeScore) {
        this.outcomeScore = outcomeScore;
    }

    public StringFilter getOutcomeComment() {
        return outcomeComment;
    }

    public void setOutcomeComment(StringFilter outcomeComment) {
        this.outcomeComment = outcomeComment;
    }

    public ZonedDateTimeFilter getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(ZonedDateTimeFilter completedDate) {
        this.completedDate = completedDate;
    }

    public LongFilter getFollowupPlanId() {
        return followupPlanId;
    }

    public void setFollowupPlanId(LongFilter followupPlanId) {
        this.followupPlanId = followupPlanId;
    }

    public LongFilter getPatientId() {
        return patientId;
    }

    public void setPatientId(LongFilter patientId) {
        this.patientId = patientId;
    }

    public LongFilter getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(LongFilter questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    @Override
    public String toString() {
        return "FollowupActionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (phase != null ? "phase=" + phase + ", " : "") +
                (scheduledDate != null ? "scheduledDate=" + scheduledDate + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (outcomeScore != null ? "outcomeScore=" + outcomeScore + ", " : "") +
                (outcomeComment != null ? "outcomeComment=" + outcomeComment + ", " : "") +
                (completedDate != null ? "completedDate=" + completedDate + ", " : "") +
                (followupPlanId != null ? "followupPlanId=" + followupPlanId + ", " : "") +
                (patientId != null ? "patientId=" + patientId + ", " : "") +
                (questionnaireId != null ? "questionnaireId=" + questionnaireId + ", " : "") +
            "}";
    }

}
