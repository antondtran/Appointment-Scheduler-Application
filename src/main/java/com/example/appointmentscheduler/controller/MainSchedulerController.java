package com.example.appointmentscheduler.controller;

import com.example.appointmentscheduler.dao.*;
import com.example.appointmentscheduler.model.*;

import javafx.collections.FXCollections;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.SQLException;
import java.time.*;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;


public class MainSchedulerController {

    @FXML
    TableColumn<Appointment, Integer> idColumn;

    @FXML
    TableColumn<Appointment, String> titleColumn;

    @FXML
    TableColumn<Appointment, String> descriptionColumn;

    @FXML
    TableColumn<Appointment, String> typeColumn;

    @FXML
    TableColumn<Appointment, String> locationColumn;

    @FXML
    TableColumn<Appointment, LocalDateTime> startDateTimeColumn;

    @FXML
    TableColumn<Appointment, LocalDateTime> endDateTimeColumn;

    @FXML
    TableColumn<Appointment, Integer> contactColumn;

    @FXML
    TableColumn<Appointment, Integer> customerColumn;

    @FXML
    TableColumn<Appointment, Integer> userColumn;

    @FXML
    TableView<Appointment> appointmentTableView;

    @FXML
    TableView<Customer> customerTableView;

    @FXML
    TableColumn<Customer, Integer> customerIdColumn;

    @FXML
    TableColumn<Customer, String> customerNameColumn;

    @FXML
    TableColumn<Customer, String> customerAddColumn;

    @FXML
    TableColumn<Customer, Integer> customerPhoneColumn;

    @FXML
    TableColumn<Customer, String> customerStateColumn;

    @FXML
    TableColumn<Customer, Integer> customerCodeColumn;

    @FXML
    RadioButton currentWeekBtn;

    @FXML
    RadioButton currentMonthBtn;

    @FXML
    RadioButton currentAppointmentBtn;

    private ToggleGroup toggleGroup;

    @FXML
    private Button logoutBtn;

    @FXML
    private TextField searchPart;





    private AppointmentController appointmentController;
    private CustomerController customerController;
    private Customer customer;
    private Appointment appointment;
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a");
    private AppointmentDAO appointmentDAO;
    private CustomerDAO customerDAO;
    private DivisionDAO divisionDAO;

    private CountryDAO countryDAO;
    private Map<String, Integer> countryIdMap;


    /**
     *
     * @throws IOException method initializes the appointmentDAO, ccustomerDAO, divisionDAO, contacctDao, and countryDAO objects.
     */

    public MainSchedulerController() throws IOException {
        // Initialize the AppointmentDAO instance here
        appointmentDAO = new AppointmentDAO();
        customerDAO = new CustomerDAO();
        divisionDAO = new DivisionDAO();
        countryDAO = new CountryDAO();
    }



