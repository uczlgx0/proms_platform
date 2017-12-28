package com.noesisinformatica.northumbriaproms.service.dto;

import com.noesisinformatica.northumbriaproms.domain.enumeration.Laterality;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LocalDateFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import java.io.Serializable;


/**
 * Criteria class for the ProcedureBooking entity. This class is used in ProcedureBookingResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /procedure-bookings?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProcedureBookingCriteria implements Serializable {

    /**
     * Class for filtering Laterality
     */
    public static class LateralityFilter extends Filter<Laterality> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter consultantName;

    private StringFilter hospitalSite;

    private LocalDateFilter scheduledDate;

    private LocalDateFilter performedDate;

    private StringFilter primaryProcedure;

    private LateralityFilter side;

    private StringFilter otherProcedures;

    private LongFilter patientId;

    private LongFilter followupPlanId;

    public ProcedureBookingCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
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

    public LocalDateFilter getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDateFilter scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public LocalDateFilter getPerformedDate() {
        return performedDate;
    }

    public void setPerformedDate(LocalDateFilter performedDate) {
        this.performedDate = performedDate;
    }

    public StringFilter getPrimaryProcedure() {
        return primaryProcedure;
    }

    public void setPrimaryProcedure(StringFilter primaryProcedure) {
        this.primaryProcedure = primaryProcedure;
    }

    public StringFilter getOtherProcedures() {
        return otherProcedures;
    }

    public void setOtherProcedures(StringFilter otherProcedures) {
        this.otherProcedures = otherProcedures;
    }

    public LongFilter getPatientId() {
        return patientId;
    }

    public void setPatientId(LongFilter patientId) {
        this.patientId = patientId;
    }

    public LongFilter getFollowupPlanId() {
        return followupPlanId;
    }

    public void setFollowupPlanId(LongFilter followupPlanId) {
        this.followupPlanId = followupPlanId;
    }

    public LateralityFilter getSide() {
        return side;
    }

    public void setSide(LateralityFilter side) {
        this.side = side;
    }

    @Override
    public String toString() {
        return "ProcedureBookingCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (consultantName != null ? "consultantName=" + consultantName + ", " : "") +
                (hospitalSite != null ? "hospitalSite=" + hospitalSite + ", " : "") +
                (scheduledDate != null ? "scheduledDate=" + scheduledDate + ", " : "") +
                (performedDate != null ? "performedDate=" + performedDate + ", " : "") +
                (primaryProcedure != null ? "primaryProcedure=" + primaryProcedure + ", " : "") +
                (side != null ? "side=" + side + ", " : "") +
                (otherProcedures != null ? "otherProcedures=" + otherProcedures + ", " : "") +
                (patientId != null ? "patientId=" + patientId + ", " : "") +
                (followupPlanId != null ? "followupPlanId=" + followupPlanId + ", " : "") +
            "}";
    }

}
