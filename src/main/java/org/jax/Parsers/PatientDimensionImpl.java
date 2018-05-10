package org.jax.Parsers;

import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.codesystems.AdministrativeGender;
import org.jax.DateModel.SourceSystemEnumType;

import java.util.Date;

/**
 * @TODO: implement methods in this class, and pass the unit test
 */
public class PatientDimensionImpl implements PatientDimension {

    private String patientRecord;

    public PatientDimensionImpl(String s) {
        this.patientRecord = s;
        //@TODO: parse record for getters
        parse(this.patientRecord);
    }

    private void parse(String s){
        //implement
    }

    @Override
    public int patient_num() {
        return 0;
    }

    @Override
    public String vital_status_cd() {
        return null;
    }

    @Override
    public Date birth_date() {
        return null;
    }

    @Override
    public Date death_date() {
        return null;
    }

    @Override
    public AdministrativeGender sex_cd() {
        return null;
    }

    @Override
    public double age_in_years_num() {
        return Double.MIN_VALUE;
    }

    @Override
    public int language_cd() {
        return 0;
    }

    @Override
    public int race() {
        return 0;
    }

    @Override
    public int marital_status_cd() {
        return 0;
    }

    @Override
    /**
     * No need to implement
     */
    public String religion_cd() {
        throw new UnsupportedOperationException();
    }

    @Override
    /**
     * No need to implement because the data is not available or fake
     */
    public String zip_cd() {
        throw new UnsupportedOperationException();
    }

    @Override
    /**
     * No need to implement because the data is not available or fake
     */
    public Address statecityzip_path() {
        throw new UnsupportedOperationException();
    }

    @Override
    /**
     * No need to implement unless the data is available
     */
    public String income_cd() {
        throw new UnsupportedOperationException();
    }

    @Override
    /**
     * No need to implement unless the data is available
     */
    public String patient_blob() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date update_date() {
        return null;
    }

    @Override
    public Date download_date() {
        return null;
    }

    @Override
    public Date import_date() {
        return null;
    }

    @Override
    public SourceSystemEnumType sourcesystem_cd() {
        return null;
    }

    @Override
    public String upload_id() {
        return null;
    }
}
