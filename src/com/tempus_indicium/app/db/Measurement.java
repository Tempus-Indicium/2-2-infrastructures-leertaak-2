package com.tempus_indicium.app.db;

import com.tempus_indicium.app.App;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by peterzen on 2016-12-23.
 * Part of the 2-2-infrastructures-leertaak-2 project.
 */
public class Measurement extends Model {
    private static final String db_table = "measurements";
    private List<String> missingVariables;

    public String dateString = null;
    public String timeString = null;

    private byte[] stn;
    private byte[] stnName;
    private byte[] stnCountry;
    private byte[] acquisition_time;
    private byte[] temperature;
    private byte[] dew;
    private byte[] station_pressure;
    private byte[] sea_pressure;
    private byte[] visibility;
    private byte[] wind_speed;
    private byte[] rainfall;
    private byte[] snowfall;
    private byte[] did_freeze;
    private byte[] did_rain;
    private byte[] did_snow;
    private byte[] did_hail;
    private byte[] did_storm;
    private byte[] did_tornado;
    private byte[] cloudiness;
    private byte[] wind_direction;
    private byte events;

    public Measurement() {
        this.missingVariables = new LinkedList<>();
    }

    public List<String> getMissingVariables() {
        return this.missingVariables;
    }

    public Field getVariable(String var) throws NoSuchFieldException {
        return this.getClass().getDeclaredField(var);
    }

    public boolean hasMissingData() {
        if (this.acquisition_time == null) {
            this.missingVariables.add("acquisition_time");
        }
        if (this.temperature == null) {
            this.missingVariables.add("temperature");
        }
        if (this.dew == null) {
            this.missingVariables.add("dew");
        }
        if (this.station_pressure == null) {
            this.missingVariables.add("station_pressure");
        }
        if (this.sea_pressure == null) {
            this.missingVariables.add("sea_pressure");
        }
        if (this.visibility == null) {
            this.missingVariables.add("visibility");
        }
        if (this.wind_speed == null) {
            this.missingVariables.add("wind_speed");
        }
        if (this.rainfall == null) {
            this.missingVariables.add("rainfall");
        }
        if (this.snowfall == null) {
            this.missingVariables.add("snowfall");
        }
        if (this.did_freeze == null) {
            this.missingVariables.add("did_freeze");
        }
        if (this.did_rain == null) {
            this.missingVariables.add("did_rain");
        }
        if (this.did_snow == null) {
            this.missingVariables.add("did_snow");
        }
        if (this.did_hail == null) {
            this.missingVariables.add("did_hail");
        }
        if (this.did_storm == null) {
            this.missingVariables.add("did_storm");
        }
        if (this.did_tornado == null) {
            this.missingVariables.add("did_tornado");
        }
        if (this.cloudiness == null) {
            this.missingVariables.add("cloudiness");
        }
        if (this.wind_direction == null) {
            this.missingVariables.add("wind_direction");
        }
        return !this.missingVariables.isEmpty();
    }

