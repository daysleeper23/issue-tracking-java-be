package org.projectmanagement.application;


import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.*;
import jakarta.mail.internet.ContentType;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.mail2.jakarta.util.MimeMessageParser;
import org.apache.commons.mail2.jakarta.util.MimeMessageParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.projectmanagement.application.dto.mail.InvitationMailBody;
import org.projectmanagement.domain.services.EmailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;


import java.io.IOException;
import java.time.Instant;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;


@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class EmailServiceIntegrationTest {

    @Autowired
    private EmailsService emailService;

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP_IMAP)
            .withConfiguration(GreenMailConfiguration
                    .aConfig()
                    .withUser("email_integration_test@localhost","tester", "password"))
            .withPerMethodLifecycle(false);


    // Test methods
    @Test
    void testSendEmail() throws MessagingException {
        InvitationMailBody invitationMailBody = new InvitationMailBody("Test company",
                "Test workspace", "Test user",
                "Test rolename",false, Instant.now().toEpochMilli());
        UriBuilder uriBuilder = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8080)
                .path("/invitation");
        emailService.sendInvitationEmail("email_integration_test@localhost",uriBuilder ,invitationMailBody);
        greenMail.waitForIncomingEmail(1);

        await().atMost(60*60, SECONDS).untilAsserted(() -> {
            MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
            assertEquals(1, receivedMessages.length);
            MimeMessage receivedMessage = receivedMessages[0];
            assertEquals("Invitation to join project management system", receivedMessage.getSubject());
            assertEquals("email_integration_test@localhost ", receivedMessage.getFrom());

        });
    }

}
