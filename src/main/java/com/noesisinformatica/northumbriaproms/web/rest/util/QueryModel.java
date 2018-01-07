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
    List<String> sides = new ArrayList<>();
    List<String> statuses = new ArrayList<>();
    List<String> careEvents = new ArrayList<>();
    Integer minAge;
    Integer maxAge;
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

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<String> getSides() {
        return sides;
    }

    public void setSides(List<String> sides) {
        this.sides = sides;
    }

    public List<String> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<String> statuses) {
        this.statuses = statuses;
    }

    public List<String> getCareEvents() {
        return careEvents;
    }

    public void setCareEvents(List<String> careEvents) {
        this.careEvents = careEvents;
    }

    public boolean isEmpty() {
        return procedures.isEmpty() && consultants.isEmpty() && locations.isEmpty() && phases.isEmpty()
            && patientIds.isEmpty() && types.isEmpty() && genders.isEmpty() && (minAge == null) && (maxAge == null)
            && sides.isEmpty() && careEvents.isEmpty() && (token == null);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("QueryModel{");
        sb.append("procedures=").append(procedures);
        sb.append(", consultants=").append(consultants);
        sb.append(", locations=").append(locations);
        sb.append(", patientIds=").append(patientIds);
        sb.append(", phases=").append(phases);
        sb.append(", statuses=").append(statuses);
        sb.append(", types=").append(types);
        sb.append(", genders=").append(genders);
        sb.append(", sides=").append(sides);
        sb.append(", careEvents=").append(careEvents);
        sb.append(", minAge=").append(minAge);
        sb.append(", maxAge=").append(maxAge);
        sb.append(", token='").append(token).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
