package com.tempus_indicium.app.db;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by peterzen on 2016-12-23.
 * Part of the 2-2-infrastructures-leertaak-2 project.
 */
public class Measurement extends Model {
    int stn;
    Date acquisition_date;
    Time acquisition_time;
    double temperature;
    double dew;
    double station_pressure;
    double sea_pressure;
    double visibility;
    double wind_speed;
    double rainfall;
    double snowfall;
    boolean did_freeze;
    boolean did_rain;
    boolean did_snow;
    boolean did_hail;
    boolean did_storm;
    boolean did_tornado;
    double cloudiness;
    int wind_direction;

    public Measurement() {

    }
    // @TODO: getters and setters
}
