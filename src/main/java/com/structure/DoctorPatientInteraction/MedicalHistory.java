package com.structure.DoctorPatientInteraction;

import com.structure.DataAccessObject.DatabaseResources;
import com.structure.Model.Patient;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MedicalHistory {

    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    static final String userRoot = "root";
    private static final String password = "WJ28@krhps";

    public static void addFeedback(Feedback feedback) throws SQLException {
        DatabaseResources.loadDriver();

        String query = "INSERT INTO feedback(doctorId , patientID ,feedback)" +
                " Values( ?, ?, ? )";
        try(Connection connection = DriverManager.getConnection(url,userRoot,password);
            PreparedStatement preparedStatement = connection.prepareStatement(query)){

            preparedStatement.setString(1,feedback.getDoctorId());
            preparedStatement.setString(2,feedback.getPatientId());
            preparedStatement.setString(3,feedback.getFeedback());
            preparedStatement.executeUpdate();
            System.out.println("Feedback uploaded succesfully");
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void addPrescription(Prescription prescription) throws SQLException {
        DatabaseResources.loadDriver();

        String query = "INSERT INTO prescription(doctorId , patientID ,medicine , dosage , schedule)" +
                " Values( ?, ?, ?, ?, ? )";
        try(Connection connection = DriverManager.getConnection(url,userRoot,password);
            PreparedStatement preparedStatement = connection.prepareStatement(query)){

            preparedStatement.setString(1,prescription.getDoctorId());
            preparedStatement.setString(2,prescription.getPatientId());
            preparedStatement.setString(3,prescription.getMedicine());
            preparedStatement.setString(4,prescription.getDosage());
            preparedStatement.setString(5,prescription.getSchedule());
            preparedStatement.executeUpdate();
            System.out.println("Prescription uploaded succesfully");
        }
        catch (SQLException e){
            e.printStackTrace();
        }


    }
    public static List<Prescription> viewPrescriptions(Patient patient) throws SQLException {
        DatabaseResources.loadDriver();

        String query = "Select * FROM  prescription  WHERE patientId = ? ";
        try (Connection connection = DriverManager.getConnection(url, userRoot, password);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, patient.getId());

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Prescription> prescriptions = new ArrayList<>();
            while (resultSet.next()) {
                LocalDate dateIssued = resultSet.getTimestamp("created_at").toLocalDateTime().toLocalDate();
                prescriptions.add(

                        new Prescription(
                                resultSet.getString("medicine"),
                                resultSet.getString("dosage"),
                                resultSet.getString("schedule"),
                                resultSet.getString("patientId"),
                                resultSet.getString("doctorId"),
                                dateIssued
                        )
                );
            }
            return prescriptions;

        }
    }
//    public static List<Feedback> viewFeedbacks(Patient patient) throws SQLException {
//        DatabaseResources.loadDriver();
//
//        String query = "Select * FROM  feedback  WHERE patientId = ? ";
//        List<Feedback> feedbacks = new ArrayList<>();
//        try(Connection connection = DriverManager.getConnection(url , userRoot , password);
//        PreparedStatement preparedStatement = connection.prepareStatement(query)){
//            preparedStatement.setString(1,patient.getId());
//            Feedback feedback = null;
//            ResultSet resultSet = preparedStatement.executeQuery();
//            while (resultSet.next()) {
//                feedback = new Feedback(
//                        resultSet.getString("doctorId"),
//                        resultSet.getString("patientId"),
//                        resultSet.getString("feedback"),
//                        resultSet.getTimestamp("created_at").toLocalDateTime().toLocalDate()
//                );
//                feedbacks.add(feedback);
//                System.out.println(feedback.getDoctorId() + " " + feedback.getPatientId() + " " + feedback.getFeedback());
//                System.out.println("feedbacks size is " + feedbacks.size());
//                System.out.println("feedbacks list is " + feedbacks);
//
//
//            }
//        }
//        return feedbacks;
//    }
//}
public static List<String> getDistinctDoctorIds(Patient patient) throws SQLException {
    List<String> doctorIds = new ArrayList<>();
    String query = "SELECT DISTINCT doctorId FROM feedback WHERE patientId = ?";

    try (Connection connection = DriverManager.getConnection(url, userRoot, password);
         PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setString(1, patient.getId());
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            doctorIds.add(resultSet.getString("doctorId"));
        }
    }
    return doctorIds;
}

    public static List<Feedback> getFeedbacksByDoctor(Patient patient, String doctorId) throws SQLException {
        List<Feedback> feedbacks = new ArrayList<>();
        String query = "SELECT * FROM feedback WHERE patientId = ? AND doctorId = ?";

        try (Connection connection = DriverManager.getConnection(url, userRoot, password);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, patient.getId());
            statement.setString(2, doctorId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Feedback feedback = new Feedback(
                        resultSet.getString("doctorId"),
                        resultSet.getString("patientId"),
                        resultSet.getString("feedback"),
                        resultSet.getTimestamp("created_at").toLocalDateTime().toLocalDate()
                );
                feedbacks.add(feedback);
            }
        }
        return feedbacks;
    }



}
