package com.example.c195.Controller;

import com.example.c195.Helper.AppointmentQuery;
import com.example.c195.Main;
import com.example.c195.Model.Appointment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.example.c195.Main.appointments;

public class AppointmentController implements Initializable {
    @FXML
    private TableView<Appointment> appointmentsTableView;

    @FXML
    private TableColumn<Appointment, Integer> appointmentIdColumn;

    @FXML
    private TableColumn<Appointment, String> titleColumn;

    @FXML
    private TableColumn<Appointment, String> descriptionColumn;

    @FXML
    private TableColumn<Appointment, String> locationColumn;

    @FXML
    private TableColumn<Appointment, String> typeColumn;

    @FXML
    private TableColumn<Appointment, LocalDateTime> startColumn;

    @FXML
    private TableColumn<Appointment, LocalDateTime> endColumn;

    @FXML
    private TableColumn<Appointment, Integer> customerIdColumn;

    @FXML
    private TableColumn<Appointment, Integer> userIdColumn;

    @FXML
    private TableColumn<Appointment, Integer> contactIdColumn;
    @FXML
    public static boolean newAppointment = false;




    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Initialize the TableView columns
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        contactIdColumn.setCellValueFactory(new PropertyValueFactory<>("contactId"));

        // Load and display all appointments initially
        showAllAppointments();
    }

    private int calculateNewAppointmentId() {
        int maxAppointmentId = -1; // Initialize with a value smaller than any possible appointmentId

        for (Appointment appointment : appointments) {
            int appointmentId = appointment.getAppointmentId();
            if (appointmentId > maxAppointmentId) {
                maxAppointmentId = appointmentId; // Update the maximum if a larger value is found
            }
        }

        // Return the new appointmentId as maxAppointmentId + 1
        return maxAppointmentId + 1;
    }

    @FXML
    private void showAllAppointments() {
        appointmentsTableView.getItems().clear(); // Clear existing items
        appointmentsTableView.getItems().addAll(appointments); // Load all appointments
    }


    @FXML
    private void showWeekAppointments() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextWeek = now.plusDays(7);

        appointmentsTableView.getItems().clear(); // Clear existing items

        for (Appointment appointment : appointments) {
            LocalDateTime appointmentStart = appointment.getStart();
            if (appointmentStart.isAfter(now) && appointmentStart.isBefore(nextWeek)) {
                appointmentsTableView.getItems().add(appointment);
            }
        }
    }


    @FXML
    private void showMonthAppointments() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextMonth = now.plusDays(30);

        appointmentsTableView.getItems().clear(); // Clear existing items

        for (Appointment appointment : appointments) {
            LocalDateTime appointmentStart = appointment.getStart();
            if (appointmentStart.isAfter(now) && appointmentStart.isBefore(nextMonth)) {
                appointmentsTableView.getItems().add(appointment);
            }
        }
    }


    @FXML
    private void newAppointment(ActionEvent event) throws IOException {
        newAppointment = true;
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("edit-appointment.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Appointment Editor");
        stage.setScene(scene);
        stage.show();

        // Calculate the new appointmentId
        int newAppointmentId = calculateNewAppointmentId();

        // Pass the new appointmentId to the EditAppointmentController
        EditAppointmentController editAppointmentController = fxmlLoader.getController();
        editAppointmentController.setAppointmentId(newAppointmentId);
    }

    @FXML
    private void editAppointment(ActionEvent event) throws IOException {
        Appointment selectedAppointment = appointmentsTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Load the appointment.fxml file
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("edit-appointment.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Appointment Editor");
            stage.setScene(scene);
            stage.show();

            EditAppointmentController editAppointmentController = fxmlLoader.getController();
            editAppointmentController.setAppointmentData(selectedAppointment);

            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    private void deleteAppointment(ActionEvent event) {
        Appointment selectedAppointment = appointmentsTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            int appointmentID = selectedAppointment.getAppointmentId();

            // Create a confirmation dialog for deleting the appointment
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText("Are you sure you would like to delete this appointment?");
            confirmationAlert.setContentText( "Appointment ID: " + selectedAppointment.getAppointmentId() + "\nAppointment Type: " + selectedAppointment.getType());

            Optional<ButtonType> result = confirmationAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                // User confirmed the deletion
                AppointmentQuery.deleteAppointment(appointmentID);
                appointments.remove(selectedAppointment);

                // Refresh the TableView data
                showAllAppointments();

                // Create a success alert for the deletion
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Appointment Removed");
                successAlert.setHeaderText(null);
                successAlert.setContentText("The appointment has been removed.");
                successAlert.showAndWait();
            }
        }
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
