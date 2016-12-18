package com.tempus_indicium.app.datagather;

import com.tempus_indicium.app.Main;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;

/**
 * Created by peterzen on 2016-12-18.
 * Part of the 2-2-infrastructures-leertaak-2 project.
 */
public class Datagather {
    private ServerSocket serverSocket = null;
    private Semaphore clientLimiter;

    public Datagather() {
        this.clientLimiter = new Semaphore(Main.CLI_LIMIT);
        this.setupServerSocket(Main.SERVER_PORT);

        //noinspection InfiniteLoopStatement
        while (true) {
            Socket clientSocket = this.acceptNewClient();
            if (clientSocket != null) {
                // NOTE: we may have a problem when trying to assign multiple clients to a single thread
                new DatagatherThread(clientSocket, this.clientLimiter).start();
            }
        }
    }

    private Socket acceptNewClient() {
        Socket socket = null;
        try {
            this.clientLimiter.acquire();
            socket = this.serverSocket.accept();
        } catch (Exception e) {
            Main.LOGGER.log(Level.WARNING, e.getMessage());
        }
        return socket;
    }

    private void setupServerSocket(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            Main.LOGGER.log(Level.INFO, "ServerSocket started, listening on port: "+port+"\n\n");
        } catch (Exception e) {
            Main.LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }

}
