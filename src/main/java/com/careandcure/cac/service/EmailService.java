package com.careandcure.cac.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;



@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Send confirmation email when an appointment is created
    public void sendAppointmentConfirmationEmail(String patientEmail, String patientName, String doctorName,
                                                 String appointmentDate, String appointmentTime) throws MessagingException {
        String subject = "Appointment Confirmation";
        String body = "<h3>Your appointment is confirmed</h3>" +
                "<p>Dear " + patientName + ",</p>" +
                "<p>Your appointment with Dr. " + doctorName + " is confirmed on " +
                appointmentDate + " at " + appointmentTime + ".</p>" +
                "<p>Thank you for choosing our service.</p>";
        sendEmail(patientEmail, subject, body);
    }

    // Send cancellation email when an appointment is canceled
    public void sendAppointmentCancellationEmail(String patientEmail, String patientName, String doctorName,
                                                 String appointmentDate, String appointmentTime, String reason) throws MessagingException {
        String subject = "Appointment Cancellation";
        String body = "<h3>Your appointment has been cancelled</h3>" +
                "<p>Dear " + patientName + ",</p>" +
                "<p>We regret to inform you that your appointment with Dr. " + doctorName + " scheduled for " +
                appointmentDate + " at " + appointmentTime + " has been cancelled.</p>" +
                "<p>Reason: " + reason + "</p>" +
                "<p>We apologize for the inconvenience caused.</p>";
        sendEmail(patientEmail, subject, body);
    }

    // General method to send email
    private void sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        try {
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // True to allow HTML content
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send email");
        }
    }
}
