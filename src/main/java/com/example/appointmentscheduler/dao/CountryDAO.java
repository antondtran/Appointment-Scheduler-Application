package com.example.appointmentscheduler.dao;

import com.example.appointmentscheduler.model.Country;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CountryDAO {


    /**
     *
     * @return method returns a list of countries
     * @throws SQLException
     */

    public List<Country> fetchAllCountries() throws SQLException {
        List<Country> countries = new ArrayList<>();

        String query = "SELECT Country_ID, Country FROM countries";

        try (Connection connection = JDBCConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("Country_ID");
                String name = resultSet.getString("Country");
                Country country = new Country(id, name);
                countries.add(country);
            }
        }

        return countries;
    }



}
