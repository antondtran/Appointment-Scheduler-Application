package com.example.appointmentscheduler.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;

public class Customer {
    private int id;
    private String name;
    private String address;
    private String phoneNumber;
    private int divisionID;
    private String country;
    private String divisionName;
    private String postalCode;
    private LocalDateTime createdDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;

    private ObservableList<Customer> customerList = FXCollections.observableArrayList();

    public Customer(){}

    public Customer(String name, String phoneNumber, int divisionID, String postalCode) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.divisionID = divisionID;
        this.postalCode = postalCode;
    }

    public Customer(int id, String name, String address, String phoneNumber, int divisionID, String postalCode, LocalDateTime createdDate, String createdBy) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.divisionID = divisionID;
        this.postalCode = postalCode;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
    }

    public Customer(int id, String name, String address, String phoneNumber, int divisionID, String divisionName, String postalCode, LocalDateTime createdDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.divisionID = divisionID;
        this.divisionName = divisionName;
        this.postalCode = postalCode;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Customer(int id, String name, String address, String phoneNumber, int divisionID, String country, String divisionName, String postalCode, LocalDateTime createdDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.divisionID = divisionID;
        this.country = country;
        this.divisionName = divisionName;
        this.postalCode = postalCode;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Customer(int id, String name, String address, String phoneNumber, int divisionID, String postalCode) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.divisionID = divisionID;
        this.postalCode = postalCode;
    }

    /**
     *
     * @return method retrieves the created date
     */

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    /**
     *
     * @param createdDate method sets the created date with the parameter input
     */

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    /**
     *
     * @return method retrieves the created by field
     */

    public String getCreatedBy() {
        return createdBy;
    }

    /**
     *
     * @param createdBy method sets the createdBy with the parameter input
     */

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     *
     * @return method retrieves the last update date and time field.
     */

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    /**
     *
     * @param lastUpdate method sets the last update with the parameter input
     */

    public void setLastUpdate(LocalDateTime lastUpdate) {
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
     * @return method retrieves the division name
     */

    public String getDivisionName() {
        return divisionName;
    }

    /**
     *
     * @param divisionName method sets the division name using the parameter input
     */

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    /**
     *
     * @return method retrieves the id
     */

    public int getId() {
        return id;
    }

    /**
     *
     * @param id method sets the id using the parameter input
     */

    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return method retrieves the customer name
     */

    public String getName() {
        return name;
    }

    /**
     *
     * @param name method sets the customer name using the parameter input
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return method retrieves the customer address
     */

    public String getAddress() {
        return address;
    }

    /**
     *
     * @param address method sets the customer address using the parameter input
     */

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     *
     * @return method retrieves the customer phone number
     */

    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     *
     * @param phoneNumber method sets the customer phone number using the parameter input
     */

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     *
     * @return method retrieves the division id
     */

    public int getDivisionID() {
        return divisionID;
    }

    /**
     *
     * @param divisionID method sets the division id
     */

    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    /**
     *
     * @return method retrieves the customer postal code
     */

    public String getPostalCode() {
        return postalCode;
    }

    /**
     *
     * @param postalCode method sets the postal code using the parameter input
     */

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    /**
     *
     * @param customer method takes a customer object passed into the parameter and adds it to the customer list
     */
    public void addList(Customer customer){
        customerList.add(customer);
    }


    /**
     *
     * @return method retrieves the customer list
     */

    public ObservableList<Customer> getCustomerList() {
        return customerList;
    }

    /**
     *
     * @param updatedCustomer method takes in a customer object and and updates the customer list with the new updated customer object
     */
    public void updateList(Customer updatedCustomer) {
        int index = -1;
        for (Customer customer : customerList) {
            if (customer.getId() == updatedCustomer.getId()) {
                index = customerList.indexOf(customer);
                break;
            }
        }

        if (index != -1) {
            // Remove the old appointment from the list
            customerList.remove(index);

            // Add the updated appointment to the list
            customerList.add(index, updatedCustomer);
        }
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", divisionID=" + divisionID +
                ", country='" + country + '\'' +
                ", divisionName='" + divisionName + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", createdDate=" + createdDate +
                ", createdBy='" + createdBy + '\'' +
                ", lastUpdate=" + lastUpdate +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", customerList=" + customerList +
                '}';
    }
}
