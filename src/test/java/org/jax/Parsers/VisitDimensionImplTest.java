package org.jax.Parsers;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class VisitDimensionImplTest {

    private static VisitDimension visit1;
    private static VisitDimension visit2;
    private static VisitDimension visit3;

    @BeforeClass
    public static void setUp() throws Exception {
        //System.out.println("522173134,93902732,\"UD\",\"\",\"\",\"OUTPATIENT\",\"UNCHCS\",\"UNCHCS/\",0,\"\",\"\",\"\",\"\",\"\",".split(",").length);
        visit1 = new VisitDimensionImpl("522173134,93902732,\"UD\",\"\",\"\",\"OUTPATIENT\",\"UNCHCS\",\"UNCHCS/\",0,\"\",\"\",\"\",\"\",\"\",");
        visit2 = new VisitDimensionImpl("223833330,117981234,\"UL\",\"\",\"\",\"EMERGENCY\",\"UNHCS\",\"UNCHCS/\",0,\"\",\"\",\"\",\"\",\"\",");
        visit3 = new VisitDimensionImpl("250142598,66497337,\"UL\",\"\",\"\",\"\",\"UNHCS\",\"UNCHCS/\",0,\"\",\"\",\"\",\"\",\"\",");
    }

    @Test
    public void encounter_num() throws Exception {
        assertEquals(522173134, visit1.encounter_num());
        assertEquals(223833330, visit2.encounter_num());
        assertEquals(250142598, visit3.encounter_num());
    }

    @Test
    public void patient_num() throws Exception {
        assertEquals(93902732, visit1.patient_num());
        assertEquals(117981234, visit2.patient_num());
        assertEquals(66497337, visit3.patient_num());
    }

    @Test
    public void inOut_cd() throws Exception {
        assertEquals("OUTPATIENT", visit1.inOut_cd());
        assertEquals("EMERGENCY", visit2.inOut_cd());
        assertEquals("", visit3.inOut_cd());
    }

}