package org.jax.database;

import org.jax.DateModel.SourceSystemEnumType;
import org.jax.Exception.IllegalDataTypeException;
import org.jax.Exception.MalformedLineException;
import org.jax.Parsers.ObservationFact;
import org.jax.Parsers.ObservationFactLazyImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class LoadObservation extends TableImporter {

    final static Logger logger = LoggerFactory.getLogger(LoadObservation.class);

    public LoadObservation(Connection dbConn, String filePath) {
        super(dbConn, filePath);
    }

    @Override
    void load() throws IOException, SQLException{
int count = 0;
System.out.println("count: " + count);
        String createTable = "CREATE TABLE IF NOT EXISTS OBSERVATION_FACT (\n " +
                "encounter_num INTEGER ," + //PRIMARY KEY
                "patient_num INTEGER ," + //PRIMARY KEY
                "concept_cd VARCHAR(20) ," + //PRIMARY KEY
                "provider_id VARCHAR(20) ," + //PRIMARY KEY
                "start_date DATE ," + //PRIMARY KEY
                "modifier_cd VARCHAR(100)," + //PRIMARY KEY
                "instance_num INTEGER," +
                "valtype_cd VARCHAR(3)," +
                "tval_char VARCHAR(50)," +
                "nval_num DOUBLE," +
                "valueflag_cd CHARACTER(1)," +
                "quantity_num DOUBLE," +
                "units_cd VARCHAR(100)," +
                "end_date DATE," +
                "location_cd VARCHAR(100)," +
                "observation_blob CLOB," +
                "confidence_num DOUBLE," +
                "update_date DATE," +
                "download_date DATE," +
                "import_date DATE," +
                "sourcesystem_cd VARCHAR(50)," +
                "upload_id INTEGER," +
                "text_search_index TEXT," +
                "PRIMARY KEY (encounter_num, patient_num, concept_cd, provider_id, start_date, modifier_cd));";

        Statement stmt = getDbConn().createStatement();
        stmt.execute(createTable);


        String insert = "INSERT OR IGNORE INTO OBSERVATION_FACT(\n" +
                "encounter_num, " +
                "patient_num, " +
                "concept_cd, " +
                "provider_id, " +
                "start_date,"+
                "modifier_cd," +
                "instance_num,"  +
                "valtype_cd,"  +
                "tval_char,"  +
                "nval_num,"  +
                "valueflag_cd,"  +
                "quantity_num," +
                "units_cd," +
                "end_date," +
                "location_cd,"  +
                "observation_blob,"  +
                "confidence_num,"  +
                "update_date,"  +
                "download_date,"  +
                "import_date,"  +
                "sourcesystem_cd,"  +
                "upload_id,"  +
                "text_search_index) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

        BufferedReader reader = new BufferedReader(new FileReader(super.getFilePath()));
        String line = reader.readLine();
        PreparedStatement preparedStatement;
        while (line != null) {
count++;
            if (line.startsWith("\"encounter_num\"")) { //skip header
                //do nothing for header
            } else {
                preparedStatement = getDbConn().prepareStatement(insert);
                try {
                    ObservationFact observationFact = new ObservationFactLazyImpl(line);
                    //if (observationFact.concept_cd().toLowerCase().startsWith("loinc"))
                    preparedStatement.setInt(1, observationFact.encounter_num());
                    preparedStatement.setInt(2, observationFact.patient_num());
                    preparedStatement.setString(3, observationFact.concept_cd());
                    preparedStatement.setString(4, observationFact.provider_id());
                    preparedStatement.setDate(5, convertUtilToSql(observationFact.start_date()));
                    preparedStatement.setString(6, observationFact.modifier_cd());
                    if (observationFact.instance_num() != Integer.MIN_VALUE) {
                        preparedStatement.setInt(7, observationFact.instance_num());
                    }
                    if (observationFact.valtype_cd() != null){
                        preparedStatement.setString(8, observationFact.valtype_cd());
                    }
                    if (observationFact.tval_char() != null) {
                        preparedStatement.setString(9, observationFact.tval_char());
                    }
                    if (observationFact.nval_num() != Double.MIN_VALUE) {
                        preparedStatement.setDouble(10, observationFact.nval_num());
                    }
                    if (observationFact.valueflag_cd() != Character.MIN_VALUE) {
                        preparedStatement.setString(11, Character.toString(observationFact.valueflag_cd()));
                    }
                    if(observationFact.quatity_num() != Double.MIN_VALUE){
                        preparedStatement.setDouble(12, observationFact.quatity_num());
                    }
                    preparedStatement.setString(13, observationFact.units_cd());
                    if (observationFact.end_date() != null) {
                        preparedStatement.setDate(14, convertUtilToSql(observationFact.end_date()));
                    }
                    if (observationFact.location_cd() != null) {
                        preparedStatement.setString(15, observationFact.location_cd());
                    }
                    if (observationFact.observation_blob() != null) {
                        preparedStatement.setString(16, observationFact.observation_blob());
                    }
                    if (observationFact.confidence_num() != Double.MIN_VALUE){
                        preparedStatement.setDouble(17, observationFact.confidence_num());
                    }
                    if (observationFact.update_date() != null) {
                        preparedStatement.setDate(18, convertUtilToSql(observationFact.update_date()));
                    }
                    if (observationFact.download_date() != null) {
                        preparedStatement.setDate(19, convertUtilToSql(observationFact.download_date()));
                    }
                    if (observationFact.import_date() != null) {
                        preparedStatement.setDate(20, convertUtilToSql(observationFact.import_date()));
                    }
                    if (observationFact.sourcesystem_cd() != null) {
                        preparedStatement.setString(21, observationFact.sourcesystem_cd().toString());
                    }
                    if (observationFact.upload_id() != Integer.MIN_VALUE) {
                        preparedStatement.setInt(22, observationFact.upload_id());
                    }
                    if (observationFact.text_search_index() != null) {
                        preparedStatement.setString(23, observationFact.text_search_index());
                    }
                    //preparedStatement.executeUpdate();
                } catch (IllegalDataTypeException e) {
                    e.printStackTrace();
                } catch (MalformedLineException e) {
                    logger.error("Malformed line:" + line);
                }
            }

            line = reader.readLine();

            //for strange reason, garbage collection did not work automatically
            //here we force garbage collection every 100,000 lines
            if (count % 100000 == 0) {
                System.out.println("lines processed: " + count);
                System.gc();
            }
        }


        reader.close();

    }

    private java.sql.Date convertUtilToSql(java.util.Date uDate) {
        if (uDate == null) {
            return null;
        }

        java.sql.Date sDate = new java.sql.Date(uDate.getTime());
        return sDate;
    }


}
