package org.projectmanagement.application.exceptions;

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
    COMPANY_CREATION_FAILED("Error creating company"),
    //Invitation errors
    INVITATION_NOT_FOUND("Invitation not found"),
    INVITATION_ALREADY_SENT("Invitation already sent"),
    INVITATION_STILL_VALID("Invitation still valid"),
    //Mail Service errors
    MAIL_SEND_ERROR("Error sending mail"),
    //Workspace errors
    WORKSPACE_NOT_FOUND("Workspace not found"),

    //Workspace Member Role errors
    WMR_ROLE_NOT_FOUND("Role not found for user in workspace"),
    WRM_USER_ALREADY_IN_WORKSPACE("User already in workspace"),
    WMR_INVALID_WORKSPACE("Invalid workspace for workspace members"),
    WMR_INVALID_USER("Invalid user for workspace members"),
    WMR_INVALID_ROLE("Invalid role for workspace members (should be Member or Workspace Manager)"),
    WMR_UPDATE_ERROR("Error updating role for member in workspace"),

    //Project errors
    USER_NOT_IN_PROJECT("User not in project"),
    PROJECT_NOT_FOUND("Project not found"),
    PROJECT_DELETE_FAILED("Error deleting project"),
    //Role errors
    ROLE_NOT_FOUND("Role not found"),

    //User errors
    USER_NOT_FOUND("User not found"),
    USER_ALREADY_JOINED_COMPANY("User already joined a company"),
    EMAIL_ALREADY_IN_USE("Email already in use"),;
    final String message;
}
