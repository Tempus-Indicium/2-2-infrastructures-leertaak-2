package com.tempus_indicium.app;

import com.tempus_indicium.app.db.FileStore;
import com.tempus_indicium.app.db.Measurement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.BufferOverflowException;
import java.util.HashSet;
import java.util.logging.Level;

/**
 * Created by peterzen on 2016-12-23.
 * Part of the 2-2-infrastructures-leertaak-2 project.
 */
public class WorkerThread extends Thread {
    // Note: this Thread is expected to loop as long as the client keeps streaming data

    private Socket clientSocket;
    private InputStream inputStream;

    public WorkerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        // step 1
        this.openClientInputStream();

//        while (MasterThread.workersCounter < 800) {
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

        try {
            String line;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.inputStream));
            boolean skipMeasurement;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("<MEASUREMENT>")) {
                    // start new measurement
                    Measurement m = new Measurement();
                    skipMeasurement = false;
                    while (!(line = bufferedReader.readLine()).contains("</MEASUREMENT>")) {
                        if (skipMeasurement)
                            continue; // skip lines for this measurement

                        if (line.contains("<STN>")) { // stn is an exception
                            if (!m.setStnFromXmlString(line)) {
                                skipMeasurement = true;
                                continue;
                            }
                        }

                        m.setVariableFromXMLString(line);
                    }
                    if (!skipMeasurement && MasterThread.workersCounter == 600) {
                        try {
                            App.measurementBytes.put(m.getArrayOfByteVariables());
                        } catch (BufferOverflowException e) {
                            FileStore.writeToFile();
                            App.measurementBytes.put(m.getArrayOfByteVariables());
                        }
                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        this.closeAndReleaseConnection();
    }

    private void openClientInputStream() {
        try {
            this.inputStream = this.clientSocket.getInputStream();
        } catch (Exception e) {
            App.LOGGER.log(Level.WARNING, e.getMessage() + "\n");
        }
    }

    private void closeAndReleaseConnection() {
        try {
            this.clientSocket.close();
        } catch (Exception e) {
            App.LOGGER.log(Level.WARNING, e.getMessage());
        }
    }
}
