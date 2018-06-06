package org.jax;

public class Constant {

    public static final String SYSTEM = "UNC_HUSH+";

    public static final String FHIRLOCALSERVER = "http://localhost:8080/hapi-fhir-jpaserver-example/baseDstu3";

    public static final String HAPIFHIRSERVER = "http://fhirtest.uhn.ca/baseDstu3";

    public static final String LOINCSYSTEM = "http://loinc.org";

    public static final String HEADER_PATIENT = "\"patient_num\",\"vital_status_cd\",\"birth_date\",\"death_date\",\"sex_cd\",\"age_in_years_num\"," +
            "\"language_cd\",\"race_cd\",\"marital_status_cd\",\"religion_cd\",\"zip_cd\",\"statecityzip_path\",\"income_cd\"," +
            "\"patient_blob\",\"update_date\",\"download_date\",\"import_date\",\"sourcesystem_cd\",\"upload_id\"";

    public static final String HEADER_OBSERVATIONFACT= "\"encounter_num\",\"patient_num\",\"concept_cd\",\"provider_id\",\"start_date\",\"modifier_cd\"," +
            "\"instance_num\"," +
            "\"valtype_cd\",\"tval_char\",\"nval_num\",\"valueflag_cd\",\"quantity_num\",\"units_cd\",\"end_date\"," +
            "\"location_cd\",\"observation_blob\",\"confidence_num\",\"update_date\",\"download_date\",\"import_date\"," +
            "\"sourcesystem_cd\",\"upload_id\",\"text_search_index\"";

}
