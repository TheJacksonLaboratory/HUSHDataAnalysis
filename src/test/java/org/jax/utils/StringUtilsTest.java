package org.jax.utils;

import org.junit.Test;

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

}