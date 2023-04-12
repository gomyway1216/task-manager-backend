//package com.yudaiyaguchi.taskmanager.models;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.fasterxml.jackson.databind.PropertyNamingStrategies;
//import com.fasterxml.jackson.databind.PropertyNamingStrategy;
//import com.fasterxml.jackson.databind.annotation.JsonNaming;
//import com.yudaiyaguchi.taskmanager.entities.Task;
//import jakarta.persistence.*;
//import lombok.Builder;
//import lombok.Data;
//import java.util.Set;
//
//@Data
//@Builder
//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
//@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
//public class TaskDTO {
//
//    private Long id;
//    private String userId;
//    private String name;
//    private String description;
//    private String link;
//    private Long appName;
//    private Long timeCreated;
//    private Long timeUpdated;
//    private Task parent;
//    private Set<Task> children;
//}
//
//
//
//
//
////@Data
////@Builder
////@JsonInclude(JsonInclude.Include.NON_NULL)
////@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
////@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
////public class PersonDTO {
////
////    private Long id;
////    private String fullName;
////    private Person parent;
////    private Set<Person> children;
////}