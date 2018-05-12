package org.jax.Parsers;

import org.hl7.fhir.dstu3.model.Coding;
import org.jax.DateModel.SourceSystemEnumType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * TODO: implement methods in this class and pass unit test
 */
public class ObservationFactImpl implements ObservationFact {

    String record;
    private static int PATIENT_NUM_IDX = 0;
    private static int ENCOUNTER_NUM_IDX = 0;
    private static int CONCEPT_CD_IDX = 0;
    private static int PROVIDER_ID_IDX = 0;
    private static int START_DAT_IDX = 0;
    private static int MODIFIER_CD_IDX = 0;
    private static int INSTANCE_NUM_IDX = 0;
    private static int VALTYPE_CD_IDX = 0;
    private static int TVAL_CHAR_IDX = 0;
    private static int NVAL_NUM_IDX = 0;
    private static int VALUEFLAG_CD__IDX = 0;
    private static int QUANTITY_NUM_IDX = 0;
    private static int UNITS_CD_IDX = 0;
    private static int END_DAT_IDX = 0;
    private static int LOCATION_CD_IDX = 0;
    private static int OBSERVATION_BLOB_IDX = 0;
    private static int CONFIDENCE_NUM_IDX = 0;
    private static int SOURCESYSTEM_IDX = 0;
    private static int UPDATE_DATE_IDX = 0;
    private static int DOWNLOAD_DATE_IDX = 0;
    private static int IMPORT_DATE_IDX = 0;
    private static int UPLOAD_ID_IDX = 0;
    private static int TEXT_SEARCH_IDX = 0;

    private int patient_num;
    private String encounter_num;
    private String birth_date;
    private String death_date;
    private String import_date;
    private String download_date;
    private String update_date;
    private static SimpleDateFormat dateFormat;
    private String sex_cd;
    private int age_in_years_num;
    private int language_cd;
    private int race_cd;
    private int marital_status_cd;
    private String upload_id;

    public ObservationFactImpl(String record) {
        this.record = record;
        //@TODO: parse this record for getters
        parse(this.record);
    }

    private void parse(String s){
        //implement
    }


    public void setIndices_ObservFact(String header) {
        String fields[] = header.split(",");
        if (fields.length < 19) {
            //logger.error(String.format("Header of PatientDimension file with only %d fields (%s), exiting", fields.length, header));
            System.exit(1);
        }
        int initializedvalues = 0;
        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            switch (fields[i]) {
                case "\"encounter_num\"":
                    ENCOUNTER_NUM_IDX = i;
                    initializedvalues++;
                    break;
                case "\"patient_num\"":
                    PATIENT_NUM_IDX = i;
                    initializedvalues++;
                    break;
                case "\"concept_cd\"":
                    CONCEPT_CD_IDX = i;
                    initializedvalues++;
                    break;
                case "\"provider_id\"":
                    PROVIDER_ID_IDX = i;
                    initializedvalues++;
                    break;
                case "\"start_dat\"":
                    START_DAT_IDX = i;
                    initializedvalues++;
                    break;
                case "\"modifier_cd\"":
                    MODIFIER_CD_IDX = i;
                    initializedvalues++;
                    break;
                case "\"instance_num\"":
                    INSTANCE_NUM_IDX = i;
                    initializedvalues++;
                    break;
                case "\"valtype_cd\"":
                    VALTYPE_CD_IDX = i;
                    initializedvalues++;
                    break;
                case "\"tval_char\"":
                    TVAL_CHAR_IDX= i;
                    initializedvalues++;
                    break;
                case "\"nval_num\"":
                    NVAL_NUM_IDX = i;
                    initializedvalues++;
                    break;
                case "\"valueflag_cd\"":
                    VALUEFLAG_CD__IDX = i;
                    initializedvalues++;
                    break;
                case "\"quantity_num\"":
                    QUANTITY_NUM_IDX = i;
                    initializedvalues++;
                    break;
                case "\"units_cd\"":
                    UNITS_CD_IDX = i;
                    initializedvalues++;
                    break;
                case "\"end_dat\"":
                    END_DAT_IDX = i;
                    initializedvalues++;
                    break;
                case "\"location_cd\"":
                    LOCATION_CD_IDX = i;
                    initializedvalues++;
                    break;
                case "\"observation_blob\"":
                    OBSERVATION_BLOB_IDX = i;
                    initializedvalues++;
                    break;
                case "\"confidence_num\"":
                    CONFIDENCE_NUM_IDX = i;
                    initializedvalues++;
                    break;
                case "\"update_date\"":
                    UPDATE_DATE_IDX = i;
                    initializedvalues++;
                    break;
                case "\"download_date\"":
                    DOWNLOAD_DATE_IDX = i;
                    initializedvalues++;
                    break;
                case "\"import_date\"":
                    IMPORT_DATE_IDX = i;
                    initializedvalues++;
                    break;
                case "\"sourcesystem_c\"":
                    SOURCESYSTEM_IDX = i;
                    initializedvalues++;
                    break;
                case "\"upload_id\"":
                    UPLOAD_ID_IDX = i;
                    initializedvalues++;
                    break;
                case "\"text_search_index\"":
                    TEXT_SEARCH_IDX = i;
                    initializedvalues++;
                    break;
            }
        }
    }
//TODO Complete parsing entries
    public void observationFactEntry() {
        String []A = record.split(",");
        if (A.length < TEXT_SEARCH_IDX ) {
            // logger.error(String.format("Malformed line of  PatientDimension file with only %d fields (%s), exiting", A.length, line));
            System.exit(1);
        }

        patient_num = Integer.parseInt(A[PATIENT_NUM_IDX]);
        encounter_num = A[ENCOUNTER_NUM_IDX].substring(1,A[ENCOUNTER_NUM_IDX].length()-1);
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
    public List<String> modifier_cd() {
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
        return Integer.MIN_VALUE;
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
