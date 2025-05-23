package com.structure.EmergencyAlertSystem;

import com.structure.EmailSender.EmailSender;

public class NotificationService {

    public static  void sendEmergencyAlerts(String email, String subject, String message) {

            EmailSender.sendEmail(email, subject, message);


    }
}
