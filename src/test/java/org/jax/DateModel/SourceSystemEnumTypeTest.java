package org.jax.DateModel;


import org.junit.Test;
import static org.junit.Assert.*;

public class SourceSystemEnumTypeTest {

    @Test
    public void testString() {
        SourceSystemEnumType source1 = SourceSystemEnumType.EPIC;
        //System.out.println(source1.toString());
        assertEquals("EPIC", source1.toString());
    }


}