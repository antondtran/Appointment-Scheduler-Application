package com.example.appointmentscheduler.controller;

import com.example.appointmentscheduler.dao.AppointmentDAO;
import com.example.appointmentscheduler.dao.CustomerDAO;
import com.example.appointmentscheduler.model.Appointment;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

import java.time.*;
import java.util.List;
import java.util.Map;



public class AppointmentController {

    @FXML
    TextField idTextField;

    @FXML
    TextField titleTextField;

    @FXML
    TextField typeTextField;

    @FXML
    TextField descriptionTextField;

    @FXML
    TextField locationTextField;

    @FXML
    DatePicker startDatePicker;

    @FXML
    DatePicker endDatePicker;

    @FXML
    ComboBox<LocalTime> startTimePicker;

    @FXML
    ComboBox<LocalTime> endTimePicker;

    @FXML
    ComboBox<String> contactPicker;

    @FXML
    ComboBox<String> userPicker;

    @FXML
    TextField customerTextField;

    private static int assignedAppointmentID;

    private MainSchedulerController mainSchedulerController;
    private Map<String, Integer> contactMap;
    private Map<String, Integer> userMap;
    private CustomerDAO customerDAO;
    private AppointmentDAO appointmentDAO;


    public AppointmentController(){
        appointmentDAO = new AppointmentDAO();
    }

    /**
     *
     * @param existingAppointment this method takes in an existing appointment object and uses it to populate the form with details
     */

    public void setAppointmentController(Appointment existingAppointment) {

        idTextField.setText(String.valueOf(existingAppointment.getId()));
        titleTextField.setText(existingAppointment.getTitle());
        typeTextField.setText(existingAppointment.getType());
        descriptionTextField.setText(existingAppointment.getDescription());
        locationTextField.setText(existingAppointment.getLocation());


        ZoneId localZone = ZoneId.systemDefault();

        LocalDateTime localStartDateTime = existingAppointment.getStartDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(localZone).toLocalDateTime();
        LocalDateTime localEndDateTime = existingAppointment.getEndDateTime().atZone(ZoneId.of("UTC")).withZoneSameInstant(localZone).toLocalDateTime();

        startTimePicker.setValue(localStartDateTime.toLocalTime());
        endTimePicker.setValue(localEndDateTime.toLocalTime());
        startDatePicker.setValue(localStartDateTime.toLocalDate());
        endDatePicker.setValue(localEndDateTime.toLocalDate());

        customerTextField.setText(String.valueOf(Integer.valueOf(existingAppointment.getCustomerID())));


        int userID = existingAppointment.getUserID();
        String selectedUserName = null;
        for (Map.Entry<String, Integer> entry : userMap.entrySet()) {
            if (entry.getValue() == userID) {
                selectedUserName = entry.getKey();
                break;
            }
        }

        userPicker.setValue(selectedUserName);



    }


