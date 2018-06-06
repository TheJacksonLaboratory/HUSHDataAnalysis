package org.jax.Parsers;

import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jax.DateModel.SourceSystemEnumType;
import org.jax.Exception.IllegalDataTypeException;
import org.jax.Exception.MalformedLineException;
import org.junit.BeforeClass;
import org.junit.Ignore;
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

        private static String obser3 = "329632380,72481410,\"MDCTN:8751\",\"NPI:1043470750\",\"2015-01-14 00:00:00\",\"MED:FREQUENCY\",1,\"T\",\"TUESDAY, WEDNESDAY, THURSDAY, SATURDAY, SUNDAY\",,\"\",,\"\",\"2015-01-15 00:00:00\",\"\",\"\",,\"2016-12-14 00:00:00\",\"\",\"2016-12-15 00:00:00\",\"EPIC\",,";

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

    @Test
    public void testReg() {
        String pattern1 = ",,";
        //String pattern2 = ",\\d{1},";
        System.out.println("original:\n" + obser3);
        //System.out.println("replace\",,\":\n" + obser3.replaceAll(pattern1, ",\"\","));
        //String s = obser3.replaceFirst(pattern2)
        Pattern pattern2 = Pattern.compile(",(\\d+),");
        Matcher matcher = pattern2.matcher(obser3);
        obser3 = obser3.replaceAll(",,", ",\"\",");
        System.out.println(obser3);
        obser3 = obser3.replaceAll(",(\\d+),", ",\"$1\",");
        System.out.println(obser3);
        obser3 = obser3.replaceAll("^(\\d+),", "\"$1\",");
        System.out.println(obser3);
        obser3 = obser3.replaceAll(",$", ",\"\"");
        System.out.println(obser3);
        assertEquals(23, obser3.split("\",\"").length);

        /**
        int group = 0;
        while (matcher.find()) {
            group++;
            String matched = matcher.group();
            System.out.println("matched:" + matched);
            System.out.println(obser3.replace(matched, String.format("\"%s\"", matched)));
        }
         **/

    }

    @Test
    @Ignore
    public void testEntireTable() throws Exception {
        final String observationPath = "/Users/zhangx/Documents/HUSH+_UNC_JAX/Hush+UNC_JAX/HUSH+/OBSERVATION_FACT.txt";
        BufferedReader reader = new BufferedReader(new FileReader(observationPath));
        String line = reader.readLine();
        int count = 0;
        while (line != null) {
            count++;
            if (line.startsWith("\"encounter_num\"")) { //skip header
                //do nothing for header
            } else {
                try {
                    ObservationFact observationFact = new ObservationFactLazyImpl(line);
                } catch (MalformedLineException e) {
                    System.out.println("Not 23 elements: " + line);
                } catch (IllegalDataTypeException e) {
                    System.out.println("Illegal data type: " + line);
                }

            }
            line = reader.readLine();
            if (count % 1000000 == 0) {
                System.out.println("processed line: " + count);
                //System.out.println(line);
            }
        }
    }

    @Test
    public void testSpecialCase() throws Exception {
        String record = "624826499,80983881,\"LOINC:5799-2\",\"NPI:1851472971\",\"2016-12-10 00:00:00\",\"@\",1,\"T\",\"Large\",,\"[A]\",,\"\",\"\",\"\",\"\",,\"2016-12-15 00:00:00\",\"\",\"2016-12-15 00:00:00\",\"EPIC\",,";
        ObservationFact observationFact = new ObservationFactLazyImpl(record);
    }

    @Test
    public void testSpecialCase2() throws Exception {
        String record = "503592180,96470504,\"LOINC:11475-1\",\"NPI:1275800526\",\"2013-05-11 00:00:00\",\"@\",1,\"N\",\"L\",10000.00000,\"\",,\",cfu/ml\",\"\",\"\",\"\",,\"2016-02-25 00:00:00\",\"\",\"2016-02-25 00:00:00\",\"ADS\",,";
        ObservationFact observationFact = new ObservationFactLazyImpl(record);
    }

}