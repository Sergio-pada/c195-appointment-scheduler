package com.example.c195.Controller;

import com.example.c195.Main;
import com.example.c195.Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.Month;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class Report1Controller implements Initializable {

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private TextField typeTextField;

    @FXML
    private ComboBox<String> monthComboBox;

    @FXML
    private TextField monthTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Populate the typeComboBox (same as before)
        Set<String> uniqueTypes = new HashSet<>();
        for (Appointment appointment : Main.appointments) {
            uniqueTypes.add(appointment.getType());
        }
        ObservableList<String> typeOptions = FXCollections.observableArrayList(uniqueTypes);
        typeComboBox.setItems(typeOptions);

        // Populate the monthComboBox with the names of all months
        ObservableList<String> monthOptions = FXCollections.observableArrayList();
        for (Month month : Month.values()) {
            monthOptions.add(month.toString());
        }
        monthComboBox.setItems(monthOptions);

        // Add a listener for the typeComboBox (similar to the previous listener)
        typeComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                long count = Main.appointments.stream().filter(appointment -> appointment.getType().equals(newValue)).count();
                typeTextField.setText(String.valueOf(count));
            } else {
                typeTextField.clear();
            }
        });

        // Add a listener for the monthComboBox
        monthComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Count the number of appointments in the selected month
                long count = Main.appointments.stream()
                        .filter(appointment -> appointment.getStart().getMonth().toString().equals(newValue))
                        .count();
                // Display the count in the monthTextField
                monthTextField.setText(String.valueOf(count));
            } else {
                monthTextField.clear();
            }
        });
    }

    @FXML
    private void mainMenu(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the appointment.fxml file
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 115, 150);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();
    }
}
