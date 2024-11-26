package org.projectmanagement.application.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.projectmanagement.application.dto.mail.InvitationMailBody;
import org.projectmanagement.application.exceptions.AppMessage;
import org.projectmanagement.application.exceptions.ApplicationException;
import org.projectmanagement.domain.services.EmailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriBuilder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
@Service
public class EmailsServiceImpl implements EmailsService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    private static final String EMAIL_HTML_TEMPLATE_NAME = "html/email-template.html";

    private static final String VERIFY_URL = "/verify";

    private final JavaMailSender mailSender;

    private final TemplateEngine htmlTemplateEngine;

    @Override
    @Async("taskExecutor")
    public void sendInvitationEmail(String recipientName, UriBuilder uriBuilder, InvitationMailBody mailBody) {
        // Send email
        log.info("Sending email to: {}", recipientName);
        try {
            Context ctx = new Context();
            ctx.setVariable("companyName", mailBody.companyName());
            ctx.setVariable("workspaceName", mailBody.workspaceName());
            ctx.setVariable("roleId",  mailBody.workspaceName() );

            URI path = uriBuilder.path(VERIFY_URL)
                    .queryParam("token",mailBody.token())
                    .queryParam("timestamp",mailBody.timestamp())
                    .build();
            // Prepare message using a Spring helper
            ctx.setVariable("verifyUrl", path.toString());
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mineMessage
                    = new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");
            mineMessage.setSubject("Invitation to join project management system");
            mineMessage.setFrom(fromEmail);
            mineMessage.setTo(recipientName);

            // Create the HTML body using Thymeleaf
            String htmlContent = htmlTemplateEngine.process(EMAIL_HTML_TEMPLATE_NAME, ctx);
            mineMessage.setText(htmlContent, true /* isHtml */);

            // Send mail
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Error while sending email", e);
        }
    }
}
