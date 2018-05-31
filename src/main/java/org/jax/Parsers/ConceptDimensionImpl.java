package org.jax.Parsers;

import org.jax.Exception.IllegalDataTypeException;
import org.jax.Exception.MalformedLineException;
import org.jax.utils.StringUtils;
import org.python.core.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;



public class ConceptDimensionImpl implements ConceptDimension {

    private static Logger logger = LoggerFactory.getLogger(ConceptDimensionImpl.class);
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String record;

    private String concept_path;
    private String concept_cd;
    private String name_char;
    private String concept_blob;
    private Date update_date;
    private Date download_date;
    private Date import_date;
    private String sourcesystem_cd;
    private int upload_id = Integer.MIN_VALUE;



    public ConceptDimensionImpl(String record) throws MalformedLineException, IllegalDataTypeException {
        this.record = record;
        parse(this.record);
    }

    private void parse(String record) throws MalformedLineException, IllegalDataTypeException {
        //logger.debug(s);
        record = record.replaceAll("\\|$", "\\|\"\"");
        record = record.replaceAll("\\|{2}", "\\|\"\"\\|");
        record = record.replaceAll("\\|{2}", "\\|\"\"\\|");

        String[] record_elements = record.split("\"\\|");
        if (record_elements.length != 9) {
            throw new MalformedLineException();
        }
        for (int i = 0; i < record_elements.length; i++) {
            record_elements[i] = StringUtils.stripEndQuotes(record_elements[i]);
        }
        try {
            this.concept_path = record_elements[0];
            this.concept_cd = record_elements[1];
            this.name_char = record_elements[2];
            if (record_elements[3].trim().length() != 0) {
                this.concept_blob = record_elements[3];
            }
            if (record_elements[4].trim().length() != 0) {
                this.update_date = dateFormat.parse(record_elements[4]);
            }
            if (record_elements[5].trim().length() != 0) {

                this.download_date = dateFormat.parse(record_elements[5]);
            }
            if (record_elements[6].trim().length() != 0){
                this.import_date = dateFormat.parse(record_elements[6]);
            }
            sourcesystem_cd = record_elements[7];
            if (record_elements[8].trim().length() != 0) {
                this.upload_id = Integer.parseInt(record_elements[8]);
            }
        } catch (Exception e) {
            throw new IllegalDataTypeException();
        }

    }


    @Override
    public String concept_path() {
        return this.concept_path;
    }

    @Override
    public String concept_cd() {
        return this.concept_cd;
    }

    @Override
    public String name_char() {
        return this.name_char;
    }

    @Override
    public String concept_blob() {
        return this.concept_blob;
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
    public String sourcesystem_cd() {
        return this.sourcesystem_cd;
    }

    @Override
    public int upload_id() {
        return this.upload_id;
    }
}
