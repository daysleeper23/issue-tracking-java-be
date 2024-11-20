package org.projectmanagement.domain.services;

public interface EmailsService {
    void sendEmail(String to, String subject, String text);
}
