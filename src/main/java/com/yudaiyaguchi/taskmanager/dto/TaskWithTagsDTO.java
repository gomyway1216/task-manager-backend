package com.yudaiyaguchi.taskmanager.dto;

import com.yudaiyaguchi.taskmanager.model.Tag;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TaskWithTagsDTO {
    private Long id;
    private String userId;
    private String title;
    private Instant timeCreated;
    private Instant timeUpdated;
    private Instant timeDue;
    private String description;
    private String link;
    private String appName;
//    private TaskWithTagsDTO parent;
//    private Set<TaskWithTagsDTO> children = new HashSet<>();
//    private Set<Tag> tags = new HashSet<>();

    private TaskWithTagsDTO parentTask;
    private List<TaskWithTagsDTO> children;
    private Set<Tag> tags;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Instant getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Instant timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Instant getTimeUpdated() {
        return timeUpdated;
    }

    public void setTimeUpdated(Instant timeUpdated) {
        this.timeUpdated = timeUpdated;
    }

    public Instant getTimeDue() {
        return timeDue;
    }

    public void setTimeDue(Instant timeDue) {
        this.timeDue = timeDue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public TaskWithTagsDTO getParentTask() {
        return parentTask;
    }

    public void setParentTask(TaskWithTagsDTO parentTask) {
        this.parentTask = parentTask;
    }

//    public Set<TaskWithTagsDTO> getChildren() {
//        return children;
//    }

    public List<TaskWithTagsDTO> getChildren() {
        if (children == null) {
            return null;
        }
        return children.stream().peek(child -> child.setParentTask(null)).collect(Collectors.toList());
    }

    public void setChildren(List<TaskWithTagsDTO> children) {
        this.children = children;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    // Add constructors, getters, and setters
}
