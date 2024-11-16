package org.projectmanagement.presentation.controllers;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.projectmanagement.application.dto.tasks.TasksCompact;
import org.projectmanagement.application.dto.tasks.TasksCreate;
import org.projectmanagement.application.dto.tasks.TasksUpdate;
import org.projectmanagement.application.dto.tasks.TaskInfo;
import org.projectmanagement.domain.entities.Tasks;
import org.projectmanagement.domain.services.TaskSubscribersService;
import org.projectmanagement.domain.services.TasksService;
import org.projectmanagement.presentation.response.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/{companyId}/tasks")
@Validated
//Todo: Roles and Privileges check
public class TasksController {

    private final TasksService tasksService;
    private final TaskSubscribersService subscribersService;

    public TasksController(TasksService tasksService,
                           TaskSubscribersService subscribersService) {
        this.tasksService = tasksService;
        this.subscribersService = subscribersService;
    }

    @PostMapping
    public ResponseEntity<GlobalResponse<Tasks>> addTask(
           @RequestBody @Valid TasksCreate dto) {
        Tasks createdTask = tasksService.addTask(dto);
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.CREATED.value(), createdTask), null, HttpStatus.CREATED);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<GlobalResponse<Tasks>> updateTask(
            @PathVariable String taskId,
            @RequestBody @Valid TasksUpdate dto
    ){
        Tasks updatedTask = tasksService.updateTask(taskId,dto);
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), updatedTask));
    }

    @GetMapping
    public ResponseEntity<GlobalResponse<List<TasksCompact>>> getTasks(
            @RequestBody RequestTasksFromProject requestTasks
    ) {
        List<TasksCompact> listTasks = tasksService.getAllTask(requestTasks.projectId());
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), listTasks));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<GlobalResponse<TaskInfo>> getTaskInfo(
            @PathVariable @NotBlank(message = "Task id must not be empty") String taskId
    ){
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), tasksService.getTaskInfo(taskId)));
    }

    @PutMapping("/{taskId}/subscribe")
    public ResponseEntity<GlobalResponse<Boolean>> subscribeToTask(
            @PathVariable @NotBlank(message = "Task id must not be empty") String taskId
    ) {
        //Todo:Get user id from security context holder
        String userId = UUID.randomUUID().toString();
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), subscribersService.subscribeToTask(taskId, userId)));
    }

    @PutMapping("/{taskId}/unsubscribe")
    public ResponseEntity<GlobalResponse<Boolean>> unsubscribeToTask(
            @PathVariable @NotBlank(message = "Task id must not be empty") String taskId,
            //Todo:For testing remove later
            @RequestParam(value = "userId") @NotBlank(message = "User id must not be empty") String userId
    ) {
        //Todo:Get user id from security context holder
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), subscribersService.unsubscribeToTask(taskId, userId)));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<GlobalResponse<Boolean>> archiveTask(
            @PathVariable @NotBlank(message = "Task id must not be empty") String taskId
    ) {
        boolean isArchived = tasksService.archiveTasks(taskId);
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), isArchived));
    }

    public record RequestTasksFromProject(String projectId) {
    }
}
