package com.tempus_indicium.app;

import com.tempus_indicium.app.db.DB;

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
//    Use this thread to:
// 1. start the ServerSocket
// 2. spawn a WorkerThread for each new client
// 3. open database connections for the WorkerThreads to use (maintain a connection pool)
// 4. including the assignment of DB connections to WorkerThreads
    // Note: let the DB execute a batch of statements every N statements
    // Note: Around 16 WorkerThreads per connection would mean 50 connections for 800 WorkerThreads

    private ServerSocket serverSocket;
    private List<DB> dbInstances = new LinkedList<>(); // also try ArrayList
    // http://stackoverflow.com/questions/322715/when-to-use-linkedlist-over-arraylist

    private static int workersPerDB = Integer.parseInt(App.config.getProperty("WORKERS_PER_DB_CONN"));

    public MasterThread() {
        // step 1
        this.setupServerSocket(App.SERVER_PORT);

        //noinspection InfiniteLoopStatement
        while (true) {
            Socket clientSocket = this.acceptNewClient();
            if (clientSocket != null) {
                // step 2
                System.out.println("TODO: start WorkerThread here");
                // NOTE: we may have a problem when trying to assign multiple clients to a single thread
//                new DatagatherThread(clientSocket, this.clientLimiter).start();
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

    @Override
    public void run() {

    }
}
