package org.projectmanagement.application.services;

import lombok.RequiredArgsConstructor;
import org.projectmanagement.domain.services.EmailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailsServiceImpl implements EmailsService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendEmail(String toEmail, String subject, String message) {
        // Send email
        SimpleMailMessage prepareMail = new SimpleMailMessage();
        prepareMail.setFrom(fromEmail);
        prepareMail.setTo(toEmail);
        prepareMail.setSubject(subject);
        prepareMail.setText(message);

        mailSender.send(prepareMail);
    }
}
