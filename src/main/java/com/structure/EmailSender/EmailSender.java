package com.structure.EmailSender;


import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;

public class EmailSender {

    //a static method to send email
    public static void sendEmail(String to, String subject, String messageText) {
        final String fromEmail = "";  // Your Gmail
        final String password = "";       // Gmail App Password

        // Create properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Create session
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            // Compose message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(messageText);

            // Send message
            Transport.send(message);
            System.out.println("📧 Email sent successfully to: " + to);

        } catch (MessagingException e) {
            System.out.println("❌ Failed to send email.");
            e.printStackTrace();
        }
    }
}
