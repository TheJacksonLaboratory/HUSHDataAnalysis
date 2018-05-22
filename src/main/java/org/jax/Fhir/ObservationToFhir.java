package org.jax.Fhir;

import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Patient;

import java.util.Map;

public class ObservationToFhir {

    static Patient getPatient(int patientNum, Map<Integer, Patient> patientMap){
        return patientMap.get(patientNum);
    }

    static Observation getObservation(String s) {
        Observation observation = new Observation();

        return observation;
    }

}
