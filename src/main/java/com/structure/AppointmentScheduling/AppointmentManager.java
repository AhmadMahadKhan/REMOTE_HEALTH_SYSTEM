package com.structure.AppointmentScheduling;

import com.structure.Exception.ForeignKeyViolationException;
import com.structure.Model.Doctor;
import com.structure.Model.Patient;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentManager {

    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String userRoot = "root";
    private static final String password = "WJ28@krhps";

    public static void loadDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver loading failed.");
            e.printStackTrace();
        }
    }
    //for patient
    public static void addAppointment(Appointment appointment) throws SQLException , ForeignKeyViolationException {
        loadDriver();

        String query = "INSERT INTO appointments (patient_id, doctor_id, appointment_date ,appointment_time,id ) VALUES (?,  ?, ?, ?,?)";
        try(Connection connection = DriverManager.getConnection(url , userRoot, password);
            PreparedStatement preparedStatement = connection.prepareStatement(query)){

            //used local date in appointment class now converting that to sql date and time for storage
            preparedStatement.setString(1, appointment.getPatientId());
            preparedStatement.setString(2, appointment.getDoctorId());
            preparedStatement.setDate(3, Date.valueOf(appointment.getDate()));
            preparedStatement.setTime(4,Time.valueOf(appointment.getTime()));
            preparedStatement.setInt(5,appointment.getAppointmentId());

            preparedStatement.executeUpdate();

        }
        catch (SQLIntegrityConstraintViolationException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Doctor does not exist");
            alert.setContentText("Please make sure the doctor exists before scheduling an appointment.");
            alert.showAndWait();
            throw new ForeignKeyViolationException("Invalid patient_id or doctor_id. Please make sure both exist before scheduling an appointment.", e);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Appointment> viewAppointments(String patientId) throws SQLException {

        System.out.println("inside view appointments method in appointment manager class");
        loadDriver();
        List<Appointment> appointments = new ArrayList<>();
        String query = "SELECT * FROM appointments WHERE patient_id = ? ORDER BY id ";
        try(Connection connection = DriverManager.getConnection(url , userRoot, password);
            PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, patientId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                appointments.add(new Appointment(
                        resultSet.getString("patient_id"),
                        resultSet.getString("doctor_id"),
                        resultSet.getDate("appointment_date").toLocalDate(),
                        resultSet.getTime("appointment_time").toLocalTime(),
                        resultSet.getInt("id"),
                        resultSet.getString("status")
                        )
                );
            }
            for (Appointment appointment : appointments) {
                System.out.println(appointment.detail());
            }
            System.out.println("the size of appointments list is " + appointments.size());

        }
        return appointments;
    }
    //doctor can check all the appointmens related to him
    //all pending rejected and confirmed
    public static List<Appointment> viewAppointmentsDoctor(Doctor doctor){
        loadDriver();
        String query = "SELECT * FROM appointments WHERE doctor_id = ? ";
//        String query = "SELECT * FROM appointments WHERE doctor_id = ? ORDER BY appointment_date DESC, appointment_time DESC";
        try(Connection connection = DriverManager.getConnection(url , userRoot , password);
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, doctor.getId());
            List<Appointment> appointments = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                appointments.add(
                        new Appointment(
                                resultSet.getString("patient_id"),
                                resultSet.getString("doctor_id"),
                                resultSet.getDate("appointment_date").toLocalDate(),
                                resultSet.getTime("appointment_time").toLocalTime(),
                                resultSet.getInt("id"),
                                resultSet.getString("status")
                        ));

            }
            System.out.println("the size of appointments list is " + appointments.size());
//            for (Appointment appointment : appointments) {
//
//            }
            return appointments;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean updateDecision(Appointment appointment ,String decision ) throws SQLException {

        String query = "UPDATE appointments SET status = ? WHERE patient_id = ? AND doctor_id = ? AND appointment_date = ? AND appointment_time = ?";
        loadDriver();
        try(Connection connection = DriverManager.getConnection(url ,userRoot , password);
        PreparedStatement preparedStatement = connection.prepareStatement(query)){

            preparedStatement.setString(1, decision);
            preparedStatement.setString(2, appointment.getPatientId());
            preparedStatement.setString(3, appointment.getDoctorId());
            preparedStatement.setDate(4, Date.valueOf(appointment.getDate()));
            preparedStatement.setTime(5,Time.valueOf(appointment.getTime()));
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Appointment updated successfully.");
                return true;
            } else {
                System.out.println("No appointment found with the given patient_id, doctor_id, appointment_date and appointment_time.");
                return false;
            }

        }
    }


    public static List<Patient> getPatientData(Doctor doctor) throws SQLException {
        loadDriver();

        String query = "SELECT DISTINCT  p.id AS patient_id, " +
                " p.Name AS patient_name, " +
                "p.address , p.email " +
                "FROM appointments a " +
                "JOIN patients p ON a.patient_id = p.id " +
                "WHERE a.doctor_id = ?";
        List<Patient> patients = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url , userRoot , password);
        PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, doctor.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Patient patient = new Patient(resultSet.getString("patient_name"), resultSet.getString("patient_id"), resultSet.getString("email"), null, null);
                patients.add(patient);
                System.out.println(resultSet.getString("patient_id") + " " + resultSet.getString("patient_name") + " " + resultSet.getString("email"));
            }



        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return patients;
    }


    public static List<String> getEmergencyEmails(String patientId) throws SQLException {
        loadDriver();
        String query = "SELECT DISTINCT d.email " +
                "FROM appointments a " +
                "JOIN doctors d ON a.doctor_id = d.id " +
                "WHERE a.patient_id = ?";
        try (Connection connection = DriverManager.getConnection(url, userRoot, password);
        PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, patientId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<String> emergencyEmails = new ArrayList<>();
            while (resultSet.next()) {
                emergencyEmails.add(resultSet.getString("email"));
            }
            if(emergencyEmails.isEmpty()){
                String query1  = "Select email FROM doctors ";
                ResultSet resultSet1 = preparedStatement.executeQuery();
                while (resultSet1.next()) {
                    emergencyEmails.add(resultSet1.getString("email"));
                }
                System.out.println("the size of emergency emails list is " + emergencyEmails.size());
            }
            return emergencyEmails;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
