package org.jax.Fhir;

import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.dstu3.model.Patient;
import org.jax.Constant;
import org.jax.Parsers.PatientDimension;
import org.jax.Parsers.PatientDimensionImpl;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class TransformToFhirTest {
    private static IParser fhirJsonParser = TransformToFhir.getFhirContext().newJsonParser();

    @Test
    public void getPatient() throws Exception {
        String patient1 = "176974728,\"UD\",\"1979-08-07 00:00:00\",\"\",\"F\",,\"114\",\"9\",\"2\",\"\",\"\",\"\"," +
                "\"\",\"\",\"2017-03-12 00:00:00\",\"\",\"2015-03-19 00:00:00\",\"EPIC\",";

        //PatientDimensionImpl.setIndices(Constant.HEADER_PATIENT);
        PatientDimensionImpl patientDimension1 = new PatientDimensionImpl(patient1);
        //patientDimension1.setIndices(Constant.HEADER_PATIENT);
        //patientDimension1.patientDimensionEntry();

        //System.out.println(patientDimension1.patient_num());
        Patient patient = TransformToFhir.getPatient(patient1);
        System.out.println(fhirJsonParser.setPrettyPrint(true).encodeResourceToString(patient));
    }

    @Test
    public void getObservation() throws Exception {
    }

    @Test
    @Ignore
    public void upload() throws Exception {
        String patient1 = "176,\"UD\",\"1979-08-07 00:00:00\",\"\",\"F\",,\"114\",\"9\",\"2\",\"\",\"\",\"\"," +
                "\"\",\"\",\"2017-03-12 00:00:00\",\"\",\"2015-03-19 00:00:00\",\"EPIC\",";
        Patient patient = TransformToFhir.getPatient(patient1);
        //System.out.println(fhirJsonParser.setPrettyPrint(true).encodeResourceToString(patient));

        assertNotNull(TransformToFhir.getFhirServer());
        MethodOutcome outcome = TransformToFhir.getFhirServer().upload(patient);
        if (outcome != null) {
            System.out.println(outcome.getId().getValue());
        } else {
            System.out.println("outcome is null");
        }
/**
        FhirServer hapiFhirTestServer = new FhirServerDstu3Impl(Constant.HAPIFHIRSERVER);
        hapiFhirTestServer.upload(patient);
        MethodOutcome outcome1 = TransformToFhir.getFhirServer().upload(patient);
        if (outcome1 != null) {
            System.out.println(outcome1.getId().getValue());
        } else {
            System.out.println("outcome is null");
        }
**/
    }

}