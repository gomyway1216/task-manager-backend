//package com.yudaiyaguchi.taskmanager.controllers;
//
//import com.yudaiyaguchi.taskmanager.entities.Task;
//import com.yudaiyaguchi.taskmanager.models.TaskDTO;
//import com.yudaiyaguchi.taskmanager.repositories.TaskRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Optional;
//import java.util.Set;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/api/v1/task")
//public class TaskControllerOld {
//
//    @Autowired
//    private TaskRepository taskRepository;
//
//    @GetMapping("/{id}")
//    public ResponseEntity<TaskDTO> getTask(@PathVariable("id") Long id) {
//        return taskRepository.findById(id).map(mapToTaskDTO).map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @GetMapping("/{id}/siblings")
//    public ResponseEntity<Set<TaskDTO>> getAllSiblings(@PathVariable("id") Long id) {
//        return taskRepository.findById(id).map(findSiblings).map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
////        return personRepo.findById(id).map(findSiblings).map(ResponseEntity::ok)
////                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @PostMapping("/")
//    public ResponseEntity<> createTask(@RequestBody TaskDTO taskDTO) {
//        try {
//            taskRepository.save(Optional.of(taskDTO).map(mapToTask).get());
////            return ResponseEntity.ok();
//            return new ResponseEntity<>(HttpStatus.OK);
//        } catch (Exception e) {
//
//        }
//    }
//
//    @PutMapping("/")
//    public ResponseEntity<> updateTask(@RequestBody TaskDTO taskDTO) {
////        Task task = taskRepository.findById(taskDTO.getId()).get();
//        taskRepository.save(Optional.of(taskDTO).map(mapToTask).get());
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<> deleteTask(@PathVariable("id") Long id) {
//        taskRepository.deleteById(id);
//    }
//
//    private Function<Task, TaskDTO> mapToTaskDTO = t -> TaskDTO.builder()
//            .id(t.getId()).userId(t.getUserId()).name(t.getName())
//            .description(t.getDescription()).link(t.getLink())
//            .appName(t.getAppName()).timeCreated(t.getTimeCreated())
//            .timeUpdated(t.getTimeUpdated()).parent(t.getParent())
//            .children(t.getChildren()).build();
//
//    private Function<Task, Set<TaskDTO>> findSiblings = task -> task.getParent().getChildren().stream()
//            .map(mapToTaskDTO).collect(Collectors.toSet());
//
//    private Function<TaskDTO, Task> mapToTask = t -> Task.builder()
//            .id(t.getId()).userId(t.getUserId()).name(t.getName())
//            .description(t.getDescription()).link(t.getLink())
//            .appName(t.getAppName()).timeCreated(t.getTimeCreated())
//            .timeUpdated(t.getTimeUpdated()).parent(t.getParent())
//            .children(t.getChildren()).build();
//}
