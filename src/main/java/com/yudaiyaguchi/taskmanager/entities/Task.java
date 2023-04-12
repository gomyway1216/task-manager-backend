//package com.yudaiyaguchi.taskmanager.entities;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//import lombok.Builder;
//import lombok.Data;
//
//import java.time.Instant;
//import java.util.Set;
//
//@Entity
//@Builder
//@Data
//@Table(name="task")
//public class Task {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private String userId;
//    private String title;
//    private Instant timeCreated;
//    private Instant timeUpdated;
//    private Instant timeDue;
//    private String description;
//    private String link;
//    private String appName;
//
//    @ManyToOne
//    @JoinColumn(name = "parent_id")
//    private Task parent;
//
//    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
//    private Set<Task> children = new HashSet<>();
//
//    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "task_tag",
//            joinColumns = @JoinColumn(name = "task_id"),
//            inverseJoinColumns = @JoinColumn(name = "tag_id")
//    )
//    private Set<Tag> tags = new HashSet<>();
//
//    public Task() {
//        timeCreated = Instant.now();
//    }
//
//    @PreUpdate
//    public void updateTimeUpdated() {
//        timeUpdated = Instant.now();
//    }
//
////    @Id
////    @GeneratedValue(strategy = GenerationType.AUTO)
////    private Long id;
////
////    private String userId;
////
////    private String name;
////
////    private String description;
////
////    private String link;
////
////    private Long appName;
////
////    private Long timeCreated;
////
////    private Long timeUpdated;
////
////    private Long timeDue;
////
////    private String tag;
////
////    private int priority;
////
//////    @ManyToOne(fetch = FetchType.LAZY)
//////    private Task parent;
//////
//////    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
//////    private Set<Task> children;
////
////    @ManyToOne
////    @JoinColumn(name = "parent_id")
////    private Task parent;
////
////    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
////    private Set<Task> children = new HashSet<>();
////
////    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
////    @JoinTable(
////            name = "task_tag",
////            joinColumns = @JoinColumn(name = "task_id"),
////            inverseJoinColumns = @JoinColumn(name = "tag_id")
////    )
////    private Set<Tag> tags = new HashSet<>();
////
////
////    @JsonIgnore
////    public Set<Task> getChildren() {
////        return this.children;
////    }
//}
//
