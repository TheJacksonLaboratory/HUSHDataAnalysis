package org.jax.Fhir;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.dstu3.model.*;
import org.jax.Constant;
import org.jax.Exception.AmbiguousSubjectException;
import org.jax.Exception.SubjectNotFoundException;
import org.jax.Parsers.ObservationFact;
import org.jax.Parsers.ObservationFactImpl;
import org.jax.Parsers.PatientDimension;
import org.jax.Parsers.PatientDimensionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.List;

/**
 * Transforms patient table to FHIR resources
 */
public class TransformToFhir {

    private final static Logger logger = LoggerFactory.getLogger(TransformToFhir.class);
    static FhirServer fhirServer = new FhirServerDstu3Impl(Constant.FHIRLOCALSERVER);
    static FhirContext fhirContext = FhirContext.forDstu3();

    public static FhirContext getFhirContext() {
        return fhirContext;
    }

    static Patient getPatient(String patientLine) throws ParseException {
        logger.trace(patientLine);

        PatientDimensionImpl patientDimension = new PatientDimensionImpl(patientLine);
        patientDimension.setIndices(Constant.HEADER_OBSERVATION);
        patientDimension.patientDimensionEntry();
        logger.trace("patient dimension: patientNum:" + patientDimension.patient_num());

        Patient patient = new Patient();


        Identifier identifier = new Identifier();
        identifier.setSystem(Constant.SYSTEM).setValue(Integer.toString(patientDimension.patient_num()));
        patient.addIdentifier(identifier);
        logger.debug(identifier.getSystem() + "\t" + identifier.getValue());

        patient.setBirthDate(patientDimension.birth_date());
        //logger.debug(patientDimension.birth_date());

        switch (patientDimension.sex_cd()) {
            case 'F':
                patient.setGender(Enumerations.AdministrativeGender.FEMALE);
                break;
            case 'f':
                patient.setGender(Enumerations.AdministrativeGender.FEMALE);
                break;
            case 'M':
                patient.setGender(Enumerations.AdministrativeGender.MALE);
                break;
            case 'm':
                patient.setGender(Enumerations.AdministrativeGender.MALE);
                break;

            default:
                patient.setGender(Enumerations.AdministrativeGender.UNKNOWN);
                break;
        }

        if (patientDimension.death_date() != null) {
            patient.setDeceased(new org.hl7.fhir.dstu3.model.BooleanType(true));
            patient.setDeceased(new DateTimeType(patientDimension.death_date()));
        }

        return patient;
    }

    static Observation getObservation(String s) throws SubjectNotFoundException, AmbiguousSubjectException {
        ObservationFact observationFact = new ObservationFactImpl(s);
        int patientNum = observationFact.patient_num();
        Identifier identifier = new Identifier();
        identifier.setSystem(Constant.SYSTEM).setValue(Integer.toString(observationFact.patient_num()));
        List<Patient> patientList = fhirServer.getPatient(identifier);
        if (patientList.size() == 0) {
            throw new SubjectNotFoundException();
        }
        if (patientList.size() > 1) {
            throw new AmbiguousSubjectException();
        }
        Observation observation = new Observation();


        return null;

    }

}