    public void setVariableFromXMLString(String xmlString) {
        if (xmlString.contains("<DATE>")) {
            return; // skip the date since we can already read this from the filename
        }
        if (xmlString.contains("<TIME>")) {
            String time;
            if ((time = this.findMatchInXMLString(App.xmlPatterns.get("time"), xmlString)) != null) {
                this.setAcquisitionTime(time);
            }
        }
        if (xmlString.contains("<TEMP>")) {
            String temp;
            if ((temp = this.findMatchInXMLString(App.xmlPatterns.get("temp"), xmlString)) != null) {
                this.setTemperature(temp);
            }
        }
        if (xmlString.contains("<DEWP>")) {
            String dewp;
            if ((dewp = this.findMatchInXMLString(App.xmlPatterns.get("dewp"), xmlString)) != null) {
                this.setDew(dewp);
            }
        }
        if (xmlString.contains("<STP>")) {
            String stp;
            if ((stp = this.findMatchInXMLString(App.xmlPatterns.get("stp"), xmlString)) != null) {
                this.setStationPressure(stp);
            }
        }
        if (xmlString.contains("<SLP>")) {
            String slp;
            if ((slp = this.findMatchInXMLString(App.xmlPatterns.get("slp"), xmlString)) != null) {
                this.setSeaPressure(slp);
            }
        }
        if (xmlString.contains("<VISIB>")) {
            String visib;
            if ((visib = this.findMatchInXMLString(App.xmlPatterns.get("visib"), xmlString)) != null) {
                this.setVisibility(visib);
            }
        }
        if (xmlString.contains("<WDSP>")) {
            String wdsp;
            if ((wdsp = this.findMatchInXMLString(App.xmlPatterns.get("wdsp"), xmlString)) != null) {
                this.setWindSpeed(wdsp);
            }
        }
        if (xmlString.contains("<PRCP>")) {
            String prcp;
            if ((prcp = this.findMatchInXMLString(App.xmlPatterns.get("prcp"), xmlString)) != null) {
                this.setRainfall(prcp);
            }
        }
        if (xmlString.contains("<SNDP>")) {
            String sndp;
            if ((sndp = this.findMatchInXMLString(App.xmlPatterns.get("sndp"), xmlString)) != null) {
                this.setSnowfall(sndp);
            }
        }
        if (xmlString.contains("<FRSHTT>")) {
            String frshtt;
            if ((frshtt = this.findMatchInXMLString(App.xmlPatterns.get("frshtt"), xmlString)) != null) {
                byte events = 0;
                // NOTICE: Freeze is bit 0
                if (frshtt.charAt(0) == '1')
                    events = (byte) (events | (1));

                if (frshtt.charAt(1) == '1')
                    events = (byte) (events | (1 << 1));

                if (frshtt.charAt(2) == '1')
                    events = (byte) (events | (1 << 2));

                if (frshtt.charAt(3) == '1')
                    events = (byte) (events | (1 << 3));

                if (frshtt.charAt(4) == '1')
                    events = (byte) (events | (1 << 4));

                if (frshtt.charAt(5) == '1')
                    events = (byte) (events | (1 << 5));

                this.events = events;
            }
        }
        if (xmlString.contains("<CLDC>")) {
            String cldc;
            if ((cldc = this.findMatchInXMLString(App.xmlPatterns.get("cldc"), xmlString)) != null) {
                this.setCloudiness(cldc);
            }
        }
        if (xmlString.contains("<WNDDIR>")) {
            String wnddir;
            if ((wnddir = this.findMatchInXMLString(App.xmlPatterns.get("wnddir"), xmlString)) != null) {
                this.setWindDirection(wnddir);
            }
        }
    }

    public boolean setStnFromXmlString(String xmlString) {
        String stn;
        if ((stn = this.findMatchInXMLString(App.xmlPatterns.get("stn"), xmlString)) != null) {
            String[] stnDetails;
            if ((stnDetails = FileStore.stations.get(Integer.parseInt(stn))) != null) {
                // station details found
                this.setStn(stnDetails[0]); // set the station id using the regenerated `id` column
                this.setStnName(stnDetails[2]);
                this.setStnCountry(stnDetails[3]);
                return true;
            }
        }
        return false;
    }

