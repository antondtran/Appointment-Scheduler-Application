package com.example.appointmentscheduler.model;

import javafx.scene.control.PasswordField;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class User {
    private int userID;
    private String userName;
    private String password;
    private LocalDateTime createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;

    public User(){};

    public User(int userID, String userName, String password, LocalDateTime createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }


    /**
     *
     * @return method retrieves the user id
     */
    public int getUserID() {
        return userID;
    }


    /**
     *
     * @param userID method sets the user id with the paremeter input
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     *
     * @return method retrieves the username
     */

    public String getUserName() {
        return userName;
    }

    /**
     *
     * @param userName method takes in a parameter input and sets it to the username field
     */

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     *
     * @return method retrieves the password field
     */

    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password method sets the password field using the parameter input value
     */

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @return method retrieves the create date field
     */

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    /**
     *
     * @param createDate method sets the create date field using the parameter input value
     */

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }


    /**
     *
     * @return method retrives the createdBy field
     */

    public String getCreatedBy() {
        return createdBy;
    }

    /**
     *
     * @param createdBy method sets the created by field with the parameter input value
     */

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     *
     * @return method retrieves the lastUpdate field
     */

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    /**
     *
     * @param lastUpdate method sets the lastUpdate field using the parameter input
     */

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     *
     * @return method retrieves the lastUpdatedBy field
     */

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     *
     * @param lastUpdatedBy method sets the lastUpdatedBy field using the parameter input value
     */

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
}
