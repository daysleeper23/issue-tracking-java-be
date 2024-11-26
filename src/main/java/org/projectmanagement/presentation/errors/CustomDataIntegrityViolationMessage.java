package org.projectmanagement.presentation.errors;

import org.hibernate.exception.ConstraintViolationException;

import java.util.HashMap;
import java.util.Map;

public class CustomDataIntegrityViolationMessage {

    final static Map<String, String> constraintMessageMap = new HashMap<>(){{
        put("uc_companies_name", "Company name already exist");
        put("uc_cm_member_role", "User already a company manager");
        put("uc_invitations_email", "Invitation already sent to this email");
        put("uc_project_members", "User already a member of this project");
        put("uc_role_name", "Role Name already exist");
        put("uc_tasks_name", "Task name already exist");
        put("uc_rp_permission", "Permission already assigned to this role");
        put("uc_tasks_subscribers", "User already subscribed to this task");
        put("uc_user_email", "User email already exist");
        put("uc_workspace_name", "Workspace name already exist");
        put("uc_workspace_member_role", "User already a in this workspace or role already assigned to this user");
    }};


    public static String getMessage(Throwable ex) {
        String constraintName = ((ConstraintViolationException) ex).getConstraintName();
        String defaultMessage = ((ConstraintViolationException) ex).getSQLException().getMessage();
        return constraintMessageMap.getOrDefault(constraintName, defaultMessage);
    }
}
