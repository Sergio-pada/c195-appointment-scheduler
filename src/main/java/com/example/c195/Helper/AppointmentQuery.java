package com.example.c195.Helper;

import com.example.c195.Main;
import com.example.c195.Model.Appointment;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class AppointmentQuery {
    public static List<Appointment> getAllAppointments() {
        // List for appointment objects
        List<Appointment> appointments = new ArrayList<>();
        String query = "SELECT * FROM APPOINTMENTS";

        try (Connection connection = JDBC.getConnection();//If no sql exception

             PreparedStatement statement = connection.prepareStatement(query);// Our query in a form ready to be run in mysql
             ResultSet resultSet = statement.executeQuery()) { //Results from the query

            while (resultSet.next()) {//Instantiations
                int appointmentID = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String type = resultSet.getString("Type");
                LocalDateTime start = resultSet.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = resultSet.getTimestamp("End").toLocalDateTime();
                int customerID = resultSet.getInt("Customer_ID");
                int userID = resultSet.getInt("User_ID");
                int contactID = resultSet.getInt("Contact_ID");

                Appointment appointment = new Appointment(appointmentID, title, description, location, type, start, end, customerID, userID, contactID);
                appointments.add(appointment);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;// Appointment objects returned
    }
    public static void deleteAppointment(int appointmentID) {
        String query = "DELETE FROM APPOINTMENTS WHERE Appointment_ID = ?";

        try (Connection connection = JDBC.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, appointmentID);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void updateAppointment(Appointment appointment) {
        String query = "UPDATE APPOINTMENTS " +
                "SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? " +
                "WHERE Appointment_ID = ?";

        try (Connection connection = JDBC.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, appointment.getTitle());
            statement.setString(2, appointment.getDescription());
            statement.setString(3, appointment.getLocation());
            statement.setString(4, appointment.getType());
            statement.setTimestamp(5, Timestamp.valueOf(appointment.getStart()));
            statement.setTimestamp(6, Timestamp.valueOf(appointment.getEnd()));
            statement.setInt(7, appointment.getCustomerId());
            statement.setInt(8, appointment.getUserId());
            statement.setInt(9, appointment.getContactId());
            statement.setInt(10, appointment.getAppointmentId());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void createAppointment(Appointment appointment) {
        String query = "INSERT INTO APPOINTMENTS (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = JDBC.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, appointment.getTitle());
            statement.setString(2, appointment.getDescription());
            statement.setString(3, appointment.getLocation());
            statement.setString(4, appointment.getType());
            statement.setTimestamp(5, Timestamp.valueOf(appointment.getStart()));
            statement.setTimestamp(6, Timestamp.valueOf(appointment.getEnd()));
            statement.setInt(7, appointment.getCustomerId());
            statement.setInt(8, appointment.getUserId());
            statement.setInt(9, appointment.getContactId());

            statement.executeUpdate();

            // Retrieve the auto-generated appointment ID
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int appointmentID = generatedKeys.getInt(1);
                appointment.setAppointmentId(appointmentID);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
