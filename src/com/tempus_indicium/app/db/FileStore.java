package com.tempus_indicium.app.db;

import com.tempus_indicium.app.App;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

import static com.tempus_indicium.app.App.config;

/**
 * Created by peterzen on 2017-01-23.
 * Part of the 2-2-infrastructures-leertaak-2 project.
 */
public class FileStore {
    public static Date currentDate;
    public static File currentFile;
    public static HashMap<Integer, String[]> stations;

    // add global vars: _currentDate & _currentFile

    public static void updateDateUpdateFileIfNeeded() {
        if (FileStore.dateComparedToCurrent() == 0) {
            return;
        }

        // dates are not identical anymore; update the date
        FileStore.updateCurrentDate();

        // date updated so: create new current file
        String fileName = FileStore.generateFileName(FileStore.currentDate);
        FileStore.newCurrentFile(fileName);
    }

    public static boolean initializeFileStore() {
        try {
            FileStore.updateCurrentDate();

            List<Path> existingFilePaths = FileStore.filesForFolder();
            String fileName = generateFileName(currentDate); // fileName for today's date

            if (FileStore.needToCreateFile(existingFilePaths, fileName)) {
                // create a file for today
                FileStore.newCurrentFile(fileName);
            } else {
                existingFilePaths.forEach(filePath -> {
                    if (filePath.getFileName().toString().equals(fileName)) {
                        FileStore.currentFile = new File(filePath.toString());
                    }
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static String getStationString(Integer stnId) {
        return stations.get(stnId)[0]
                +","+stations.get(stnId)[1]
                +","+stations.get(stnId)[2]
                +","+stations.get(stnId)[3]
                +","+stations.get(stnId)[4]
                +","+stations.get(stnId)[5];
    }

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
//         stations.forEach((k, v) -> System.out.println("stn: "+v[0]+", name: "+v[1]+", country: "+v[2])); // uncomment to debug
    }

    private static void updateCurrentDate() {
        FileStore.currentDate = new Date();
    }

    // check if current date is equal to the variable currentDate
    private static int dateComparedToCurrent() {
        // @TODO: properly implement this function, do not use deprecated methods.
        // its almost 1am and i cba to figure this Java jibber dabber out.
        Date d1 = new Date();
        Date d2 = new Date();
        if (d1.getYear() != d2.getYear())
            return d1.getYear() - d2.getYear();
        if (d1.getMonth() != d2.getMonth())
            return d1.getMonth() - d2.getMonth();
        return d1.getDate() - d2.getDate();
    }

    private static void newCurrentFile(String fileName) {
        File file = new File(config.getProperty("FILE_STORE_DIR") + fileName);
        try {
            if (file.createNewFile()) {
//                FileWriter fileWriter = new FileWriter(file);
//                fileWriter.write(config.getProperty("COLUMNS") + System.lineSeparator());
//                fileWriter.close();

                FileStore.currentFile = file;
            } else {
                System.out.println("File already exists. But error should be given earlier..");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Path> filesForFolder() throws IOException {
        List<Path> filePaths = new LinkedList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(config.getProperty("FILE_STORE_DIR")))) {
            paths.forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    filePaths.add(filePath);
                    System.out.println("fileExists: " + filePath.toString());
                }
            });
        }
        return filePaths;
    }

    private static String generateFileName(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(config.getProperty("FILE_DATE_FORMAT"));
        return dateFormat.format(date);
    }

    private static boolean needToCreateFile(List<Path> existingFilePaths, String fileNameToday) {
        final boolean[] fileMissing = {true};
        existingFilePaths.forEach(filePath -> {
            if (filePath.getFileName().toString().equals(fileNameToday)) {
                fileMissing[0] = false;
            }
        });
        return fileMissing[0];
    }

    public static synchronized void writeToFileIfNeeded() {
        if (App.measurementRows.size() >= Integer.parseInt(App.config.getProperty("ROWS_PER_WRITE"))) {
            try {
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FileStore.currentFile, true));
                out.writeObject(App.measurementRows);
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                App.measurementRows.clear();
            }
        }
    }
}
