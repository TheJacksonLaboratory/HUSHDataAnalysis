package org.jax.Parsers;

import org.jax.Exception.IllegalDataTypeException;
import org.jax.Exception.MalformedLineException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;

import static org.junit.Assert.*;

public class ConceptDimensionImplTest {
    private String record1;
    private String record2;
    private ConceptDimension conceptDimension1;
    private ConceptDimension conceptDimension2;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Before
    public void setUp() throws Exception {
        record1 = "\"\\i2b2\\MEDS\\N0000189939\\N0000175655\\N0000147889\\N0000147256\\MDCTN:171202\\\"|\"MDCTN:171202\"|\"Leuprolide Acetate\"||\"2016-10-27 10:57:38\"||\"2016-10-27 10:57:38\"|\"MANUAL\"|";

        record2 = "\"\\i2b2\\MEDS\\N0000010574\\N0000029253\\N0000029358\\N0000029437\\RX:237420\\MDCTN:57896074606|ADS\\\"|\"MDCTN:57896074606|ADS\"|\"Calcium Carbonate 1500 MG Oral Tablet\"||\"2016-10-27 10:57:38\"||\"2016-10-27 10:57:38\"|\"MANUAL\"|";

        conceptDimension1 = new ConceptDimensionImpl(record1);
        conceptDimension2 = new ConceptDimensionImpl(record2);
    }

    @Test
    public void concept_path() throws Exception {
        assertEquals("\\i2b2\\MEDS\\N0000189939\\N0000175655\\N0000147889\\N0000147256\\MDCTN:171202\\", conceptDimension1.concept_path());
        assertEquals("\\i2b2\\MEDS\\N0000010574\\N0000029253\\N0000029358\\N0000029437\\RX:237420\\MDCTN:57896074606|ADS\\", conceptDimension2.concept_path());
    }

    @Test
    public void concept_cd() throws Exception {
        assertEquals("MDCTN:171202", conceptDimension1.concept_cd());
        assertEquals("MDCTN:57896074606|ADS", conceptDimension2.concept_cd());
    }

    @Test
    public void name_char() throws Exception {
        assertEquals("Leuprolide Acetate", conceptDimension1.name_char());
        assertEquals("Calcium Carbonate 1500 MG Oral Tablet", conceptDimension2.name_char());
    }

    @Test
    public void concept_blob() throws Exception {
        assertNull(conceptDimension1.concept_blob());
        assertNull(conceptDimension2.concept_blob());
    }

    @Test
    public void update_date() throws Exception {
        assertEquals("2016-10-27 10:57:38", dateFormat.format(conceptDimension1.update_date()));
        assertEquals("2016-10-27 10:57:38", dateFormat.format(conceptDimension2.update_date()));
    }

    @Test
    public void download_date() throws Exception {
        assertNull(conceptDimension1.download_date());
        assertNull(conceptDimension2.download_date());
    }

    @Test
    public void import_date() throws Exception {
        assertEquals("2016-10-27 10:57:38", dateFormat.format(conceptDimension1.import_date()));
        assertEquals("2016-10-27 10:57:38", dateFormat.format(conceptDimension2.import_date()));
    }

    @Test
    public void sourcesystem_cd() throws Exception {
        assertEquals("MANUAL", conceptDimension1.sourcesystem_cd());
        assertEquals("MANUAL", conceptDimension2.sourcesystem_cd());
    }

    @Test
    public void upload_id() throws Exception {
        assertEquals(Integer.MIN_VALUE, conceptDimension1.upload_id());
        assertEquals(Integer.MIN_VALUE, conceptDimension2.upload_id());
    }

    @Test
    @Ignore
    public void testEntireFile() {
        String path = "/Users/zhangx/Documents/HUSH+_UNC_JAX/Hush+UNC_JAX/HUSH+/CONCEPT_DIMENSION.csv";
        try (BufferedReader reader = new BufferedReader(new FileReader(path))){
            String line = reader.readLine();
            while (line != null) {
                try {
                    ConceptDimension conceptDimension = new ConceptDimensionImpl(line);
                } catch (MalformedLineException e) {
                    System.out.println("Malformed line: " + line);
                } catch (IllegalDataTypeException e) {
                    System.out.println("Illegal data type in line: " + line);
                }
                line = reader.readLine();
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSpecialCase() throws Exception{
        String record = "\"\\i2b2\\ENC\\POC\\UNC\\LegInp\\1ADN\\\"|\"NURS|1ADN|ADS\"|\"ONE ANDERSON - Legacy\"|||||\"ADS\"|";
        record = record.replaceAll("\\|$", "\\|\"\"");
        record = record.replaceAll("\\|{2}", "\\|\"\"\\|");
        record = record.replaceAll("\\|{2}", "\\|\"\"\\|");
        //System.out.println(record);
        ConceptDimension conceptDimension = new ConceptDimensionImpl(record);
    }

}