package org.jax.Fhir;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.BooleanType;
import org.hl7.fhir.dstu3.model.*;
import org.jax.Constant;
import org.jax.Parsers.PatientDimension;
import org.jax.Parsers.PatientDimensionImpl;

/**
 * Transforms patient table to FHIR resources
 */
public class PatientToFhir {

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

}
