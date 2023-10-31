package com.movsoftware.blockhouse.route_tracker.notification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class NotificationService {

    @Value("${main.server.url}")
    private String mainServerUrl;

    private JavaMailSender javaMailSender;

    public NotificationService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public boolean sendInvitation(String email, String invite_code, String organization_name) {
        try {
            System.out.println("Sending Notification");
            MimeMessage mail = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(email);
            helper.setSubject("Invitation to Blockhouse App");

            String link = mainServerUrl + "/login/register?invite_code=" + invite_code;

            String htmlText = "<h1>Welcome to Blockhouse!</h1>"
                    + "<p>You have been invited by " + organization_name
                    + " to join the Blockhouse App. Please use the following link to register:</p>"
                    + "<a href=\"" + link + "\">Register Here</a>"
                    + "<p>Please note invitation code will expire 72 hours after time of receipt.</p>"
                    + "<p>Thankyou</p>";

            helper.setText(htmlText, true); // true signifies that this is HTML email

            javaMailSender.send(mail);
            return true; // email sent successfully
        } catch (MessagingException e) {
            // log the exception
            System.out.println("Failed to send email: " + e.getMessage());
            return false; // email sending failed
        }
    }

}
