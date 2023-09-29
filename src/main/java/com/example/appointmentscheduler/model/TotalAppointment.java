package com.example.appointmentscheduler.model;

import java.time.LocalDateTime;

public class TotalAppointment {
    private int total;
    private String type;
    private LocalDateTime month;

    public TotalAppointment() {
    }

    public TotalAppointment(int total, String type, LocalDateTime month) {
        this.total = total;
        this.type = type;
        this.month = month;
    }

    /**
     *
     * @return method retrieves the total
     */

    public int getTotal() {
        return total;
    }

    /**
     *
     * @param total method takes in a parameteer value and sets it to the total
     */

    public void setTotal(int total) {
        this.total = total;
    }

    /**
     *
     * @return method retrieves the type
     */

    public String getType() {
        return type;
    }

    /**
     *
     * @param type method sets the type with the parameter input
     */

    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return method retrieves the month
     */

    public LocalDateTime getMonth() {
        return month;
    }

    /**
     *
     * @param month method takes a parameter input and sets the month field
     */

    public void setMonth(LocalDateTime month) {
        this.month = month;
    }


}
