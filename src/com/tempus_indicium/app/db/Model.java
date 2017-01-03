package com.tempus_indicium.app.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by peterzen on 2016-12-20.
 * Part of the 2-2-infrastructures-leertaak-2 project.
 */
public class Model {

    // @TODO: function to build insert query for a specific Model
    // Idea: may make this into an interface or abstract class?
    // @TODO: build generic select statements

    protected static boolean saveBatch(List<Model> rows, Connection conn) throws SQLException {
        // @TODO: sql string is not generic enough
        String sql = "INSERT INTO "+Measurement.db_table+" VALUES (?, ?)";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);

        for (Model row : rows) {
            preparedStatement.clearParameters();
            // set ps values
        }

        return false;
    }


}
