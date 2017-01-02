package com.tempus_indicium.app;

import com.tempus_indicium.app.db.DB;

import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
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
// 2. open database connections for the WorkerThreads to use (maintain a connection pool)
// 3. spawn a WorkerThread for each new client
// 4. including the assignment of DB connections to WorkerThreads

    // Note: Around 16 WorkerThreads per connection would mean 50 connections for 800 WorkerThreads

    // http://stackoverflow.com/questions/322715/when-to-use-linkedlist-over-arraylist
    private List<Connection> dbConnections = new LinkedList<>();
    private ServerSocket serverSocket;

    public MasterThread() {
    }

    @Override
    public void run() {
        int workersPerConnection = Integer.parseInt(App.config.getProperty("WORKERS_PER_DB_CONN"));
        int workersCounter = 0;

        // step 1
        this.setupServerSocket(App.SERVER_PORT);

        //noinspection InfiniteLoopStatement
        while (true) {
            Socket clientSocket = this.acceptNewClient();
            if (clientSocket != null) {
                // step 2: add a new dbConnection if needed
                this.spinUpConnectionsAsNeeded(workersCounter, workersPerConnection);

                // step 3: spawn new WorkerThread with the last dbConnection available
                new WorkerThread(this.dbConnections.get(this.dbConnections.size()-1), clientSocket).run();
                workersCounter++;
                System.out.println("Current workers: "+workersCounter);
            }
        }
    }

    // step 2
    private void spinUpConnectionsAsNeeded(int workersCounter, int workersPerConnection) {
        if (workersCounter % workersPerConnection == 0) { // if workersCounter is a multiple of workersPerConnection
            // spin up a new dbInstance (step 2)
            try {
                this.dbConnections.add(new DB().getConnection());
                System.out.println("new DB instance! workersPerConnection=" + workersPerConnection
                        + ", dbConnections.size()=" + this.dbConnections.size()); // @TODO: remove debugging
            } catch (SQLException e) {
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
