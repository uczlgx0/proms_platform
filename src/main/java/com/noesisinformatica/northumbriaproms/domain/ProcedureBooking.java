package com.noesisinformatica.northumbriaproms.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;


/**
 * A ProcedureBooking.
 */
@Entity
@Table(name = "procedure_booking")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "procedurebooking")
public class ProcedureBooking implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "consultant_name", nullable = false)
    private String consultantName;

    @NotNull
    @Column(name = "hospital_site", nullable = false)
    private String hospitalSite;

    @Column(name = "scheduled_date")
    private ZonedDateTime scheduledDate;

    @Column(name = "performed_date")
    private ZonedDateTime performedDate;

    @NotNull
    @Column(name = "primary_procedure", nullable = false)
    private String primaryProcedure;

    @Column(name = "other_procedures")
    private String otherProcedures;

    @ManyToOne(optional = false)
    @NotNull
    private Patient patient;

    @OneToOne
    @JoinColumn(unique = true)
    private FollowupPlan followupPlan;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConsultantName() {
        return consultantName;
    }

    public ProcedureBooking consultantName(String consultantName) {
        this.consultantName = consultantName;
        return this;
    }

    public void setConsultantName(String consultantName) {
        this.consultantName = consultantName;
    }

    public String getHospitalSite() {
        return hospitalSite;
    }

    public ProcedureBooking hospitalSite(String hospitalSite) {
        this.hospitalSite = hospitalSite;
        return this;
    }

    public void setHospitalSite(String hospitalSite) {
        this.hospitalSite = hospitalSite;
    }

    public ZonedDateTime getScheduledDate() {
        return scheduledDate;
    }

    public ProcedureBooking scheduledDate(ZonedDateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
        return this;
    }

    public void setScheduledDate(ZonedDateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public ZonedDateTime getPerformedDate() {
        return performedDate;
    }

    public ProcedureBooking performedDate(ZonedDateTime performedDate) {
        this.performedDate = performedDate;
        return this;
    }

    public void setPerformedDate(ZonedDateTime performedDate) {
        this.performedDate = performedDate;
    }

    public String getPrimaryProcedure() {
        return primaryProcedure;
    }

    public ProcedureBooking primaryProcedure(String primaryProcedure) {
        this.primaryProcedure = primaryProcedure;
        return this;
    }

    public void setPrimaryProcedure(String primaryProcedure) {
        this.primaryProcedure = primaryProcedure;
    }

    public String getOtherProcedures() {
        return otherProcedures;
    }

    public ProcedureBooking otherProcedures(String otherProcedures) {
        this.otherProcedures = otherProcedures;
        return this;
    }

    public void setOtherProcedures(String otherProcedures) {
        this.otherProcedures = otherProcedures;
    }

    public Patient getPatient() {
        return patient;
    }

    public ProcedureBooking patient(Patient patient) {
        this.patient = patient;
        return this;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public FollowupPlan getFollowupPlan() {
        return followupPlan;
    }

    public ProcedureBooking followupPlan(FollowupPlan followupPlan) {
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
        ProcedureBooking procedureBooking = (ProcedureBooking) o;
        if (procedureBooking.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), procedureBooking.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProcedureBooking{" +
            "id=" + getId() +
            ", consultantName='" + getConsultantName() + "'" +
            ", hospitalSite='" + getHospitalSite() + "'" +
            ", scheduledDate='" + getScheduledDate() + "'" +
            ", performedDate='" + getPerformedDate() + "'" +
            ", primaryProcedure='" + getPrimaryProcedure() + "'" +
            ", otherProcedures='" + getOtherProcedures() + "'" +
            "}";
    }
}
