package com.noesisinformatica.northumbriaproms.web.rest.util;

import java.util.ArrayList;
import java.util.List;

/**
 * A wrapper for query model with various attributes on {@link com.noesisinformatica.northumbriaproms.domain.FollowupAction}.
 *
 * Created by Jay Kola on 17/12/17.
 */
public class QueryModel {

    List<String> procedures = new ArrayList<>();
    List<String> consultants = new ArrayList<>();
    List<String> locations = new ArrayList<>();
    List<String> patientIds = new ArrayList<>();
    List<String> phases = new ArrayList<>();
    List<String> types = new ArrayList<>();
    List<String> genders = new ArrayList<>();
    Integer age;
    String token;

    public List<String> getProcedures() {
        return procedures;
    }

    public void setProcedures(List<String> procedures) {
        this.procedures = procedures;
    }

    public List<String> getConsultants() {
        return consultants;
    }

    public void setConsultants(List<String> consultants) {
        this.consultants = consultants;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public List<String> getPatientIds() {
        return patientIds;
    }

    public void setPatientIds(List<String> patientIds) {
        this.patientIds = patientIds;
    }

    public List<String> getPhases() {
        return phases;
    }

    public void setPhases(List<String> phases) {
        this.phases = phases;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public List<String> getGenders() {
        return genders;
    }

    public void setGenders(List<String> genders) {
        this.genders = genders;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isEmpty() {
        return procedures.isEmpty() && consultants.isEmpty() && locations.isEmpty() && phases.isEmpty()
            && patientIds.isEmpty() && types.isEmpty() && genders.isEmpty() && (age == null) && (token == null);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("QueryModel{");
        sb.append("procedures=").append(procedures);
        sb.append(", consultants=").append(consultants);
        sb.append(", locations=").append(locations);
        sb.append(", patientIds=").append(patientIds);
        sb.append(", phases=").append(phases);
        sb.append(", types=").append(types);
        sb.append(", genders=").append(genders);
        sb.append(", age=").append(age);
        sb.append(", token='").append(token).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
