package org.projectmanagement.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppMessage {
    //Task errors
    TASK_NOT_FOUND("Task not found"),
    TASK_SUBSCRIBE_ERROR("Error subscribing to task"),
    //Common errors
    NO_CHANGE("No change was made"),
    INVALID_STATUS("Invalid status value"),
    INVALID_UUID("Invalid UUID"),
    //Severe errors
    INTERNAL_ERROR("Internal server error"),
    //Company
    COMPANY_NOT_FOUND("Company not found");
    final String message;
}
