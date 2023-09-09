package com.example.c195;

import com.example.c195.Helper.*;
import com.example.c195.Model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Main extends Application {
    // Appointment object list
    public static List<Appointment> appointments = AppointmentQuery.getAllAppointments();//Public because it's used in AppointmentsController
    public static List<Customer> customers = CustomerQuery.getAllCustomers();//Public because it's used in AppointmentsController
    public static List<FirstLevelDivision> divisions = FirstLevelDivisionQuery.getAllDivisions();
    public static void main(String[] args) throws SQLException {

        JDBC.openConnection();

        // Other object lists
        List<Contact> contacts = ContactQuery.getAllContacts();
        List<Country> countries = CountryQuery.getAllCountries();

        List<User> users = UserQuery.getAllUsers();


        JDBC.closeConnection();
        launch();
    }
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }





}