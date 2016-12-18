package com.tempus_indicium.app;

import com.tempus_indicium.app.datagather.Datagather;

import java.util.logging.Logger;

/**
 * Created by peterzen on 2016-12-17.
 * Part of the 2-2-infrastructures-leertaak-2 project.
 */
public class Main {
    public final static Logger LOGGER = Logger.getLogger("com.tempus_indicium.app");
    public final static int SERVER_PORT = 7789;
    public final static int CLI_LIMIT = 5;

    /**
     * Start of the Tempus Indicium app
     * @param args any arguments
     */
    public static void main(String[] args) {
        Datagather dataGatherer = new Datagather();

    }

}
