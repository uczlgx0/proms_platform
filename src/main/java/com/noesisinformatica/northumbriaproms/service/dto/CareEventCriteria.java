package com.noesisinformatica.northumbriaproms.service.dto;

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

import java.io.Serializable;
import com.noesisinformatica.northumbriaproms.domain.enumeration.EventType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the CareEvent entity. This class is used in CareEventResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /care-events?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CareEventCriteria implements Serializable {
    /**
     * Class for filtering EventType
     */
    public static class EventTypeFilter extends Filter<EventType> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private EventTypeFilter type;

    private LongFilter timepointId;

    private LongFilter patientId;

    private LongFilter followupActionsId;

    private LongFilter followupPlanId;

    public CareEventCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public EventTypeFilter getType() {
        return type;
    }

    public void setType(EventTypeFilter type) {
        this.type = type;
    }

    public LongFilter getTimepointId() {
        return timepointId;
    }

    public void setTimepointId(LongFilter timepointId) {
        this.timepointId = timepointId;
    }

    public LongFilter getPatientId() {
        return patientId;
    }

    public void setPatientId(LongFilter patientId) {
        this.patientId = patientId;
    }

    public LongFilter getFollowupActionsId() {
        return followupActionsId;
    }

    public void setFollowupActionsId(LongFilter followupActionsId) {
        this.followupActionsId = followupActionsId;
    }

    public LongFilter getFollowupPlanId() {
        return followupPlanId;
    }

    public void setFollowupPlanId(LongFilter followupPlanId) {
        this.followupPlanId = followupPlanId;
    }

    @Override
    public String toString() {
        return "CareEventCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (timepointId != null ? "timepointId=" + timepointId + ", " : "") +
                (patientId != null ? "patientId=" + patientId + ", " : "") +
                (followupActionsId != null ? "followupActionsId=" + followupActionsId + ", " : "") +
                (followupPlanId != null ? "followupPlanId=" + followupPlanId + ", " : "") +
            "}";
    }

}
