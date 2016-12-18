package com.tempus_indicium.app.datagather;

import com.tempus_indicium.app.Main;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
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
    private DataOutputStream dataOutStream;

    DatagatherThread(Socket clientSocket, Semaphore cliLock) {
        this.clientSocket = clientSocket;
        this.cliLock = cliLock;
    }

    @Override
    public void run() {
        Main.LOGGER.log(Level.INFO, "A new DatagatherThread has started!\nClient: "+this.clientSocket.toString()+"\n"
                +"Client limit status: "+cliLock.toString()+"\n\n");




        this.closeAndReleaseConnection();
    }

    private DataOutputStream getDataOutStream() {

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
