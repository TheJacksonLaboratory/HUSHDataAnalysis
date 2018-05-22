package org.jax.Fhir;

import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.dstu3.model.Patient;
import org.jax.Constant;
import org.jax.Parsers.PatientDimension;
import org.jax.Parsers.PatientDimensionImpl;
import org.junit.Test;

import static org.junit.Assert.*;

public class TransformToFhirTest {
    private static IParser fhirJsonParser = TransformToFhir.getFhirContext().newJsonParser();

    @Test
    public void getPatient() throws Exception {
        String patient1 = "176974728,\"UD\",\"1979-08-07 00:00:00\",\"\",\"F\",,\"114\",\"9\",\"2\",\"\",\"\",\"\"," +
                "\"\",\"\",\"2017-03-12 00:00:00\",\"\",\"2015-03-19 00:00:00\",\"EPIC\",";

        //PatientDimensionImpl.setIndices(Constant.HEADER_OBSERVATION);
        PatientDimensionImpl patientDimension1 = new PatientDimensionImpl(patient1);
        //patientDimension1.setIndices(Constant.HEADER_OBSERVATION);
        //patientDimension1.patientDimensionEntry();

        //System.out.println(patientDimension1.patient_num());
        Patient patient = TransformToFhir.getPatient(patient1);
        System.out.println(fhirJsonParser.setPrettyPrint(true).encodeResourceToString(patient));
    }

    @Test
    public void getObservation() throws Exception {
    }

}