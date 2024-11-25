package org.projectmanagement.application.dto.mail;

public record InvitationMailBody(String companyName,
                                 String token,
                                 String workspaceName,
                                 String roleName,
                                 boolean isAdmin,
                                 long timestamp
                                 ) {
}
