package com.tempus_indicium.app;

import com.tempus_indicium.app.db.Measurement;
import com.tempus_indicium.app.parsing.MeasurementExtractor;
import com.tempus_indicium.app.parsing.MeasurementParser;

import java.io.InputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by peterzen on 2016-12-23.
 * Part of the 2-2-infrastructures-leertaak-2 project.
 */
public class WorkerThread extends Thread {
//    Use this thread to:
// 1. open the client input stream
// 2. use a bufferedReader to read through the inputStream
// 3. build an xml string
// 4. parse the xml string into Measurement objects
// 5. make corrections where needed
// 6. prepare insert queries batch (stmt.addBatch / stmt.executeBatch)
// 7. execute batch, close statement
    // Note: this Thread is expected to loop as long as the client keeps streaming data
    // @TODO: Maybe built in some form of time to live to properly close thread
    // Note: scenario where amount of clients reduces might introduce problems

    private Connection dbConnection;
    private Socket clientSocket;
    private InputStream inputStream;

    public WorkerThread(Connection dbConnection, Socket clientSocket) {
        this.dbConnection = dbConnection;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        // step 1
        this.openClientInputStream();

        while (!clientSocket.isClosed()) {
            this.processInputStream();
        }

//        this.closeAndReleaseConnection();
    }

    private void processInputStream() {
        // step 4: parse into Measurement objects
        MeasurementParser dataParser = new MeasurementParser();
        MeasurementExtractor extractor = new MeasurementExtractor(this.inputStream, dataParser);

        List<Measurement> measurementsData = extractor.extractDataFromXML();
        if (measurementsData == null) {
            this.closeAndReleaseConnection();
            return;
        }

        for (Measurement m : measurementsData)
            System.out.println(m.toString());

        // step 5
        // @TODO: make corrections on the measurement data

        // @TODO: save measurements into the database (Model.saveBatch(List))
        try {
            Measurement.saveBatch(measurementsData, this.dbConnection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openClientInputStream() {
        try {
            this.inputStream = this.clientSocket.getInputStream();
        } catch (Exception e) {
            App.LOGGER.log(Level.WARNING, e.getMessage()+"\n");
        }
    }

    private void closeAndReleaseConnection() {
        try {
            this.clientSocket.close();
            // @TODO: tell MasterThread that we are donezo, so that it can update the dbConnections as needed
        } catch (Exception e) {
            App.LOGGER.log(Level.WARNING, e.getMessage());
        }
    }
}
