package org.jax.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteJDBCDriverConnection {

    //String url = "jdbc:sqlite:C:/sqlite/db/chinook.db";
    private String databasePath = null;
    private Connection conn = null;

    public SQLiteJDBCDriverConnection(String databasePath) {
        this.databasePath = databasePath;
    }

    /**
     * Connect to a sample database
     */
    public Connection connect() throws SQLException {
        // create a connection to the database
        if (this.conn == null) {
            this.conn = DriverManager.getConnection(this.databasePath);
 System.out.println("connection established");
        }
        return this.conn;
        //System.out.println("Connection to SQLite has been established.");
    }

    public void close() throws SQLException {
        if (this.conn != null) {
            this.conn.close();
        }
    }
}
