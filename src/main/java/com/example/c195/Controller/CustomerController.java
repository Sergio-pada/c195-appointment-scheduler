package com.example.c195.Controller;

import com.example.c195.Helper.AppointmentQuery;
import com.example.c195.Helper.CustomerQuery;
import com.example.c195.Main;
import com.example.c195.Model.Appointment;
import com.example.c195.Model.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


import static com.example.c195.Main.appointments;
import static com.example.c195.Main.customers;

public class CustomerController implements Initializable {
    @FXML
    private TableView<Customer> customersTableView;
    @FXML
    private TableColumn<Customer, Integer> customerIdColumn;
    @FXML
    private TableColumn<Customer, String> nameColumn;
    @FXML
    private TableColumn<Customer, String> addressColumn;
    @FXML
    private TableColumn<Customer, String> postalCodeColumn;
    @FXML
    private TableColumn<Customer, String> phoneColumn;
    @FXML
    private TableColumn<Customer, Integer> divIdColumn;
    @FXML
    public static boolean newCustomer = false;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Initialize the TableView columns
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        divIdColumn.setCellValueFactory(new PropertyValueFactory<>("divisionId"));

        // Load and display all appointments initially
        showAllCustomers();
    }
    @FXML
    private void showAllCustomers() {
        customersTableView.getItems().clear(); // Clear existing items
        customersTableView.getItems().addAll(customers); // Load all customers
    }

    @FXML
    private void newCustomer(ActionEvent event) throws IOException {
        newCustomer = true;
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("edit-customer.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Customer Editor");
        stage.setScene(scene);
        stage.show();

        // Calculate the new appointmentId
        int newCustomerId = calculateNewCustomerId();

        // Pass the new id to the EditCustomerController
        EditCustomerController editCustomerController = fxmlLoader.getController();
        editCustomerController.setCustomerId(newCustomerId);
    }
    private int calculateNewCustomerId() {
        int maxCustomerId = -1; // Initialize with a value smaller than any possible appointmentId

        for (Customer customer : customers) {
            int customerId = customer.getCustomerId();
            if (customerId > maxCustomerId) {
                maxCustomerId = customerId; // Update the maximum if a larger value is found
            }
        }

        // Return the new appointmentId as maxAppointmentId + 1
        return maxCustomerId + 1;
    }
    @FXML
    private void deleteCustomer(ActionEvent event) {
        Customer selectedCustomer = customersTableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            int customerId = selectedCustomer.getCustomerId();

            // Check if the selected customer has upcoming appointments
            boolean hasAppointments = Main.appointments.stream().anyMatch(appointment -> appointment.getCustomerId() == customerId);

            if (hasAppointments) {
                // Create a confirmation dialog for deleting the customer and associated appointments
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirmation");
                confirmationAlert.setHeaderText("This customer has upcoming appointments.");
                confirmationAlert.setContentText("These appointments will be deleted as well. Continue?");

                Optional<ButtonType> result = confirmationAlert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // User confirmed the deletion
                    // Delete associated appointments first
                    List<Appointment> appointmentsToDelete = Main.appointments.stream().filter(appointment -> appointment.getCustomerId() == customerId).collect(Collectors.toList());

                    appointmentsToDelete.forEach(appointment -> {
                        AppointmentQuery.deleteAppointment(appointment.getAppointmentId());
                        Main.appointments.remove(appointment);
                    });

                    // Now delete the customer
                    CustomerQuery.deleteCustomer(customerId);
                    customers.remove(selectedCustomer);

                    // Refresh the TableView data
                    showAllCustomers();

                    // Create a success alert for the deletion
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Customer Removed");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("The customer and associated appointments have been removed.");
                    successAlert.showAndWait();
                }
            } else {
                // No upcoming appointments, proceed with deleting just the customer
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirmation");
                confirmationAlert.setHeaderText("Are you sure you want to delete this customer?");
                confirmationAlert.setContentText("This action cannot be undone.");

                Optional<ButtonType> result = confirmationAlert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // User confirmed the deletion
                    CustomerQuery.deleteCustomer(customerId);
                    customers.remove(selectedCustomer);

                    // Refresh the TableView data
                    showAllCustomers();

                    // Create a success alert for the deletion
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Customer Removed");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("The customer has been removed.");
                    successAlert.showAndWait();
                }
            }
        }
    }




    @FXML
    private void editCustomer(ActionEvent event) throws IOException {
        Customer selectedCustomer = customersTableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Load the appointment.fxml file
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("edit-customer.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Customer Editor");
            stage.setScene(scene);
            stage.show();

            EditCustomerController editCustomerController = fxmlLoader.getController();
            editCustomerController.setCustomerData(selectedCustomer);

            stage.setScene(scene);
            stage.show();
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
