package com.tempus_indicium.datafilter;

import com.tempus_indicium.datafilter.config.ConfigManager;
import com.tempus_indicium.datafilter.db.FileStore;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Created by peterzen on 2016-12-17.
 * Part of the 2-2-infrastructures-leertaak-2 project.
 */
public class App {
    public final static Logger LOGGER = Logger.getLogger("com.tempus_indicium.datafilter");
    public final static Properties config = ConfigManager.getProperties("config.properties");

    public final static int SERVER_PORT = Integer.parseInt(config.getProperty("SERVER_PORT"));

    public static HashMap<String, Pattern> xmlPatterns;

    /**
     * Start of the Tempus Indicium datafilter
     * @param args any arguments
     */
    public static void main(String[] args) {
        try {
            FileStore.dataGatherSocket = new Socket(config.getProperty("DATAGATHER_HOST"),
                    Integer.parseInt(config.getProperty("DATAGATHER_PORT")));
            FileStore.dataOutputStream = new DataOutputStream(FileStore.dataGatherSocket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Exception thrown when connecting to the datagather application. Make sure datagather is running.");
            System.exit(1);
        }

        FileStore.loadStationsFile();

        // prepare regex patterns
        xmlPatterns = new HashMap<>();
        xmlPatterns.put("stn", Pattern.compile("([0-9]+)"));
        xmlPatterns.put("date", Pattern.compile("([0-9-]+)"));
        xmlPatterns.put("time", Pattern.compile("([0-9:]+)"));
        xmlPatterns.put("temp", Pattern.compile("([0-9.-]+)"));
        xmlPatterns.put("dewp", Pattern.compile("([0-9.-]+)"));
//        xmlPatterns.put("stp", Pattern.compile("([0-9.]+)"));
//        xmlPatterns.put("slp", Pattern.compile("([0-9.]+)"));
        xmlPatterns.put("visib", Pattern.compile("([0-9.]+)"));
//        xmlPatterns.put("wdsp", Pattern.compile("([0-9.]+)"));
//        xmlPatterns.put("prcp", Pattern.compile("([0-9.]+)"));
//        xmlPatterns.put("sndp", Pattern.compile("([0-9-.]+)"));
//        xmlPatterns.put("frshtt", Pattern.compile("([0-1]+)"));
//        xmlPatterns.put("cldc", Pattern.compile("([0-9.]+)"));
//        xmlPatterns.put("wnddir", Pattern.compile("([0-9]+)"));

        new MasterThread().start();
    }

}
