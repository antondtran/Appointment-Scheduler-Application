package com.example.appointmentscheduler.model;

public class TotalStateCustomer {
    private String divisionName;
    private int totalCustomers;

    public TotalStateCustomer(String divisionName, int totalCustomers){
        this.divisionName = divisionName;
        this.totalCustomers = totalCustomers;

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
     * @param divisionName method sets the division name with the parameter input
     */

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }


    /**
     *
     * @return method retrieves total customers
     */
    public int getTotalCustomers() {
        return totalCustomers;
    }

    /**
     *
     * @param totalCustomers method takes in a parameter input and sets it to the totalCustomers field
     */

    public void setTotalCustomers(int totalCustomers) {
        this.totalCustomers = totalCustomers;
    }
}
