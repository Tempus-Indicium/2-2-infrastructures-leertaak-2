package com.tempus_indicium.app.parsing;

import com.tempus_indicium.app.db.Measurement;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by peterzen on 2017-01-18.
 * Part of the 2-2-infrastructures-leertaak-2 project.
 */
public class MeasurementCorrection {

    public static void correctMeasurement(Measurement measurement) {
        if (measurement.hasMissingData()) {
            List<String> missingVariables = measurement.getMissingVariables();
            for (String variable : missingVariables) {
                try {
                    Field field = measurement.getVariable(variable);
        // @TODO: finish or change this depending on the way we decide to do data correction



                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
