package com.example.appointmentscheduler;

import com.example.appointmentscheduler.controller.LoginController;
import com.example.appointmentscheduler.controller.MainSchedulerController;
import com.example.appointmentscheduler.dao.AppointmentDAO;
import com.example.appointmentscheduler.dao.JDBCConnection;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Java Doc is located in a folder in src folder. Located above the main folder
 */
public class Main extends Application {


    /**
     *
     * @param stage when method is invoked it will displlay the login form
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {




        ZoneId userTimeZone = ZoneId.systemDefault();




        FXMLLoader loader = new FXMLLoader(Main.class.getResource("loginform.fxml"));
        Parent root = loader.load();

        Label loginLabel = (Label) loader.getNamespace().get("loginLabel");
        Button loginBtn = (Button) loader.getNamespace().get("loginBtn");
        Button cancelBtn = (Button) loader.getNamespace().get("cancelBtn");
        Label timeZoneOneLabel = (Label) loader.getNamespace().get("timeZoneOneLabel");
        TextField usernameTextField = (TextField) loader.getNamespace().get("usernameTextField");
        PasswordField passwordTextField = (PasswordField) loader.getNamespace().get("passwordTextField");


        Locale userLocale = Locale.getDefault();    // Load the appropriate resource bundle based on the user's locale
        ResourceBundle bundle = ResourceBundle.getBundle("com.example.appointmentscheduler.messages", userLocale);
        // Update the UI components with the translated text
        loginLabel.setText(bundle.getString("login.title"));
        loginBtn.setText(bundle.getString("login.loginBtn"));
        cancelBtn.setText(bundle.getString("login.cancelBtn"));
        timeZoneOneLabel.setText(bundle.getString("login.timeZoneOneLabel"));
        usernameTextField.setPromptText(bundle.getString("login.username"));
        passwordTextField.setPromptText(bundle.getString("login.password"));



        Label timeZoneLabel = (Label) loader.getNamespace().get("timeZoneLabel");
        timeZoneLabel.setText(userTimeZone.getId());

        Scene scene = new Scene(root);
        stage.setTitle("Appointment Scheduler");
        stage.setScene(scene);
        stage.show();
    }




    public static void main(String[] args) {
        launch();
    }


}