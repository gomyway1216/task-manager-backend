package com.yudaiyaguchi.taskmanager.service;

import com.yudaiyaguchi.taskmanager.dto.TaskNameIdDTO;
import com.yudaiyaguchi.taskmanager.request.TaskRequest;
import com.yudaiyaguchi.taskmanager.exception.ResourceNotFoundException;
import com.yudaiyaguchi.taskmanager.model.Tag;
import com.yudaiyaguchi.taskmanager.model.Task;
import com.yudaiyaguchi.taskmanager.repository.TagRepository;
import com.yudaiyaguchi.taskmanager.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private static Logger LOGGER = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TagRepository tagRepository;

    public Page<Task> getTasksByUserId(String userId, Pageable pageable) {
        Page<Task> tasks = taskRepository.findAllByUserId(userId, pageable);
        return tasks;
    }

    public Page<Task> getTasksWithFilters(Specification<Task> spec, Pageable pageable) {
        Page<Task> tasks = taskRepository.findAll(spec, pageable);
        return tasks;
    }

    public List<TaskNameIdDTO> getTasksByUserId(String userId) {
        return taskRepository.findNameIdByUserId(userId);
    }

    // delete the task only but not the child tasks. It delete the reference to the parent task in
    public void deleteTask(Long id, String userId) throws ResourceNotFoundException {
        Task task = taskRepository.findByIdAndUserId(id, userId);
        if (task != null) {
            for (Task child: task.getChildren()) {
                child.setParent(null);
                taskRepository.save(child);
            }
            task.getTags().clear(); // Clear the tags association
            taskRepository.save(task); // Save the updated Task
            taskRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Task not found with id " + id);
        }
    }

    public Task getTaskByIdAndUserId(String userId, Long id) {
        Task task = taskRepository.findByIdAndUserId(id, userId);
        Set<Task> children = task.getChildren();
        task.setChildren(children);
        return task;
    }

    public Task createTask(TaskRequest taskRequest) {
        Task task = new Task();
        task.setUserId(taskRequest.getUserId());
        task.setTitle(taskRequest.getTitle());
        task.setTimeDue(taskRequest.getTimeDue());
        task.setDescription(taskRequest.getDescription());
        task.setLink(taskRequest.getLink());
        task.setAppName(taskRequest.getAppName());

        // Set tags
        if (taskRequest.getTagIds() != null && !taskRequest.getTagIds().isEmpty()) {
            List<Tag> tags = tagRepository.findAllById(taskRequest.getTagIds());
            // check if the tag belongs to the user
            tags.stream().forEach(tag -> {
                if (!tag.getUserId().equals(taskRequest.getUserId())) {
                    throw new ResourceNotFoundException("Tag not found with ID: " + tag.getId());
                }
            });
            task.setTags(new HashSet<>(tags));
        }

        if (taskRequest.getParentId() != null) {
            Task parentTask = taskRepository.findByIdAndUserId(taskRequest.getParentId(), taskRequest.getUserId());
            if (parentTask == null) {
                throw new ResourceNotFoundException("Parent task not found with ID: " + taskRequest.getParentId());
            }
            task.setParent(parentTask);
        }

        if (taskRequest.getChildrenIds() != null && !taskRequest.getChildrenIds().isEmpty()) {
            Set<Task> childrenTasks = taskRequest.getChildrenIds().stream()
                    .map(id -> {
                        LOGGER.info("Child task ID: " + id);
                        Task childTask = taskRepository.findByIdAndUserId(id, taskRequest.getUserId());
                        LOGGER.info("childTask {}", childTask);
                        if (childTask == null) {
                            LOGGER.info("Child task not found with ID: " + id);
                            throw new ResourceNotFoundException("Child task not found with ID: " + id);
                        }
                        return childTask;
                    }).collect(Collectors.toSet());

            // Set parent for the children tasks
            task.setChildren(childrenTasks);

            // Save the new task to the database
            task = taskRepository.save(task);

            // Update the parent of the child tasks in the database
            for(Task child: childrenTasks) {
                child.setParent(task);
                taskRepository.save(child);
            }
        }

        return taskRepository.save(task);
    }

    public Task updateTask(Long taskId, TaskRequest taskRequest) {
        // make sure the other user cannot update the task
        Task task = taskRepository.findByIdAndUserId(taskId, taskRequest.getUserId());
        if(task == null) {
            throw new ResourceNotFoundException("Task not found with ID: " + taskId);
        }
        task.setTitle(taskRequest.getTitle());
        task.setTimeDue(taskRequest.getTimeDue());
        task.setDescription(taskRequest.getDescription());
        task.setLink(taskRequest.getLink());
        task.setAppName(taskRequest.getAppName());

        // Set tags
        if (taskRequest.getTagIds() != null && !taskRequest.getTagIds().isEmpty()) {
            List<Tag> tags = tagRepository.findAllById(taskRequest.getTagIds());
            // check if the tag belongs to the user
            tags.stream().forEach(tag -> {
                if (!tag.getUserId().equals(taskRequest.getUserId())) {
                    throw new ResourceNotFoundException("Tag not found with ID: " + tag.getId());
                }
            });
            task.setTags(new HashSet<>(tags));
        } else {
            task.setTags(Collections.emptySet());
        }

        // Set parent task
        if (taskRequest.getParentId() != null) {
            Task parentTask = taskRepository.findByIdAndUserId(taskRequest.getParentId(), taskRequest.getUserId());
            if (parentTask == null) {
                throw new ResourceNotFoundException("Parent task not found with ID: " + taskRequest.getParentId());
            }
            task.setParent(parentTask);
        } else {
            task.setParent(null);
        }

        if (task.getChildren() != null) {
            task.getChildren().forEach(child -> {
                child.setParent(null);
                taskRepository.save(child);
            });
        }

        // Set child tasks
        if (taskRequest.getChildrenIds() != null && !taskRequest.getChildrenIds().isEmpty()) {
            Set<Task> childrenTasks = taskRequest.getChildrenIds().stream()
                    .map(id -> {
                        LOGGER.info("Child task ID: " + id);
                        Task childTask = taskRepository.findByIdAndUserId(id, taskRequest.getUserId());
                        LOGGER.info("childTask {}", childTask);
                        if (childTask == null) {
                            LOGGER.info("Child task not found with ID: " + id);
                            throw new ResourceNotFoundException("Child task not found with ID: " + id);
                        }
                        return childTask;
                    }).collect(Collectors.toSet());

            // Set parent for the children tasks
            task.setChildren(childrenTasks);

            // Update the parent of the child tasks in the database
            for(Task child: childrenTasks) {
                child.setParent(task);
                taskRepository.save(child);
            }
        } else {
            // Unlink existing child tasks
            List<Task> existingChildTasks = taskRepository.findAllByParentId(taskId);
            for (Task childTask : existingChildTasks) {
                childTask.setParent(null);
            }
            task.setChildren(Collections.emptySet());
        }

        return taskRepository.save(task);
    }
}
