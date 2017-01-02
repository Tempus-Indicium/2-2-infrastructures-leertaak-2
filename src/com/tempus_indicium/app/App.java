package com.tempus_indicium.app;

import com.tempus_indicium.app.config.ConfigManager;

import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by peterzen on 2016-12-17.
 * Part of the 2-2-infrastructures-leertaak-2 project.
 */
public class App {
    public final static Logger LOGGER = Logger.getLogger("com.tempus_indicium.app");
    public final static Properties config = ConfigManager.getProperties("config.properties");

    public final static int SERVER_PORT = Integer.parseInt(config.getProperty("SERVER_PORT"));

    /**
     * Start of the Tempus Indicium app
     * @param args any arguments
     */
    public static void main(String[] args) {
        new MasterThread().run();

//        @TODO: here we could define worker threads using Datagather
//        Datagather dataGatherer = new Datagather(); // is blocking
        // idea for DB connections: limit amount of statements / connection (load N statements: start statement
        // execution thread in one of the DB connections (also limit connections?))
        // or rather probably use JDBC ConnectionPool thingy (caches db connections)
//        DB db = new DB(config.getProperty("DB_SYSTEM"), config.getProperty("DB_USER"),
//            config.getProperty("DB_PASSWORD"), config.getProperty("DB_SERVER"),
//            Integer.parseInt(config.getProperty("DB_PORT")), config.getProperty("DB_DATABASE"));
//        try {
//            Connection dbConnection = db.getConnection();
//            Statement dbStatement = dbConnection.createStatement();
//            ResultSet results = dbStatement.executeQuery("SELECT * FROM `stations` LIMIT 1, 10");
//            while (results.next()) {
//                System.out.println(results.getInt("stn"));
//            }
//            dbConnection.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

}
