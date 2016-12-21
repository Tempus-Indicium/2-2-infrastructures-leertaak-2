package com.tempus_indicium.app;

import com.tempus_indicium.app.config.ConfigManager;
import com.tempus_indicium.app.datagather.Datagather;
import com.tempus_indicium.app.db.DB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by peterzen on 2016-12-17.
 * Part of the 2-2-infrastructures-leertaak-2 project.
 */
public class Main {
    public final static Logger LOGGER = Logger.getLogger("com.tempus_indicium.app");
    public final static int SERVER_PORT = 7789;
    public final static int CLI_LIMIT = 5;
    private final static Properties config = ConfigManager.getProperties("config.properties"); // may want this public

    /**
     * Start of the Tempus Indicium app
     * @param args any arguments
     */
    public static void main(String[] args) {
//        @TODO: here we could define worker threads using Datagather
//        Datagather dataGatherer = new Datagather(); // is blocking
        DB db = new DB(config.getProperty("DB_SYSTEM"), config.getProperty("DB_USER"),
            config.getProperty("DB_PASSWORD"), config.getProperty("DB_SERVER"),
            Integer.parseInt(config.getProperty("DB_PORT")), config.getProperty("DB_DATABASE"));
        try {
            Connection dbConnection = db.getConnection();
            Statement dbStatement = dbConnection.createStatement();
            ResultSet results = dbStatement.executeQuery("SELECT * FROM `stations` LIMIT 1, 10");
            while (results.next()) {
                System.out.println(results.getInt("stn"));
            }
            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
