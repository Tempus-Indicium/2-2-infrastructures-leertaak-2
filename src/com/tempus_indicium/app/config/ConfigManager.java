package com.tempus_indicium.app.config;

import com.tempus_indicium.app.App;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;

/**
 * Created by peterzen on 2016-12-20.
 * Part of the 2-2-infrastructures-leertaak-2 project.
 */
public class ConfigManager {

    public static Properties getProperties(String propFileName) {
        String result = "";
        InputStream inputStream;
        Properties prop = new Properties();

        try {
            inputStream = ConfigManager.class.getClassLoader().getResourceAsStream(propFileName);

            prop.load(inputStream);
        } catch (IOException e) {
            App.LOGGER.log(Level.SEVERE, "Cannot read properties file: \n"+e.getMessage());
        }
        return prop;
    }


}
