package org.jax.database;

import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.*;

public class LoadPatientOnPrednisonTest {
    @Test
    public void load() throws Exception {
        String hushSqlite = "jdbc:sqlite:/Users/zhangx/Documents/HUSH+_UNC_JAX/HUSH+_UNC_JAX.sqlite";
        SQLiteJDBCDriverConnection connection = new SQLiteJDBCDriverConnection(hushSqlite);
        String filePath = getClass().getClassLoader().getResource("patientOnPrednison.txt").getPath();
        TableImporter loader = new LoadPatientOnPrednison(connection.connect(), filePath);
        loader.load();

        connection.close();
    }

}