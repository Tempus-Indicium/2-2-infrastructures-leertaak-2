package com.tempus_indicium.datafilter;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by peterzen on 2016-12-22.
 * Part of the 2-2-infrastructures-leertaak-2 project.
 */
public class MasterThread extends Thread {
    private ServerSocket serverSocket;
    public static int workersCounter;

    public MasterThread() {
    }

    @Override
    public void run() {
        MasterThread.workersCounter = 0;

        // step 1
        this.setupServerSocket(App.SERVER_PORT);

        List<WorkerThread> workers = new LinkedList<>();

        while (MasterThread.workersCounter < 800) {
            Socket clientSocket = this.acceptNewClient();

            if (clientSocket != null) {
                workers.add(new WorkerThread(clientSocket));
                MasterThread.workersCounter++;
                App.LOGGER.log(Level.INFO, "Current workers: "+MasterThread.workersCounter);
            }
        }
        workers.forEach(Thread::start);
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
