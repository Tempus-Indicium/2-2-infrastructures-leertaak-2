package com.tempus_indicium.app.parsing;

import com.tempus_indicium.app.App;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;

/**
 * Created by peterzen on 2016-12-23.
 * Part of the 2-2-infrastructures-leertaak-2 project.
 *
 * MeasurementParser:
 * Parses the measurement data to be in a correct form for Measurement models
 */
public class MeasurementParser {

    public Integer stn(String stnId) {
        try {
            return Integer.parseUnsignedInt(stnId);
        } catch (Exception e) {
//            App.LOGGER.log(Level.WARNING, e.getMessage());
        }
        return null;
    }

    public Integer windDirection(String windDirection) {
        try {
            return Integer.parseUnsignedInt(windDirection);
        } catch (Exception e) {
//            App.LOGGER.log(Level.WARNING, e.getMessage());
        }
        return null;
    }

    public java.sql.Date acquisitionDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return new java.sql.Date(dateFormat.parse(date).getTime());
        } catch (ParseException e) {
//            App.LOGGER.log(Level.WARNING, e.getMessage());
        }
        return null;
    }

    public Time acquisitionTime(String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        try {
            return new java.sql.Time(dateFormat.parse(time).getTime());
        } catch (ParseException e) {
//            App.LOGGER.log(Level.WARNING, e.getMessage());
        }
        return null;
    }

    /**
     * Notice: if special behaviour is needed, do not edit this method
     * Instead you may create an alternative method based on this one
     * @param String standardDouble string to be parsed into a Double
     * @return nullable Double object
     */
    public Double standardDouble(String standardDouble) {
        try {
            return Double.parseDouble(standardDouble);
        } catch (Exception e) {
//            App.LOGGER.log(Level.WARNING, e.getMessage());
        }
        return null;
    }

}
