package org.jax.database;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class LoadPatientOnPrednisonTest {
    @Test
    public void load() throws Exception {
        String hushSqlite = "jdbc:sqlite:/Users/zhangx/Documents/HUSH+_UNC_JAX/HUSH+_UNC_JAX.sqlite";
        SQLiteJDBCDriverConnection connection = new SQLiteJDBCDriverConnection(hushSqlite);
        String filePath = getClass().getClassLoader().getResource("data/patientOnPrednison.txt").getPath();
        TableImporter loader = new LoadPatientOnPrednison(connection.connect(), filePath);
        loader.load();

        connection.close();
    }

}