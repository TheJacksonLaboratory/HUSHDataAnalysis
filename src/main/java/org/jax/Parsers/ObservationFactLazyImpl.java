package org.jax.Parsers;

import org.jax.DateModel.SourceSystemEnumType;
import org.jax.Exception.IllegalDataTypeException;
import org.jax.Exception.MalformedLineException;
import org.jax.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ObservationFactLazyImpl implements ObservationFact{

    public static final String HEADER_OBSERVATIONFACT= "\"encounter_num\",\"patient_num\",\"concept_cd\",\"provider_id\",\"start_date\",\"modifier_cd\"," +
            "\"instance_num\"," +
            "\"valtype_cd\",\"tval_char\",\"nval_num\",\"valueflag_cd\",\"quantity_num\",\"units_cd\",\"end_date\"," +
            "\"location_cd\",\"observation_blob\",\"confidence_num\",\"update_date\",\"download_date\",\"import_date\"," +
            "\"sourcesystem_cd\",\"upload_id\",\"text_search_index\"";

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String recordLine;


    private int encounter_num = Integer.MIN_VALUE;
    private int patient_num = Integer.MIN_VALUE;
    private String concept_cd;
    private String provider_id;
    private Date start_date;
    private String modifier_cd;
    private int instance_num = Integer.MIN_VALUE;
    private String valtype_cd;
    private String tval_char;
    private double nval_num = Double.MIN_VALUE;
    private char valueflag_cd = Character.MIN_VALUE;
    private double quatity_num = Double.MIN_VALUE;
    private String units_cd;
    private Date end_date;
    private String location_cd;
    private String observation_blob;
    private double confidence_num;
    private Date update_date;
    private Date download_date;
    private Date import_date;
    private SourceSystemEnumType sourcesystem_cd;
    private int upload_id = Integer.MIN_VALUE;
    private String text_search_index;




    public ObservationFactLazyImpl(String s) throws IllegalDataTypeException, MalformedLineException {
        this.recordLine = s;
        parse(this.recordLine);
    }


    private void parse(String s) throws IllegalDataTypeException, MalformedLineException {
 //System.out.println("line: \n" + s);
        if (s.endsWith(",")){
            s = s.concat("\"\"");
        }
 //System.out.println("adding quote: \n" + s);
        String [] elements = s.split(",");
        if (elements.length != 23) {
            //try to change the format
            s = s.replaceAll(",,", ",\"\",");
            s = s.replaceAll(",([\\d\\.]+),", ",\"$1\",");
            s = s.replaceAll("^(\\d+),", "\"$1\",");
            //s = s.replaceAll(",$", ",\"\"");
            elements = s.split("\",\"");
            if (elements.length != 23) {
                System.out.println(s);
                throw new MalformedLineException();
            }
        }
        for (int i = 0; i < elements.length; i++) {
            elements[i] = StringUtils.stripEndQuotes(elements[i]);
        }

        try {
            this.encounter_num = Integer.parseInt(elements[0]);
            this.patient_num = Integer.parseInt(elements[1]);
            this.concept_cd = elements[2];
            this.provider_id = elements[3];
            this.start_date = dateFormat.parse(elements[4]);
            this.modifier_cd = elements[5];
            if (elements[6].trim().length() != 0) {
                this.instance_num = Integer.parseInt(elements[6]);
            }
            this.valtype_cd = elements[7];
            this.tval_char = elements[8];
            if (elements[9].trim().length() != 0) {
                this.nval_num = Double.parseDouble(elements[9]);
            }
  //System.out.println(elements[10]);
            if (elements[10].trim().length() == 1) {
                this.valueflag_cd = elements[10].trim().charAt(0);
            } else if (elements[10].trim().length() == 3) {
                this.valueflag_cd = elements[10].trim().charAt(1);
            }
            if (elements[11].trim().length() != 0) {
                this.quatity_num = Double.parseDouble(elements[11]);
            }
            this.units_cd = elements[12];
            if (elements[13].trim().length() != 0) {
                this.end_date = dateFormat.parse(elements[13]);
            }
            this.location_cd = elements[14];
            this.observation_blob = elements[15];
            if(elements[16].trim().length() != 0) {
                this.confidence_num = Double.parseDouble(elements[16]);
            }
            if (elements[17].trim().length() != 0) {
                this.update_date = dateFormat.parse(elements[17]);
            }
            if (elements[18].trim().length() != 0) {
                this.download_date = dateFormat.parse(elements[18]);
            }
            if (elements[19].trim().length() != 0) {
                this.import_date = dateFormat.parse(elements[19]);
            }
            this.sourcesystem_cd = SourceSystemEnumType.valueOf(elements[20]);
            if (elements[21].trim().length() != 0){
                this.upload_id = Integer.parseInt(elements[21]);
            }
            this.text_search_index = elements[22];
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalDataTypeException();
        }
    }

    @Override
    public int encounter_num() {
        return this.encounter_num;
    }

    @Override
    public int patient_num() {
        return this.patient_num;
    }

    @Override
    public String concept_cd() {
        return this.concept_cd;
    }

    @Override
    public String provider_id() {
        return this.provider_id;
    }

    @Override
    public Date start_date() {
        return this.start_date;
    }

    @Override
    public String modifier_cd() {
        return this.modifier_cd;
    }

    @Override
    public int instance_num() {
        return this.instance_num;
    }

    @Override
    public String valtype_cd() {
        return this.valtype_cd;
    }

    @Override
    public String tval_char() {
        return this.tval_char;
    }

    @Override
    public double nval_num() {
        return this.nval_num;
    }

    @Override
    public char valueflag_cd() {
        return this.valueflag_cd;
    }

    @Override
    public double quatity_num() {
        return this.quatity_num;
    }

    @Override
    public String units_cd() {
        return this.units_cd;
    }

    @Override
    public Date end_date() {
        return this.end_date;
    }

    @Override
    public String location_cd() {
        return this.location_cd;
    }

    @Override
    public String observation_blob() {
        return this.observation_blob;
    }

    @Override
    public double confidence_num() {
        return this.confidence_num;
    }

    @Override
    public Date update_date() {
        return this.update_date;
    }

    @Override
    public Date download_date() {
        return this.download_date;
    }

    @Override
    public Date import_date() {
        return this.import_date;
    }

    @Override
    public SourceSystemEnumType sourcesystem_cd() {
        return this.sourcesystem_cd;
    }

    @Override
    public int upload_id() {
        return this.upload_id;
    }

    @Override
    public String text_search_index() {
        return this.text_search_index;
    }

    /**
    @Override
    public String toString() {
        String SEPERATOR = ""
        StringBuilder builder = new StringBuilder();
        builder.append(this.encounter_num);
        builder.append()
    }
    **/
}
