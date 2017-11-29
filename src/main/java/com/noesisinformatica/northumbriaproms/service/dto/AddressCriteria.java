package com.noesisinformatica.northumbriaproms.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Address entity. This class is used in AddressResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /addresses?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AddressCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter street;

    private StringFilter line;

    private StringFilter city;

    private StringFilter county;

    private StringFilter postalCode;

    private StringFilter country;

    private LongFilter patientId;

    public AddressCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getStreet() {
        return street;
    }

    public void setStreet(StringFilter street) {
        this.street = street;
    }

    public StringFilter getLine() {
        return line;
    }

    public void setLine(StringFilter line) {
        this.line = line;
    }

    public StringFilter getCity() {
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getCounty() {
        return county;
    }

    public void setCounty(StringFilter county) {
        this.county = county;
    }

    public StringFilter getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(StringFilter postalCode) {
        this.postalCode = postalCode;
    }

    public StringFilter getCountry() {
        return country;
    }

    public void setCountry(StringFilter country) {
        this.country = country;
    }

    public LongFilter getPatientId() {
        return patientId;
    }

    public void setPatientId(LongFilter patientId) {
        this.patientId = patientId;
    }

    @Override
    public String toString() {
        return "AddressCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (street != null ? "street=" + street + ", " : "") +
                (line != null ? "line=" + line + ", " : "") +
                (city != null ? "city=" + city + ", " : "") +
                (county != null ? "county=" + county + ", " : "") +
                (postalCode != null ? "postalCode=" + postalCode + ", " : "") +
                (country != null ? "country=" + country + ", " : "") +
                (patientId != null ? "patientId=" + patientId + ", " : "") +
            "}";
    }

}
