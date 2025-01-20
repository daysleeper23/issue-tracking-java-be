package org.projectmanagement.presentation.controllers;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.projectmanagement.application.dto.tasks.TasksCompact;
import org.projectmanagement.application.dto.tasks.TasksCreate;
import org.projectmanagement.application.dto.tasks.TasksUpdate;
import org.projectmanagement.application.dto.tasks.TasksInfo;
import org.projectmanagement.domain.services.TaskSubscribersService;
import org.projectmanagement.domain.services.TasksService;
import org.projectmanagement.presentation.response.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/{companyId}")
@RequiredArgsConstructor
public class TasksController {

    private final TasksService tasksService;
    private final TaskSubscribersService subscribersService;

    @PreAuthorize("@permissionEvaluator.hasPermissionOnSingleResource(authentication,#projectId,{'PROJECT_UPDATE_ONE','PROJECT_UPDATE_ALL'})")
    @PostMapping("/{projectId}/tasks")
    public ResponseEntity<GlobalResponse<TasksInfo>> addTask(
            @PathVariable(name = "projectId") String projectId,
           @RequestBody @Valid TasksCreate dto
    ) {
        TasksInfo createdTask = tasksService.addTask(dto);
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.CREATED.value(), createdTask), null, HttpStatus.CREATED);
    }

    @PreAuthorize("@permissionEvaluator.hasPermissionOnSingleResource(authentication,#projectId,{'PROJECT_UPDATE_ONE','PROJECT_UPDATE_ALL'})")
    @PutMapping("/{projectId}/tasks/{taskId}")
    public ResponseEntity<GlobalResponse<TasksInfo>> updateTask(
            @PathVariable String projectId,
            @PathVariable String taskId,
            @RequestBody @Valid TasksUpdate dto
    ){
        TasksInfo updatedTask = tasksService.updateTask(taskId,dto);
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), updatedTask));
    }

    @PreAuthorize("@permissionEvaluator.hasPermissionOnSingleResource(authentication,#projectId,{'PROJECT_READ_ALL','PROJECT_READ_ONE'})")
    @GetMapping("/tasks")
    public ResponseEntity<GlobalResponse<List<TasksCompact>>> getAllTaskAssociated(

    ) {
        List<TasksCompact> listTasks = tasksService.getAllTaskByUser();
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), listTasks));
    }

    @PreAuthorize("@permissionEvaluator.hasPermissionOnSingleResource(authentication, #projectId,{'PROJECT_READ_ALL','PROJECT_READ_ONE'})")
    @GetMapping("/{workspaceId}/{projectId}/tasks")
    public ResponseEntity<GlobalResponse<List<TasksCompact>>> getTasksInProject(
            @PathVariable(value = "projectId", required = false) @org.hibernate.validator.constraints.UUID String projectId
    ) {
        List<TasksCompact> listTasks = tasksService.getAllTaskInProject(projectId);
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), listTasks));
    }

    @PreAuthorize("@permissionEvaluator.hasPermissionOnSingleResource(authentication, #workspaceId,{'WORKSPACE_READ_ALL','WORKSPACE_READ_ONE'})")
    @GetMapping("/{workspaceId}/tasks")
    public ResponseEntity<GlobalResponse<List<TasksInfo>>> getTasksInWorkspace(
        @PathVariable(value = "companyId", required = false) @org.hibernate.validator.constraints.UUID String companyId,
        @PathVariable(value = "workspaceId", required = false) @org.hibernate.validator.constraints.UUID String workspaceId
    ) {
        List<TasksInfo> listTasks = tasksService.getAllTaskInWorkspace(workspaceId);
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), listTasks));
    }

    @PreAuthorize("@permissionEvaluator.hasPermissionOnSingleResource(authentication,#projectId,{'PROJECT_READ_ALL','PROJECT_READ_ONE'})")
    @GetMapping("/{projectId}/tasks/{taskId}")
    public ResponseEntity<GlobalResponse<TasksInfo>> getTaskInfo(
            @PathVariable @NotBlank(message = "Task id must not be empty") String taskId
    ){
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), tasksService.getTaskInfo(taskId)));
    }

    @PreAuthorize("@permissionEvaluator.hasPermissionOnSingleResource(authentication,#projectId,{'PROJECT_READ_ALL','PROJECT_READ_ONE'})")
    @PutMapping("/{projectId}/tasks/{taskId}/subscribe")
    public ResponseEntity<GlobalResponse<Boolean>> subscribeToTask(
            @PathVariable @NotBlank(message = "Task id must not be empty") String taskId
    ) {

        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), subscribersService.subscribeToTask(taskId )));
    }

    @PreAuthorize("@permissionEvaluator.hasPermissionOnSingleResource(authentication,#projectId,{'PROJECT_READ_ALL','PROJECT_READ_ONE'})")
    @PutMapping("/{projectId}/tasks/{taskId}/unsubscribe")
    public ResponseEntity<GlobalResponse<Boolean>> unsubscribeToTask(
            @PathVariable @NotBlank(message = "Task id must not be empty") String taskId
    ) {
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), subscribersService.unsubscribeToTask(taskId)));
    }

    @PreAuthorize("@permissionEvaluator.hasPermissionOnSingleResource(authentication,#projectId,{'PROJECT_UPDATE_ONE','PROJECT_UPDATE_ALL'})")
    @DeleteMapping("/{projectId}/tasks/{taskId}")
    public ResponseEntity<GlobalResponse<Boolean>> archiveTask(
            @PathVariable @NotBlank(message = "Task id must not be empty") String taskId
    ) {
        boolean isArchived = tasksService.archiveTasks(taskId);
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), isArchived));
    }
}
