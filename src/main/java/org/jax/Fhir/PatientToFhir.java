package org.jax.Fhir;

import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Patient;
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

        patient.setGender(patient.getGender());

    }

}
