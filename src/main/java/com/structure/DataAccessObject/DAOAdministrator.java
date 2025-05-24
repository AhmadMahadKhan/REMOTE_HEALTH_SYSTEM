package com.structure.DataAccessObject;

import com.structure.DataBase.DBUtil;
import com.structure.Exception.DuplicateUserException;
import com.structure.Exception.UserNotFoundException;
import com.structure.Model.Doctor;
import com.structure.Model.Patient;
import com.structure.Model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DAOAdministrator {



    //used in ADMINISTRSTORCONTROLLER to add doctor and patient in database
    public static void addUser(User user) throws SQLException ,DuplicateUserException {


        String query ="";
        //created these instances because of some class specific attributes to be added in the database table
        Patient patient = null;
        Doctor doctor = null;
        boolean isDoctor = false  ;
        boolean isPatient = false  ;

        System.out.println("checking instabces");
        if(user instanceof Patient){
             query = "INSERT INTO patients (id, name, email, password,address , emergency_email) VALUES (?, ?, ?, ?, ?, ?)";
              patient = (Patient) user;
              isPatient = true;
        }
        else if(user instanceof Doctor){
             query = "INSERT INTO doctors (id, name, email, password, specialization) VALUES (?, ?, ?, ?, ?)";
              doctor = (Doctor) user;
              isDoctor = true;
        }

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)){

            preparedStatement.setString(1, user.getId());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());

            if(isPatient){
                preparedStatement.setString(5, patient.getAddress());
                preparedStatement.setString(6,patient.getEmergencyEmail());
            }
            else if(isDoctor){
                preparedStatement.setString(5, doctor.getSpecialization());
            }
            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("User added successfully.");

            }

        }
        catch (SQLIntegrityConstraintViolationException e) {
            String message = e.getMessage().toLowerCase();

            if (message.contains("primary") || message.contains("id")) {
                throw new DuplicateUserException("ID", "A user with ID '" + user.getId() + "' already exists.");
            } else if (message.contains("email")) {
                throw new DuplicateUserException("Email", "The email '" + user.getEmail() + "' is already in use.");
            } else {
                throw new DuplicateUserException("Unknown", "A unique constraint was violated.");
            }
        }

        catch (SQLException e) {
            System.out.println("Connection or SQL error.");
            e.printStackTrace();
            System.out.println(e.getMessage());
        }


    }

    //to delete doctor or patient from the database
    public static void deleteUser(String id, String person, String email, String name)
            throws SQLException, UserNotFoundException {

        String query = "";

        //checkong which menu item is pressed and then the same query is used
        if (person.equals("patient")) {
            query = "DELETE FROM patients WHERE id = ? AND email = ? AND name = ?";
        } else if (person.equals("doctor")) {
            query = "DELETE FROM doctors WHERE id = ? AND email = ? AND name = ?";
        }

        try (Connection connection =DBUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, id);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, name);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("User with id " + id + " has been deleted.");
            } else {
                throw new UserNotFoundException("No user found with ID: " + id);
            }
        }
    }


    public static String getEmail(String id ){

        String query ="SELECT email FROM patients WHERE id = ?" +
                "UNION  SELECT email FROM doctors WHERE id = ?";
        try (Connection connection =DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1,id);
            preparedStatement.setString(2,id);
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getString("email");
            }

        } catch (SQLException e) {
            System.err.println("Database operation failed: " + e.getMessage());

            throw new RuntimeException(e);
        }
        return null;
    }

        // For viewing all patients
        public static List<Patient> viewAllPatients() {

            String query = "SELECT id, name, email, address, emergency_email FROM patients";
            List<Patient> patients = new ArrayList<>();

            try (Connection connection = DBUtil.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    String id = resultSet.getString("id");
                    String name = resultSet.getString("name");
                    String email = resultSet.getString("email");
                    String address = resultSet.getString("address");
                    String emergencyEmail = resultSet.getString("emergency_email");
                    patients.add(new Patient(name, id, email, address, emergencyEmail));
                }

            } catch (SQLException e) {
                System.err.println("SQL Error: " + e.getMessage());
            }

            return patients;
        }

        // For viewing all doctors
        public static List<Doctor> viewAllDoctors() {

            String query = "SELECT id, name, email, specialization FROM doctors";
            List<Doctor> doctors = new ArrayList<>();

            try (Connection connection =DBUtil.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    String id = resultSet.getString("id");
                    String name = resultSet.getString("name");
                    String email = resultSet.getString("email");
                    String specialization = resultSet.getString("specialization");
                    doctors.add(new Doctor(name, id, email, specialization));
                }

            } catch (SQLException e) {
                System.err.println("SQL Error: " + e.getMessage());
            }

            return doctors;
        }


}