    public void initialize() throws SQLException {


        TimeZone userTimeZone = TimeZone.getDefault();
        TimeZone.setDefault(userTimeZone);

        updateDateTimeColumnsWithTimeZone(userTimeZone);

        currentWeekBtn.setOnAction(event -> filterAppointmentsForCurrentWeek());


        /**
         * Lambda
         * Event handler for the logout button.
         *
         * This event handler is triggered when the user clicks the logout button.
         * It closes the current window (Stage) logging the user off.
         *
         * @param event The action event that triggers the logout action.
         */
        logoutBtn.setOnAction(event -> {
            Stage stage = (Stage) logoutBtn.getScene().getWindow();
            stage.close();
        });

        toggleGroup = new ToggleGroup();
        currentWeekBtn.setToggleGroup(toggleGroup);
        currentMonthBtn.setToggleGroup(toggleGroup);
        currentAppointmentBtn.setToggleGroup(toggleGroup);
        currentAppointmentBtn.setSelected(true);
        checkAndShowAppointmentAlerts();




        appointment = new Appointment();
        customer = new Customer();


        appointmentDAO.fetchAppointmentsFromDatabase(appointment.getAppointmentList(), appointmentTableView);
        customerDAO.fetchCustomersFromDatabase(customer.getCustomerList(), customerTableView);


        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        startDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        endDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));




        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));


        appointmentTableView.setItems(appointment.getAppointmentList());


        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        customerStateColumn.setCellValueFactory(new PropertyValueFactory<>("divisionName"));
        customerCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerTableView.setItems(customer.getCustomerList());

        searchPart.textProperty().addListener((observable, oldValue, newValue) -> {
            String searchQuery = newValue.toLowerCase();

            ObservableList<Customer> filteredCustomers = customer.getCustomerList().filtered(customer -> customer.getName().toLowerCase().contains(searchQuery));

            customerTableView.setItems(filteredCustomers);
        });

    }





    /**
     *
     * @param timeZone method takes in the useers time zone and updates the column accordingly
     */

    private void updateDateTimeColumnsWithTimeZone(TimeZone timeZone) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a");

        startDateTimeColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime dateTime, boolean empty) {
                super.updateItem(dateTime, empty);

                if (empty || dateTime == null) {
                    setText(null);
                } else {
                    ZonedDateTime zonedDateTime = dateTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(timeZone.toZoneId());
                    setText(dateTimeFormatter.format(zonedDateTime));
                }
            }
        });

        endDateTimeColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime dateTime, boolean empty) {
                super.updateItem(dateTime, empty);

                if (empty || dateTime == null) {
                    setText(null);
                } else {
                    ZonedDateTime zonedDateTime = dateTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(timeZone.toZoneId());
                    setText(dateTimeFormatter.format(zonedDateTime));
                }
            }
        });
    }




    /**
     *
     * @param event add appointment button when clicked will open up and display an appointment add form for users to add new appointments to the table.
     * @throws IOException
     */


    public void addAppointmentBtn(ActionEvent event) throws IOException {

        ObservableList<LocalTime> timeOptions = FXCollections.observableArrayList(
                LocalTime.of(8, 0),
                LocalTime.of(8, 30),
                LocalTime.of(9, 0),
                LocalTime.of(9, 30),
                LocalTime.of(10, 0),
                LocalTime.of(10, 30),
                LocalTime.of(11, 0),
                LocalTime.of(11, 30),
                LocalTime.of(12, 0),
                LocalTime.of(12, 30),
                LocalTime.of(13, 0),
                LocalTime.of(13, 30),
                LocalTime.of(14, 0),
                LocalTime.of(14, 30),
                LocalTime.of(15, 0),
                LocalTime.of(15, 30),
                LocalTime.of(16, 0),
                LocalTime.of(16, 30),
                LocalTime.of(17, 0),
                LocalTime.of(17, 30),
                LocalTime.of(18, 0),
                LocalTime.of(18, 30),
                LocalTime.of(19, 0),
                LocalTime.of(19, 30),
                LocalTime.of(20, 0),
                LocalTime.of(20, 30),
                LocalTime.of(21, 0),
                LocalTime.of(21, 30),
                LocalTime.of(22, 0)
        );



        Map<String, Integer> userMap = new HashMap<>();
        userMap.put("admin", 1);


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/appointmentscheduler/AppointmentForm.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Add Form");
        stage.setScene(scene);
        stage.show();


        TextField appointmentIDText = (TextField) loader.getNamespace().get("idTextField");
        appointmentIDText.setEditable(false);
        appointmentIDText.setPromptText("Auto Generated ID");

        ComboBox<LocalTime> startTimePicker = (ComboBox<LocalTime>) loader.getNamespace().get("startTimePicker");
        startTimePicker.setItems(timeOptions);

        ComboBox<LocalTime> endTimePicker = (ComboBox<LocalTime>) loader.getNamespace().get("endTimePicker");
        endTimePicker.setItems(timeOptions);


        ComboBox<String> userPicker = (ComboBox) loader.getNamespace().get("userPicker");
        userPicker.setItems(FXCollections.observableArrayList(userMap.keySet()));


        // Get the instance of AddAppointmentController
        appointmentController = loader.getController();
        appointmentController.setUserMap(userMap);

        // Set the MainSchedulerController instance
        appointmentController.setMainSchedulerController(this);
        appointmentTableView.refresh();


    }


    /**
     * method refreshes the appointment table view
     */

    public void refreshAppointmentTable() {
        appointmentTableView.refresh();
    }

    /**
     * method refreshes the customer table view
     */
    public void refreshCustomerTable() {
        customerTableView.refresh();
    }


    /**
     *
     * @return method returns an appointment object
     */
    public Appointment getAppointment() {
        return appointment;
    }


    /**
     *
     * @return method returns a customer object
     */

    public Customer getCustomer(){
        return customer;
    }


    /**
     *
     * @param event method add customer button will display a customer add form when invoked. This form allows users to add customers to the table
     * @throws IOException
     * @throws SQLException
     */
    public void addCustomerBtn(ActionEvent event) throws IOException, SQLException {



        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/appointmentscheduler/CustomerForm.fxml"));
        Parent root = loader.load();

        ComboBox<String> countryComboField = (ComboBox<String>) loader.getNamespace().get("countryCombo");
        ComboBox<String> stateComboField = (ComboBox<String>) loader.getNamespace().get("stateComboField");



        // Create a map to store country names and their corresponding country IDs
        Map<String, Integer> countryIdMap = new HashMap<>();
        List<Country> countries = countryDAO.fetchAllCountries();
        for (Country country : countries) {
            countryIdMap.put(country.getCountry(), country.getCountryID());
        }

        ObservableList<String> countryNames = FXCollections.observableArrayList(countryIdMap.keySet());
        countryComboField.setItems(countryNames);



        /**
         * Lambda | countryComboField.valueProperty().addListener Method
         * Listener for changes in the selected value of the country combo box.
         * This listener is triggered when the user selects a different country.
         * It fetches divisions (states) based on the selected country and populates the state combo box
         * with the retrieved divisions.
         *
         * @param observable The observable property representing the selected value
         * @param oldValue   The previous selected value
         * @param newValue   The new selected value
         * @implNote When a new country value is selected, this listener queries the divisionDAO to fetch
         *           divisions associated with the selected country. The retrieved divisions are then used
         *           to update the items in the state combo box.
         */
        countryComboField.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    // Fetch divisions based on the selected country
                    ObservableList<String> divisions = FXCollections.observableArrayList(divisionDAO.fetchDivisionsByCountry(newValue));
                    stateComboField.setItems(divisions);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });




        TextField customerIDText = (TextField) loader.getNamespace().get("idTextField");
        customerIDText.setEditable(false);
        customerIDText.setPromptText("Auto Generated ID");


        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Add Form");
        stage.setScene(scene);
        stage.show();

        customerController = loader.getController();
        customerController.setMainSchedulerController(this);
        customerTableView.refresh();

    }


    /**
     *
     * @param event method update appointment button will display a populated form with the selected appointments data.
     * @throws IOException
     */

    public void updateAppointmentBtn(ActionEvent event) throws IOException {
        ObservableList<LocalTime> timeOptions = FXCollections.observableArrayList(
                LocalTime.of(8, 0),
                LocalTime.of(8, 30),
                LocalTime.of(9, 0),
                LocalTime.of(9, 30),
                LocalTime.of(10, 0),
                LocalTime.of(10, 30),
                LocalTime.of(11, 0),
                LocalTime.of(11, 30),
                LocalTime.of(12, 0),
                LocalTime.of(12, 30),
                LocalTime.of(13, 0),
                LocalTime.of(13, 30),
                LocalTime.of(14, 0),
                LocalTime.of(14, 30),
                LocalTime.of(15, 0),
                LocalTime.of(15, 30),
                LocalTime.of(16, 0),
                LocalTime.of(16, 30),
                LocalTime.of(17, 0),
                LocalTime.of(17, 30),
                LocalTime.of(18, 0),
                LocalTime.of(18, 30),
                LocalTime.of(19, 0),
                LocalTime.of(19, 30),
                LocalTime.of(20, 0),
                LocalTime.of(20, 30),
                LocalTime.of(21, 0),
                LocalTime.of(21, 30),
                LocalTime.of(22, 0)
        );


        Map<String, Integer> userMap = new HashMap<>();
        userMap.put("admin", 1);





        Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            // Handle the case when no product is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Appointment Selected");
            alert.setHeaderText("Please select an appointment to update.");
            alert.showAndWait();
            return;
        }

        // Load the FXML file for the appointment form
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/appointmentscheduler/AppointmentForm.fxml"));
        Parent root = loader.load();



        TextField appointmentIDText = (TextField) loader.getNamespace().get("idTextField");
        appointmentIDText.setEditable(false);

        ComboBox<LocalTime> startTimePicker = (ComboBox<LocalTime>) loader.getNamespace().get("startTimePicker");
        startTimePicker.setItems(timeOptions);

        ComboBox<LocalTime> endTimePicker = (ComboBox<LocalTime>) loader.getNamespace().get("endTimePicker");
        endTimePicker.setItems(timeOptions);


        ComboBox<String> userPicker = (ComboBox) loader.getNamespace().get("userPicker");
        userPicker.setItems(FXCollections.observableArrayList(userMap.keySet()));


        AppointmentController appointmentController = loader.getController();
        appointmentController.setMainSchedulerController(this);

        appointmentController.setUserMap(userMap);
        appointmentController.setAppointmentController(selectedAppointment);

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.showAndWait();


    }


    /**
     *
     * @param event method delete appointment will delete a selected appointment from the table view
     * @throws SQLException
     */

    public void deleteAppointmentBtn(ActionEvent event) throws SQLException {
        Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Appointment Selected");
            alert.setHeaderText("Please select an appointment to update.");
            alert.showAndWait();
            return;
        } else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Are you sure?");
            alert.setContentText("Do you want to proceed?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    if (selectedAppointment != null) {
                        try {
                            AppointmentDAO.deleteAppointment(selectedAppointment.getId());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        appointment.getAppointmentList().remove(selectedAppointment);
                        appointmentTableView.refresh();
                    }
                } else {
                    System.out.println("Cancelled");
                }
            });

        }
    }


    /**
     *
     * @param event delete customer method when invoked will delete the selected customer from the database and the table view
     * @throws SQLException
     */
    public void deleteCustomerBtn(ActionEvent event) throws SQLException {
        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Customer Selected");
            alert.setHeaderText("Please select a customer to delete.");
            alert.showAndWait();
            return;
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Are you sure?");
            alert.setContentText("Do you want to proceed?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    if (selectedCustomer != null) {

                        boolean hasAppointments = false;

                        for (Appointment appointment : appointment.getAppointmentList()){
                            if (appointment.getCustomerID() == selectedCustomer.getId()) {
                                hasAppointments = true;
                                break;
                            }
                        }

                        if (hasAppointments) {
                            Alert alert1 = new Alert(Alert.AlertType.WARNING);
                            alert1.setTitle("Cannot Delete Customer");
                            alert1.setHeaderText("The selected customer has associated appointments.");
                            alert1.setContentText("Please delete the associated appointments first.");
                            alert1.showAndWait();
                        } else {
                            try {
                                CustomerDAO.deleteCustomer(selectedCustomer.getId());
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            customer.getCustomerList().remove(selectedCustomer);
                            customerTableView.refresh();
                        }
                    }
                } else {
                    System.out.println("Cancelled");
                }
            });

        }
    }


    /**
     *
     * @param event update customer method when invoked will display a form with the selected customers information.
     * @throws IOException
     * @throws SQLException
     */

    public void updateCustomerBtn(ActionEvent event) throws IOException, SQLException {
        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Customer Selected");
            alert.setHeaderText("Please select a customer to update.");
            alert.showAndWait();
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/appointmentscheduler/CustomerForm.fxml"));
        Parent root = loader.load();

        TextField customerIDText = (TextField) loader.getNamespace().get("idTextField");
        customerIDText.setEditable(false);
        customerIDText.setPromptText("Auto Generated ID");

        ComboBox<String> stateComboField = (ComboBox<String>) loader.getNamespace().get("stateComboField");
        ComboBox<String> countryComboField = (ComboBox<String>) loader.getNamespace().get("countryCombo");

        String selectedState = selectedCustomer.getDivisionName();
        System.out.println("Selected State: " + selectedState);

        String selectedCountry = divisionDAO.fetchCountryByDivision(selectedCustomer.getDivisionName());
        System.out.println("Selected Country based on State: " + selectedCountry);

        // Fetch country names and populate the country combo box
        ObservableList<String> countryNames = FXCollections.observableArrayList(divisionDAO.fetchCountryNames());
        countryComboField.setItems(countryNames);

        countryComboField.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    // Fetch divisions based on the selected country
                    ObservableList<String> divisions = FXCollections.observableArrayList(divisionDAO.fetchDivisionsByCountry(newValue));
                    stateComboField.setItems(divisions);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        countryComboField.setValue(selectedCountry);

        stateComboField.setValue(selectedState);

        customerController = loader.getController();
        customerController.setMainSchedulerController(this);
        customerController.setCustomerController(selectedCustomer);

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.showAndWait();
    }



    /**
     * Filters the list of appointments to display only those occurring within the current week.
     *
     * This method retrieves the current date and time and calculates the start and end boundaries
     * of the current week using the DayOfWeek enum constants. It then filters the appointments list
     * to include only appointments that fall within this time range.
     *
     * The method uses lambda expressions to define the filtering criteria for appointments.
     */
    private void filterAppointmentsForCurrentWeek() {
        LocalDateTime startOfWeek = LocalDateTime.now().with(DayOfWeek.MONDAY).with(LocalTime.MIN);
        LocalDateTime endOfWeek = LocalDateTime.now().with(DayOfWeek.SUNDAY).with(LocalTime.MAX);

        ObservableList<Appointment> filteredAppointments = appointment.getAppointmentList().filtered(app -> {
            LocalDateTime startDateTime = app.getStartDateTime();
            LocalDateTime endDateTime = app.getEndDateTime();
            return (startDateTime.isAfter(startOfWeek) || startDateTime.isEqual(startOfWeek))
                    && (endDateTime.isBefore(endOfWeek) || endDateTime.isEqual(endOfWeek));
        });



        appointmentTableView.setItems(filteredAppointments);
    }




    private void filterAppointmentsForCurrentMonth() {
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).with(LocalTime.MIN);
        LocalDateTime endOfMonth = LocalDateTime.now().withDayOfMonth(LocalDateTime.now().toLocalDate().lengthOfMonth()).with(LocalTime.MAX);

        ObservableList<Appointment> filteredAppointments = appointment.getAppointmentList().filtered(app -> {
            LocalDateTime startDateTime = app.getStartDateTime();
            LocalDateTime endDateTime = app.getEndDateTime();
            return (startDateTime.isAfter(startOfMonth) || startDateTime.isEqual(startOfMonth))
                    && (endDateTime.isBefore(endOfMonth) || endDateTime.isEqual(endOfMonth));
        });

        appointmentTableView.setItems(filteredAppointments);
    }


    /**
     *
     * @param event current month method will filter through the appoinment list and only populate and display appointments in the current month.
     */


    public void currentMonthBtn(ActionEvent event) {
        filterAppointmentsForCurrentMonth();
    }

    /**
     *
     * @param event method will display all appointments in the appointments table.
     */
    public void currentAppointmentBtn(ActionEvent event) {
        appointmentTableView.setItems(appointment.getAppointmentList());
    }

    /**
     *
     * @param event report button method will display a report form with 3 different reports.
     * @throws IOException
     * @throws SQLException
     */

    public void reportBtn(ActionEvent event) throws IOException, SQLException {


        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/appointmentscheduler/reports.fxml"));
        Parent root = loader.load();

        TableColumn<String, TotalAppointment> appointmentType = (TableColumn<String, TotalAppointment>) loader.getNamespace().get("appointmentTypeColumn");
        appointmentType.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<TotalAppointment, LocalDateTime> appointmentMonth = (TableColumn<TotalAppointment, LocalDateTime>) loader.getNamespace().get("appointmentMonthColumn");
        appointmentMonth.setCellValueFactory(new PropertyValueFactory<>("month"));

        TableColumn<Integer, TotalAppointment> total = (TableColumn<Integer, TotalAppointment>) loader.getNamespace().get("totalColumn");
        total.setCellValueFactory(new PropertyValueFactory<>("total"));

        appointmentMonth.setCellFactory(column -> new TableCell<TotalAppointment, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime dateTime, boolean empty) {
                super.updateItem(dateTime, empty);

                if (empty || dateTime == null) {
                    setText(null);
                } else {
                    ZonedDateTime utcTime = dateTime.atZone(ZoneId.of("UTC"));
                    ZonedDateTime localTime = utcTime.withZoneSameInstant(ZoneId.systemDefault());
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a");
                    setText(formatter.format(localTime));
                }
            }
        });



        TableColumn<String, TotalStateCustomer> divisionColumn = (TableColumn<String, TotalStateCustomer>) loader.getNamespace().get("divisionColumn");
        divisionColumn.setCellValueFactory(new PropertyValueFactory<>("divisionName"));

        TableColumn<String, TotalStateCustomer> totalCustomerColumn = (TableColumn<String, TotalStateCustomer>) loader.getNamespace().get("totalCustomerColumn");
        totalCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("totalCustomers"));


        TableColumn<String, Appointment> titleColumn = (TableColumn<String, Appointment>) loader.getNamespace().get("title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<String, Appointment> typeColumn = (TableColumn<String, Appointment>) loader.getNamespace().get("type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<String, Appointment> descriptionColumn = (TableColumn<String, Appointment>) loader.getNamespace().get("description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<String, LocalDateTime> startDateColumn = (TableColumn<String, LocalDateTime>) loader.getNamespace().get("start");
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        startDateColumn.setCellFactory(column -> new TableCell<String, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime dateTime, boolean empty) {
                super.updateItem(dateTime, empty);

                if (empty || dateTime == null) {
                    setText(null);
                } else {
                    ZonedDateTime utcTime = dateTime.atZone(ZoneId.of("UTC"));
                    ZonedDateTime localTime = utcTime.withZoneSameInstant(ZoneId.systemDefault());
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a");
                    setText(formatter.format(localTime));
                }
            }
        });



        TableColumn<String, LocalDateTime> endDateColumn = (TableColumn<String, LocalDateTime>) loader.getNamespace().get("end");
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));
        endDateColumn.setCellFactory(column -> new TableCell<String, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime dateTime, boolean empty) {
                super.updateItem(dateTime, empty);

                if (empty || dateTime == null) {
                    setText(null);
                } else {
                    ZonedDateTime utcTime = dateTime.atZone(ZoneId.of("UTC"));
                    ZonedDateTime localTime = utcTime.withZoneSameInstant(ZoneId.systemDefault());
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a");
                    setText(formatter.format(localTime));
                }
            }
        });


        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        TableView<Appointment> cTableView = (TableView<Appointment>) loader.getNamespace().get("cTableView");
        TableView<TotalStateCustomer> divisionTableView = (TableView<TotalStateCustomer>) loader.getNamespace().get("divisionTableView");
        divisionTableView.setItems(divisionDAO.fetchDataFromDatabase());
        TableView<TotalAppointment> appointmentTypeTableView = (TableView<TotalAppointment>) loader.getNamespace().get("appointmentTypeTableView");
        appointmentTypeTableView.setItems(appointmentDAO.fetchTotalAppointmentFromDatabase());



        ComboBox<String> reportCombo = (ComboBox<String>) loader.getNamespace().get("reportCombo");

        try {
            HashMap<Integer, String> customerIdToNameMap = AppointmentDAO.getCustomerIdToNameMapping();

            // Set the items in the ComboBox to customer names
            reportCombo.setItems(FXCollections.observableArrayList(customerIdToNameMap.values()));

            reportCombo.setOnAction(event1 -> {
                String selectedCustomerName = reportCombo.getSelectionModel().getSelectedItem();
                if (selectedCustomerName != null) {
                    // Use the mapping to get the corresponding customer ID
                    Integer selectedCustomerId = getKeyByValue(customerIdToNameMap, selectedCustomerName);

                    if (selectedCustomerId != null) {
                        try {
                            ObservableList<Appointment> appointments = AppointmentDAO.fetchAppointmentsForCustomer(String.valueOf(selectedCustomerId));
                            cTableView.setItems(appointments);

                        } catch (SQLException e) {
                            e.printStackTrace(); // Handle the exception appropriately
                        }
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }


    }


    /**
     * method displays the view with appointment alerts if the appoinment is within 15 minutes
     */

    public void checkAndShowAppointmentAlerts() {
        ObservableList<Appointment> appointments = appointmentDAO.fetchAppointmentsFromDB();
        LocalDateTime currentDateTime = LocalDateTime.now();

        boolean hasUpcomingAppointment = false;

        for (Appointment appointment : appointments) {
            LocalDateTime appointmentDateTime = appointment.getStartDateTime();

            // Check if the appointment is within 15 minutes from the current datetime
            long minutesUntilAppointment = currentDateTime.until(appointmentDateTime, ChronoUnit.MINUTES);
            if (minutesUntilAppointment <= 15 && minutesUntilAppointment >= 0) {
                String alertMessage = "You have an appointment within 15 minutes:\n"
                        + "Appointment Type: " + appointment.getType() + "\n"
                        + "Appointment Start Time: " + appointmentDateTime;
                showAlert(Alert.AlertType.INFORMATION, "Upcoming Appointment", alertMessage);
                hasUpcomingAppointment = true;
            }
        }

        if (!hasUpcomingAppointment) {
            String noAppointmentMessage = "You have no appointments within the next 15 minutes.";
            showAlert(Alert.AlertType.INFORMATION, "No Upcoming Appointments", noAppointmentMessage);
        }
    }


    /**
     * method displays the view with an alert
     * @param alertType input for type of alert
     * @param title input for the alert box title
     * @param content input for setting the content
     */

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static <K, V> K getKeyByValue(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }







}
