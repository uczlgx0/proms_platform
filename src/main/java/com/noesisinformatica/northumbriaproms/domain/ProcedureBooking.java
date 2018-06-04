package com.noesisinformatica.northumbriaproms.domain;

/*-
 * #%L
 * Proms Platform
 * %%
 * Copyright (C) 2017 - 2018 Termlex
 * %%
 * This software is Copyright and Intellectual Property of Termlex Inc Limited.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation as version 3 of the
 * License.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public
 * License along with this program.  If not, see
 * <https://www.gnu.org/licenses/agpl-3.0.en.html>.
 * #L%
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.noesisinformatica.northumbriaproms.domain.enumeration.Laterality;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
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
    private LocalDate scheduledDate;

    @Column(name = "performed_date")
    private LocalDate performedDate;

    @NotNull
    @Column(name = "primary_procedure", nullable = false)
    private String primaryProcedure;

    @Column(name = "other_procedures")
    private String otherProcedures;

    @ManyToOne(optional = false)
    @NotNull
    private Patient patient;

    @OneToOne
    @JsonIgnore
    @JoinColumn(unique = true)
    private FollowupPlan followupPlan;

    @Enumerated(EnumType.STRING)
    @Column(name = "side")
    private Laterality side;

    @Column(name = "patient_age")
    private Integer patientAge;

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

    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public ProcedureBooking scheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
        return this;
    }

    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public LocalDate getPerformedDate() {
        return performedDate;
    }

    public ProcedureBooking performedDate(LocalDate performedDate) {
        this.performedDate = performedDate;
        return this;
    }

    public void setPerformedDate(LocalDate performedDate) {
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

    public Laterality getSide() {
        return side;
    }

    public void setSide(Laterality side) {
        this.side = side;
    }

    public Integer getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(Integer patientAge) {
        this.patientAge = patientAge;
    }

    public void setFollowupPlan(FollowupPlan followupPlan) {
        this.followupPlan = followupPlan;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @PrePersist @PreUpdate
    public void setAge() {
        /*
            NOTE this could change between scheduledDate and completionDate, we need need both prePersist and preUpdate.
            We set default value from current date. Then we first see if complete date is set, if so we use it.
            Otherwise since scheduled date is mandatory, we use it
         */
        int age = Period.between(getPatient().getBirthDate(), LocalDate.now()).getYears();
        if(getPerformedDate() != null) {
            age = Period.between(getPatient().getBirthDate(), getPerformedDate()).getYears();
        } else if (getScheduledDate() != null) {
            age = Period.between(getPatient().getBirthDate(), getScheduledDate()).getYears();
        }
        // set age of patient at time of surgery
        this.setPatientAge(age);
    }

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
