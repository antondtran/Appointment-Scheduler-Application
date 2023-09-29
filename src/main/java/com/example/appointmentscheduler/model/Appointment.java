package com.example.appointmentscheduler.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;

public class Appointment {
    private int id;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalDateTime createdDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;
    private int userID;
    private int customerID;

    private final ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

    public Appointment(){}

    public Appointment(String title, String description, String location, String type, LocalDateTime startDateTime, LocalDateTime endDateTime, LocalDateTime createdDate, String createdBy, int userID, int customerID) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.userID = userID;
        this.customerID = customerID;
    }

    public Appointment(int id, String title, String description, String location, String type, LocalDateTime startDateTime, LocalDateTime endDateTime, LocalDateTime createdDate, String createdBy, int userID, int customerID) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.userID = userID;
        this.customerID = customerID;
    }

    public Appointment(int id, String title, String type, String description, String location, LocalDateTime startDateTime, LocalDateTime endDateTime, LocalDateTime createdDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy, int userID, int customerID) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.description = description;
        this.location = location;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.userID = userID;
        this.customerID = customerID;
    }

    public Appointment(int id, String title, String type, String description, LocalDateTime startDateTime, LocalDateTime endDateTime, int customerID) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.customerID = customerID;
    }

    public Appointment(int i, String s, LocalDateTime now, LocalDateTime plusHours) {
        this.id = i;
        this.title = s;
        this.startDateTime = now;
        this.endDateTime = plusHours;
    }

    /**
     *
     * @return retrieves appointment id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id method sets the id with the provided id input
     */

    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return retrieves appointment title
     */

    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title method takes in a string input and sets it as the appointment title
     */

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return retrieves appointment type
     */

    public String getType() {
        return type;
    }

    /**
     *
     * @param type method sets the appointment type with the string input
     */

    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return method retrieves the appointment description
     */

    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description method takes in a description string and sets it to the description field.
     */

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return retrieves the appointment location
     */

    public String getLocation() {
        return location;
    }


    /**
     *
     * @param location method takes in a location string input and sets it to the location field
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     *
     * @return method retrieves the appointment start date and time
     */

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    /**
     *
     * @param startDateTime method takes in a start date and time and sets it to the startDateTime field
     */
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     *
     * @return retrieves the appointment end date and time
     */

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    /**
     *
     * @param endDateTime parameter input used to set the end date and time of the appointment
     */

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }


    /**
     *
     * @return retrieves the appointment user id
     */

    public int getUserID() {
        return userID;
    }

    /**
     *
     * @param userID parameter used to set the user id field
     */

    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     *
     * @return retrieves the customer id
     */

    public int getCustomerID() {
        return customerID;
    }

    /**
     *
     * @param customerID paremeter input used to set the customer id field
     */

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     *
     * @return retrieves the created date
     */


    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    /**
     *
     * @param createdDate parameter input used to set the created date
     */

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    /**
     *
     * @return retrieves the createdby String text
     */

    public String getCreatedBy() {
        return createdBy;
    }

    /**
     *
     * @param createdBy parameter input used to set the createdby field
     */

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     *
     * @return retrieves the last update date and time
     */

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    /**
     *
     * @param lastUpdate parameter input used to set the last update
     */

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     *
     * @return retrieves the last updated by field
     */

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     *
     * @param lastUpdatedBy parameter input used to set the lastupdatedBy
     */

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     *
     * @param appointment parameter input used to add an appointment object to the appointment list
     */

    public void addList(Appointment appointment ){
        appointmentList.add(appointment);
    }

    /**
     *
     * @param updatedAppointment parameter input used to update the appointment object
     */

    public void updateList(Appointment updatedAppointment) {
        int index = -1;
        for (Appointment appointment : appointmentList) {
            if (appointment.getId() == updatedAppointment.getId()) {
                index = appointmentList.indexOf(appointment);
                break;
            }
        }

        if (index != -1) {

            appointmentList.remove(index);

            appointmentList.add(index, updatedAppointment);
        }
    }


    /**
     *
     * @return retrieves the list of appointments
     */
    public ObservableList<Appointment> getAppointmentList() {
        return appointmentList;
    }
}
