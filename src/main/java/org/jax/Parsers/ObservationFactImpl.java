package org.jax.Parsers;

import org.hl7.fhir.dstu3.model.Coding;
import org.jax.DateModel.SourceSystemEnumType;

import java.util.Date;

public class ObservationFactImpl implements ObservationFact {

    String record;

    public ObservationFactImpl(String record) {
        this.record = record;
        //@TODO: parse this record
    }


    @Override
    public int encounter_num() {
        return 0;
    }

    @Override
    public int patient_num() {
        return 0;
    }

    @Override
    public Coding concept_cd() {
        return null;
    }

    @Override
    public Coding provider_id() {
        return null;
    }

    @Override
    public Date start_date() {
        return null;
    }

    @Override
    public Coding modifier_cd() {
        return null;
    }

    @Override
    public int instance_num() {
        return 0;
    }

    @Override
    public char valtype_cd() {
        return 0;
    }

    @Override
    public String tval_char() {
        return null;
    }

    @Override
    public double nval_num() {
        return 0;
    }

    @Override
    public char valueflag_cd() {
        return 0;
    }

    @Override
    public String quatity_num() {
        return null;
    }

    @Override
    public String units_cd() {
        return null;
    }

    @Override
    public Date end_date() {
        return null;
    }

    @Override
    public String location_cd() {
        return null;
    }

    @Override
    public String observation_blob() {
        return null;
    }

    @Override
    public int confidence_num() {
        return 0;
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

    @Override
    public String text_search_index() {
        return null;
    }
}
