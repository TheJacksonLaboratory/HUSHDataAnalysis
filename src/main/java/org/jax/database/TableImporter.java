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

    public abstract void load() throws IOException, SQLException;

    public java.sql.Date convertUtilToSql(java.util.Date uDate) {
        if (uDate == null) {
            return null;
        }

        java.sql.Date sDate = new java.sql.Date(uDate.getTime());
        return sDate;
    }
}
