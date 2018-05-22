package org.jax.Parsers;


import org.hl7.fhir.dstu3.model.Address;
import org.jax.DateModel.SourceSystemEnumType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * TODO: implement methods in this class, and pass the unit test
 */
public class PatientDimensionImpl implements PatientDimension {

    private static final Logger logger = LoggerFactory.getLogger(PatientDimensionImpl.class);


    private String patientRecord;

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    private static int PATIENT_NUM_IDX = 0;
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
    private static int SOURCESYSTEM_IDX = 0;
    private static int INCOME_IDX = 0;
    private static int PATIENT_BLOB_IDX = 0;
    private static int UPDATE_DATE_IDX = 0;
    private static int DOWNLOAD_DATE_IDX = 0;
    private static int IMPORT_DATE_IDX = 0;
    private static int UPLOAD_ID_IDX = 0;


    private int patient_num;
    private String vital_status_cd;
    private String birth_date;
    private Date birthDate;
    private String death_date;
    private Date deathDate;
    private String import_date;
    private Date importDate;
    private String download_date;
    private Date downloadDate;
    private String update_date;
    private Date updateDate;
    private char sex_cd;
    private String age_in_years_num;
    private int language_cd;
    private int race_cd;
    private int marital_status_cd;
    private String upload_id;
    private SourceSystemEnumType sourcesystem_cd;




    public PatientDimensionImpl(String s)  {
        this.patientRecord = s;
        //@TODO: parse record for getters
    }

    public void setIndices(String header) {
        String fields[] = header.split(",");
        if (fields.length < 19) {
            logger.error(String.format("Header of PatientDimension file with only %d fields (%s), exiting", fields.length, header));
            System.exit(1);
        }
        int initializedvalues = 0;
        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            switch (fields[i]) {
                case "\"patient_num\"":
                    PATIENT_NUM_IDX = i;
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
                 logger.error(String.format("Error while parsing header of PatientDimension file. We expected to determine indices of 19 fields, but got %d", initializedvalues));
                 logger.error("The offending line was: " + fields);
                 System.exit(1);
            }
    }

