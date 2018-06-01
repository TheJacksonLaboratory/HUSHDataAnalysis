package org.jax.utils;

import org.jax.App;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class StringUtilsTest {
    @Test
    public void stripEndQuotes() throws Exception {
        assertEquals("quotes", StringUtils.stripEndQuotes("\"quotes\""));
        assertEquals("quotes", StringUtils.stripEndQuotes("\"quotes"));
        assertEquals("quotes", StringUtils.stripEndQuotes("quotes\""));
        assertEquals("quotes", StringUtils.stripEndQuotes("quotes"));
        assertEquals("qu\"ote\"s", StringUtils.stripEndQuotes("qu\"ote\"s"));
        assertEquals("qu\"otes", StringUtils.stripEndQuotes("qu\"otes"));
    }

    @Test
    @Ignore
    public void testres() throws Exception{
        Set<String> predCodes = App.importPrednisoneCodes();
        assertNotNull(predCodes);
        System.out.println(predCodes.size());
    }
}