package com.tempus_indicium.app.db;

import com.tempus_indicium.app.App;

import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by peterzen on 2016-12-23.
 * Part of the 2-2-infrastructures-leertaak-2 project.
 */
public class Measurement {
    private byte[] stn;
    private byte[] acquisition_time;
    private byte[] temperature;
    private byte[] dew;
    private byte[] visibility;

    public Measurement() {
    }

    public void setVariableFromXMLString(String xmlString) {
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
        if (xmlString.contains("<VISIB>")) {
            String visib;
            if ((visib = this.findMatchInXMLString(App.xmlPatterns.get("visib"), xmlString)) != null) {
                this.setVisibility(visib);
            }
        }
    }

    // returns true if station is in our data store, false otherwise
    public boolean setStnFromXmlString(String xmlString) {
        String stn;
        if ((stn = this.findMatchInXMLString(App.xmlPatterns.get("stn"), xmlString)) != null) {
            String[] stnDetails;
            if ((stnDetails = FileStore.stations.get(Integer.parseInt(stn))) != null) {
                // station details found
                this.setStn(stnDetails[0]); // set the station id using the regenerated `id` column
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

    // @TODO: debug and solve potential bug with the saving of time (timestamps do not make sense when reading)
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

    private void setVisibility(String visibility) {
        // unsigned integer(actually float) (2 bytes) (NOTICE: var is multiplied by 10)
        int intVisibility = (int) Float.parseFloat(visibility) * 10;
        this.visibility = new byte[]{
                (byte) ((intVisibility >> 8) & 0xFF),
                (byte) (intVisibility & 0xFF)
        };
    }

    public byte[] getArrayOfByteVariables() {
        // This function will form the structure of a Measurement row
//        System.out.println(this.stn.length +
//                this.acquisition_time.length +
//                this.temperature.length +
//                this.dew.length +
//                this.visibility.length);
        byte[] arr = new byte[10];
        ByteBuffer wrapper = ByteBuffer.wrap(arr);
        // 2 bytes for the station identifier
        wrapper.put(valOrNull(this.stn));
        // a single byte containing the amount of bytes for the station country (UTF-8)
//        wrapper.put(new byte[]{(byte) (this.stnCountry.length & 0xFF)});
//        wrapper.put(valOrNull(this.stnCountry));
        // a single byte containing the amount of bytes for the station name (UTF-8)
//        wrapper.put(new byte[]{ (byte) (this.stnCountry.length & 0xFF) });
//        wrapper.put(valOrNull(this.stnName));
        // 2 bytes containing the acquisition time in unix timestamp format (HH:mm:ss, also timestamp is divided by 1000)
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

    // to prevent NullPointerExceptions write zeroes instead
    private byte[] valOrNull(byte[] val) {
        if (val == null) {
            return new byte[]{(byte) 0};
        }
        return val;
    }
}
