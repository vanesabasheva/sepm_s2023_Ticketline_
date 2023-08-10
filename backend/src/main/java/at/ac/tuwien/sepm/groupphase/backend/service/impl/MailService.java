package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.Properties;

@Service
public class MailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final JavaMailSenderImpl mailSender;

    public MailService() {
        this.mailSender = new JavaMailSenderImpl();
        this.mailSender.setHost("smtp.gmail.com");
        this.mailSender.setPort(587);

        this.mailSender.setUsername("sepmtest45@gmail.com");
        this.mailSender.setPassword("gorzabomffavsruu"); //app password

        // Enable STARTTLS
        Properties props = this.mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "false");

        // You can also configure other properties if needed, e.g., SSL/TLS settings

        this.mailSender.setJavaMailProperties(props);
    }

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public void sendPasswordResetEmail(ApplicationUser user, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        String userEmail = user.getEmail();
        message.setTo(userEmail);
        message.setSubject("Reset Password");
        String text =
            "Only valid for 15 minutes!!! \n"
                + "Click here to reset password: \n"
                + "http://localhost:4200/#/reset/" + token;
        message.setText(text);
        mailSender.send(message);
    }
}