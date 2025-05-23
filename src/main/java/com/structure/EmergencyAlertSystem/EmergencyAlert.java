package com.structure.EmergencyAlertSystem;

import com.structure.AppointmentScheduling.AppointmentManager;
import com.structure.Exception.NoAppointmentException;
import com.structure.HealthDataHandling.VitalSign;
import com.structure.Model.Patient;

import java.sql.SQLException;
import java.util.List;

public class EmergencyAlert {



    public EmergencyAlert(){
    }


    public static boolean alert(Patient patient, VitalSign vitalSign) throws SQLException, NoAppointmentException {
        System.out.println("in alert method in emergency alert class");

        String patientId = patient.getId();
        int oxygenLevel = vitalSign.getOxygenLevel();
        double temp = vitalSign.getTemp();
        int heartRate = vitalSign.getHeartRate();
        int diastolicPressure = vitalSign.getDiastolicPressure();
        int systolicPressure = vitalSign.getSystolicPressure();

        boolean isEmergency = oxygenLevel < 90 ||
                temp > 39.0 || temp < 35.0 ||
                heartRate > 120 || heartRate < 50 ||
                systolicPressure > 180 || systolicPressure < 90 ||
                diastolicPressure > 120 || diastolicPressure < 60;

        if (isEmergency) {
            List<String> emergencyEmails = AppointmentManager.getEmergencyEmails(patientId);
            if (emergencyEmails.isEmpty()) {
                throw new NoAppointmentException("No doctor has been assigned to this patient.");
            }

            // Add patient's emergency email if not null/empty
            String patientEmergencyEmail = patient.getEmergencyEmail();
            if (patientEmergencyEmail != null && !patientEmergencyEmail.isBlank()) {
                emergencyEmails.add(patientEmergencyEmail);
            }

            // Send alert
            String subject = "Patient with abnormal vitals.";
            String message = "Patient with ID " + patientId + " has abnormal vitals. Please send rescue service.";

            for (String email : emergencyEmails) {
                NotificationService.sendEmergencyAlerts(email, subject, message);
            }
        }

        return isEmergency;
    }

}
