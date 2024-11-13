package org.projectmanagement.presentation.controllers;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.projectmanagement.application.dto.tasks.TaskDTO;
import org.projectmanagement.domain.entities.Tasks;
import org.projectmanagement.domain.services.TaskService;
import org.projectmanagement.presentation.response.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("{companyId}/tasks")
@Validated
public class TasksController {
    private final TaskService taskService;

    public TasksController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<GlobalResponse<Tasks>> addTask(
           @RequestBody @Valid TaskDTO dto) {
        Tasks createdTask = taskService.addTask(dto);
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.CREATED.value(), createdTask), null, HttpStatus.CREATED);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<GlobalResponse<Tasks>> updateTask(
            @PathVariable String taskId,
            @RequestBody @Valid TaskDTO dto
    ){
        Tasks updatedTask = taskService.updateTask(taskId,dto);
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), updatedTask));
    }

    @GetMapping
    public ResponseEntity<GlobalResponse<List<Tasks>>> getTasks(
            @RequestBody RequestTasksFromProject requestTasks
    ) {
        List<Tasks> listTasks = taskService.getAllTask(requestTasks.projectId());
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), listTasks));
    }

//    @GetMapping("/{taskId}")
//    public ResponseEntity<GlobalResponse<Tasks>> getTaskInfo(
//            @PathVariable @NotBlank(message = "Task id must not be empty") String taskId
//    ){
//        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), taskService.getTaskInfo(taskId)));
//    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<GlobalResponse<Boolean>> archiveTask(
            @PathVariable @NotBlank(message = "Task id must not be empty") String taskId
    ) {
        boolean isArchived = taskService.archiveTasks(taskId);
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), isArchived));
    }

    public record RequestTasksFromProject(String projectId) {
    }
}
