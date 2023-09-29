package com.example.appointmentscheduler.dao;

import com.example.appointmentscheduler.model.Appointment;
import com.example.appointmentscheduler.model.Customer;
import com.example.appointmentscheduler.model.TotalAppointment;
import com.example.appointmentscheduler.model.TotalStateCustomer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;

import static com.example.appointmentscheduler.dao.JDBCConnection.getConnection;

public class AppointmentDAO {

    /**
     *
     * @param appointment insertAppointment method inserts an appointment object to the database when called.
     * @throws SQLException
     */
    public static void insertAppointment(Appointment appointment) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO appointments (Appointment_ID, Title, Description, Type, Location, Start, End, Create_Date, Created_By, Customer_ID, User_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            LocalDateTime startDateTime = appointment.getStartDateTime();
            LocalDateTime endDateTime = appointment.getEndDateTime();
            Timestamp createDate = Timestamp.valueOf(appointment.getCreatedDate());

            Timestamp timestampStart = (startDateTime != null) ? Timestamp.valueOf(startDateTime) : null;
            Timestamp timestampEnd = (endDateTime != null) ? Timestamp.valueOf(endDateTime) : null;


            preparedStatement.setInt(1, appointment.getId());
            preparedStatement.setString(2, appointment.getTitle());
            preparedStatement.setString(3, appointment.getDescription());
            preparedStatement.setString(4, appointment.getType());
            preparedStatement.setString(5, appointment.getLocation());
            preparedStatement.setTimestamp(6, timestampStart);
            preparedStatement.setTimestamp(7, timestampEnd);
            preparedStatement.setTimestamp(8, createDate);
            preparedStatement.setString(9, appointment.getCreatedBy());
            preparedStatement.setInt(10, appointment.getCustomerID());
            preparedStatement.setInt(11, appointment.getUserID());


            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * @param existingAppointment method updates the existing appointment in the database
     * @throws SQLException
     */
    public static void updateAppointment(Appointment existingAppointment) throws SQLException {

        Connection connection = getConnection();

        Timestamp lastUpdate = Timestamp.valueOf(existingAppointment.getLastUpdate());

        String sql = "UPDATE appointments SET Title = ?, Type = ?, Description = ?, Location = ?, Start = ?, End = ?, Last_Update = ?, Last_Updated_By = ?, User_ID = ?, Customer_ID = ? WHERE Appointment_ID = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, existingAppointment.getTitle());
            statement.setString(2, existingAppointment.getType());
            statement.setString(3, existingAppointment.getDescription());
            statement.setString(4, existingAppointment.getLocation());
            statement.setTimestamp(5, Timestamp.valueOf(existingAppointment.getStartDateTime()));
            statement.setTimestamp(6, Timestamp.valueOf(existingAppointment.getEndDateTime()));
            statement.setTimestamp(7, lastUpdate);
            statement.setString(8, existingAppointment.getLastUpdatedBy());
            statement.setInt(9, existingAppointment.getUserID());
            statement.setInt(10, existingAppointment.getCustomerID());
            statement.setInt(11, existingAppointment.getId());


            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Appointment updated successfully.");
            } else {
                System.out.println("Failed to update appointment.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }


    /**
     *
     * @return method returns the appointment id number that was last used
     */


    public static int getLastUsedAppointmentId() {
        int lastUsedAppointmentId = 0;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT MAX(Appointment_ID) AS max_id FROM appointments")) {

            if (resultSet.next()) {
                lastUsedAppointmentId = resultSet.getInt("max_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lastUsedAppointmentId;
    }


    /**
     * method takes in two parameters. Appointments list and a tableview. This method fetches appointments from a database and adds them to the appointments list. Then the appointments get binded to the table view
     * @param appointments
     * @param appointmentTableView
     */

    public void fetchAppointmentsFromDatabase(ObservableList<Appointment> appointments, TableView<Appointment> appointmentTableView) {

        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM appointments");

            while (resultSet.next()) {
                int id = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String type = resultSet.getString("Type");
                String location = resultSet.getString("Location");
                Timestamp timestamp = resultSet.getTimestamp("Start");
                Timestamp timestamp2 = resultSet.getTimestamp("End");
                Timestamp timestamp3 = resultSet.getTimestamp("Create_Date");

                LocalDateTime startDateTime = (timestamp != null) ? timestamp.toLocalDateTime() : null;
                LocalDateTime endDateTime = (timestamp2 != null) ? timestamp2.toLocalDateTime() : null;
                LocalDateTime createDate = (timestamp3 != null) ? timestamp3.toLocalDateTime() : null;
                String createdBy = resultSet.getString("Created_By");

                int userID = resultSet.getInt("User_ID");
                int customerID = resultSet.getInt("Customer_ID");

                Appointment appointment = new Appointment(id, title, type, description, location, startDateTime, endDateTime, createDate, createdBy, userID, customerID);
                appointments.add(appointment);
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        appointmentTableView.setItems(appointments);
    }

    /**
     *
     * @param id method takes in an id input.
     * @return and retrieves the appointment based on the appointment id.
     */

    public static Appointment getAppointmentById(int id) {
        Appointment appointment = null;
        String query = "SELECT * FROM appointments WHERE Appointment_ID = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int appointmentId = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String type = resultSet.getString("Type");
                String location = resultSet.getString("Location");
                Timestamp startTimestamp = resultSet.getTimestamp("Start");
                Timestamp endTimestamp = resultSet.getTimestamp("End");
                Timestamp createTimestamp = resultSet.getTimestamp("Create_Date");
                String createdBy = resultSet.getString("Created_By");
                int userId = resultSet.getInt("User_ID");
                int customerId = resultSet.getInt("Customer_ID");
                int contactId = resultSet.getInt("Contact_ID");

                // Convert the Timestamp to LocalDateTime
                LocalDateTime startDateTime = (startTimestamp != null) ? startTimestamp.toLocalDateTime() : null;
                LocalDateTime endDateTime = (endTimestamp != null) ? endTimestamp.toLocalDateTime() : null;
                LocalDateTime createDateTime = (createTimestamp != null) ? createTimestamp.toLocalDateTime() : null;

                // Create the Appointment object
                appointment = new Appointment(appointmentId, title, type, description, location, startDateTime,
                        endDateTime, createDateTime, createdBy, userId, customerId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointment;
    }

    /**
     *
     * @param appointmentId method takes in an appointmentId parameter input and deletes the appointment with the given id
     * @throws SQLException
     */
    public static void deleteAppointment(int appointmentId) throws SQLException {
        String query = "DELETE FROM appointments WHERE Appointment_ID = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, appointmentId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     *
     * @return method returns total appointments
     * @throws SQLException
     */

    public ObservableList<TotalAppointment> fetchTotalAppointmentFromDatabase() throws SQLException {
        ObservableList<TotalAppointment> totalAppointmentList = FXCollections.observableArrayList();

        String query = "SELECT COUNT(a.Appointment_ID) AS total_appointments, a.Type, a.Start " +
                "FROM appointments a GROUP BY a.Type, a.Start";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String appointmentType = resultSet.getString("Type");
                int totalAppointments = resultSet.getInt("total_appointments");
                LocalDateTime appointmentMonth = resultSet.getTimestamp("Start").toLocalDateTime();
                TotalAppointment totalAppointment = new TotalAppointment(totalAppointments, appointmentType, appointmentMonth);
                totalAppointmentList.add(totalAppointment);
            }
        }

        return totalAppointmentList;
    }


    /**
     *
     * @return method returns a list of appointments
     */
    public ObservableList<Appointment> fetchAppointmentsFromDB() {

        ObservableList<Appointment> appointmentObservableList = FXCollections.observableArrayList();

        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM appointments");

            while (resultSet.next()) {
                int id = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String type = resultSet.getString("Type");
                String location = resultSet.getString("Location");
                Timestamp timestamp = resultSet.getTimestamp("Start");
                Timestamp timestamp2 = resultSet.getTimestamp("End");
                Timestamp timestamp3 = resultSet.getTimestamp("Create_Date");

                LocalDateTime startDateTime = (timestamp != null) ? timestamp.toLocalDateTime() : null;
                LocalDateTime endDateTime = (timestamp2 != null) ? timestamp2.toLocalDateTime() : null;
                LocalDateTime createDate = (timestamp3 != null) ? timestamp3.toLocalDateTime() : null;
                String createdBy = resultSet.getString("Created_By");

                int userID = resultSet.getInt("User_ID");
                int customerID = resultSet.getInt("Customer_ID");

                Appointment appointment = new Appointment(id, title, type, description, location, startDateTime, endDateTime, createDate, createdBy, userID, customerID);
                appointmentObservableList.add(appointment);
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointmentObservableList;
    }





    public static ObservableList<Appointment> fetchAppointmentsForCustomer(String customerName) throws SQLException {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(
                     "SELECT * FROM appointments WHERE Customer_ID = ?")) {

            statement.setString(1, customerName);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("Appointment_ID");
                    String title = resultSet.getString("Title");
                    String description = resultSet.getString("Description");
                    String type = resultSet.getString("Type");
                    String location = resultSet.getString("Location");
                    Timestamp timestamp = resultSet.getTimestamp("Start");
                    Timestamp timestamp2 = resultSet.getTimestamp("End");
                    Timestamp timestamp3 = resultSet.getTimestamp("Create_Date");

                    LocalDateTime startDateTime = (timestamp != null) ? timestamp.toLocalDateTime() : null;
                    LocalDateTime endDateTime = (timestamp2 != null) ? timestamp2.toLocalDateTime() : null;
                    LocalDateTime createDate = (timestamp3 != null) ? timestamp3.toLocalDateTime() : null;
                    String createdBy = resultSet.getString("Created_By");

                    int userID = resultSet.getInt("User_ID");
                    int customerID = resultSet.getInt("Customer_ID");

                    Appointment appointment = new Appointment(id, title, type, description, startDateTime, endDateTime, customerID);
                    appointments.add(appointment);
                }
            }
        }

        return appointments;
    }

    public static HashMap<Integer, String> getCustomerIdToNameMapping() throws SQLException {
        HashMap<Integer, String> customerIdToNameMap = new HashMap<>();

        try (Connection conn = getConnection();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT Customer_ID, Customer_Name FROM customers")) {

            while (resultSet.next()) {
                int customerId = resultSet.getInt("Customer_ID");
                String customerName = resultSet.getString("Customer_Name");
                customerIdToNameMap.put(customerId, customerName);
            }
        }

        return customerIdToNameMap;
    }


}
