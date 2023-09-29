module com.example.appointmentscheduler {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.appointmentscheduler to javafx.fxml;
    exports com.example.appointmentscheduler;
    exports com.example.appointmentscheduler.controller;
    opens com.example.appointmentscheduler.controller to javafx.fxml;
    opens com.example.appointmentscheduler.model to javafx.base;

}