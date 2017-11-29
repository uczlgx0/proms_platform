package com.noesisinformatica.northumbriaproms.service.dto;

import java.io.Serializable;
import com.noesisinformatica.northumbriaproms.domain.enumeration.GenderType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;



import io.github.jhipster.service.filter.ZonedDateTimeFilter;


/**
 * Criteria class for the Patient entity. This class is used in PatientResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /patients?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PatientCriteria implements Serializable {
    /**
     * Class for filtering GenderType
     */
    public static class GenderTypeFilter extends Filter<GenderType> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter familyName;

    private StringFilter givenName;

    private ZonedDateTimeFilter birthDate;

    private GenderTypeFilter gender;

    private LongFilter nhsNumber;

    private StringFilter email;

    private LongFilter addressId;

    private LongFilter procedureBookingsId;

    public PatientCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFamilyName() {
        return familyName;
    }

    public void setFamilyName(StringFilter familyName) {
        this.familyName = familyName;
    }

    public StringFilter getGivenName() {
        return givenName;
    }

    public void setGivenName(StringFilter givenName) {
        this.givenName = givenName;
    }

    public ZonedDateTimeFilter getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(ZonedDateTimeFilter birthDate) {
        this.birthDate = birthDate;
    }

    public GenderTypeFilter getGender() {
        return gender;
    }

    public void setGender(GenderTypeFilter gender) {
        this.gender = gender;
    }

    public LongFilter getNhsNumber() {
        return nhsNumber;
    }

    public void setNhsNumber(LongFilter nhsNumber) {
        this.nhsNumber = nhsNumber;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public LongFilter getAddressId() {
        return addressId;
    }

    public void setAddressId(LongFilter addressId) {
        this.addressId = addressId;
    }

    public LongFilter getProcedureBookingsId() {
        return procedureBookingsId;
    }

    public void setProcedureBookingsId(LongFilter procedureBookingsId) {
        this.procedureBookingsId = procedureBookingsId;
    }

    @Override
    public String toString() {
        return "PatientCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (familyName != null ? "familyName=" + familyName + ", " : "") +
                (givenName != null ? "givenName=" + givenName + ", " : "") +
                (birthDate != null ? "birthDate=" + birthDate + ", " : "") +
                (gender != null ? "gender=" + gender + ", " : "") +
                (nhsNumber != null ? "nhsNumber=" + nhsNumber + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (addressId != null ? "addressId=" + addressId + ", " : "") +
                (procedureBookingsId != null ? "procedureBookingsId=" + procedureBookingsId + ", " : "") +
            "}";
    }

}
