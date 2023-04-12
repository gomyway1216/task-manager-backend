package com.yudaiyaguchi.taskmanager.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yudaiyaguchi.taskmanager.service.TaskSerializer;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="task")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@JsonSerialize(using = TaskSerializer.class)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String title;
    private Instant timeCreated;
    private Instant timeUpdated;
    private Instant timeDue;
    private String description;
    private String link;
    private String appName;

    @ManyToOne(fetch = FetchType.LAZY)
    private Task parent;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "parent")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<Task> children;


    @JsonBackReference
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "task_tag",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    public Task() {
        timeCreated = Instant.now();
    }

    // Getter and setter
    public Task getParent() {
        return parent;
    }

    @JsonProperty
    public void setParent(Task parent) {
        this.parent = parent;
    }

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

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @JsonIgnore
    public Set<Task> getChildren() {
        return children;
    }

    public void setChildren(Set<Task> children) {
        this.children = children;
    }

    @PreUpdate
    public void updateTimeUpdated() {
        timeUpdated = Instant.now();
    }

//    @Override
//    public int hashCode() {
//        final int prime = 31;
//        int result = 1;
//        result = prime * result + ((id == null) ? 0 : id.hashCode());
//        return result;
//    }

//    @Override
//    public String toString() {
//        return this.id + " " + this.title + " " + this.timeCreated + " " + this.timeUpdated + " " + this.timeDue + " " + this.description + " " + this.link + " " + this.appName
//                + " " + this.parent + " " + this.children + " " + this.tags;
//    }

}
