package org.jax.Fhir;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.BooleanType;
import org.hl7.fhir.dstu3.model.*;
import org.jax.Constant;
import org.jax.Exception.AmbiguousSubjectException;
import org.jax.Exception.SubjectNotFoundException;
import org.jax.Parsers.ObservationFact;
import org.jax.Parsers.ObservationFactImpl;
import org.jax.Parsers.PatientDimension;
import org.jax.Parsers.PatientDimensionImpl;

import java.util.List;

/**
 * Transforms patient table to FHIR resources
 */
public class TransformToFhir {

    static FhirServer fhirServer = new FhirServerDstu3Impl(Constant.FHIRLOCALSERVER);

    static Patient getPatient(String patientLine) {
        Patient patient = new Patient();
        PatientDimension patientDimension = new PatientDimensionImpl(patientLine);

        Identifier identifier = new Identifier();
        identifier.setSystem(Constant.SYSTEM).setValue(Integer.toString(patientDimension.patient_num()));
        patient.addIdentifier(identifier);

        patient.setBirthDate(patientDimension.birth_date());

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
