package org.jax.Parsers;

import junit.framework.TestCase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.jax.DateModel.SourceSystemEnumType;
import org.jax.Exception.IllegalDataTypeException;
import org.jax.Exception.MalformedLineException;
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

        private static String obser2 = "286599439,50110676,\"LOINC:728-6\",\"NPI:1811058670\",\"2015-12-08 00:00:00\",\"@\",1,\"T\",\"1+\",,\"\",,\"\",\"\",\"\",\"\",,\"2016-11-01 00:00:00\",\"\",\"2016-11-01 00:00:00\",\"EPIC\",,";
        //obser2 = new ObservationFactImpl("746113071,140952640,\"LOINC:777-3\",\"NPI:1902923253\",\"2015-11-05 00:00:00\",\"@\",1,\"N\",\"E\",66.00000,\"[L]\",,\"10*9/L\",\"\",\"\",\"\",,\"2016-11-13 00:00:00\",\"\",\"2016-11-13 00:00:00\",\"EPIC\",,");
        private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        static ObservationFact observFact; //= new ObservationFactImpl(obser1);


    @BeforeClass
    public  static void setupIndices_ObservFact() throws IllegalDataTypeException, MalformedLineException {
        //ObservationFactImpl ObFact = new ObservationFactImpl(obser1);
        //ObFact.setIndices_ObservFact(header);
        //observFact = new ObservationFactImpl(obser1);
        //Arrays.stream(obser1.split(",")).forEach(System.out::println);
        observFact = new ObservationFactLazyImpl(obser1);

        ObservationFact observationFact2 = new ObservationFactLazyImpl(obser2);
    }


    @Test
    public void testConstructor()throws Exception {
        //ObservFact.observationFactEntry();
        //assertNotNull(obser1);
        //assertNotNull(obser2);
    }
    @Test
    public void testEncounter_num() throws Exception {
        //ObservFact.observationFactEntry();
        assertEquals(505163653, observFact.encounter_num());
    }
    @Test
    public void testPatient_num() throws Exception {
        //observFact.observationFactEntry();
        assertEquals(113179285, observFact.patient_num());
    }
    @Test
    public void testConcept_cd() throws Exception {
        //ObservFact.observationFactEntry();
        assertNotNull(observFact.concept_cd());
        assertEquals("LOINC:26474-7", observFact.concept_cd());
        //System.out.println(observFact.concept_cd());
        //assertEquals("LOINC", observFact.concept_cd().getSystem());
        //assertEquals("26474-7", observFact.concept_cd().getCode());
    }
    @Test
    public void testProvider_id() throws Exception {
        //ObservFact.observationFactEntry();
        assertNotNull(observFact.provider_id());
        assertEquals("NPI:1902123060", observFact.provider_id());
       // assertEquals("NPI", ObservFact.provider_id().getSystem());
        //assertEquals("1902123060", ObservFact.provider_id().getCode());
    }
    @Test
    public void testStart_date() throws Exception {
        //ObservFact.observationFactEntry();
        assertNotNull(observFact.start_date());
        Date expectedDate = dateFormat.parse("2010-08-12 00:00:00");
        assertEquals("2010-08-12 00:00:00", dateFormat.format(observFact.start_date()));
    }

    /**Complete modifier
    public void testModifier_cd() throws Exception {
        assertNotNull(obser1.modifier_cd());
        assertTrue(obser1.modifier_cd().size() == 1);
        assertEquals("@", obser1.modifier_cd().get(0));
    }
     **/
    @Test
    public void testInstance_num() throws Exception {
        //ObservFact.observationFactEntry();
        assertEquals(1, observFact.instance_num());
    }
    @Test
    public void testValtype_cd() throws Exception {
        //ObservFact.observationFactEntry();
        assertEquals("N", observFact.valtype_cd());
    }
    @Test
    public void testTval_char() throws Exception {
        //ObservFact.observationFactEntry();
        assertEquals("E", observFact.tval_char());
    }
    @Test
    public void testNval_num() throws Exception {
        //ObservFact.observationFactEntry();
        assertEquals(1.20000, observFact.nval_num(), 0.00001);
    }
    @Test
    public void testValueflag_cd() throws Exception {
        //ObservFact.observationFactEntry();
        assertEquals('L', observFact.valueflag_cd());
    }
    @Test
    public void testQuatity_num() throws Exception {
        //assertNull(obser1.quantity_num());
    }
    @Test
    public void testUnits_cd() throws Exception {
        //ObservFact.observationFactEntry();
        assertEquals("x10 9th/L", observFact.units_cd());
    }
    @Test
    public void testEnd_date() throws Exception {
        //ObservFact.observationFactEntry();
        assertNull(observFact.end_date());
    }
    @Test
    public void testLocation_cd() throws Exception {
        //ObservFact.observationFactEntry();
        //assertNull(observFact.location_cd());
        assertEquals("", observFact.location_cd());
    }
    @Test
    public void testObservation_blob() throws Exception {
        //ObservFact.observationFactEntry();
        //assertNull(observFact.observation_blob());
        assertEquals("", observFact.observation_blob());
    }
    /*@Test
    public void testConfidence_num() throws Exception {
        ObservFact.observationFactEntry();
        assertEquals(Integer.MIN_VALUE, ObservFact.confidence_num());
    }*/
    @Test
    public void testUpdate_date() throws Exception {
        //ObservFact.observationFactEntry();
        assertNotNull(observFact.update_date());
        assertEquals("2016-03-12 00:00:00", dateFormat.format(observFact.update_date()));
    }
    @Test
    public void testDownload_date() throws Exception {
        //ObservFact.observationFactEntry();
        assertNull(observFact.download_date());
    }

    //Error??
    /*@Test
    public void testImport_date() throws Exception {
        ObservFact.observationFactEntry();
        assertNotNull(ObservFact.import_date());
        assertEquals("2016-03-12 00:00:00", ObservFact.import_date());
    }*/
    @Test
    public void testSourcesystem_cd() throws Exception {
        //ObservFact.observationFactEntry();
        assertNotNull(observFact.sourcesystem_cd());
        assertEquals(SourceSystemEnumType.ADS, observFact.sourcesystem_cd());
    }
    @Test
    public void testUpload_id() throws Exception {
        //ObservFact.observationFactEntry();
        assertEquals(Integer.MIN_VALUE, observFact.upload_id());
    }
    @Test
    public void testText_search_index() throws Exception {
        //ObservFact.observationFactEntry();
        //assertNull(observFact.text_search_index());
        assertEquals("", observFact.text_search_index());
    }

}