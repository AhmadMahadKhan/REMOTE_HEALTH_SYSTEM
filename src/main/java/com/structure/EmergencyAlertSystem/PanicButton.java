package com.structure.EmergencyAlertSystem;

import com.structure.DataAccessObject.DAOAdministrator;
import com.structure.Model.Doctor;
import com.structure.Model.Patient;

import java.util.ArrayList;
import java.util.List;

public class PanicButton {

    public static void panicButton(Patient patient) {

        List<Doctor> doctors = DAOAdministrator.viewAllDoctors();
        List<String> emails = new ArrayList<>();
        for(Doctor doctor : doctors){
            emails.add(doctor.getEmail());
        }
        emails.add(patient.getEmergencyEmail());
        for(String email : emails){

            NotificationService.sendEmergencyAlerts(
                    email,"PANIC ATTACK",
                    "Patient with id " + patient.getId() +
                            " has a PANIC ATTACK. Please send rescue service");
        }

    }

}
