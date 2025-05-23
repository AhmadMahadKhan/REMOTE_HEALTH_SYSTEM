package com.structure.HealthDataHandling;

import com.structure.Model.Patient;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VitalsDatabase {

    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String userRoot = "root";
    private static final String password = "WJ28@krhps";

    //loading jdbc drivers
    public static void loadDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver loading failed.");
            e.printStackTrace();
        }
    }

    public static void saveVitalSign(VitalSign vital) {
        loadDriver();
        String query = "INSERT INTO vitalSigns (patient_id, heart_rate, temperature, systolic, diastolic, oxygen_level) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, userRoot, password);

             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, vital.getPatientId());
            preparedStatement.setInt(2, vital.getHeartRate());
            preparedStatement.setDouble(3, vital.getTemp());
            preparedStatement.setInt(4, vital.getSystolicPressure());
            preparedStatement.setInt(5, vital.getDiastolicPressure());
            preparedStatement.setInt(6, vital.getOxygenLevel());

            preparedStatement.executeUpdate();
            System.out.println("Vital Sign Saved Successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static VitalSign getVitals(Patient patient) {

        loadDriver();
        VitalSign vitalSign = null ;

        System.out.println("data base checking");
        String query = "SELECT * FROM vitalsigns WHERE patient_id = ? ORDER BY id DESC LIMIT 1;";

        System.out.println("entering try block");
        try(Connection connection = DriverManager.getConnection(url , userRoot, password);
        PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1, patient.getId().trim());
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){

                vitalSign = new VitalSign(resultSet.getString("patient_id") ,resultSet.getInt("oxygen_Level"), resultSet.getDouble("temperature"), resultSet.getInt("heart_rate")
                , resultSet.getInt("systolic"), resultSet.getInt("diastolic"));

            }

        }
        catch (SQLException e) {
            System.out.println("error executing query in vital database in get vitals method");
            e.printStackTrace();
        }



    return vitalSign ;
    }

    public static String getPatientsVital(String patientId){

        loadDriver();
        String query = "SELECT * FROM vitalsigns WHERE patient_id = ? ORDER BY recorded_at DESC LIMIT 1";
        try(Connection connection = DriverManager.getConnection(url ,userRoot , password);
        PreparedStatement preparedStatement = connection.prepareStatement(query )){

            preparedStatement.setString(1,patientId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String vitals = String.format(
                        "Oxygen Level: %d%%\nHeart Rate: %d bpm\nTemperature: %.1f°C\nBlood Pressure: %d/%d\nRecorded At: %s",
                        resultSet.getInt("oxygen_level"),
                        resultSet.getInt("heart_rate"),
                        resultSet.getDouble("temperature"),
                        resultSet.getInt("systolic"),
                        resultSet.getInt("diastolic"),
                        resultSet.getTimestamp("recorded_at")
                );
                return vitals;
            } else {

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return  null;
    }

    public static List<String> getPatientsForPDF() throws SQLException {
        loadDriver();
        List<String> patientIds = new ArrayList<>();

        String query= "SELECT DISTINCT patient_id FROM vitalsigns";
        String id;
        try(Connection connection = DriverManager.getConnection(url, userRoot, password);
        PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.executeQuery();
           while(resultSet.next()){
                id = resultSet.getString("patient_id");
               patientIds.add(id);
           }
    } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return patientIds;
    }
    public static List<VitalSign> getVitalsForPDF(String patientID){
        loadDriver();
        List<VitalSign> vitalSigns = new ArrayList<>();
        String id ;
        int oxygenLevel;
        double temp;
        int heartRate;
        int diastolicPressure;
        int systolicPressure;
        LocalDateTime dateTime;
        VitalSign vitalSign = null;

        String query ="SELECT * FROM vitalsigns WHERE patient_id = ? ";
        try(Connection connection = DriverManager.getConnection(url,userRoot,password);
        PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setString(1,patientID);


            ResultSet resultSet = preparedStatement.executeQuery();
           while (resultSet.next()){
               id= resultSet.getString("patient_id");
               heartRate=resultSet.getInt("heart_rate");
               oxygenLevel=resultSet.getInt("oxygen_level");
               temp = resultSet.getDouble("temperature");
               systolicPressure=resultSet.getInt("systolic");
               diastolicPressure=resultSet.getInt("diastolic");
               dateTime= resultSet.getTimestamp("recorded_at").toLocalDateTime();
                vitalSign = new VitalSign(id , oxygenLevel,temp , heartRate,diastolicPressure, systolicPressure,dateTime);
               vitalSigns.add(vitalSign);
           }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return vitalSigns;
    }






}
