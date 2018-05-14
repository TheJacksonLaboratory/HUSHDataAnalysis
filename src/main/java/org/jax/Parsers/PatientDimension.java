package org.jax.Parsers;

import org.hl7.fhir.dstu3.model.Address;
import org.jax.DateModel.SourceSystemEnumType;

import java.util.Date;
/**
 * This class is used to parse PATIENT_DIMENTION.txt
 * Priority: 2 (range 1-MaxInfinity, 1 is highest)
 */
public interface PatientDimension {

    /**
     * Return patient num.
     * Note: check whether patient num will overflow int; change to long if that's the case.
     * @return
     */
    int patient_num();

    String vital_status_cd();

    String birth_date();

    String death_date();

    /**
     * Return sex (char[1])
     * @return
     */
    char sex_cd();

    /**
     * No need to implement
     * @return
     */
    double age_in_years_num();

    String language_cd();

    String race();

    char marital_status_cd();

    String religion_cd();

    String zip_cd();

    /**
     * This is Hush+ data, so no address are available
     * @return
     */
    Address statecityzip_path();

    /**
     * Not available
     * @return
     */
    String income_cd();

    /**
     * Not available
     * @return
     */
    String patient_blob();

    Date update_date();

    Date download_date();

    Date import_date();

    SourceSystemEnumType sourcesystem_cd();

    int upload_id();

}
