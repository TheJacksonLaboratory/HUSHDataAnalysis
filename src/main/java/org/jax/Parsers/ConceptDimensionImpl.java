package org.jax.Parsers;

import org.jax.Exception.MalformedLineException;
import org.jax.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;



public class ConceptDimensionImpl implements ConceptDimension {

    private static Logger logger = LoggerFactory.getLogger(ConceptDimensionImpl.class);
    private String record;

    private String concept_path;
    private String concept_cd;
    private String name_char;


    public ConceptDimensionImpl(String record) throws MalformedLineException {
        this.record = record;
        parse(this.record);
    }

    private void parse(String s) throws MalformedLineException {
        //logger.debug(s);
        s = s.concat("\"\"");
        /**
        if (s.endsWith("\\|")) {
            logger.trace("enter here");
            s = s.concat("\"\"");
        }
         **/
        //logger.debug(s);
        s = s.replaceAll("\\|\\|", "\\|\"\"\\|");

        String[] record_elements = s.split("\"\\|");
        if (record_elements.length != 9) {
            throw new MalformedLineException();
        }
        this.concept_path = StringUtils.stripEndQuotes(record_elements[0]);
        this.concept_cd = StringUtils.stripEndQuotes(record_elements[1]);
        this.name_char = StringUtils.stripEndQuotes(record_elements[2]);
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
        throw new UnsupportedOperationException();
    }

    @Override
    public Date update_date() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date download_date() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date import_date() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String sourcesystem_cd() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int upload_id() {
        throw new UnsupportedOperationException();
    }
}
