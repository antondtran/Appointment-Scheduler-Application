package com.example.appointmentscheduler.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class FirstLevelDivision {
    private int divisionID;
    private String division;
    private LocalDateTime createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;
    private int countryID;


    /**
     *
     * @return method retrieves division id
     */

    public int getDivisionID() {
        return divisionID;
    }

    /**
     *
     * @param divisionID method sets the division id with the parameter input
     */

    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    /**
     *
     * @return method retrieves the division
     */

    public String getDivision() {
        return division;
    }


    /**
     *
     * @param division method sets the division with the parameter input
     */
    public void setDivision(String division) {
        this.division = division;
    }

    /**
     *
     * @return method retrieves the create date
     */

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    /**
     *
     * @param createDate method sets the create date with the parameter input
     */

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }


    /**
     *
     * @return method retrieves the created by
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     *
     * @param createdBy method takes in a parameter input and sets the createdBy field
     */

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     *
     * @return Method retrieves the last update time
     */

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    /**
     *
     * @param lastUpdate method takes a input parameter value and sets the lastUpdate field
     */

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     *
     * @return method retrieves the last updated by field
     */

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     *
     * @param lastUpdatedBy method sets the last updated by field with the parameter input
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     *
     * @return method retrieves the country id field
     */

    public int getCountryID() {
        return countryID;
    }

    /**
     *
     * @param countryID method sets the country id value
     */

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }
}
