package org.projectmanagement.application.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.projectmanagement.application.exceptions.ApplicationException;
import org.projectmanagement.domain.services.EmailsService;
import org.springframework.beans.factory.annotation.Value;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

@RequiredArgsConstructor
@Service
public class EmailsServiceImpl implements EmailsService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    private static final String EMAIL_HTML_TEMPLATE_NAME = "html/email-template.html";

    private final JavaMailSender mailSender;

    private final TemplateEngine htmlTemplateEngine;

    @Async
    @Override
    public void sendEmail(String recipientName, String subject, String message) {
        // Send email
        try {
            Context ctx = new Context();
            ctx.setVariable("name", recipientName);
            ctx.setVariable("subscriptionDate", new Date());
            ctx.setVariable("hobbies", Arrays.asList("Cinema", "Sports", "Music"));

            // Prepare message using a Spring helper
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mineMessage
                    = new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");
            mineMessage.setSubject("Example HTML email");
            mineMessage.setFrom(fromEmail);
            mineMessage.setTo(recipientName);

            // Create the HTML body using Thymeleaf
            String htmlContent = htmlTemplateEngine.process(EMAIL_HTML_TEMPLATE_NAME, ctx);
            mineMessage.setText(htmlContent, true /* isHtml */);

            // Send mail
            this.mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new ApplicationException("Failed to send email");
        }
    }
}
