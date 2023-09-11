package com.example.c195.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import com.example.c195.Main;
import com.example.c195.Model.Contact;
import com.example.c195.Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Report2Controller implements Initializable {

    @FXML
    private ComboBox<String> contactComboBox;

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
    private TableColumn<Appointment, String> startColumn;

    @FXML
    private TableColumn<Appointment, String> endColumn;

    @FXML
    private TableColumn<Appointment, Integer> customerIdColumn;

    @FXML
    private TableColumn<Appointment, Integer> userIdColumn;

    @FXML
    private TableColumn<Appointment, Integer> contactIdColumn;

    // HashMap to map contactId to contactName
    private Map<Integer, String> contactIdToName = new HashMap<>();

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

        // Populate the contactComboBox and build the contactIdToName map
        ObservableList<String> contactOptions = FXCollections.observableArrayList();
        for (Contact contact : Main.contacts) {
            contactOptions.add(contact.getContactName());
            contactIdToName.put(contact.getContactId(), contact.getContactName());
        }
        contactComboBox.setItems(contactOptions);

        // Handle ComboBox selection
        contactComboBox.setOnAction(event -> {
            String selectedContactName = contactComboBox.getSelectionModel().getSelectedItem();
            if (selectedContactName != null) {
                int selectedContactId = getContactIdByName(selectedContactName);
                displayAppointmentsByContact(selectedContactId);
            }
        });
    }

    // Get the contactId by contactName
    private int getContactIdByName(String contactName) {
        for (Map.Entry<Integer, String> entry : contactIdToName.entrySet()) {
            if (entry.getValue().equals(contactName)) {
                return entry.getKey();
            }
        }
        return -1; // Return -1 if not found (you can handle this case as needed)
    }

    // Display appointments by contactId
    private void displayAppointmentsByContact(int contactId) {
        ObservableList<Appointment> appointmentsByContact = FXCollections.observableArrayList();
        for (Appointment appointment : Main.appointments) {
            if (appointment.getContactId() == contactId) {
                appointmentsByContact.add(appointment);
            }
        }
        appointmentsTableView.setItems(appointmentsByContact);
    }

    // Handle "Back" button click to return to the main menu
    @FXML
    private void mainMenu(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Load the appointment.fxml file
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 115, 150);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();    }
}
