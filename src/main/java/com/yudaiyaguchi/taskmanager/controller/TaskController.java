package com.yudaiyaguchi.taskmanager.controller;

import com.yudaiyaguchi.taskmanager.dto.TaskRequest;
import com.yudaiyaguchi.taskmanager.dto.TaskWithTagsDTO;
import com.yudaiyaguchi.taskmanager.model.Task;
import com.yudaiyaguchi.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

//    @GetMapping("/user/{userId}")
//    public ResponseEntity<Page<Task>> getAllTasks(
//            @PathVariable String userId,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "id") String sortBy,
//            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
////        return taskService.getAllTasksByUserId(PageRequest.of(page, size, Sort.by(direction, sortBy)));
//        Page<Task> tasks = taskService.getAllTasksByUserId(
//                userId, PageRequest.of(page, size, Sort.by(direction, sortBy)));
//        return ResponseEntity.ok(tasks);
//    }

        @GetMapping("/user/{userId}")
    public ResponseEntity<Page<TaskWithTagsDTO>> getAllTasks(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
//        return taskService.getAllTasksByUserId(PageRequest.of(page, size, Sort.by(direction, sortBy)));
        Page<TaskWithTagsDTO> tasks = taskService.getTasksByUserId(
                userId, PageRequest.of(page, size, Sort.by(direction, sortBy)));
        return ResponseEntity.ok(tasks);
    }

//    @GetMapping("/user/{userId}")
//    public ResponseEntity<Page<TaskWithTagsDTO>> getTasksByUserId(@PathVariable String userId, Pageable pageable) {
//        Page<Task> tasks = taskRepository.findByUserId(userId, pageable);
//        Page<TaskWithTagsDTO> taskWithTagsDTOs = tasks.map(this::convertToTaskWithTagsDTO);
//        return ResponseEntity.ok(taskWithTagsDTOs);
//    }

    @GetMapping("/user/{userId}/tag/{tagId}")
    public ResponseEntity<Page<Task>> getAllTasksByUserIdAndTagId(
            @PathVariable String userId,
            @PathVariable Long tagId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        Page<Task> tasks = taskService.getAllTasksByUserIdAndTagId(
                userId, tagId, PageRequest.of(page, size, Sort.by(direction, sortBy)));
        return ResponseEntity.ok(tasks);
    }

//    @GetMapping("/tags/{tagName}")
//    public Page<Task> getTasksByTagName(
//            @PathVariable String tagName,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "id") String sortBy,
//            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
//        return taskService.getTasksByTagName(tagName, PageRequest.of(page, size, Sort.by(direction, sortBy)));
//    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Optional<Task> task = taskService.getTaskById(id);
        return task.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody TaskRequest taskRequest) {
        Task createdTask = taskService.createTask(taskRequest);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @Valid @RequestBody TaskRequest taskRequest) {
        return ResponseEntity.ok(taskService.updateTask(taskId, taskRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        Optional<Task> existingTask = taskService.getTaskById(id);
        if (existingTask.isPresent()) {
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
