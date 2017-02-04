package com.tempus_indicium.datafilter.db;

import com.tempus_indicium.datafilter.App;
import com.tempus_indicium.datafilter.WorkerThread;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

import static com.tempus_indicium.datafilter.App.config;

/**
 * Created by peterzen on 2017-01-23.
 * Part of the 2-2-infrastructures-leertaak-2 project.
 */
public class FileStore {
    public static HashMap<Integer, String[]> stations;
    public static DataOutputStream dataOutputStream;
    public static Socket dataGatherSocket;
    public static final Semaphore listLock = new Semaphore(1, true);

    // load the stations.csv file in memory so we can match the data for later saving of measurements
    public static void loadStationsFile() {
        String csvFile = config.getProperty("STATIONS_CSV");
        BufferedReader bufferedReader = null;
        String line;
        String csvSplitBy = ",";
        stations = new HashMap<>();
        try {
            bufferedReader = new BufferedReader(new FileReader(csvFile));
            while ((line = bufferedReader.readLine()) != null) {

                String[] row = line.split(csvSplitBy);
                if (row[0].equals("id"))
                    continue;

                stations.put(Integer.parseInt(row[1]), row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static synchronized void writeToOutputStreamIfNeeded() {
        if (App.measurementsList.size() >= Integer.parseInt(App.config.getProperty("ROWS_PER_WRITE"))) {
            try {
                listLock.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("PERFORMING WRITE MEASUREMENTS: "+App.measurementsList.size());
            App.measurementsList.forEach(m -> {
                try {
                    FileStore.dataOutputStream.write(m);
                } catch (IOException | NullPointerException e) {
                    System.out.println("Invalid measurement ignored");
                }
            });
            System.out.println("CLEARING LIST");
            App.measurementsList.clear();
            listLock.release();
            try {
                FileStore.dataOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void directWriteToOutputStream(byte[] bytes) {
        try {
            FileStore.dataOutputStream.write(bytes);
            FileStore.dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
