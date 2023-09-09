package com.example.c195.Helper;

import com.example.c195.Model.FirstLevelDivision;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FirstLevelDivisionQuery {
    public static List<FirstLevelDivision> getAllDivisions() {
        List<FirstLevelDivision> divisions = new ArrayList<>();
        String query = "SELECT * FROM FIRST_LEVEL_DIVISIONS";

        try (Connection connection = JDBC.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int divisionId = resultSet.getInt("Division_ID");
                String divisionName = resultSet.getString("Division");
                int countryId = resultSet.getInt("Country_ID");

                FirstLevelDivision division = new FirstLevelDivision(divisionId, divisionName, countryId);
                divisions.add(division);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return divisions;
    }

}
