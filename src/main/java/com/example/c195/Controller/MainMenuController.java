package com.example.c195.Controller;

import com.example.c195.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {
    @FXML
    private void openAppointments(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the appointment.fxml file
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("appointment.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 710, 500);
        stage.setTitle("Appointments");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void openCustomers(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the appointment.fxml file
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("customer.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Customers");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void openReports(ActionEvent event) {
    }
}
