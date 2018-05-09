package org.jax.Parsers;

import org.hl7.fhir.dstu3.model.Coding;
import org.jax.DateModel.SourceSystemEnumType;
import java.util.Date;


/**
 * This class is used to parse Observation_Fact.txt
 * Priority: 1 (range 1-MaxInfinity, 1 is highest)
 */
public interface ObservationFact {

    /**
     * return the encounter_num.
     * max encounter_num: 1474124400 < Integer.MAXINFINITY
     * @return
     */
    int encounter_num();

    /**
     * return the patient_num.
     * max patient_num: 211326672 < Integer.MAXINFINITY
     * @return
     */
    int patient_num();

    /**
     * Return concept_cd. Use Coding to represent it.
     * eg. ICD9:357.2
     * To represent it, call
     (org.hl7.fhir.dstu3.model.Coding) Coding coding = new Coding();
     coding.setSystem("ICD9");
     coding.setCode("357.2");
     * @return
     */
    Coding concept_cd();

    /**
     * Return provider_id
     * @return
     */
    Coding provider_id();

    /**
     * Return start_date
     * @return
     */
    Date start_date();

    /**
     * Return modifier_cd
     * @return
     */
    Coding modifier_cd();

    /**
     * Return instance_num. The number is extremely unlikely to have overflow problem. But pay attention.
     * @return
     */
    int instance_num();

    /**
     * Unknown data type. Change to a more specific one if necessary
     * @return
     */
    char valtype_cd();

    /**
     * Unknown data type. Change to a more specific one if necessary
     * @return
     */
    String tval_char();

    double nval_num();

    /**
     * Unknown data type. Change to a more specific one if necessary
     * @return
     */
    char valueflag_cd();

    /**
     * Unknown data type. Change to a more specific one if necessary
     * @return
     */
    String quatity_num();

    /**
     * Return unit
     * @return
     */
    String units_cd();

    /**
     * Return end_date
     * @return
     */
    Date end_date();

    /**
     * Return location_cd
     * @return
     */
    String location_cd();

    /**
     * Return observation_blob
     * @return
     */
    String observation_blob();

    /**
     * Return confidence_num
     * @return
     */
    int confidence_num();

    /**
     * Return update_date
     * @return
     */
    Date update_date();

    /**
     * Return download_date
     * @return
     */
    Date download_date();

    /**
     * Return import_date
     * @return
     */
    Date import_date();

    /**
     * Return sourcesystem_cd
     * Note: only ADS and EPIC is defined in the enum class. Add others if found
     * @return
     */
    SourceSystemEnumType sourcesystem_cd();

    /**
     * Return upload_id
     * @return
     */
    String upload_id();

    /**
     * Return text_search_index
     * @return
     */
    String text_search_index();


}
