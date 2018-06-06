package org.jax.database;

import org.jax.Exception.IllegalDataTypeException;
import org.jax.Exception.MalformedLineException;
import org.jax.Parsers.ConceptDimension;
import org.jax.Parsers.ConceptDimensionImpl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class ConceptImporter extends TableImporter {

    public ConceptImporter(Connection connection, String filePath) {
        super(connection, filePath);
    }

    @Override
    public void load() throws IOException, SQLException {

        String create = "CREATE TABLE IF NOT EXISTS CONCEPT_DIMENSION (\n" +
                "concept_path VARCHAR(700) PRIMARY KEY," +
                "concept_cd VARCHAR(20)," +
                "name_char VARCHAR(2000)," +
                " concept_blob CLOB," +
                " update_date DATE," +
                " download_date DATE," +
                " import_date DATE," +
                " sourcesystem_cd VARCHAR(50)," +
                " upload_id INTEGER);";

        Statement stmt = getDbConn().createStatement();
        stmt.execute(create);


        String insert = "INSERT OR IGNORE INTO CONCEPT_DIMENSION (\n"+
                "concept_path," +
                "concept_cd," +
                "name_char," +
                " concept_blob," +
                " update_date," +
                " download_date," +
                " import_date," +
                " sourcesystem_cd," +
                " upload_id) VALUES (?,?,?,?,?,?,?,?,?);";

        PreparedStatement preparedStatement = getDbConn().prepareStatement(insert);

        int count = 0;
        BufferedReader reader = new BufferedReader(new FileReader(super.getFilePath()));
        String line = reader.readLine();
        while (line != null) {
            count++;

            if (count % 100 == 1) {
                getDbConn().setAutoCommit(false);
            }

            try {
                ConceptDimension conceptDimension = new ConceptDimensionImpl(line);
                preparedStatement.setString(1, conceptDimension.concept_path());
                preparedStatement.setString(2, conceptDimension.concept_cd());
                preparedStatement.setString(3, conceptDimension.name_char());
                preparedStatement.setString(4, conceptDimension.concept_blob());
                if (conceptDimension.update_date() != null) {
                    preparedStatement.setDate(5, convertUtilToSql(conceptDimension.update_date()));
                } else{
                    preparedStatement.setNull(5, Types.DATE);
                }
                if (conceptDimension.download_date() != null) {
                    preparedStatement.setDate(6, convertUtilToSql(conceptDimension.download_date()));
                } else {
                    preparedStatement.setNull(6, Types.DATE);
                }
                if (conceptDimension.import_date() != null) {
                    preparedStatement.setDate(7, convertUtilToSql(conceptDimension.import_date()));
                } else {
                    preparedStatement.setNull(7, Types.DATE);
                }
                preparedStatement.setString(8, conceptDimension.sourcesystem_cd());
                if (conceptDimension.upload_id() != Integer.MIN_VALUE) {
                    preparedStatement.setInt(9, conceptDimension.upload_id());
                } else {
                    preparedStatement.setNull(9, Types.INTEGER);
                }
                preparedStatement.executeUpdate();
            } catch (MalformedLineException e) {
                System.out.println("Malformed line: " + line);
            } catch (IllegalDataTypeException e) {
                System.out.println("Illegal data type in line: " + line);
            }

            if (count % 100 == 0) {
                getDbConn().commit();
            }

            line = reader.readLine();
        }

        if (!getDbConn().getAutoCommit()){
            getDbConn().commit();
        }
        reader.close();

    }
}
