package org.projectmanagement.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppMessage {
    //Severe errors
    INTERNAL_ERROR("Internal server error"),
    //Task errors
    TASK_NOT_FOUND("Task not found"),
    TASK_SUBSCRIBE_ERROR("Error subscribing to task"),
    //Common errors
    NO_CHANGE("No change was made"),
    INVALID_STATUS("Invalid status value"),
    INVALID_UUID("Invalid UUID"),
    //Company
    COMPANY_NOT_FOUND("Company not found"),
    //Invitation errors
    INVITATION_NOT_FOUND("Invitation not found"),
    INVITATION_ALREADY_SENT("Invitation already sent"),
    INVITATION_STILL_VALID("Invitation still valid");
    final String message;
}
