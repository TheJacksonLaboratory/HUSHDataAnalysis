package org.jax.Parsers;

import org.jax.Exception.IllegalDataTypeException;
import org.jax.Exception.MalformedLineException;
import org.jax.utils.StringUtils;

public class VisitDimensionImpl implements VisitDimension {

    private String visit_line;
    private String[] visit_array;
    private int encounter_num;
    private int patient_num;
    private String inOut_cd;

    public VisitDimensionImpl(String s) throws MalformedLineException, IllegalDataTypeException {
        this.visit_line = s;
        parse(this.visit_line);
    }

    private void parse(String s) throws MalformedLineException, IllegalDataTypeException {
        if (s.endsWith(",")) {
            s = s.concat("\"\"");
        }
        this.visit_array = s.split(",");
        if (this.visit_array.length!= 15) {
            throw new MalformedLineException();
        }
        try {
            encounter_num = Integer.parseInt(StringUtils.stripEndQuotes(visit_array[0]));
            patient_num = Integer.parseInt(StringUtils.stripEndQuotes(visit_array[1]));
            inOut_cd = StringUtils.stripEndQuotes(visit_array[5]);
        } catch (Exception e) {
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
    public String inOut_cd() {
        return this.inOut_cd;
    }
}
