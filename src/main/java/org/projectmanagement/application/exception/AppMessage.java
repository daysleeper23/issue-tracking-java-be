package org.projectmanagement.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppMessage {
    //Task errors
    TASK_NOT_FOUND("Task not found"),
    TASK_NO_CHANGE("No change was made for task"),
    TASK_SUBSCRIBE_ERROR("Error subscribing to task"),
    ;
    final String message;
}
