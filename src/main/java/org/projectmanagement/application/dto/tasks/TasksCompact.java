package org.projectmanagement.application.dto.tasks;


public record TasksCompact(String name, String status, String assigneeId,short priority) {
}
