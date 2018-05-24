package org.jax.Fhir;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.codesystems.V3ObservationInterpretation;
import org.jax.Constant;
import org.jax.Exception.AmbiguousSubjectException;
import org.jax.Exception.IllegalDataTypeException;
import org.jax.Exception.SubjectNotFoundException;
import org.jax.Parsers.ObservationFact;
import org.jax.Parsers.ObservationFactImpl;
import org.jax.Parsers.PatientDimensionImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

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

    public static FhirServer getFhirServer() {
        return fhirServer;
    }

    public static Patient getPatient(String patientLine) throws ParseException {
        logger.trace(patientLine);

        PatientDimensionImpl patientDimension = new PatientDimensionImpl(patientLine);
        patientDimension.setIndices(Constant.HEADER_PATIENT);
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

    //create an observation with a reference to local patient resource
    public static Observation getObservation(String s, Map<Integer, Patient> patientMap) throws SubjectNotFoundException, AmbiguousSubjectException, ParseException, IllegalDataTypeException{
        ObservationFact observationFact = new ObservationFactImpl(s);
        Patient subject = patientMap.get(observationFact.patient_num());
        Observation observation = new Observation();
        Coding loinc = new Coding();
        String loincString = observationFact.concept_cd();
        if (loincString.toLowerCase().startsWith("loinc") && loincString.split(":").length == 2) {
            loinc.setSystem(Constant.LOINCSYSTEM).setSystem(loincString.split(":")[1]);
            observation.setCode(new CodeableConcept().addCoding(loinc));
        }
        observation.setSubject(new Reference(Integer.toString(observationFact.patient_num())));
        DateTimeType effectiveTime = new DateTimeType(observationFact.start_date());
        observation.setEffective(effectiveTime);
        observation.setValue(new SimpleQuantity()
                .setValue(observationFact.nval_num())
                .setUnit(observationFact.units_cd()));
        char interpretation = Character.toUpperCase(observationFact.valueflag_cd());
        V3ObservationInterpretation v3ObservationInterpretation;
        switch (interpretation) {
            case 'L':
                v3ObservationInterpretation = V3ObservationInterpretation.valueOf("L");
                break;
            case 'H':
                v3ObservationInterpretation = V3ObservationInterpretation.valueOf("H");
                break;

                default:
                    v3ObservationInterpretation = V3ObservationInterpretation.valueOf("N");

        }
        observation.setInterpretation(new CodeableConcept().addCoding(new Coding().setSystem("").setCode("L")));



        return observation;

    }

    public static Observation getObservation(String s) {
        return null;
    }

}
