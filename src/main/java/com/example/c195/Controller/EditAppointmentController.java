package com.example.c195.Controller;

import com.example.c195.Helper.AppointmentQuery;
import com.example.c195.Main;
import com.example.c195.Model.Appointment;
import com.example.c195.Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class EditAppointmentController implements Initializable {
    @FXML
    private TextField appointmentIdField;
    @FXML
    private TextField titleField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField locationField;
    @FXML
    private TextField typeField;
    @FXML
    private TextField startDateField;
    @FXML
    private TextField endDateField;
    @FXML
    private TextField customerIdField;
    @FXML
    private TextField userIdField;
    @FXML
    private TextField contactIdField;
    @FXML
    private ComboBox<String> customerComboBox;
    @FXML
    private String customerIdToName(int customerId){
        for (Customer customer : Main.customers) {
            if (customer.getCustomerId() == customerId) {
                return customer.getCustomerName();
            }
        }
        return null;
    }
    @FXML
    public void setAppointmentData(Appointment appointment) {
        // Set the text fields with appointment data
        appointmentIdField.setText(String.valueOf(appointment.getAppointmentId()));
        titleField.setText(appointment.getTitle());
        descriptionField.setText(appointment.getDescription());
        locationField.setText(appointment.getLocation());
        typeField.setText(appointment.getType());
        startDateField.setText(appointment.getStart().toString());
        endDateField.setText(appointment.getEnd().toString());
        userIdField.setText(String.valueOf(appointment.getUserId()));
        contactIdField.setText(String.valueOf(appointment.getContactId()));
        customerComboBox.setValue(customerIdToName(appointment.getCustomerId()));
    }
    public void setAppointmentId(int appointmentId) {
        appointmentIdField.setText(String.valueOf(appointmentId));
    }
    @FXML
    private void cancelAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void saveAction(ActionEvent event) throws IOException {
        // Retrieve the modified appointment data from the text fields
        String appointmentIdText = appointmentIdField.getText();
        String title = titleField.getText();
        String description = descriptionField.getText();
        String location = locationField.getText();
        String type = typeField.getText();
        LocalDateTime start = LocalDateTime.parse(startDateField.getText());
        LocalDateTime end = LocalDateTime.parse(endDateField.getText());
        int userId = Integer.parseInt(userIdField.getText());
        int contactId = Integer.parseInt(contactIdField.getText());
        String selectedCustomer = customerComboBox.getSelectionModel().getSelectedItem();

        // Create a mapping between customer names and customer IDs
        Map<String, Integer> customerIdMapping = new HashMap<>();
        for (Customer customer : Main.customers) {
            customerIdMapping.put(customer.getCustomerName(), customer.getCustomerId());
        }

        // Look up the customer ID based on the selected customer name
        int customerId = customerIdMapping.get(selectedCustomer);

        // Check if the appointment falls outside of business hours (3:00-17:00 UTC)
        if (!isBusinessHours(start)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Appointment Time Error");
            alert.setContentText("Appointments must be scheduled during business hours (3:00-17:00 UTC).");
            alert.showAndWait();
            return; // Do not save the appointment
        }
        // Check for conflicts with existing appointments
        if (hasAppointmentConflicts(customerId, start, end, Integer.parseInt(appointmentIdText))) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Appointment Conflict");
            alert.setContentText("An appointment with the same customer conflicts with this time slot.");
            alert.showAndWait();
            return; // Do not save the appointment
        }


        // Check if the appointment ID field is empty
        if (AppointmentController.newAppointment) {
            // Creating a new appointment
            int appointmentId = Integer.parseInt(appointmentIdText);
            Appointment newAppointment = new Appointment(appointmentId, title, description, location, type, start, end, customerId, userId, contactId);

            // Call the createAppointment method to insert the new appointment
            AppointmentQuery.createAppointment(newAppointment);
        } else {
            // Editing an existing appointment
            int appointmentId = Integer.parseInt(appointmentIdText);

            // Find the existing appointment by ID in the list of appointments
            Appointment existingAppointment = null;
            for (Appointment appointment : Main.appointments) {
                if (appointment.getAppointmentId() == appointmentId) {
                    existingAppointment = appointment;
                    break;
                }
            }

            if (existingAppointment != null) {
                // Remove the existing appointment
                Main.appointments.remove(existingAppointment);
            }

            // Create a new Appointment object with the updated data
            Appointment updatedAppointment = new Appointment(appointmentId, title, description, location, type, start, end, customerId, userId, contactId);

            // Add the updated appointment to the list
            Main.appointments.add(updatedAppointment);

            // Call the updateAppointment method to update the database record
            AppointmentQuery.updateAppointment(updatedAppointment);
        }
        AppointmentController.newAppointment = false;
        Main.appointments = AppointmentQuery.getAllAppointments();
        // Close the current window and return to the main menu
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();
    }
    public boolean hasAppointmentConflicts(int customerId, LocalDateTime start, LocalDateTime end, int appointmentIdToExclude) {
        for (Appointment appointment : Main.appointments) {
            if (appointment.getCustomerId() == customerId && appointment.getAppointmentId() != appointmentIdToExclude) {
                LocalDateTime appointmentStart = appointment.getStart();
                LocalDateTime appointmentEnd = appointment.getEnd();

                // Check if the new appointment overlaps with an existing appointment
                if (!start.isAfter(appointmentEnd) && !end.isBefore(appointmentStart)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Method to check if an appointment falls outside of business hours
    private boolean isBusinessHours(LocalDateTime start) {
        System.out.println(start);
        ZonedDateTime startUtc = convertToUTC(start);
        System.out.println(startUtc);
        // Define business hours (3:00-17:00 UTC)
        ZonedDateTime businessStartTime = startUtc.withHour(12).withMinute(0).withSecond(0);
        ZonedDateTime businessEndTime = startUtc.withHour(2).withMinute(0).withSecond(0);

        // Check if the appointment starts and ends within business hours
        return (startUtc.isAfter(businessStartTime) || startUtc.isEqual(businessStartTime)) || (startUtc.isBefore(businessEndTime) || startUtc.isEqual(businessEndTime));
    }

    // Method to convert a LocalDateTime to UTC
    private ZonedDateTime convertToUTC(LocalDateTime localDateTime) {
        ZoneId localZoneId = ZoneId.systemDefault();
        ZoneId utcZoneId = ZoneId.of("UTC");
        return localDateTime.atZone(localZoneId).withZoneSameInstant(utcZoneId);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> customerOptions = FXCollections.observableArrayList();
        for (Customer customer : Main.customers) {
            customerOptions.add(customer.getCustomerName());
        }
        customerComboBox.setItems(customerOptions);
    }
}
