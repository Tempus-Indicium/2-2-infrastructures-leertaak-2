package com.tempus_indicium.app.datagather;

import com.tempus_indicium.app.Main;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;

/**
 * Created by peterzen on 2016-12-18.
 * Part of the 2-2-infrastructures-leertaak-2 project.
 */
class DatagatherThread extends Thread {
    private Socket clientSocket;
    private Semaphore cliLock;
    private InputStream inputStream;
    private BufferedReader bufferedReader;

    DatagatherThread(Socket clientSocket, Semaphore cliLock) {
        this.clientSocket = clientSocket;
        this.cliLock = cliLock;
    }

    @Override
    public void run() {
        Main.LOGGER.log(Level.INFO, "A new DatagatherThread has started!\nClient: "+this.clientSocket.toString()+"\n"
                +"Client limit status: "+cliLock.toString()+"\n\n");

        this.openClientInputStream();
        StringBuilder xmlStringIn = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                xmlStringIn.append(line);
                if (line.equals("</WEATHERDATA>"))
                    break; // idea: call a new thread to do the parsing, correcting and storing of data
            }
        } catch (Exception e) {
            Main.LOGGER.log(Level.WARNING, e.getMessage());
        }
        System.out.println(xmlStringIn.toString());
//  @TODO: parse XML string (idea: multiple measurement objects, use Measurement class as Model for db table)
//  @TODO: correct measurement data (based on last 30 measurements of station; write stored procedure)
//  @TODO: save Measurement objects to
//  @TODO: Loop back to reading from the bufferedReader

        this.closeAndReleaseConnection();
    }

    private void openClientInputStream() {
        try {
            this.inputStream = this.clientSocket.getInputStream();
            this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        } catch (Exception e) {
            Main.LOGGER.log(Level.WARNING, e.getMessage()+"\n");
        }
    }

    private void closeAndReleaseConnection() {
        try {
            this.clientSocket.close();
        } catch (Exception e) {
            Main.LOGGER.log(Level.WARNING, e.getMessage());
        }
        this.cliLock.release();
    }
}
