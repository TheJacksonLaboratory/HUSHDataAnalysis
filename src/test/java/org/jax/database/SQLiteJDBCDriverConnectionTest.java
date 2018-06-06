package org.jax.database;

import org.junit.Ignore;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.*;

@Ignore
public class SQLiteJDBCDriverConnectionTest {
    @Test
    public void connect() throws Exception{
        String hushSqlite = "jdbc:sqlite:/Users/zhangx/Documents/HUSH+_UNC_JAX/HUSH+_UNC_JAX.sqlite";
        SQLiteJDBCDriverConnection connection = new SQLiteJDBCDriverConnection(hushSqlite);
        //System.out.println(hushConn.getMetaData().);
        String query = "SELECT * FROM VISIT_DIMENSION LIMIT 5";
        Statement stmt = connection.connect().createStatement();
        ResultSet resultSet = stmt.executeQuery(query);
        int count = 0;
        while (resultSet.next()) {
            //System.out.println(resultSet.getInt(1) + "\t" + resultSet.getInt(2));
            count++;
        }
        assertEquals(5, count);
        connection.close();

    }

    @Test
    public void close() throws Exception {
        String hushSqlite = "jdbc:sqlite:/Users/zhangx/Documents/HUSH+_UNC_JAX/HUSH+_UNC_JAX.sqlite";
        SQLiteJDBCDriverConnection hushConn = new SQLiteJDBCDriverConnection(hushSqlite);
        hushConn.close();

    }

}