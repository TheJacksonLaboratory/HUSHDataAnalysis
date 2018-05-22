package org.jax.Parsers;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConceptDimensionImplTest {
    private String record1;
    private String record2;
    private ConceptDimension conceptDimension1;
    private ConceptDimension conceptDimension2;

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
    }

    @Test
    public void update_date() throws Exception {
    }

    @Test
    public void download_date() throws Exception {
    }

    @Test
    public void import_date() throws Exception {
    }

    @Test
    public void sourcesystem_cd() throws Exception {
    }

    @Test
    public void upload_id() throws Exception {
    }

}