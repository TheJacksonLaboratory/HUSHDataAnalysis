package org.jax.database;

import java.sql.Connection;

public class LoadPatient extends TableImporter {

    public LoadPatient(Connection dbConn, String filePath) {
        super(dbConn, filePath);
    }

    @Override
    public void load() {

    }
}
