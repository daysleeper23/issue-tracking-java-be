package org.projectmanagement.presentation.controllers;


import jakarta.validation.Valid;
import org.projectmanagement.application.dto.tasks.TaskDTO;
import org.projectmanagement.domain.entities.Tasks;
import org.projectmanagement.domain.services.TaskService;
import org.projectmanagement.presentation.response.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<GlobalResponse<Tasks>> createTask(
           @RequestBody @Valid TaskDTO dto) {
        Tasks createdTask = taskService.addTask(dto);
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.CREATED.value(), createdTask), null, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<GlobalResponse<List<Tasks>>> getTasks(
            @RequestBody RequestTasksFromProject requestTasks
    ) {
        List<Tasks> listTasks = taskService.getAllTask(requestTasks.projectId());
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), listTasks));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<GlobalResponse<Boolean>> archiveTask(
            @PathVariable String taskId
    ) {
        boolean isArchived = taskService.archiveTasks(taskId);
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), isArchived));
    }

    public record RequestTasksFromProject(String projectId) {
    }
}
