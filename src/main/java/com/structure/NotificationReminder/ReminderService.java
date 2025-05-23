package com.structure.NotificationReminder;

import com.structure.EmailSender.EmailSender;

public class ReminderService {

    public static void sendAppointmentReminders(String email, String subject, String message) {


        EmailSender.sendEmail(email, subject, message);


    }
}
