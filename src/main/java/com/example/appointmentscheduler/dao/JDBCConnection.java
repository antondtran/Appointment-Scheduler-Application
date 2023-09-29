package com.example.appointmentscheduler.dao;

import com.example.appointmentscheduler.model.User;

import java.sql.*;

public class JDBCConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/client_schedule";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Passw0rd!";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    /**
     *
     * @param username parameter input for username field
     * @param password paremeter input for password field
     * @return returns a boolean value based on whether the connection was successful
     */

    public static boolean verifyLogin(String username, String password) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }



}
