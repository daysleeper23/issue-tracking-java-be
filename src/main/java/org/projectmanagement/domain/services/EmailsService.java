package org.projectmanagement.domain.services;

import org.projectmanagement.application.dto.mail.InvitationMailBody;

public interface EmailsService {

    void sendInvitationEmail(String recipientName, InvitationMailBody mailBody);
}
