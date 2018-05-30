package org.jax.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class LoadPatientOnPrednison extends TableImporter {

    private static Logger logger = LoggerFactory.getLogger(LoadPatientOnPrednison.class);
    public LoadPatientOnPrednison(Connection dbConn, String filePath) {
        super(dbConn, filePath);
    }

    @Override
    public void load() throws IOException, SQLException{

        String create = "CREATE TABLE IF NOT EXISTS PatientOnPrednison ( \n"
                + " patient_num integer PRIMARY KEY, \n"
                + " prescrib_times integer \n"
                + ");";

        Statement stmt = getDbConn().createStatement();
        stmt.execute(create);


        String path = super.getFilePath();
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line = reader.readLine();
        int patient_num;
        int prescrib_times;
        String insertStmt = "INSERT OR IGNORE INTO PatientOnPrednison(patient_num, prescrib_times) VALUES(?, ?)";
        while (line != null) {
            String [] elements = line.split("\t");
            if (elements.length == 2) {
                try {
                    patient_num = Integer.parseInt(elements[0]);
                    prescrib_times = Integer.parseInt(elements[1]);
                    PreparedStatement psmt = getDbConn().prepareStatement(insertStmt);
                    psmt.setInt(1, patient_num);
                    psmt.setInt(2, prescrib_times);
                    psmt.executeUpdate();
                } catch (Exception e) {
                    logger.error("non-integers encountered: " + line);
                }
            } else {
                logger.error("num of elements are not two: " + line);
            }
            line = reader.readLine();
        }

        reader.close();

    }
}
