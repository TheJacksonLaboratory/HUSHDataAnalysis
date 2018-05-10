package org.jax.Parsers;

import junit.framework.TestCase;
import org.hl7.fhir.dstu3.model.codesystems.AdministrativeGender;
import org.jax.DateModel.SourceSystemEnumType;
import org.junit.Ignore;

import java.text.SimpleDateFormat;

public class PatientDimensionImplTest extends TestCase {


    private static PatientDimension patient1;
    private static SimpleDateFormat dateFormat;

    @Override
    public void setUp() throws Exception {

        patient1 = new PatientDimensionImpl("176974728,\"UD\",\"1979-08-07 00:00:00\",\"\",\"F\",,\"114\",\"9\",\"2\",\"\",\"\",\"\",\"\",\"\",\"2017-03-12 00:00:00\",\"\",\"2015-03-19 00:00:00\",\"EPIC\",");
        dateFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
    }

    public void testPatient_num() throws Exception {
        assertEquals(176974728, patient1.patient_num());
    }

    public void testVital_status_cd() throws Exception {
        assertEquals("UD", patient1.vital_status_cd());
    }

    public void testBirth_date() throws Exception {
        assertNotNull(patient1.birth_date());
        assertEquals("1979-08-07 00:00:00", dateFormat.format(patient1.birth_date()));
    }

    public void testDeath_date() throws Exception {
        assertNull(patient1.death_date());
    }

    public void testSex_cd() throws Exception {
        assertEquals(AdministrativeGender.FEMALE, patient1.sex_cd());
    }

    public void testAge_in_years_num() throws Exception {
        assertEquals(Double.MIN_VALUE, patient1.age_in_years_num());
    }

    public void testLanguage_cd() throws Exception {
        assertEquals(114, patient1.language_cd());
    }

    public void testRace() throws Exception {
        assertEquals(9, patient1.race());
    }

    public void testMarital_status_cd() throws Exception {
        assertEquals(2, patient1.marital_status_cd());
    }

    @Ignore
    public void testReligion_cd() throws Exception {

    }

    @Ignore
    public void testZip_cd() throws Exception {
    }

    @Ignore
    public void testStatecityzip_path() throws Exception {
    }

    @Ignore
    public void testIncome_cd() throws Exception {
    }

    @Ignore
    public void testPatient_blob() throws Exception {

    }

    public void testUpdate_date() throws Exception {
        assertNotNull(patient1.update_date());
        assertEquals("2017-03-12 00:00:00", dateFormat.format(patient1.update_date()));
    }

    public void testDownload_date() throws Exception {
        assertNull(patient1.download_date());
    }

    public void testImport_date() throws Exception {
        assertNotNull(patient1.import_date());
        assertEquals("2015-03-19 00:00:00", patient1.import_date());
    }

    public void testSourcesystem_cd() throws Exception {
        assertEquals(SourceSystemEnumType.EPIC, patient1.sourcesystem_cd());
    }

    public void testUpload_id() throws Exception {
        assertNull(patient1.upload_id());
    }

}