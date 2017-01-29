package com.tempus_indicium.app;

import com.tempus_indicium.app.config.ConfigManager;
import com.tempus_indicium.app.db.FileStore;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Created by peterzen on 2016-12-17.
 * Part of the 2-2-infrastructures-leertaak-2 project.
 */
public class App {
    public final static Logger LOGGER = Logger.getLogger("com.tempus_indicium.app");
    public final static Properties config = ConfigManager.getProperties("config.properties");

    public final static int SERVER_PORT = Integer.parseInt(config.getProperty("SERVER_PORT"));

    public static HashMap<String, Pattern> xmlPatterns;
    public static List<byte[]> measurementRows;
    public static ByteBuffer measurementBytes;

    /**
     * Start of the Tempus Indicium app
     * @param args any arguments
     */
    public static void main(String[] args) {
        if(!FileStore.initializeFileStore()) {
            LOGGER.log(Level.SEVERE, "File store could not be initialized.");
            System.exit(1);
        }
        try {
            FileStore.fileOutputStream = new FileOutputStream(FileStore.currentFile, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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

        App.measurementRows = new ArrayList<>();
        // bytes per measurement is 10, so multiply by 20 for a safe zone
        byte[] bigByteArray = new byte[Integer.parseInt(App.config.getProperty("ROWS_PER_WRITE")) * 11];
        App.measurementBytes = ByteBuffer.wrap(bigByteArray);

        new MasterThread().start();
    }

}
