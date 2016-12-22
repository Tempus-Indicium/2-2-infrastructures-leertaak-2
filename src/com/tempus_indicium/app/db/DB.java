package com.tempus_indicium.app.db;

import com.tempus_indicium.app.App;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

/**
 * Created by peterzen on 2016-12-20.
 * Part of the 2-2-infrastructures-leertaak-2 project.
 */
public class DB {
    private String dbms, username, password, server, database;
    private int port;


    public DB(String dbms, String username, String password, String server, int port, String database) {
        this.dbms = dbms;
        this.username = username;
        this.password = password;
        this.server = server;
        this.port = port;
        this.database = database;
    }

    public Connection getConnection() throws SQLException {
        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", this.username);
        connectionProps.put("password", this.password);

        if (this.dbms.equals("mysql")) {
            conn = DriverManager.getConnection(
                    "jdbc:" + this.dbms + "://" +
                            this.server +
                            ":" + Integer.toString(port) + "/" + this.database,
                    connectionProps);
        }
        App.LOGGER.log(Level.INFO, "Connected to MySQL database.");
        return conn;
    }

}
