package com.tempus_indicium.app;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by peterzen on 2016-12-17.
 * Part of the 2-2-infrastructures-leertaak-2 project.
 */
public class Main {
    private final static int CONNECTIONS_PER_THREAD = 10;


    /**
     * Start of the Tempus Indicium app
     * @param args any arguments
     */
    public static void main(String[] args) {
        try {
            Socket sock = new Socket("127.0.0.1", 7789);
            InputStream in = sock.getInputStream();
            BufferedReader bin = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = bin.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
