package com.tempus_indicium.app.db;

import java.sql.*;
import java.util.List;

import static java.sql.Statement.EXECUTE_FAILED;

/**
 * Created by peterzen on 2016-12-23.
 * Part of the 2-2-infrastructures-leertaak-2 project.
 */
public class Measurement extends Model {
    private static final String db_table = "measurements";

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

    public static boolean saveBatch(List<Measurement> measurements, Connection conn) throws SQLException {
        // INSERT INTO `unwdmi`.`measurements` (`stn`, `acquisition_date`, `acquisition_time`, `temperature`, `dew`, `station_pressure`, `sea_pressure`, `visibility`, `wind_speed`, `rainfall`, `snowfall`, `did_freeze`, `did_rain`, `did_snow`, `did_hail`, `did_storm`, `did_tornado`, `cloudiness`, `wind_direction`) VALUES ('1', '2017-01-14', '15:40', '20.0', '23.20', '10.0', '5.0', '59', '4', '10', '5', '1', '1', '1', '0', '0', '0', '10', '300');
        String columns = "(`stn`, `acquisition_date`, `acquisition_time`, `temperature`, `dew`, `station_pressure`, `sea_pressure`, `visibility`, `wind_speed`, `rainfall`, `snowfall`, `did_freeze`, `did_rain`, `did_snow`, `did_hail`, `did_storm`, `did_tornado`, `cloudiness`, `wind_direction`)";

        String sql = "INSERT INTO " + Measurement.db_table + " " + columns + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);

        for (Measurement m : measurements) {
            // set ps values
            m.prepSetters(preparedStatement); // prepare the PreparedStatement object for this measurement
            preparedStatement.addBatch();
        }

        int[] result = preparedStatement.executeBatch();
        preparedStatement.close();

        return result[0] != EXECUTE_FAILED;
    }

    private PreparedStatement prepSetters(PreparedStatement preparedStatement) throws SQLException {
        if (this.stn != null) {
            preparedStatement.setInt(1, this.stn);
        } else {
            preparedStatement.setNull(1, Types.INTEGER);
        }
        if (this.acquisition_date != null) {
            preparedStatement.setDate(2, this.acquisition_date);
        } else {
            preparedStatement.setNull(2, Types.DATE);
        }
        if (this.acquisition_time != null) {
            preparedStatement.setTime(3, this.acquisition_time);
        } else {
            preparedStatement.setNull(3, Types.TIME);
        }
        if (this.temperature != null) {
            preparedStatement.setDouble(4, this.temperature);
        } else {
            preparedStatement.setNull(4, Types.DOUBLE);
        }
        if (this.dew != null) {
            preparedStatement.setDouble(5, this.dew);
        } else {
            preparedStatement.setNull(5, Types.DOUBLE);
        }
        if (this.station_pressure != null) {
            preparedStatement.setDouble(6, this.station_pressure);
        } else {
            preparedStatement.setNull(6, Types.DOUBLE);
        }
        if (this.sea_pressure != null) {
            preparedStatement.setDouble(7, this.sea_pressure);
        } else {
            preparedStatement.setNull(7, Types.DOUBLE);
        }
        if (this.visibility != null) {
            preparedStatement.setDouble(8, this.visibility);
        } else {
            preparedStatement.setNull(8, Types.DOUBLE);
        }
        if (this.wind_speed != null) {
            preparedStatement.setDouble(9, this.wind_speed);
        } else {
            preparedStatement.setNull(9, Types.DOUBLE);
        }
        if (this.rainfall != null) {
            preparedStatement.setDouble(10, this.rainfall);
        } else {
            preparedStatement.setNull(10, Types.DOUBLE);
        }
        if (this.snowfall != null) {
            preparedStatement.setDouble(11, this.snowfall);
        } else {
            preparedStatement.setNull(11, Types.DOUBLE);
        }
        if (this.did_freeze != null) {
            preparedStatement.setBoolean(12, this.did_freeze);
        } else {
            preparedStatement.setNull(12, Types.TINYINT);
        }
        if (this.did_rain != null) {
            preparedStatement.setBoolean(13, this.did_rain);
        } else {
            preparedStatement.setNull(13, Types.TINYINT);
        }
        if (this.did_snow != null) {
            preparedStatement.setBoolean(14, this.did_snow);
        } else {
            preparedStatement.setNull(14, Types.TINYINT);
        }
        if (this.did_hail != null) {
            preparedStatement.setBoolean(15, this.did_hail);
        } else {
            preparedStatement.setNull(15, Types.TINYINT);
        }
        if (this.did_storm != null) {
            preparedStatement.setBoolean(16, this.did_storm);
        } else {
            preparedStatement.setNull(16, Types.TINYINT);
        }
        if (this.did_tornado != null) {
            preparedStatement.setBoolean(17, this.did_tornado);
        } else {
            preparedStatement.setNull(17, Types.TINYINT);
        }
        if (this.cloudiness != null) {
            preparedStatement.setDouble(18, this.cloudiness);
        } else {
            preparedStatement.setNull(18, Types.DOUBLE);
        }
        if (this.wind_direction != null) {
            preparedStatement.setInt(19, this.wind_direction);
        } else {
            preparedStatement.setNull(19, Types.INTEGER);
        }
        return preparedStatement;
    }
}
