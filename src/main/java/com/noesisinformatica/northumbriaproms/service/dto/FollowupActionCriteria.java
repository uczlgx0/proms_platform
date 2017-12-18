package com.noesisinformatica.northumbriaproms.service.dto;

import com.noesisinformatica.northumbriaproms.domain.enumeration.ActionPhase;
import com.noesisinformatica.northumbriaproms.domain.enumeration.ActionType;
import io.github.jhipster.service.filter.*;

import java.io.Serializable;


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

    private LocalDateFilter scheduledDate;

    private StringFilter name;

    private ActionTypeFilter type;

    private IntegerFilter outcomeScore;

    private StringFilter outcomeComment;

    private LocalDateFilter completedDate;

    private LongFilter followupPlanId;

    private LongFilter patientId;

    private LongFilter questionnaireId;

    private StringFilter consultantName;

    private StringFilter hospitalSite;

    private StringFilter primaryProcedure;

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

    public LocalDateFilter getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDateFilter scheduledDate) {
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

    public LocalDateFilter getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(LocalDateFilter completedDate) {
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

    public StringFilter getConsultantName() {
        return consultantName;
    }

    public void setConsultantName(StringFilter consultantName) {
        this.consultantName = consultantName;
    }

    public StringFilter getHospitalSite() {
        return hospitalSite;
    }

    public void setHospitalSite(StringFilter hospitalSite) {
        this.hospitalSite = hospitalSite;
    }

    public StringFilter getPrimaryProcedure() {
        return primaryProcedure;
    }

    public void setPrimaryProcedure(StringFilter primaryProcedure) {
        this.primaryProcedure = primaryProcedure;
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
                (consultantName != null ? "consultantName=" + consultantName + ", " : "") +
                (hospitalSite != null ? "hospitalSite=" + hospitalSite + ", " : "") +
                (primaryProcedure != null ? "primaryProcedure=" + primaryProcedure + ", " : "") +
            "}";
    }

}
