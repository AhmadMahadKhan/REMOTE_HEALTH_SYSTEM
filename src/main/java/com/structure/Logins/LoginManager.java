package com.structure.Logins;

import com.structure.Model.Administrator;
import com.structure.Model.Doctor;
import com.structure.Model.Patient;

import java.sql.*;

public class LoginManager {

    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String userRoot = "root";
    private static final String password = "WJ28@krhps";

    //loading the jdbc drivers
    public static void loadDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver loading failed.");
            e.printStackTrace();
        }
    }

    //a method to store password of the user if not set
    //checking and validating the credidentials and then storing the password
    public static void setPassword(String userEmail, String id, String newPassword) throws SQLException {
        System.out.println("Setting password for " + userEmail + " in all tables.");
        loadDriver();

        String[] tables = {"patients", "doctors", "admin"};

        try (Connection connection = DriverManager.getConnection(url, userRoot, password)) {
            for (String table : tables) {
                //retriving the data from the tables
                String query = "SELECT password FROM " + table + " WHERE email = ? AND id = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, userEmail);
                    preparedStatement.setString(2, id);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        String dbPass = resultSet.getString("password");
                        if (dbPass == null || dbPass.isEmpty()) {
                            // Password not set so setting the password
                            String updateQuery = "UPDATE " + table + " SET password = ? WHERE email = ? AND id = ?";
                            try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                                updateStmt.setString(1, newPassword);
                                updateStmt.setString(2, userEmail);
                                updateStmt.setString(3, id);
                                updateStmt.executeUpdate();
                                System.out.println("Password updated successfully for " + table);
                                return;
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Password not updated for any of the tables.");
    }

    //used in sign-up page
    public static boolean passwordValidation(String userEmail, String id) throws SQLException {
        loadDriver();


        try (Connection connection = DriverManager.getConnection(url, userRoot, password)) {
            ResultSet resultSet;

            // If user is attempting to register (password not set yet)

                //  Check in doctors table
                String doctorQuery = "SELECT password FROM doctors WHERE email = ? AND id = ?";
                try (PreparedStatement doctorStmt = connection.prepareStatement(doctorQuery)) {
                    doctorStmt.setString(1, userEmail);
                    doctorStmt.setString(2, id);
                    resultSet = doctorStmt.executeQuery();
                    if (resultSet.next()) {
                        String dbPass = resultSet.getString("password");
                        if (dbPass == null || dbPass.isEmpty()) {
                            //return true will display an alert on the screen tht the pass is not set
                            return true; //the user has to set the password for the first time
                        } else {
                            return false; // Password is already set
                        }
                    }
                }

                //  Check in patients table
                String patientQuery = "SELECT password FROM patients WHERE email = ? AND id = ?";
                try (PreparedStatement patientStmt = connection.prepareStatement(patientQuery)) {
                    patientStmt.setString(1, userEmail);
                    patientStmt.setString(2, id);
                    resultSet = patientStmt.executeQuery();
                    if (resultSet.next()) {
                        String dbPass = resultSet.getString("password");
                        if (dbPass == null || dbPass.isEmpty()) {
                            return true; //the user has to set the password for the first time
                        } else {
                            return false ; //password is already set
                        }
                    }
                }
                // Check in admins table
                String adminQuery = "SELECT password FROM admin WHERE email = ? AND id = ?";
                try (PreparedStatement adminStmt = connection.prepareStatement(adminQuery)) {
                    adminStmt.setString(1, userEmail);
                    adminStmt.setString(2, id);
                    resultSet = adminStmt.executeQuery();
                    if (resultSet.next()) {
                        String dbPass = resultSet.getString("password");
                        if (dbPass == null || dbPass.isEmpty()) {
                            return true; //the user has to set the password for the first time
                        } else {
                            return false ; //password is already set
                        }
                    }
                }

                return false;
            }
     }

     //give the class type of the user by checking all the data
    //retrives all the data from database
    //creates an object of it an then returns
    //if not present then return null
     public static Object checkUser(String userEmail,String id ,  String pass) throws SQLException {

        loadDriver();
         String query ;
         try (Connection connection = DriverManager.getConnection(url, userRoot, password)) {
             ResultSet resultSet;
             //checking the user enter data in patient table
             query = "SELECT * FROM patients WHERE email = ? AND id = ? AND password = ?";
             try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                 preparedStatement.setString(1, userEmail);
                 preparedStatement.setString(2, id);
                 preparedStatement.setString(3, pass);
                 resultSet = preparedStatement.executeQuery();
                 if (resultSet.next()) {
                        return new Patient(resultSet.getString("name"),
                                resultSet.getString("id"),
                                resultSet.getString("email"),
                                resultSet.getString("address"),
                                resultSet.getString("emergency_email"));
                 }
             }
             //now checking user enter data in doctor table
             query = "SELECT * FROM doctors WHERE email = ? AND id = ? AND password = ?";
             try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                 preparedStatement.setString(1, userEmail);
                 preparedStatement.setString(2, id);
                 preparedStatement.setString(3, pass);
                 resultSet = preparedStatement.executeQuery();
                 if (resultSet.next()) {

                     return new Doctor(resultSet.getString("name"),
                             resultSet.getString("id"),
                             resultSet.getString("email"),
                             resultSet.getString("specialization"));
                 }
             }
             //now checking the user data in admin table in database
             query = "SELECT * FROM admin WHERE email = ? AND id = ? AND password = ?";
             try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                 preparedStatement.setString(1, userEmail);
                 preparedStatement.setString(2, id);
                 preparedStatement.setString(3, pass);
                 resultSet = preparedStatement.executeQuery();
                 if (resultSet.next()) {

                     return new Administrator(resultSet.getString("name"),
                             resultSet.getString("id"),
                             resultSet.getString("email"));
                 }
             }
         }
         return null ;
    }
}


