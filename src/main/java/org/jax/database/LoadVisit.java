package org.jax.database;

import java.sql.Connection;

public class LoadVisit extends TableImporter {

    public LoadVisit(Connection dbConn, String filePath) {
        super(dbConn, filePath);
    }

    @Override
    void load() {

    }
}
