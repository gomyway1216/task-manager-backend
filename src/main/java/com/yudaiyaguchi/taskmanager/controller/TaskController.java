package com.yudaiyaguchi.taskmanager.controller;

import com.yudaiyaguchi.taskmanager.dto.TaskNameIdDTO;
import com.yudaiyaguchi.taskmanager.model.Status;
import com.yudaiyaguchi.taskmanager.specification.criteria.SearchCriteria;
import com.yudaiyaguchi.taskmanager.specification.TaskSpecification;
import com.yudaiyaguchi.taskmanager.request.TaskRequest;
import com.yudaiyaguchi.taskmanager.model.Task;
import com.yudaiyaguchi.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
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
            @RequestParam(defaultValue = "ASC") Sort.Direction direction,
            @RequestParam(required = false) String tagId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String timeDueBefore,
            @RequestParam(required = false) String parentId
    ) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Specification<Task> spec = Specification.where(null);

        if(userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        spec = spec.and(new TaskSpecification(new SearchCriteria("userId", ":", userId)));

        if (tagId != null) {
            spec = spec.and(new TaskSpecification(new SearchCriteria("tagId", ":", tagId)));
        }
        if (status != null) {
            spec = spec.and(new TaskSpecification(new SearchCriteria("status", ":", Status.valueOf(status))));
        }
        if (timeDueBefore != null) {
            spec = spec.and(new TaskSpecification(new SearchCriteria("timeDue", "<", Instant.parse(timeDueBefore))));
        }
        if (parentId != null) {
            spec = spec.and(new TaskSpecification(new SearchCriteria("parent", ":", parentId)));
        }

        Page<Task> tasks = taskService.getTasksWithFilters(spec, pageable);
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
