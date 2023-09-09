package com.example.c195.Controller;

import com.example.c195.Helper.AppointmentQuery;
import com.example.c195.Helper.UserQuery;
import com.example.c195.Main;
import com.example.c195.Model.Appointment;
import com.example.c195.Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class LoginController {
    int userId;
    @FXML
    private Label timeZoneLabel;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private UserQuery userQuery;
    @FXML
    private Button loginButton;

    public void initialize() {
        // Set the current time zone in the label
        timeZoneLabel.setText("Time Zone: " + TimeZone.getDefault().getID());
        // Get the default locale
        Locale locale = Locale.getDefault();

        // Get the language code
        String languageCode = locale.getLanguage();

        // Check if the language code is "fr"
        if (languageCode.equals("fr")) {
            timeZoneLabel.setText("Fuseau Horaire: " + TimeZone.getDefault().getID());
            usernameField.setPromptText("Nom d'utilisateur");
            passwordField.setPromptText("Mot de passe");
            loginButton.setText("Se connecter");
        } else {
            System.out.println("The default language is not French");
        }



    }

    @FXML
    private void loginButtonClicked(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Validate username and password
        boolean valid = validateCredentials(username, password);
        Locale locale = Locale.getDefault();
        String languageCode = locale.getLanguage();

        if (valid) {
            // Check for upcoming appointments within the next 15 minutes
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime fifteenMinutesLater = now.plus(15, ChronoUnit.MINUTES);

            for (Appointment appointment : Main.appointments) {
                if (userId == appointment.getUserId() &&
                        appointment.getStart().isAfter(now) &&
                        appointment.getStart().isBefore(fifteenMinutesLater)) {

                    // Display an alert for the upcoming appointment
                    String alertTitle = languageCode.equals("fr") ? "Rendez-vous imminent" : "Upcoming Appointment";
                    String alertContent = languageCode.equals("fr") ?
                            "Vous avez un rendez-vous dans les 15 prochaines minutes." :
                            "You have an appointment in the next 15 minutes.";

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle(alertTitle);
                    alert.setContentText(alertContent);
                    alert.showAndWait();

                    break; // No need to continue checking for appointments
                }
            }

            System.out.println("Login successful!");
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Load the appointment.fxml file
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-menu.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Main Menu");
            stage.setScene(scene);
            stage.show();
        } else {
            if (languageCode.equals("fr")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur d'identification");
                alert.setContentText("Combinaison de nom d'utilisateur et de mot de passe invalide.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Error");
                alert.setContentText("Invalid username/password combination.");
                alert.showAndWait();
            }
        }
    }

    public boolean validateCredentials(String username, String password) {
        for (User user : userQuery.getAllUsers()) {
            if (user.getUserName().equals(username) && user.getPassword().equals(password)) {
                userId = user.getUserId();
                return true; // Valid credentials
            }
        }
        return false; // Invalid credentials
    }

}
