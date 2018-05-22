package org.jax.Parsers;

import junit.framework.TestCase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jax.DateModel.SourceSystemEnumType;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;



public class ObservationFactImplTest  {

        private static String header= "\"encounter_num\",\"patient_num\",\"concept_cd\",\"provider_id\",\"start_date\",\"modifier_cd\"," +
                "\"instance_num\"," +
                "\"valtype_cd\",\"tval_char\",\"nval_num\",\"valueflag_cd\",\"quantity_num\",\"units_cd\",\"end_date\"," +
                "\"location_cd\",\"observation_blob\",\"confidence_num\",\"update_date\",\"download_date\",\"import_date\"," +
                "\"sourcesystem_cd\",\"upload_id\",\"text_search_index\"";

        private static String obser1= "505163653,113179285,\"LOINC:26474-7\",\"NPI:1902123060\",\"2010-08-12 00:00:00\",\"@\",1,\"N\",\"E\",1.20000," +
                "\"L\",,\"x10 9th/L\",\"\",\"\",\"\",,\"2016-03-12 00:00:00\",\"\",\"2016-03-12 00:00:00\",\"ADS\",,";
        //obser2 = new ObservationFactImpl("746113071,140952640,\"LOINC:777-3\",\"NPI:1902923253\",\"2015-11-05 00:00:00\",\"@\",1,\"N\",\"E\",66.00000,\"[L]\",,\"10*9/L\",\"\",\"\",\"\",,\"2016-11-13 00:00:00\",\"\",\"2016-11-13 00:00:00\",\"EPIC\",,");
        private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        static ObservationFactImpl ObservFact = new ObservationFactImpl(obser1);


    @BeforeClass
    public  static void setupIndices_ObservFact() throws ParseException {
        ObservationFactImpl ObFact = new ObservationFactImpl(obser1);
        ObFact.setIndices_ObservFact(header);
    }


    @Test
    public void testConstructor()throws Exception {
        ObservFact.observationFactEntry();
        //assertNotNull(obser1);
        //assertNotNull(obser2);
    }
    @Test
    public void testEncounter_num() throws Exception {
        ObservFact.observationFactEntry();
        assertEquals(505163653, ObservFact.encounter_num());
    }
    @Test
    public void testPatient_num() throws Exception {
        ObservFact.observationFactEntry();
        assertEquals(113179285, ObservFact.patient_num());
    }
    @Test
    public void testConcept_cd() throws Exception {
        ObservFact.observationFactEntry();
        assertNotNull(ObservFact.concept_cd());
       // assertEquals("LOINC", ObservFact.concept_cd().getSystem());
        //assertEquals("26474-7", ObservFact.concept_cd().getCode());
    }
    @Test
    public void testProvider_id() throws Exception {
        ObservFact.observationFactEntry();
        assertNotNull(ObservFact.provider_id());
       // assertEquals("NPI", ObservFact.provider_id().getSystem());
        //assertEquals("1902123060", ObservFact.provider_id().getCode());
    }
    @Test
    public void testStart_date() throws Exception {
        ObservFact.observationFactEntry();
        assertNotNull(ObservFact.start_date());
        Date expectedDate = dateFormat.parse("2010-08-12 00:00:00");
        assertEquals("2010-08-12 00:00:00", dateFormat.format(ObservFact.start_date()));
    }

    /**
    public void testModifier_cd() throws Exception {
        assertNotNull(obser1.modifier_cd());
        assertTrue(obser1.modifier_cd().size() == 1);
        assertEquals("@", obser1.modifier_cd().get(0));
    }
     **/
    @Test
    public void testInstance_num() throws Exception {
        ObservFact.observationFactEntry();
        assertEquals(1, ObservFact.instance_num());
    }
    @Test
    public void testValtype_cd() throws Exception {
        ObservFact.observationFactEntry();
        assertEquals("N", ObservFact.valtype_cd());
    }
    @Test
    public void testTval_char() throws Exception {
        ObservFact.observationFactEntry();
        assertEquals("E", ObservFact.tval_char());
    }
    @Test
    public void testNval_num() throws Exception {
        ObservFact.observationFactEntry();
        assertEquals(1.20000, ObservFact.nval_num(), 0.00001);
    }
    @Test
    public void testValueflag_cd() throws Exception {
        ObservFact.observationFactEntry();
        assertEquals('L', ObservFact.valueflag_cd());
    }
    @Test
    public void testQuatity_num() throws Exception {
        //assertNull(obser1.quantity_num());
    }
    @Test
    public void testUnits_cd() throws Exception {
        ObservFact.observationFactEntry();
        assertEquals("x10 9th/L", ObservFact.units_cd());
    }
    @Test
    public void testEnd_date() throws Exception {
        ObservFact.observationFactEntry();
        assertNull(ObservFact.end_date());
    }
    @Test
    public void testLocation_cd() throws Exception {
        ObservFact.observationFactEntry();
        assertNull(ObservFact.location_cd());
    }
    @Test
    public void testObservation_blob() throws Exception {
        ObservFact.observationFactEntry();
        assertNull(ObservFact.observation_blob());
    }
    /*@Test
    public void testConfidence_num() throws Exception {
        ObservFact.observationFactEntry();
        assertEquals(Integer.MIN_VALUE, ObservFact.confidence_num());
    }*/
    @Test
    public void testUpdate_date() throws Exception {
        ObservFact.observationFactEntry();
        assertNotNull(ObservFact.update_date());
        assertEquals("2016-03-12 00:00:00", dateFormat.format(ObservFact.update_date()));
    }
    @Test
    public void testDownload_date() throws Exception {
        ObservFact.observationFactEntry();
        assertNull(ObservFact.download_date());
    }
    /*@Test
    public void testImport_date() throws Exception {
        ObservFact.observationFactEntry();
        assertNotNull(ObservFact.import_date());
        assertEquals("2016-03-12 00:00:00", ObservFact.import_date());
    }*/
    @Test
    public void testSourcesystem_cd() throws Exception {
        ObservFact.observationFactEntry();
        assertNotNull(ObservFact.sourcesystem_cd());
        assertEquals(SourceSystemEnumType.ADS, ObservFact.sourcesystem_cd());
    }
    @Test
    public void testUpload_id() throws Exception {
        ObservFact.observationFactEntry();
        assertEquals(0, ObservFact.upload_id());
    }
    @Test
    public void testText_search_index() throws Exception {
        ObservFact.observationFactEntry();
        assertNull(ObservFact.text_search_index());
    }

}