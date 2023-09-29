package com.example.appointmentscheduler.controller;

import com.example.appointmentscheduler.Main;
import com.example.appointmentscheduler.dao.AppointmentDAO;
import com.example.appointmentscheduler.dao.JDBCConnection;
import com.example.appointmentscheduler.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController {


    @FXML
    TextField usernameTextField;

    @FXML
    PasswordField passwordTextField;



    @FXML
    Label loginLabel;

    @FXML
    Button loginBtn;

    @FXML
    Button cancelBtn;

    @FXML
    Label timeZoneOneLabel;



    User user = new User();


    /**
     *
     * @param event LoginBtn method when clicked will log a user in if the credentials provided matches the username and password in the database.
     */

    public void loginBtn(ActionEvent event){
        user.setUserName(usernameTextField.getText());
        user.setPassword(passwordTextField.getText());
        try {




            boolean loginSuccessful = JDBCConnection.verifyLogin(user.getUserName(), user.getPassword());

            recordLoginActivity(user.getUserName(), LocalDateTime.now(), loginSuccessful);

            if (loginSuccessful) {
                Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                mainStage.close();


                // Load the FXML file and get the root node
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("mainscheduler.fxml"));
                Parent root = loader.load();


                // Create the scene and set it to the stage
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setTitle("Appointment Scheduler");
                stage.setScene(scene);
                stage.show();
            } else {
                Locale userLocale = Locale.getDefault();
                if (userLocale.getLanguage().equals("en")) {
                    // English language error message
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Invalid username or password");
                    alert.setContentText("Please try again");
                    alert.showAndWait();
                } else if (userLocale.getLanguage().equals("fr")) {
                    // French language error message
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Nom d'utilisateur ou mot de passe invalide");
                    alert.setContentText("Veuillez réessayer");    alert.showAndWait();
                } else {
                    // Default language error message
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Invalid username or password");
                    alert.setContentText("Please try again");
                    alert.showAndWait();}

                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





        /**
         *
         * @param event cancel button will close the scene
         */
    public void cancelBtn(ActionEvent event) {
        Stage stage = (Stage) usernameTextField.getScene().getWindow();
        stage.close();

    }

    /**
     *
     * @param username username input
     * @param dateTime date and time input
     * @param loginSuccessful boolean value based on if login attempt was successful
     */

    private void recordLoginActivity(String username, LocalDateTime dateTime, boolean loginSuccessful) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("login_activity.txt", true))) {
            String status = loginSuccessful ? "Successful Attempt" : "Failed Attempt";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = dateTime.format(formatter);

            writer.write(timestamp + " - " + username + " - " + status);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
