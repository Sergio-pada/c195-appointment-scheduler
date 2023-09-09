package com.example.c195.Controller;

import com.example.c195.Helper.CustomerQuery;
import com.example.c195.Main;
import com.example.c195.Model.Customer;
import com.example.c195.Model.FirstLevelDivision;
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
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class EditCustomerController implements Initializable {
    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField postalCodeField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private ComboBox<String> countryComboBox;
    @FXML
    private ComboBox<String> divisionComboBox;
    @FXML
    private String divIdToName(int divisionId){
        for (FirstLevelDivision division : Main.divisions) {
            if (division.getDivisionId() == divisionId) {
                return division.getDivision();
            }
        }
        return null;
    }
    @FXML
    public void setCustomerData(Customer customer) {
        // Set the text fields with appointment data
        idField.setText(String.valueOf(customer.getCustomerId()));
        nameField.setText(customer.getCustomerName());
        addressField.setText(customer.getAddress());
        postalCodeField.setText(customer.getPostalCode());
        phoneNumberField.setText(customer.getPhone());
        divisionComboBox.setValue(divIdToName(customer.getDivisionId()));

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
        // Retrieve the modified customer data from the text fields
        int customerId = Integer.parseInt(idField.getText());
        String name = nameField.getText();
        String address = addressField.getText();
        String postalCode = postalCodeField.getText();
        String phoneNumber = phoneNumberField.getText();

        // Get the selected division name from the divisionComboBox
        String selectedDivision = divisionComboBox.getSelectionModel().getSelectedItem();

        // Create a mapping between division names and division IDs
        Map<String, Integer> divisionIdMapping = new HashMap<>();
        for (FirstLevelDivision division : Main.divisions) {
            divisionIdMapping.put(division.getDivision(), division.getDivisionId());
        }

        // Look up the division ID based on the selected division name
        int divisionId = divisionIdMapping.get(selectedDivision);




        if (CustomerController.newCustomer) {
            // Creating a new customer
            Customer newCustomer = new Customer(customerId, name, address, postalCode, phoneNumber, divisionId);

            // Call the createAppointment method to insert the new appointment
            CustomerQuery.createCustomer(newCustomer);
        } else {
            // Editing an existing customer
            // Find the existing customer by ID in the list of appointments
            Customer existingCustomer = null;
            for (Customer customer : Main.customers) {
                if (customer.getCustomerId() == customerId) {
                    existingCustomer = customer;
                    break;
                }
            }

            if (existingCustomer != null) {
                // Remove the existing customer
                Main.customers.remove(existingCustomer);
            }

            // Create a new Appointment object with the updated data
            Customer updatedCustomer = new Customer(customerId, name, address, postalCode, phoneNumber, divisionId);

            // Add the updated appointment to the list
            Main.customers.add(updatedCustomer);

            // Call the updateAppointment method to update the database record
            CustomerQuery.updateCustomer(updatedCustomer);
        }
        CustomerController.newCustomer = false;
        Main.customers = CustomerQuery.getAllCustomers();
        // Close the current window and return to the main menu
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> divisionsCountry1 = FXCollections.observableArrayList();
        ObservableList<String> divisionsCountry2 = FXCollections.observableArrayList();
        ObservableList<String> divisionsCountry3 = FXCollections.observableArrayList();

        // Filter divisions and add them to respective lists
        for (FirstLevelDivision division : Main.divisions) {
            int countryId = division.getCountryId();
            if (countryId == 1) {
                divisionsCountry1.add(division.getDivision());
            } else if (countryId == 2) {
                divisionsCountry2.add(division.getDivision());
            } else if (countryId == 3) {
                divisionsCountry3.add(division.getDivision());
            }
        }

        ObservableList<String> countryOptions = FXCollections.observableArrayList(
                "U.S.",
                "U.K.",
                "Canada"
        );

        countryComboBox.setItems(countryOptions);

        // Add a listener to the countryComboBox to update divisionComboBox
        countryComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.equals("U.S.")) {
                    divisionComboBox.setItems(divisionsCountry1);
                } else if (newValue.equals("U.K.")) {
                    divisionComboBox.setItems(divisionsCountry2);
                } else if (newValue.equals("Canada")) {
                    divisionComboBox.setItems(divisionsCountry3);
                }
            }
        });
    }

    void setCustomerId(int customerId) {
        idField.setText(String.valueOf(customerId));
    }
}
