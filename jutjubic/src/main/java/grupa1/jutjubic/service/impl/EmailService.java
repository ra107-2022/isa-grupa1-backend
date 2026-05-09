package grupa1.jutjubic.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String recipientEmail, String verificationLink) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("mejl.za.projekte.kojov@gmail.com");
        
        message.setTo(recipientEmail);
        message.setSubject("Complete your Registration");
        message.setText("Welcome to our platform! \n\n" +
                "Please click the link below to verify your account:\n" +
                verificationLink + "\n\n" +
                "This link will expire in 24 hours.");

        mailSender.send(message);
    }
}
