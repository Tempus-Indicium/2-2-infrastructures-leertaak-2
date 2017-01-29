package com.tempus_indicium.app;

import com.tempus_indicium.app.db.FileStore;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ConcurrentModificationException;
import java.util.logging.Level;

/**
 * Created by peterzen on 2016-12-22.
 * Part of the 2-2-infrastructures-leertaak-2 project.
 */
public class MasterThread extends Thread {
    private ServerSocket serverSocket;

    public MasterThread() {
    }

    @Override
    public void run() {
        int workersCounter = 0;

        // step 1
        this.setupServerSocket(App.SERVER_PORT);

        while (workersCounter < 800) {
            Socket clientSocket = this.acceptNewClient();

            if (clientSocket != null) {
                new WorkerThread(clientSocket).start();
                workersCounter++;
                App.LOGGER.log(Level.INFO, "Current workers: "+workersCounter);
            }
        }

        //noinspection InfiniteLoopStatement
        while (true) {
            try {
                Thread.sleep(10);
                try {
                    FileStore.writeToFileIfNeeded();
                } catch (ConcurrentModificationException e) {
                    Thread.sleep(10);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private Socket acceptNewClient() {
        Socket socket = null;
        try {
            socket = this.serverSocket.accept();
        } catch (Exception e) {
            App.LOGGER.log(Level.WARNING, e.getMessage());
        }
        return socket;
    }

    private void setupServerSocket(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            App.LOGGER.log(Level.INFO, "MasterThread: ServerSocket started, listening on port: "+port+"\n\n");
        } catch (Exception e) {
            App.LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }
}
