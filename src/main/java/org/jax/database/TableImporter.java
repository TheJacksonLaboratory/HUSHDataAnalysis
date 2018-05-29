package org.jax.database;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class TableImporter {
    private Connection dbConn;
    private String filePath;

    public TableImporter(Connection dbConn, String filePath) {
        this.dbConn = dbConn;
        this.filePath = filePath;
    }

    public Connection getDbConn() {
        return this.dbConn;
    }

    public String getFilePath() {
        return this.filePath;
    }

    abstract void load() throws IOException, SQLException;
}
