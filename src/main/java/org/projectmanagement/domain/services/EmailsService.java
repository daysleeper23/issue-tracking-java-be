package org.projectmanagement.domain.services;

import org.projectmanagement.application.dto.mail.InvitationMailBody;
import org.springframework.web.util.UriBuilder;

public interface EmailsService {

    void sendInvitationEmail(String recipientName, UriBuilder uriBuilder, InvitationMailBody mailBody);
}
