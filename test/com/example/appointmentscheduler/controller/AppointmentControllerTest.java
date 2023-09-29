package com.example.appointmentscheduler.controller;

import com.example.appointmentscheduler.dao.AppointmentDAO;
import com.example.appointmentscheduler.model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentControllerTest {

    @Test
    void isAppointmentOverlapping() {

        AppointmentController appointmentController = new AppointmentController();

        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        appointments.add(new Appointment(1, "Computer Repair", LocalDateTime.now(), LocalDateTime.now().plusHours(1)));
        appointments.add(new Appointment(2, "Battery Component Repair", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1)));

        LocalDateTime newStartDateTime1 = LocalDateTime.now().plusDays(2);
        LocalDateTime newEndDateTime1 = LocalDateTime.now().plusDays(2).plusHours(1);
        boolean result1 = appointmentController.isAppointmentOverlapping(newStartDateTime1, newEndDateTime1, appointments);
        assertFalse(result1);

        System.out.println("Test passed. Appointment does not overlap");

    }


    @Test
    void isWithinBusinessHours() {
        AppointmentController appointmentController = new AppointmentController();

        LocalDateTime dateTime = LocalDateTime.of(2023, 9, 25, 10, 0); // Monday, 10 AM

        boolean result = appointmentController.isWithinBusinessHours(dateTime);

        assertTrue(result, "Expected within business hours");
        System.out.println("Test passed. Appointment is within business hours");
    }
}
