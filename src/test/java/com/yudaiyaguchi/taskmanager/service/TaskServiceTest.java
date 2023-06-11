package com.yudaiyaguchi.taskmanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.yudaiyaguchi.taskmanager.model.Status;
import com.yudaiyaguchi.taskmanager.model.Task;
import com.yudaiyaguchi.taskmanager.repository.TaskRepository;
import com.yudaiyaguchi.taskmanager.repository.TagRepository;
import com.yudaiyaguchi.taskmanager.request.TaskRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TagRepository tagRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    private Task createTask(Long id, String title) {
        Task task = new Task();
        task.setId(id);
        task.setTitle(title);
        task.setUserId("user-1");
        return task;
    }

    private TaskRequest createTaskRequest(String title) {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setUserId("user-1");
        taskRequest.setTitle(title);
        return taskRequest;
    }

    private TaskRequest createTaskRequest(String userId, String title, Instant timeDue, String description, String link, String appName, Long parentId, List<Long> childrenIds, List<Long> tagIds) {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setUserId(userId);
        taskRequest.setTitle(title);
        taskRequest.setTimeDue(timeDue);
        taskRequest.setDescription(description);
        taskRequest.setLink(link);
        taskRequest.setAppName(appName);
        taskRequest.setParentId(parentId);
        taskRequest.setChildrenIds(childrenIds);
        taskRequest.setTagIds(tagIds);
        return taskRequest;
    }

    @Test
    public void testCreateTask() {
        Task task = createTask(1L, "Task 1");
        TaskRequest taskRequest = createTaskRequest("Task 1");

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task createdTask = taskService.createTask(taskRequest);

        assertEquals(task.getId(), createdTask.getId());
        assertEquals(task.getTitle(), createdTask.getTitle());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    public void testCreateTask_withoutParentAndChildren() {
        TaskRequest taskRequest = createTaskRequest("user-1", "Task 1", Instant.now(), "Description", "Link", "App", null, Collections.emptyList(), Collections.emptyList());
        Task task = new Task();
        BeanUtils.copyProperties(taskRequest, task);
        task.setId(1L);

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task result = taskService.createTask(taskRequest);

        assertEquals("Task 1", result.getTitle());
        assertNull(result.getParent());
        assertTrue(result.getChildren().isEmpty());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    public void testCreateTask_withParentAndChildren() {
        Task parentTask = createTask(1L, "Parent Task");
        Task childTask1 = createTask(2L, "Child Task 1");
        Task childTask2 = createTask(3L, "Child Task 2");

        TaskRequest taskRequest = createTaskRequest("user-1", "Task 1", Instant.now(), "Description", "Link", "App", 1L, Arrays.asList(2L, 3L), Collections.emptyList());

        when(taskRepository.findByIdAndUserId(1L, "user-1")).thenReturn(parentTask);
        when(taskRepository.findByIdAndUserId(2L, "user-1")).thenReturn(childTask1);
        when(taskRepository.findByIdAndUserId(3L, "user-1")).thenReturn(childTask2);
        when(taskRepository.save(any(Task.class))).thenAnswer(i -> i.getArgument(0, Task.class));

        Task result = taskService.createTask(taskRequest);

        assertEquals("Task 1", result.getTitle());
        assertEquals(parentTask, result.getParent());
        assertEquals(2, result.getChildren().size());
        assertTrue(result.getChildren().contains(childTask1));
        assertTrue(result.getChildren().contains(childTask2));
        verify(taskRepository, times(4)).save(any(Task.class));
    }

    @Test
    public void testUpdateTask() {
        TaskRequest taskRequest = createTaskRequest("user-1", "Updated Task", Instant.now(), "Updated Description", "Updated Link", "Updated App", null, Collections.emptyList(), Collections.emptyList());
        Task existingTask = createTask(1L, "Task 1");
        Task updatedTask = new Task();
        BeanUtils.copyProperties(taskRequest, updatedTask);
        updatedTask.setId(1L);

        when(taskRepository.findByIdAndUserId(1L, "user-1")).thenReturn(existingTask);
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        Task result = taskService.updateTask(1L, taskRequest);

        assertEquals("Updated Task", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        assertEquals("Updated Link", result.getLink());
        assertEquals("Updated App", result.getAppName());
        verify(taskRepository, times(1)).findByIdAndUserId(1L, "user-1");
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    public void testGetTasksByUserId() {
        List<Task> tasks = Arrays.asList(createTask(1L, "Task 1"), createTask(2L, "Task 2"));
        Page<Task> taskPage = new PageImpl<>(tasks);

        when(taskRepository.findAllByUserId("user-1", PageRequest.of(0, 10))).thenReturn(taskPage);

        Page<Task> result = taskService.getTasksByUserId("user-1", PageRequest.of(0, 10));

        assertEquals(2, result.getContent().size());
        assertEquals("Task 1", result.getContent().get(0).getTitle());
        assertEquals("Task 2", result.getContent().get(1).getTitle());
        verify(taskRepository, times(1)).findAllByUserId("user-1", PageRequest.of(0, 10));
    }

    @Test
    public void testGetTaskByIdAndUserId() {
        Task task = createTask(1L, "Task 1");
        when(taskRepository.findByIdAndUserId(1L, "user-1")).thenReturn(task);

        Task result = taskService.getTaskByIdAndUserId("user-1", 1L);

        assertEquals("Task 1", result.getTitle());
        verify(taskRepository, times(1)).findByIdAndUserId(1L, "user-1");
    }
}