    public void patientDimensionEntry() throws ParseException {
        String []A = patientRecord.split(",");
        if (A.length < UPLOAD_ID_IDX) {
            logger.error(String.format("Malformed line of  PatientDimension file with only %d fields (%s), exiting", A.length, patientRecord));
            System.exit(1);
        }

        //patient_num
        if(!A[PATIENT_NUM_IDX].equals(" ")) {
            patient_num = Integer.parseInt(A[PATIENT_NUM_IDX]);
        }
        else{
            System.err.println("patient_num is not available!");
        }

        //vital_status_cd
        if(!A[VITAL_STATUTS_IDX].equals("\"\"")){
            vital_status_cd = A[VITAL_STATUTS_IDX].substring(1,A[VITAL_STATUTS_IDX].length()-1);
        }
        else{
            vital_status_cd = null;
        }
        //birthday_date

        if(!A[BIRTHDATE_IDX].equals("\"\"")) {
            birth_date = A[BIRTHDATE_IDX].substring(1, A[BIRTHDATE_IDX].length() - 1);
            birthDate = dateFormat.parse(birth_date);
        }
        else{
             System.out.println("birth_date is empty!");
             birthDate = null;
        }

        //death_date
        if(!A[DEATHDATE_IDX].equals("\"\"")) {
            death_date = A[DEATHDATE_IDX].substring(1, A[DEATHDATE_IDX].length() - 1);
            deathDate = dateFormat.parse(death_date);
        }
        else{
            System.out.println("death_date is not available. The patient is alive:-)!");
            deathDate = null;
        }
        //import_date
        if(!A[IMPORT_DATE_IDX].equals("\"\"")){
            import_date= A[IMPORT_DATE_IDX].substring(1,A[IMPORT_DATE_IDX].length()-1);
            importDate = dateFormat.parse(import_date);
        }
        else{
            System.out.println("import_date is not available!");
            importDate = null;
        }
        //download_date
        if(!A[DOWNLOAD_DATE_IDX].equals("\"\"")) {
            download_date = A[DOWNLOAD_DATE_IDX].substring(1, A[DOWNLOAD_DATE_IDX].length() - 1);
            downloadDate = dateFormat.parse(download_date);
        }
        else{
            System.out.println("download_date is not available!");
            downloadDate = null;
        }

        //update_date
        if(!A[UPDATE_DATE_IDX].equals("\"\"")) {
            update_date = A[UPDATE_DATE_IDX].substring(1, A[UPDATE_DATE_IDX].length() - 1);
            updateDate = dateFormat.parse(update_date);
        }
        else{
            System.out.println("update_date is not available!");
            updateDate = null;
        }

      //sex
        if(!A[SEX_CD_IDX].equals("\"\"")) {
            sex_cd = A[SEX_CD_IDX].matches("\"F\"") ? 'F' : 'M';//Assuming that that there are 2
        }
        else{
            sex_cd = '0'; // If sex is not available
            System.err.println("Sex is not available");
        }

       //marital_status_cd
        if(!A[MARITAL_STATUS_IDX].equals("\"\"")){
            if(A[MARITAL_STATUS_IDX].equals("UNKNOWN")){
                marital_status_cd = -1;//If marital status is unknown, we set it to -1.
                System.out.println("marital_status is unknown!");
            }
            else{
                marital_status_cd = Integer.parseInt(A[MARITAL_STATUS_IDX].substring(1,A[MARITAL_STATUS_IDX].length() - 1));
            }
        }
        //race_cd
        if(!A[RACE_IDX].equals("\"\"")){
            race_cd = Integer.parseInt(A[RACE_IDX].substring(1,A[RACE_IDX].length()-1));
        }
        else{
            System.out.println("Race is not available!");
        }

        //language
        if (!A[LANGUAGE_IDX].equals("\"\"")){
            language_cd = Integer.parseInt(A[LANGUAGE_IDX].substring(1,A[LANGUAGE_IDX].length() - 1));
        }
        else{
            System.out.println("Language is not available!");
        }

        //source system type
        if (!A[SOURCESYSTEM_IDX].equals("\"\"")){
            sourcesystem_cd = SourceSystemEnumType.valueOf(A[SOURCESYSTEM_IDX].substring(1,A[SOURCESYSTEM_IDX].length() - 1));
        }
        else{
            System.out.println("source system cd is not available!");
            sourcesystem_cd = null;
        }

        //upload ID
        if(A.length == UPLOAD_ID_IDX){ //It means that parsing stopped after ',', because there is no entry for upload_id
            System.out.println("upload_id is not available");
            upload_id = null;
        }
        else if(!A[UPLOAD_ID_IDX].equals("\"\"")){
            upload_id = A[UPLOAD_ID_IDX].substring(1,A[UPLOAD_ID_IDX].length()-1);
        }
        else{
            upload_id = null;
        }
    }


    @Override
    public int patient_num() {return patient_num;}

    @Override
    public String vital_status_cd() {return vital_status_cd;}

    @Override
    public Date birth_date() {return birthDate;}

    @Override

    public Date death_date() {return deathDate;}


    @Override
    public char sex_cd() {return sex_cd;}


    @Override
    public String age_in_years_num() {return age_in_years_num;}


    @Override
    public String language_cd() {return Integer.toString(language_cd);}

    @Override
    public String race() {return Integer.toString(race_cd);}

    @Override
    public char marital_status_cd() {return Integer.toString(marital_status_cd).charAt(0);}

    @Override
    public String religion_cd() {
        return null;
    }

    @Override
    public String zip_cd() {
        return null;
    }

    @Override
    public Address statecityzip_path() {
        return null;
    }


    /**
     * No need to implement unless the data is available
     */
    @Override
    public String income_cd() {
        throw new UnsupportedOperationException();
    }


    /**
     * No need to implement unless the data is available
     */
    @Override
    public String patient_blob() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date update_date() {return updateDate;}

    @Override
    public Date download_date() {return downloadDate;}

    @Override
    public Date import_date() {return importDate;}

    @Override
    public SourceSystemEnumType sourcesystem_cd() {return sourcesystem_cd;}

    @Override

    public int upload_id() {
        return 0;

    }
}
