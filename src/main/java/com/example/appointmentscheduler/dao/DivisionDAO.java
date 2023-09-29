package com.example.appointmentscheduler.dao;

import com.example.appointmentscheduler.model.TotalStateCustomer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DivisionDAO {


    /**
     *
     * @return method returns a list division names that are from the U.S
     * @throws SQLException
     */

    public List<String> fetchUSStateFromDatabase() throws SQLException {
        List<String> states = new ArrayList<>();

        String query = "SELECT Division FROM first_level_divisions WHERE Country_ID = 1";

        try (Connection connection = JDBCConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String state = resultSet.getString("Division");
                states.add(state);
            }
        }

        return states;
    }


    /**
     *
     * @return method returns a list of divisions from the UK
     * @throws SQLException
     */

    public List<String> fetchUKFromDatabase() throws SQLException {
        List<String> states = new ArrayList<>();

        String query = "SELECT Division FROM first_level_divisions WHERE Country_ID = 2";

        try (Connection connection = JDBCConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String state = resultSet.getString("Division");
                states.add(state);
            }
        }

        return states;
    }

    /**
     *
     * @return method returns a list of divisions from Canada
     * @throws SQLException
     */

    public List<String> fetchCanadaFromDatabase() throws SQLException {
        List<String> states = new ArrayList<>();

        String query = "SELECT Division FROM first_level_divisions WHERE Country_ID = 3";

        try (Connection connection = JDBCConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String state = resultSet.getString("Division");
                states.add(state);
            }
        }

        return states;
    }

    /**
     *
     * @return method returns division id and division name from the database.
     * @throws SQLException
     */

    public Map<String, Integer> fetchDivisionFromDb() throws SQLException {
        Map<String, Integer> divisionMap = new HashMap<>();

        String query = "SELECT Division_ID, Division FROM first_level_divisions";

        try (Connection connection = JDBCConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int divisionId = resultSet.getInt("Division_ID");
                String divisionName = resultSet.getString("Division");
                divisionMap.put(divisionName, divisionId);
            }
        }

        return divisionMap;
    }


    /**
     *
     * @return method maps the corresponding division name with its division id
     * @throws SQLException
     */

    public Map<Integer, String> fetchDivisionFromDbb() throws SQLException {
        Map<Integer, String> divisionMap = new HashMap<>();

        String query = "SELECT Division_ID, Division FROM first_level_divisions";

        try (Connection connection = JDBCConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int divisionId = resultSet.getInt("Division_ID");
                String divisionName = resultSet.getString("Division");
                divisionMap.put(divisionId, divisionName);
            }
        }

        return divisionMap;
    }

    /**
     *
     * @return method returns a list of total state customer. This method is used for finding the total customers for each division(state)
     * @throws SQLException
     */

    public ObservableList<TotalStateCustomer> fetchDataFromDatabase() throws SQLException {
        ObservableList<TotalStateCustomer> totalStateCustomerList = FXCollections.observableArrayList();

        String query = "SELECT s.Division, COUNT(c.Customer_ID) AS total_customers " +
                "FROM first_level_divisions s " +
                "LEFT JOIN customers c ON s.Division_ID = c.Division_ID " +
                "GROUP BY s.Division";

        try (Connection connection = JDBCConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String stateName = resultSet.getString("Division");
                int totalCustomers = resultSet.getInt("total_customers");
                TotalStateCustomer totalStateCustomer = new TotalStateCustomer(stateName, totalCustomers);
                totalStateCustomerList.add(totalStateCustomer);
            }
        }

        return totalStateCustomerList;
    }


    /**
     *
     * @param selectedCountry method takes the user selected country as a parameter input
     * @return method returns a list of division based on the selected country
     * @throws SQLException
     */

    public ObservableList<String> fetchDivisionsByCountry(String selectedCountry) throws SQLException {
        ObservableList<String> divisions = FXCollections.observableArrayList();

        try {
            if ("U.S".equals(selectedCountry)) {
                divisions.addAll(fetchUSStateFromDatabase());
            } else if ("UK".equals(selectedCountry)) {
                divisions.addAll(fetchUKFromDatabase());
            } else if ("Canada".equals(selectedCountry)) {
                divisions.addAll(fetchCanadaFromDatabase());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return divisions;
    }






    /**
     *
     * @param division method takes in a division input
     * @return method returns thee country based on the division name
     */
    public String fetchCountryByDivision(String division) {
        try {
            String query = "SELECT c.Country " +
                    "FROM first_level_divisions d " +
                    "JOIN countries c ON d.Country_ID = c.Country_ID " +
                    "WHERE d.Division = ?";

            try (Connection connection = JDBCConnection.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, division);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("Country");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     *
     * @return method returns the list of country names
     * @throws SQLException
     */

    public List<String> fetchCountryNames() throws SQLException {
        List<String> countryNames = new ArrayList<>();

        try (Connection connection = JDBCConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT DISTINCT Country FROM countries");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String countryName = resultSet.getString("Country");
                countryNames.add(countryName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return countryNames;
    }





}