    /**
     *
     * @param event save button is called when a user clicks on the save button. This method saves a new appointment to the database and if its an exisiting object then it gets updated.
     */
    public void saveBtn(ActionEvent event) {



        LocalDateTime currentDateTime = LocalDateTime.now();


        // Get the form data from the input fields
        int id;
        try {
            id = Integer.parseInt(idTextField.getText());
        } catch (NumberFormatException e) {
            id = 0;
        }
        String title = titleTextField.getText();
        String type = typeTextField.getText();
        String description = descriptionTextField.getText();
        String location = locationTextField.getText();
        String selectedUserName = String.valueOf(userPicker.getValue());
        int userID = userMap.get(selectedUserName);
        int customerID = Integer.parseInt(customerTextField.getText());


        String startTimeString = startTimePicker.getSelectionModel().getSelectedItem().toString();
        String endTimeString = endTimePicker.getSelectionModel().getSelectedItem().toString();

        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        LocalTime startTime = LocalTime.parse(startTimeString);
        LocalTime endTime = LocalTime.parse(endTimeString);

        // Combine the selected date (startDate) with the selected time (startTime) to create LocalDateTime
        LocalDateTime combinedStartDateTime = startDate.atTime(startTime);
        LocalDateTime combinedEndDateTime = endDate.atTime(endTime);

        ZoneId localZone = ZoneId.systemDefault();
        ZoneId utcZone = ZoneId.of("UTC");

        ZonedDateTime combinedStartDateTimeUTC = combinedStartDateTime.atZone(localZone).withZoneSameInstant(utcZone);
        ZonedDateTime combinedEndDateTimeUTC = combinedEndDateTime.atZone(localZone).withZoneSameInstant(utcZone);

        LocalDateTime createDate = LocalDateTime.now();
        LocalDateTime lastUpdate = LocalDateTime.now();
        String createdBy = "script";
        String lastUpdatedBy = "script";

        if (!isWithinBusinessHours(combinedStartDateTimeUTC.toLocalDateTime()) || !isWithinBusinessHours(combinedEndDateTimeUTC.toLocalDateTime())) {
            // Show an error message if the appointment time is outside business hours or on weekends
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Appointment Time");
            alert.setContentText("Appointments must be scheduled between 8:00 a.m. and 10:00 p.m on weekdays.");
            alert.showAndWait();
            return;
        }

        ObservableList<Appointment> existingAppointments = appointmentDAO.fetchAppointmentsFromDB();

        if (isAppointmentOverlapping(combinedStartDateTimeUTC.toLocalDateTime(), combinedEndDateTimeUTC.toLocalDateTime(), existingAppointments)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Overlap Detected");
            alert.setContentText("The appointment time overlaps with an existing appointment.");
            alert.showAndWait();
            return;
        }




        Appointment existingAppointment = AppointmentDAO.getAppointmentById(id);


        if (existingAppointment != null) {
            // This is an existing appointment being updated
            existingAppointment.setTitle(title);
            existingAppointment.setType(type);
            existingAppointment.setDescription(description);
            existingAppointment.setLocation(location);
            existingAppointment.setStartDateTime(combinedStartDateTimeUTC.toLocalDateTime());
            existingAppointment.setEndDateTime(combinedEndDateTimeUTC.toLocalDateTime());
            existingAppointment.setCreatedBy(createdBy);
            existingAppointment.setLastUpdate(lastUpdate);
            existingAppointment.setLastUpdatedBy(lastUpdatedBy);
            existingAppointment.setUserID(userID);
            existingAppointment.setCustomerID(customerID);

            try {
                // Use the AppointmentDAO to update the appointment in the database

                if (CustomerDAO.doesCustomerExist(existingAppointment.getCustomerID())){

                    if (existingAppointment.getStartDateTime().toLocalDate().isBefore(existingAppointment.getEndDateTime().toLocalDate()) || existingAppointment.getStartDateTime().toLocalDate().isEqual(existingAppointment.getEndDateTime().toLocalDate()) && existingAppointment.getStartDateTime().toLocalTime().isBefore(existingAppointment.getEndDateTime().toLocalTime())){

                            AppointmentDAO.updateAppointment(existingAppointment);

                            // Refresh the main controller's appointment list and table view
                            mainSchedulerController.getAppointment().updateList(existingAppointment);
                            mainSchedulerController.refreshAppointmentTable();

                            // Close the stage
                            Stage stage = (Stage) idTextField.getScene().getWindow();
                            stage.close();


                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Invalid Date or Time.");
                        alert.setContentText("Please ensure the appointment date and time is before the end date and time");
                        alert.showAndWait();
                    }

                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Invalid Customer ID");
                    alert.showAndWait();

                }


            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // This is a new appointment being added
            int newAppointmentId = id == 0 ? AppointmentDAO.getLastUsedAppointmentId() + 1 : id;
            Appointment newAppointment = new Appointment(newAppointmentId, title, type, description, location, combinedStartDateTimeUTC.toLocalDateTime(), combinedEndDateTimeUTC.toLocalDateTime(), createDate, createdBy, userID, customerID);

            try {

                if (CustomerDAO.doesCustomerExist(newAppointment.getCustomerID())){

                    if (newAppointment.getStartDateTime().toLocalDate().isBefore(newAppointment.getEndDateTime().toLocalDate()) || newAppointment.getStartDateTime().toLocalDate().isEqual(newAppointment.getEndDateTime().toLocalDate()) && newAppointment.getStartDateTime().toLocalTime().isBefore(newAppointment.getEndDateTime().toLocalTime())){
                        // Use the AppointmentDAO to insert the appointment into the database
                        AppointmentDAO.insertAppointment(newAppointment);


                        // Update the main controller's appointment list
                        mainSchedulerController.getAppointment().addList(newAppointment);
                        mainSchedulerController.refreshAppointmentTable();

                        // Close the stage
                        Stage stage = (Stage) idTextField.getScene().getWindow();
                        stage.close();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Invalid Date or Time.");
                        alert.setContentText("Please ensure the appointment date and time is before the end date and time");
                        alert.showAndWait();
                    }

                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Invalid Customer ID");
                    alert.showAndWait();

                }


            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     *
     * @param mainSchedulerController method is used to pass an instance of the main scheduler controller for another class to be able to access its components.
     */

    public void setMainSchedulerController(MainSchedulerController mainSchedulerController){
        this.mainSchedulerController = mainSchedulerController;
    }

    /**
     *
     * @param event cancel Button method is used to close the scene
     */

    public void cancelBtn(ActionEvent event) {
        Stage stage = (Stage) idTextField.getScene().getWindow();
        stage.close();

    }


    /**
     *
     * @return generate Appointment ID method is used to auto generate an appointment ID. The method returns the auto generated ID.
     */

    public static int generateAppointmentID(){

        return ++assignedAppointmentID;
    }


    /**
     *
     * @param contactMap method maps contact key value pairs.
     */

    public void setContactMap(Map<String, Integer> contactMap) {
        this.contactMap = contactMap;
    }

    /**
     *
     * @param userMap method maps user key value pairs.
     */

    public void setUserMap(Map<String, Integer> userMap) {
        this.userMap = userMap;
    }

    /**
     *
     * @param dateTime method takes in a dateTime parameter and then checks to see if the given date time is within the business hour.
     * @return method returns a boolean value based on if the given date time is within business hour
     */

    public boolean isWithinBusinessHours(LocalDateTime dateTime) {
        LocalTime startTime = dateTime.toLocalTime();
        int startHour = startTime.getHour();
        DayOfWeek dayOfWeek = dateTime.getDayOfWeek();

        return (startHour >= 8 && startHour <= 22) &&
                (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY);
    }

    /**
     *
     * @param newStartDateTime new user appointment start date and time
     * @param newEndDateTime new user appointment end date and time
     * @param existingAppointments list of existing appointments
     * @return
     */
    public boolean isAppointmentOverlapping(LocalDateTime newStartDateTime, LocalDateTime newEndDateTime,
                                            ObservableList<Appointment> existingAppointments) {
        for (Appointment existingAppointment : existingAppointments) {
            LocalDateTime existingStartDateTime = existingAppointment.getStartDateTime();
            LocalDateTime existingEndDateTime = existingAppointment.getEndDateTime();

            // Check for overlap by comparing start and end times
            if ((newStartDateTime.isAfter(existingStartDateTime) && newStartDateTime.isBefore(existingEndDateTime)) ||
                    (newEndDateTime.isAfter(existingStartDateTime) && newEndDateTime.isBefore(existingEndDateTime)) || newStartDateTime.isEqual(existingStartDateTime) ||
                    (newStartDateTime.isBefore(existingStartDateTime) && newEndDateTime.isAfter(existingEndDateTime)) || newEndDateTime.isEqual(existingEndDateTime)) {
                return true;

            }
        }
        return false;
    }




}
