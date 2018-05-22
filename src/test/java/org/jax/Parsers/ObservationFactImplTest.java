package org.jax.Parsers;

import junit.framework.TestCase;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jax.DateModel.SourceSystemEnumType;
import org.junit.Test;

public class ObservationFactImplTest extends TestCase {

    private static ObservationFact obser1, obser2;
    private static SimpleDateFormat dateFormat;
    private static String header;

    @Override
    public void setUp() throws Exception {
        header= "\"encounter_num\",\"patient_num\",\"concept_cd\",\"provider_id\",\"start_date\",\"modifier_cd\",\"instance_num\"," +
                "\"valtype_cd\",\"tval_char\",\"nval_num\",\"valueflag_cd\",\"quantity_num\",\"units_cd\",\"end_date\"," +
                "\"location_cd\",\"observation_blob\",\"confidence_num\",\"update_date\",\"download_date\",\"import_date\"," +
                "\"sourcesystem_cd\",\"upload_id\",\"text_search_index\"";
        obser1 = new ObservationFactImpl("505163653,113179285,\"LOINC:26474-7\",\"NPI:1902123060\",\"2010-08-12 00:00:00\",\"@\",1,\"N\",\"E\",1.20000,\"L\",,\"x10 9th/L\",\"\",\"\",\"\",,\"2016-03-12 00:00:00\",\"\",\"2016-03-12 00:00:00\",\"ADS\",,");
        //obser2 = new ObservationFactImpl("746113071,140952640,\"LOINC:777-3\",\"NPI:1902923253\",\"2015-11-05 00:00:00\",\"@\",1,\"N\",\"E\",66.00000,\"[L]\",,\"10*9/L\",\"\",\"\",\"\",,\"2016-11-13 00:00:00\",\"\",\"2016-11-13 00:00:00\",\"EPIC\",,");
        dateFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
    }

    public void testConstructor() {
        assertNotNull(obser1);
        //assertNotNull(obser2);
    }

    public void testEncounter_num() throws Exception {
        assertEquals(505163653, obser1.encounter_num());
    }

    public void testPatient_num() throws Exception {
        assertEquals(113179285, obser1.patient_num());
    }

    public void testConcept_cd() throws Exception {
        assertNotNull(obser1.concept_cd());
        assertEquals("LOINC", obser1.concept_cd().getSystem());
        assertEquals("26474-7", obser1.concept_cd().getCode());
    }

    public void testProvider_id() throws Exception {
        assertNotNull(obser1.provider_id());
        assertEquals("NPI", obser1.provider_id().getSystem());
        assertEquals("1902123060", obser1.provider_id().getCode());
    }

    public void testStart_date() throws Exception {
        assertNotNull(obser1.start_date());
        Date expectedDate = dateFormat.parse("2010-08-12 00:00:00");
        assertEquals("2010-08-12 00:00:00", dateFormat.format(obser1.start_date()));
    }

    /**
    public void testModifier_cd() throws Exception {
        assertNotNull(obser1.modifier_cd());
        assertTrue(obser1.modifier_cd().size() == 1);
        assertEquals("@", obser1.modifier_cd().get(0));
    }
     **/

    public void testInstance_num() throws Exception {
        assertEquals(1, obser1.instance_num());
    }

    public void testValtype_cd() throws Exception {
        assertEquals('N', obser1.valtype_cd());
    }

    public void testTval_char() throws Exception {
        assertEquals('E', obser1.tval_char());
    }

    public void testNval_num() throws Exception {
        assertEquals(1.20000, obser1.nval_num(), 0.00001);
    }

    public void testValueflag_cd() throws Exception {
        assertEquals('L', obser1.valueflag_cd());
    }

    public void testQuatity_num() throws Exception {
        //assertNull(obser1.quantity_num());
    }

    public void testUnits_cd() throws Exception {
        assertEquals("x10 9th/L", obser1.units_cd());
    }

    public void testEnd_date() throws Exception {
        assertNull(obser1.end_date());
    }

    public void testLocation_cd() throws Exception {
        assertNull(obser1.location_cd());
    }

    public void testObservation_blob() throws Exception {
        assertNull(obser1.observation_blob());
    }

    public void testConfidence_num() throws Exception {
        assertEquals(Integer.MIN_VALUE, obser1.confidence_num());
    }

    public void testUpdate_date() throws Exception {
        assertNotNull(obser1.update_date());
        assertEquals("2016-03-12 00:00:00", dateFormat.format(obser1.update_date()));
    }

    public void testDownload_date() throws Exception {
        assertNull(obser1.download_date());
    }

    public void testImport_date() throws Exception {
        assertNotNull(obser1.import_date());
        assertEquals("2016-03-12 00:00:00", obser1.import_date());
    }

    public void testSourcesystem_cd() throws Exception {
        assertNotNull(obser1.sourcesystem_cd());
        assertEquals(SourceSystemEnumType.ADS, obser1.sourcesystem_cd());
    }

    public void testUpload_id() throws Exception {
        assertNull(obser1.upload_id());
    }

    public void testText_search_index() throws Exception {
        assertNull(obser1.text_search_index());
    }

}