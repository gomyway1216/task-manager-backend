package com.yudaiyaguchi.taskmanager.controller;

import com.yudaiyaguchi.taskmanager.dto.TaskNameIdDTO;
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

import java.util.List;

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

    @GetMapping("/user/{userId}/all")
    public ResponseEntity<List<TaskNameIdDTO>> getAllTasks(@PathVariable String userId) {
        List<TaskNameIdDTO> tasks = taskService.getTasksByUserId(userId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/user/{userId}/task/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable String userId, @PathVariable Long taskId) {
        Task task = taskService.getTaskByIdAndUserId(userId, taskId);
        if (task == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(task);
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody TaskRequest taskRequest) {
        if (taskRequest.getUserId() == null || taskRequest.getTitle() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Task createdTask = taskService.createTask(taskRequest);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody TaskRequest taskRequest) {
        if (taskRequest.getUserId() == null || taskRequest.getTitle() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Task updatedTask = taskService.updateTask(id, taskRequest);
        if (updatedTask == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(updatedTask);
    }



    @DeleteMapping("/user/{userId}/task/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, @PathVariable String userId) {
        taskService.deleteTask(id, userId);
        return ResponseEntity.noContent().build();
    }
}
