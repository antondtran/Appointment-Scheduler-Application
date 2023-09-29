package com.example.appointmentscheduler.dao;

import com.example.appointmentscheduler.model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.sql.*;

import java.util.Map;

import static com.example.appointmentscheduler.dao.JDBCConnection.getConnection;

public class CustomerDAO {


    /**
     *
     * @return method returns the last customer id in the database
     */
    public static int getLastUsedCustomerId() {
        int lastUsedCustomerId = 0;

        String query = "SELECT MAX(Customer_ID) FROM customers";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                lastUsedCustomerId = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lastUsedCustomerId;
    }

    /**
     * method takes in two parameter inputs a customer list object and a tableview. Method fetches the customer from the db and saves it to the customer list. the list is then used to populate the tableview
     * @param customers (customer list object)
     * @param customerTableView (tableview)
     */

    public void fetchCustomersFromDatabase(ObservableList<Customer> customers, TableView<Customer> customerTableView) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM customers")) {

            customers.clear();

            DivisionDAO divisionDAO = new DivisionDAO();
            Map<Integer, String> divisionMap = divisionDAO.fetchDivisionFromDbb();

            while (resultSet.next()) {
                int id = resultSet.getInt("Customer_ID");
                String name = resultSet.getString("Customer_Name");
                String address = resultSet.getString("Address");
                int divisionId = resultSet.getInt("Division_ID");
                String postalCode = resultSet.getString("Postal_Code");
                String phone = resultSet.getString("Phone");

                String divisionName = divisionMap.getOrDefault(divisionId, "Unknown");

                Customer customer = new Customer(id, name, address, phone, divisionId, postalCode);
                customer.setDivisionName(divisionName);

                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        customerTableView.setItems(customers);
        return;
    }



    public static ObservableList<Customer> fetchCustomers() throws SQLException {
        ObservableList<Customer> customers = FXCollections.observableArrayList();

        try (Connection conn = getConnection();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM customers")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("Customer_ID");
                String name = resultSet.getString("Customer_Name");
                String address = resultSet.getString("Address");
                int divisionId = resultSet.getInt("Division_ID");
                String postalCode = resultSet.getString("Postal_Code");
                String phone = resultSet.getString("Phone");

                Customer customer = new Customer(id, name, address, phone, divisionId, postalCode);
                customers.add(customer);
            }
        }

        return customers;
    }


    /**
     *
     * @param customer method takes in a customer object that gets saved to the database.
     * @throws SQLException
     */

    public static void insertCustomer(Customer customer) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO customers (Customer_ID, Customer_Name, Address, Division_ID, Phone, Postal_Code, Create_Date, Created_By) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {

            Timestamp createDate = Timestamp.valueOf(customer.getCreatedDate());
            preparedStatement.setInt(1, customer.getId());
            preparedStatement.setString(2, customer.getName());
            preparedStatement.setString(3, customer.getAddress());
            preparedStatement.setInt(4, customer.getDivisionID());
            preparedStatement.setString(5, customer.getPhoneNumber());
            preparedStatement.setString(6, customer.getPostalCode());
            preparedStatement.setTimestamp(7, createDate);
            preparedStatement.setString(8, customer.getCreatedBy());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    /**
     *
     * @param customerID method takes in a customer id and based on the id the database will delete the customer object.
     * @throws SQLException
     */

    public static void deleteCustomer(int customerID) throws SQLException {
        String query = "DELETE FROM customers WHERE Customer_ID = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, customerID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }


    /**
     *
     * @param existingCustomer method takes in an existing customer object and updates it in the database
     * @throws SQLException
     */
    public static void updateCustomer(Customer existingCustomer) throws SQLException {

        Connection connection = getConnection();
        Timestamp lastUpdate = Timestamp.valueOf(existingCustomer.getLastUpdate());

        String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Division_ID = ?, Phone = ?, Postal_Code = ?, Last_Update = ?, Last_Updated_By = ? WHERE Customer_ID = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, existingCustomer.getName());
            statement.setString(2, existingCustomer.getAddress());
            statement.setInt(3, existingCustomer.getDivisionID());
            statement.setString(4, existingCustomer.getPhoneNumber());
            statement.setString(5, existingCustomer.getPostalCode());
            statement.setTimestamp(6, lastUpdate);
            statement.setString(7, existingCustomer.getLastUpdatedBy());
            statement.setInt(8, existingCustomer.getId());


            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Customer updated successfully.");
            } else {
                System.out.println("Failed to update customer.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            connection.close();
        }
    }

    /**
     *
     * @param id method takes an integer id input
     * @return method retrieves the customer based on the id input
     */

    public static Customer getCustomerById(int id) {
        Customer customer = null;
        String query = "SELECT * FROM customers WHERE Customer_ID = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int customerID = resultSet.getInt("Customer_ID");
                String customerName = resultSet.getString("Customer_Name");
                String address = resultSet.getString("Address");
                String phone = resultSet.getString("Phone");
                int division = resultSet.getInt("Division_ID");
                String postalCode = resultSet.getString("Postal_Code");

                customer = new Customer(customerID, customerName, address, phone, division, postalCode);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customer;
    }


    /**
     *
     * @param customerID method takes in a customer id as input
     * @return method returns a boolean value based on whether the customer exists in the database
     */

    public static boolean doesCustomerExist(int customerID) {
        String query = "SELECT COUNT(*) FROM customers WHERE Customer_ID = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, customerID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