    private String findMatchInXMLString(Pattern pattern, String xmlString) {
        Matcher matcher = pattern.matcher(xmlString);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    private void setStn(String stn) {
        int intStn = Integer.parseInt(stn);
        this.stn = new byte[]{
                (byte) ((intStn >> 8) & 0xFF),
                (byte) (intStn & 0xFF)
        };
//        System.out.println(this.stn[0] + ":" + this.stn[1]); // debug bytes
    }

    private void setAcquisitionTime(String acquisitionTime) {
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        try {
            Date timeOnly = timeFormat.parse(acquisitionTime);
            int intTime = (int) (timeOnly.getTime() / 1000);
            this.acquisition_time = new byte[]{
                    (byte) ((intTime >> 8) & 0xFF),
                    (byte) (intTime & 0xFF)
            };
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void setTemperature(String temperature) {
        // signed integer(actually float) (2 bytes) (NOTICE: var is multiplied by 10)
        int intTemp = (int) Float.parseFloat(temperature) * 10;
        this.temperature = new byte[]{
                (byte) ((intTemp >> 8) & 0xFF),
                (byte) (intTemp & 0xFF)
        };
    }

    private void setDew(String dew) {
        // signed integer(actually float) (2 bytes) (NOTICE: var is multiplied by 10)
        int intDew = (int) Float.parseFloat(dew) * 10;
        this.dew = new byte[]{
                (byte) ((intDew >> 8) & 0xFF),
                (byte) (intDew & 0xFF)
        };
    }

    private void setStationPressure(String station_pressure) {
        // unsigned integer(actually float) (3 bytes) (NOTICE: var is multiplied by 10)
        int intPressure = (int) Float.parseFloat(station_pressure) * 10;
        this.station_pressure = new byte[]{
                (byte) ((intPressure >> 16) & 0xFF),
                (byte) ((intPressure >> 8) & 0xFF),
                (byte) (intPressure & 0xFF)
        };
    }

    private void setSeaPressure(String sea_pressure) {
        // unsigned integer(actually float) (3 bytes) (NOTICE: var is multiplied by 10)
        int intPressure = (int) Float.parseFloat(sea_pressure) * 10;
        this.sea_pressure = new byte[]{
                (byte) ((intPressure >> 16) & 0xFF),
                (byte) ((intPressure >> 8) & 0xFF),
                (byte) (intPressure & 0xFF)
        };
    }

    private void setVisibility(String visibility) {
        // unsigned integer(actually float) (2 bytes) (NOTICE: var is multiplied by 10)
        int intVisibility = (int) Float.parseFloat(visibility) * 10;
        this.visibility = new byte[]{
                (byte) ((intVisibility >> 8) & 0xFF),
                (byte) (intVisibility & 0xFF)
        };
    }

    private void setWindSpeed(String wind_speed) {
        // unsigned integer(actually float) (2 bytes) (NOTICE: var is multiplied by 10)
        int intWindSpeed = (int) Float.parseFloat(wind_speed) * 10;
        this.wind_speed = new byte[]{
                (byte) ((intWindSpeed >> 8) & 0xFF),
                (byte) (intWindSpeed & 0xFF)
        };
    }

    private void setRainfall(String rainfall) {
        // unsigned integer(actually float) (2 bytes) (NOTICE: var is multiplied by 100)
        int intRainfall = (int) Float.parseFloat(rainfall) * 100;
        this.rainfall = new byte[]{
                (byte) ((intRainfall >> 16) & 0xFF),
                (byte) ((intRainfall >> 8) & 0xFF),
                (byte) (intRainfall & 0xFF)
        };
    }

    private void setSnowfall(String snowfall) {
        // ! signed integer(actually float) (3 bytes) (NOTICE: var is multiplied by 10)
        int intSnowfall = (int) Float.parseFloat(snowfall) * 10;
        this.snowfall = new byte[]{
                (byte) ((intSnowfall >> 16) & 0xFF),
                (byte) ((intSnowfall >> 8) & 0xFF),
                (byte) (intSnowfall & 0xFF)
        };
    }

    private void setCloudiness(String cloudiness) {
        // unsigned integer(actually float) (2 bytes) (NOTICE: var is multiplied by 10)
        int intCloudiness = (int) Float.parseFloat(cloudiness) * 10;
        this.cloudiness = new byte[]{
                (byte) ((intCloudiness >> 8) & 0xFF),
                (byte) (intCloudiness & 0xFF)
        };
    }

    private void setWindDirection(String wind_direction) {
        // unsigned integer (0 - 359) (2 bytes)
        int intWindDirection = Integer.parseInt(wind_direction);
        this.wind_direction = new byte[]{
                (byte) ((intWindDirection >> 8) & 0xFF),
                (byte) (intWindDirection & 0xFF)
        };
    }

    public static void saveToFileStore(Set<Measurement> measurements) { // might need to add synchronized when every1 starts writing
        try {

            measurements.forEach(measurement -> {
//                    if (measurement.hasMissingData()) {
//                        List<String> missingVars = measurement.getMissingVariables();
//                        for (String var : missingVars) {
//                            Field varField = measurement.getVariable(var);
//                            Object value = varField.get(measurement);
//                            if (value == null) {
//                                // @TODO: object / variable needs corrections
//                                // for now: use the next else statement to throw-away measurement
//                            }
//                        }
//                    } else {
                App.measurementRows.add(measurement.getArrayOfByteVariables()); // adds the row to be written
//                    }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] getArrayOfByteVariables() {
        // This function will form the structure of a Measurement row
//        System.out.println(this.stn.length +
//                this.acquisition_time.length +
//                this.temperature.length +
//                this.dew.length +
//                this.visibility.length);
        byte[] arr = new byte[
                    10
                ];
        ByteBuffer wrapper = ByteBuffer.wrap(arr);
        // 3 bytes for the station identifier
        wrapper.put(valOrNull(this.stn));
        // a single byte containing the amount of bytes for the station country (UTF-8)
//        wrapper.put(new byte[]{(byte) (this.stnCountry.length & 0xFF)});
//        wrapper.put(valOrNull(this.stnCountry));
        // a single byte containing the amount of bytes for the station name (UTF-8)
//        wrapper.put(new byte[]{ (byte) (this.stnCountry.length & 0xFF) });
//        wrapper.put(valOrNull(this.stnName));
        // 4 bytes containing the acquisition date and time in unix timestamp format
        wrapper.put(valOrNull(this.acquisition_time));
        // 2 bytes containing the temperature in a value*10 matter to avoid decimals
        wrapper.put(valOrNull(this.temperature));
        // 2 bytes containing the dew point in a value*10 matter to avoid decimals
        wrapper.put(valOrNull(this.dew));
        // 3 bytes containing the station level pressure in a value*10 matter to avoid decimals
//        wrapper.put(valOrNull(this.station_pressure));
        // 3 bytes containing the sea level pressure in a value*10 matter to avoid decimals
//        wrapper.put(valOrNull(this.sea_pressure));
        // 2 bytes containing the visibility in a value*10 matter to avoid decimals
        wrapper.put(valOrNull(this.visibility));
        // 2 bytes containing the wind speed in a value*10 matter to avoid decimals
//        wrapper.put(valOrNull(this.wind_speed));
        // 2 bytes containing the rainfall in a value*10 matter to avoid decimals
//        wrapper.put(valOrNull(this.rainfall));
        // 3 bytes containing the snowfall in a value*10 matter to avoid decimals
//        wrapper.put(valOrNull(this.snowfall));
        // a single byte with individual bits set for following events:
        // Freezing[0], Raining[1], Snowing[2], Hailing[3], (T)Storms[4] and Tornados[5]
//        wrapper.put(new byte[]{this.events});
        // 2 bytes containing the cloudiness in a value*10 matter to avoid decimals
//        wrapper.put(valOrNull(this.cloudiness));
        // 2 bytes containing the wind direction. notice: no decimals, no value * 10.
//        wrapper.put(valOrNull(this.wind_direction));
        return wrapper.array();
    }

    private byte[] valOrNull(byte[] val) {
        if (val == null) {
            return new byte[]{(byte) 0};
        }
        return val;
    }

    private void setStnName(String stnName) {
        this.stnName = stnName.getBytes(StandardCharsets.UTF_8);
    }

    private void setStnCountry(String stnCountry) {
        this.stnCountry = stnCountry.getBytes(StandardCharsets.UTF_8);
    }
}
