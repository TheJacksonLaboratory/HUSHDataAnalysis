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
    private int encounter_num;
    private String concept_cd;
    private String provider_id;
    private String start_dat;
    private String modifier_cd;
    private int instance_num;
    private static SimpleDateFormat dateFormat;
    private String valtype_cd;
    private String tval_char;
    private String nval_num;
    private String valueflag_cd;
    private String quantity_num;
    private String units_cd;
    private String end_dat;
    private String location_cd;
    private String observation_blob;
    private String confidence_num;
    private String update_date;
    private String download_date;
    private String import_date;
    private String sourcesystem_cd;
    private String text_search_index;
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
        if (fields.length < 23) {
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
                case "\"sourcesystem_cd\"":
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
        if (initializedvalues != 24) {
            // logger.error(String.format("Error while parsing header of PatientDimension file. We expected to determine indices of 19 fields, but got %d", initializedvalues));
            //logger.error("The offending line was: " + fields);
            System.exit(1);
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
        encounter_num = Integer.parseInt(A[ENCOUNTER_NUM_IDX]);
        concept_cd  = A[CONCEPT_CD_IDX];
        provider_id = A[PROVIDER_ID_IDX].substring(1,A[PROVIDER_ID_IDX].length()-1);
        start_dat = A[START_DAT_IDX].substring(1,A[START_DAT_IDX].length()-1);
        modifier_cd = A[MODIFIER_CD_IDX].substring(1,A[MODIFIER_CD_IDX].length()-1);
        instance_num = Integer.parseInt(A[INSTANCE_NUM_IDX]);
        valtype_cd = A[VALTYPE_CD_IDX].substring(1,A[VALTYPE_CD_IDX].length()-1);
        tval_char = A[TVAL_CHAR_IDX].substring(1,A[TVAL_CHAR_IDX].length()-1);
        nval_num = A[NVAL_NUM_IDX].substring(1,A[NVAL_NUM_IDX].length()-1);
        valueflag_cd = A[VALUEFLAG_CD__IDX].substring(1,A[VALUEFLAG_CD__IDX].length()-1);
        quantity_num = A[QUANTITY_NUM_IDX].substring(1,A[QUANTITY_NUM_IDX].length()-1);
        units_cd = A[UNITS_CD_IDX].substring(1,A[UNITS_CD_IDX].length()-1);
        end_dat = A[END_DAT_IDX].substring(1,A[END_DAT_IDX].length()-1);
        location_cd = A[LOCATION_CD_IDX].substring(1,A[LOCATION_CD_IDX].length()-1);
        observation_blob = A[OBSERVATION_BLOB_IDX].substring(1,A[OBSERVATION_BLOB_IDX].length()-1);
        confidence_num = A[CONFIDENCE_NUM_IDX].substring(1,A[CONFIDENCE_NUM_IDX].length()-1);
        update_date = A[UPDATE_DATE_IDX].substring(1,A[UPDATE_DATE_IDX].length()-1);
        download_date = A[DOWNLOAD_DATE_IDX].substring(1,A[DOWNLOAD_DATE_IDX].length()-1);
        import_date = A[IMPORT_DATE_IDX].substring(1,A[IMPORT_DATE_IDX].length()-1);
        sourcesystem_cd = A[SOURCESYSTEM_IDX].substring(1,A[SOURCESYSTEM_IDX].length()-1);
        upload_id = A[UPLOAD_ID_IDX].substring(1,A[UPLOAD_ID_IDX].length()-1);
        text_search_index = A[TEXT_SEARCH_IDX].substring(1,A[TEXT_SEARCH_IDX].length()-1);

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
