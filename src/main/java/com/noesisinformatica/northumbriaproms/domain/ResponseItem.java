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

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;


/**
 * An entity that represents a response on a {@link Questionnaire}.
 */
@Entity
@Table(name = "response_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "responseitem")
public class ResponseItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "value", nullable = false)
    private Integer value;

    @Column(name = "question")
    private String question;

    @Column(name = "local_id", nullable = false)
    private String localId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JsonBackReference
    @NotNull
    @JoinColumn(name = "action_id")
    private FollowupAction followupAction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public FollowupAction getFollowupAction() {
        return followupAction;
    }

    public void setFollowupAction(FollowupAction followupAction) {
        this.followupAction = followupAction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ResponseItem responseItem = (ResponseItem) o;
        if (responseItem.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), responseItem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ResponseItem{" +
            "id=" + getId() +
            ", question='" + getQuestion() + "'" +
            ", localId='" + getLocalId() + "'" +
            ", value=" + getValue() +
            "}";
    }
}
