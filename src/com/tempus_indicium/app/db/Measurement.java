package com.tempus_indicium.app.db;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by peterzen on 2016-12-23.
 * Part of the 2-2-infrastructures-leertaak-2 project.
 */
public class Measurement extends Model {
    protected static final String db_table = "measurements";

    private Integer stn;
    private Date acquisition_date;
    private Time acquisition_time;
    private Double temperature;
    private Double dew;
    private Double station_pressure;
    private Double sea_pressure;
    private Double visibility;
    private Double wind_speed;
    private Double rainfall;
    private Double snowfall;
    private Boolean did_freeze;
    private Boolean did_rain;
    private Boolean did_snow;
    private Boolean did_hail;
    private Boolean did_storm;
    private Boolean did_tornado;
    private Double cloudiness;
    private Integer wind_direction;

    public Measurement() {

    }

    @Override
    public String toString() {
        return "Measurement{" +
                "stn=" + stn +
                ", acquisition_date=" + acquisition_date +
                ", acquisition_time=" + acquisition_time +
                ", temperature=" + temperature +
                ", dew=" + dew +
                ", station_pressure=" + station_pressure +
                ", sea_pressure=" + sea_pressure +
                ", visibility=" + visibility +
                ", wind_speed=" + wind_speed +
                ", rainfall=" + rainfall +
                ", snowfall=" + snowfall +
                ", did_freeze=" + did_freeze +
                ", did_rain=" + did_rain +
                ", did_snow=" + did_snow +
                ", did_hail=" + did_hail +
                ", did_storm=" + did_storm +
                ", did_tornado=" + did_tornado +
                ", cloudiness=" + cloudiness +
                ", wind_direction=" + wind_direction +
                '}';
    }

    public void setStn(Integer stn) {
        this.stn = stn;
    }

    public void setAcquisitionDate(Date acquisitionDate) {
        this.acquisition_date = acquisitionDate;
    }

    public void setAcquisitionTime(Time acquisitionTime) {
        this.acquisition_time = acquisitionTime;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public void setDew(Double dew) {
        this.dew = dew;
    }

    public void setStationPressure(Double station_pressure) {
        this.station_pressure = station_pressure;
    }

    public void setSeaPressure(Double sea_pressure) {
        this.sea_pressure = sea_pressure;
    }

    public void setVisibility(Double visibility) {
        this.visibility = visibility;
    }

    public void setWindSpeed(Double wind_speed) {
        this.wind_speed = wind_speed;
    }

    public void setRainfall(Double rainfall) {
        this.rainfall = rainfall;
    }

    public void setSnowfall(Double snowfall) {
        this.snowfall = snowfall;
    }

    public void setFreeze(Boolean did_freeze) {
        this.did_freeze = did_freeze;
    }

    public void setRain(Boolean did_rain) {
        this.did_rain = did_rain;
    }

    public void setSnow(Boolean did_snow) {
        this.did_snow = did_snow;
    }

    public void setHail(Boolean did_hail) {
        this.did_hail = did_hail;
    }

    public void setStorm(Boolean did_storm) {
        this.did_storm = did_storm;
    }

    public void setTornado(Boolean did_tornado) {
        this.did_tornado = did_tornado;
    }

    public void setCloudiness(Double cloudiness) {
        this.cloudiness = cloudiness;
    }

    public void setWindDirection(Integer wind_direction) {
        this.wind_direction = wind_direction;
    }
}
