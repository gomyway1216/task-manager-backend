package com.yudaiyaguchi.taskmanager.dto;

import com.yudaiyaguchi.taskmanager.model.Task;
import lombok.Data;

import java.util.Set;

//@Data
public class TagWithTasksDTO {

    private Long id;
    private String userId;
    private String name;
    private Set<Task> tasks;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }
}
