package org.jax.Parsers;

import org.hl7.fhir.dstu3.model.Address;
import org.jax.DateModel.SourceSystemEnumType;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * @TODO: implement methods in this class, and pass the unit test
 */
public class PatientDimensionImpl  {
   // private static final Logger logger = LoggerFactory.getLogger(PatientDimensionImpl.class);


    private String patientRecord;

    private static int PATIENTNUM_IDX = 0;
    private static int VITAL_STATUTS_IDX = 0;
    private static int BIRTHDATE_IDX = 0;
    private static int DEATHDATE_IDX = 0;
    private static int SEX_CD_IDX = 0;
    private static int AGE_YEARS_IDX = 0;
    private static int LANGUAGE_IDX = 0;
    private static int RACE_IDX = 0;
    private static int MARITAL_STATUS_IDX = 0;
    private static int RELIGION_IDX = 0;
    private static int ZIP_IDX = 0;
    private static int STATECITYZIP_IDX = 0;
    private static int INCOME_IDX = 0;
    private static int PATIENT_BLOB_IDX = 0;
    private static int UPDATE_DATE_IDX = 0;
    private static int DOWNLOAD_DATE_IDX = 0;
    private static int IMPORT_DATE_IDX = 0;
    private static int SOURCESYSTEM_IDX = 0;
    private static int UPLOAD_ID_IDX = 0;

    private int patient_num;
    private String vital_status_cd;
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



    public PatientDimensionImpl(String s)  {
        this.patientRecord = s;
        //@TODO: parse record for getters
    }

    public void setIndices(String header) {
        String fields[] = header.split(",");
        if (fields.length < 19) {
            //logger.error(String.format("Header of PatientDimension file with only %d fields (%s), exiting", fields.length, header));
            System.exit(1);
        }
        int initializedvalues = 0;
        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            switch (fields[i]) {
                case "\"patient_num\"":
                    PATIENTNUM_IDX = i;
                    initializedvalues++;
                    break;
                case "\"vital_status_cd\"":
                    VITAL_STATUTS_IDX = i;
                    initializedvalues++;
                    break;
                case "\"birth_date\"":
                    BIRTHDATE_IDX = i;
                    initializedvalues++;
                    break;
                case "\"death_date\"":
                    DEATHDATE_IDX = i;
                    initializedvalues++;
                    break;
                case "\"sex_cd\"":
                    SEX_CD_IDX = i;
                    initializedvalues++;
                    break;
                case "\"age_in_years_num\"":
                    AGE_YEARS_IDX = i;
                    initializedvalues++;
                    break;
                case "\"language_cd\"":
                    LANGUAGE_IDX = i;
                    initializedvalues++;
                    break;
                case "\"race_cd\"":
                    RACE_IDX = i;
                    initializedvalues++;
                    break;
                case "\"marital_status_cd\"":
                    MARITAL_STATUS_IDX= i;
                    initializedvalues++;
                    break;
                case "\"religion_cd\"":
                    RELIGION_IDX = i;
                    initializedvalues++;
                    break;
                case "\"zip_cd\"":
                    ZIP_IDX = i;
                    initializedvalues++;
                    break;
                case "\"statecityzip_path\"":
                    STATECITYZIP_IDX = i;
                    initializedvalues++;
                    break;
                case "\"income_cd\"":
                    INCOME_IDX = i;
                    initializedvalues++;
                    break;
                case "\"patient_blob\"":
                    PATIENT_BLOB_IDX = i;
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
            }
        }

            if (initializedvalues != 19) {
                // logger.error(String.format("Error while parsing header of PatientDimension file. We expected to determine indices of 19 fields, but got %d", initializedvalues));
                //logger.error("The offending line was: " + fields);
                System.exit(1);
            }
    }

    public void patientDimensionEntry() {
        String []A = patientRecord.split(",");
        if (A.length < UPLOAD_ID_IDX ) {
           // logger.error(String.format("Malformed line of  PatientDimension file with only %d fields (%s), exiting", A.length, line));
            System.exit(1);
        }
        patient_num = Integer.parseInt(A[PATIENTNUM_IDX]);
        vital_status_cd = A[VITAL_STATUTS_IDX].substring(1,A[VITAL_STATUTS_IDX].length()-1);
        birth_date = A[BIRTHDATE_IDX].substring(1,A[BIRTHDATE_IDX].length()-1);
        death_date = A[DEATHDATE_IDX].substring(1,A[DEATHDATE_IDX].length()-1);
        import_date = A[IMPORT_DATE_IDX].substring(1,A[IMPORT_DATE_IDX].length()-1);
        download_date = A[DOWNLOAD_DATE_IDX].substring(1,A[DOWNLOAD_DATE_IDX].length()-1);
        update_date = A[UPDATE_DATE_IDX].substring(1,A[UPDATE_DATE_IDX].length()-1);
        sex_cd = A[SEX_CD_IDX].substring(1,A[SEX_CD_IDX].length()-1);
        //TODO
       // if(A[AGE_YEARS_IDX] != "") {
         //   age_in_years_num = Integer.parseInt(A[AGE_YEARS_IDX].substring(1, A[AGE_YEARS_IDX].length() - 1));
        //}
        marital_status_cd = Integer.parseInt(A[MARITAL_STATUS_IDX].substring(1,A[MARITAL_STATUS_IDX].length() - 1));
        race_cd = Integer.parseInt(A[RACE_IDX].substring(1,A[RACE_IDX].length()-1));
        language_cd = Integer.parseInt(A[LANGUAGE_IDX].substring(1,A[LANGUAGE_IDX].length() - 1));
        //TODO
       // upload_id = A[UPLOAD_ID_IDX];
    }



    public int patient_num() {return patient_num;}


    public String vital_status_cd() {return vital_status_cd;}


    public String birth_date() {return birth_date;}


    public String death_date() {
        return death_date;
    }


    public String sex_cd() {
        return sex_cd;
    }


    public int age_in_years_num() {
        return age_in_years_num;
    }


    public int language_cd() {
        return language_cd;
    }


    public int race() {
        return race_cd;
    }


    public int marital_status_cd() {
        return marital_status_cd;
    }


    /**
     * No need to implement unless the data is available
     */
    public String income_cd() {
        throw new UnsupportedOperationException();
    }


    /**
     * No need to implement unless the data is available
     */
    public String patient_blob() {
        throw new UnsupportedOperationException();
    }


    public Date update_date() {
        return null;
    }


    public Date download_date() {
        return null;
    }


    public Date import_date() {
        return null;
    }


    public SourceSystemEnumType sourcesystem_cd() {
        return null;
    }


    public String upload_id() {
        return null;
    }
}
