package com.tempus_indicium.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.Connection;
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
    private BufferedReader bufferedReader;

    public WorkerThread(Connection dbConnection, Socket clientSocket) {
        this.dbConnection = dbConnection;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        // step 1
        this.openClientInputStream();

        String line;
        String xmlStringIn = "";
        try {
            // step 2: read through stream|also start of loop till streaming stops
            while ((line = bufferedReader.readLine()) != null) {
                // step 3: building the xml string
                xmlStringIn += line;
                if (line.equals("</WEATHERDATA>")) {
                    this.processXmlString(xmlStringIn);
                    xmlStringIn = ""; // reset string, gather new Measurements
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.closeAndReleaseConnection();
    }

    private void processXmlString(String xmlStringIn) {
        // step 4: parse into Measurement objects

    }

    private void openClientInputStream() {
        try {
            this.inputStream = this.clientSocket.getInputStream();
            this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
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
