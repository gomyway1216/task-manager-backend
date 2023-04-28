package com.yudaiyaguchi.taskmanager.controller;

import com.yudaiyaguchi.taskmanager.model.Task;
import com.yudaiyaguchi.taskmanager.request.TaskRequest;
import com.yudaiyaguchi.taskmanager.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskService taskService;

    @Test
    public void testGetAllTasks() {
        Task task = new Task();
        task.setId(1L);
        task.setUserId("123");
        task.setTitle("Test Task");

        List<Task> tasks = Collections.singletonList(task);
        Page<Task> taskPage = new PageImpl<>(tasks);

        when(taskService.getTasksWithFilters(any(), any())).thenReturn(taskPage);

        ResponseEntity<Page<Task>> response = taskController.getAllTasks("123", 0, 10, "id",
                Sort.Direction.ASC, null, null, null,  null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getContent().size());
    }

    @Test
    public void testGetTaskById() {
        Task task = new Task();
        task.setId(1L);
        task.setUserId("123");
        task.setTitle("Test Task");

        when(taskService.getTaskByIdAndUserId("123", 1L)).thenReturn(task);

        ResponseEntity<Task> response = taskController.getTaskById("123", 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test Task", response.getBody().getTitle());
    }

    @Test
    public void testGetTaskByIdNotFound() {
        when(taskService.getTaskByIdAndUserId("123", 1L)).thenReturn(null);

        ResponseEntity<Task> response = taskController.getTaskById("123", 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCreateTask() {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setUserId("123");
        taskRequest.setTitle("Test Task");

        Task task = new Task();
        task.setId(1L);
        task.setUserId("123");
        task.setTitle("Test Task");

        when(taskService.createTask(taskRequest)).thenReturn(task);

        ResponseEntity<Task> response = taskController.createTask(taskRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Test Task", response.getBody().getTitle());
    }

    @Test
    public void testCreateTaskInvalidInput() {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setUserId(null);
        taskRequest.setTitle(null);

        ResponseEntity<Task> response = taskController.createTask(taskRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testCreateTaskMissingUserId() {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setTitle("New Task");

        ResponseEntity<Task> response = taskController.createTask(taskRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testCreateTaskMissingTitle() {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setUserId("123");

        ResponseEntity<Task> response = taskController.createTask(taskRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testUpdateTask() {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setUserId("123");
        taskRequest.setTitle("Updated Task");

        Task task = new Task();
        task.setId(1L);
        task.setUserId("123");
        task.setTitle("Updated Task");

        when(taskService.updateTask(1L, taskRequest)).thenReturn(task);

        ResponseEntity<Task> response = taskController.updateTask(1L, taskRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Task", response.getBody().getTitle());
    }

    @Test
    public void testUpdateTaskNotFound() {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setUserId("123");
        taskRequest.setTitle("Updated Task");

        when(taskService.updateTask(1L, taskRequest)).thenReturn(null);

        ResponseEntity<Task> response = taskController.updateTask(1L, taskRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateTaskInvalidUserId() {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setUserId(null);
        taskRequest.setTitle("Updated Task");

        ResponseEntity<Task> response = taskController.updateTask(1L, taskRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testUpdateTaskInvalidTitle() {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setUserId("123");
        taskRequest.setTitle(null);

        ResponseEntity<Task> response = taskController.updateTask(1L, taskRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testDeleteTask() {
        ResponseEntity<Void> response = taskController.deleteTask(1L, "123");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}


