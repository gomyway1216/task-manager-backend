package com.yudaiyaguchi.taskmanager.dto;


import com.yudaiyaguchi.taskmanager.model.Status;

public class TaskNameIdDTO {
    private Long id;
    private String title;
    private Status status;

    public TaskNameIdDTO(Long id, String title, Status status) {
        this.id = id;
        this.title = title;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}