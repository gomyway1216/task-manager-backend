package com.yudaiyaguchi.taskmanager.controller;

import com.yudaiyaguchi.taskmanager.request.TaskRequest;
import com.yudaiyaguchi.taskmanager.model.Task;
import com.yudaiyaguchi.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<Task>> getAllTasks(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        Page<Task> tasks = taskService.getTasksByUserId(
                userId, PageRequest.of(page, size, Sort.by(direction, sortBy)));
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/user/{userId}/task/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable String userId, @PathVariable Long taskId) {
        Task task = taskService.getTaskByIdAndUserId(userId, taskId);
        return ResponseEntity.ok(task);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody TaskRequest taskRequest) {
        Task createdTask = taskService.createTask(taskRequest);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequest taskRequest) {
        return ResponseEntity.ok(taskService.updateTask(id, taskRequest));
    }

    @DeleteMapping("/user/{userId}/tag/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, @PathVariable String userId) {
        taskService.deleteTask(id, userId);
        return ResponseEntity.noContent().build();
    }
}
