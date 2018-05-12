package org.jax.Parsers;

import org.hl7.fhir.dstu3.model.codesystems.AdministrativeGender;
import org.jax.DateModel.SourceSystemEnumType;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;


import java.io.IOException;
import java.text.SimpleDateFormat;
import static org.junit.Assert.*;


public class PatientDimensionImplTest  {

      private static String header = "\"patient_num\",\"vital_status_cd\",\"birth_date\",\"death_date\",\"sex_cd\",\"age_in_years_num\"," +
           "\"language_cd\",\"race_cd\",\"marital_status_cd\",\"religion_cd\",\"zip_cd\",\"statecityzip_path\",\"income_cd\"," +
           "\"patient_blob\",\"update_date\",\"download_date\",\"import_date\",\"sourcesystem_cd\",\"upload_id\"";

    private static String patient1 = "176974728,\"UD\",\"1979-08-07 00:00:00\",\"\",\"F\",,\"114\",\"9\",\"2\",\"\",\"\",\"\"," +
            "\"\",\"\",\"2017-03-12 00:00:00\",\"\",\"2015-03-19 00:00:00\",\"EPIC\",";

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");

    @BeforeClass
    public static void setupIndices()  {
        PatientDimensionImpl pat = new PatientDimensionImpl(patient1);
        pat.setIndices(header);
    }

    @Test
    public void testPatient_num() throws Exception {
      PatientDimensionImpl patient = new PatientDimensionImpl(patient1);
      patient.patientDimensionEntry();
      assertEquals(176974728, patient.patient_num());
    }
    @Test
    public void testVital_status_cd() throws Exception {
        PatientDimensionImpl patient = new PatientDimensionImpl(patient1);
        patient.patientDimensionEntry();
        assertEquals("UD", patient.vital_status_cd());
    }
    @Test//TODO
    public void testBirth_date() throws Exception {
        PatientDimensionImpl patient = new PatientDimensionImpl(patient1);
        patient.patientDimensionEntry();
        assertNotNull(patient.birth_date());
        assertEquals("1979-08-07 00:00:00", dateFormat.format(patient.birth_date()));
    }
    @Test
    public void testDeath_date() throws Exception {
        PatientDimensionImpl pat = new PatientDimensionImpl(patient1);
        assertNull(pat.death_date());
    }
    @Test
    public void testSex_cd() throws Exception {
        PatientDimensionImpl patient = new PatientDimensionImpl(patient1);
        assertEquals(AdministrativeGender.FEMALE, patient.sex_cd());
    }
    @Test
    public void testAge_in_years_num() throws Exception {
        PatientDimensionImpl patient = new PatientDimensionImpl(patient1);

        assertEquals(Double.MIN_VALUE, patient.age_in_years_num());
    }
    @Test
    public void testLanguage_cd() throws Exception {
        PatientDimensionImpl patient = new PatientDimensionImpl(patient1);

        assertEquals(114, patient.language_cd());
    }
    @Test
    public void testRace() throws Exception {
        PatientDimensionImpl patient = new PatientDimensionImpl(patient1);

        assertEquals(9, patient.race());
    }
    @Test
    public void testMarital_status_cd() throws Exception {
        PatientDimensionImpl patient = new PatientDimensionImpl(patient1);
        assertEquals(2, patient.marital_status_cd());
    }


    @Test
    public void testUpdate_date() throws Exception {
        PatientDimensionImpl patient = new PatientDimensionImpl(patient1);

        assertNotNull(patient.update_date());
        assertEquals("2017-03-12 00:00:00", dateFormat.format(patient.update_date()));
    }
    @Test
    public void testDownload_date() throws Exception {
        PatientDimensionImpl patient = new PatientDimensionImpl(patient1);

        assertNull(patient.download_date());
    }
    @Test
    public void testImport_date() throws Exception {
        PatientDimensionImpl patient = new PatientDimensionImpl(patient1);

        assertNotNull(patient.import_date());
        assertEquals("2015-03-19 00:00:00", patient.import_date());
    }
    @Test
    public void testSourcesystem_cd() throws Exception {
        PatientDimensionImpl patient = new PatientDimensionImpl(patient1);

        assertEquals(SourceSystemEnumType.EPIC, patient.sourcesystem_cd());
    }
    @Test
    public void testUpload_id() throws Exception {
        PatientDimensionImpl patient = new PatientDimensionImpl(patient1);
        assertNull(patient.upload_id());
    }

}