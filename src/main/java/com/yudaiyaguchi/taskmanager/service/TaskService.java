package com.yudaiyaguchi.taskmanager.service;

import com.yudaiyaguchi.taskmanager.dto.TaskRequest;
import com.yudaiyaguchi.taskmanager.dto.TaskWithTagsDTO;
import com.yudaiyaguchi.taskmanager.exception.ResourceNotFoundException;
import com.yudaiyaguchi.taskmanager.model.Tag;
import com.yudaiyaguchi.taskmanager.model.Task;
import com.yudaiyaguchi.taskmanager.repository.TagRepository;
import com.yudaiyaguchi.taskmanager.repository.TaskRepository;
//import org.apache.log4j.Logger;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private static Logger LOGGER = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ModelMapper modelMapper;

//    public Page<Task> getAllTasks(Pageable pageable) {
//        return taskRepository.findAll(pageable);
//    }

    public Page<Task> getAllTasksByUserId(String userId, Pageable pageable) {
//        return taskRepository.findAllByUserId(userId);
        return taskRepository.findAllByUserId(userId, pageable);
    }

    public Page<TaskWithTagsDTO> getTasksByUserId(@PathVariable String userId, Pageable pageable) {
        Page<Task> tasks = taskRepository.findAllByUserId(userId, pageable);
        LOGGER.info("Tasks??");
//        tasks.map(task -> {
//            LOGGER.info("task: {}", task);
//            return task;
//        });
//        LOGGER.info(tasks);
//        LOGGER.info();
        Page<TaskWithTagsDTO> taskWithTagsDTOs = tasks.map(this::convertToTaskWithTagsDTO);
        return taskWithTagsDTOs;
    }

//    public TaskWithTagsDTO convertToTaskWithTagsDTO(Task task) {
//        Set<Tag> tags = task.getTags();
//        TaskWithTagsDTO taskWithTagsDTO = modelMapper.map(task, TaskWithTagsDTO.class);
//        taskWithTagsDTO.setTags(tags);
//        return taskWithTagsDTO;
//    }

//    private TaskDTO convertToDto(Task task) {
//        TaskDTO taskDTO = modelMapper.map(task, TaskDTO.class);
//        if (task.getParent() != null) {
//            taskDTO.setParentId(task.getParent().getId());
//        }
//        taskDTO.setChildren(task.getChildren().stream()
//                .map(childTask -> {
//                    TaskDTO childTaskDTO = modelMapper.map(childTask, TaskDTO.class);
//                    childTaskDTO.setParentId(childTask.getParent().getId());
//                    return childTaskDTO;
//                })
//                .collect(Collectors.toList()));
//        taskDTO.setTags(task.getTags().stream()
//                .map(tag -> modelMapper.map(tag, TagDTO.class))
//                .collect(Collectors.toSet()));
//        return taskDTO;
//    }

    private TaskWithTagsDTO convertToTaskWithTagsDTO(Task task) {
        TypeMap<Task, TaskWithTagsDTO> typeMap = modelMapper.getTypeMap(Task.class, TaskWithTagsDTO.class);
        if (typeMap == null) {
            typeMap = modelMapper.createTypeMap(Task.class, TaskWithTagsDTO.class);
            typeMap.addMappings(mapper -> {
                mapper.<Set<Tag>>map(src -> {
                    if (src.getTags() == null) {
                        return Collections.emptySet();
                    }
                    return src.getTags().stream().map(tag -> {
                        Tag newTag = new Tag();
                        newTag.setId(tag.getId());
                        newTag.setUserId(tag.getUserId());
                        newTag.setName(tag.getName());
                        return newTag;
                    }).collect(Collectors.toSet());
                }, TaskWithTagsDTO::setTags);
            });
        }

        // Remove the nested tasks attribute from the Tag object when mapping it to the TaskWithTagsDTO.
        modelMapper.typeMap(Tag.class, Tag.class).addMappings(mapper -> mapper.skip(Tag::setTasks));

//        LOGGER.info("Task!!");
//        LOGGER.info(task);

        TaskWithTagsDTO taskDTO = modelMapper.map(task, TaskWithTagsDTO.class);
//        taskDTO.setChildren();

//        return modelMapper.map(task, TaskWithTagsDTO.class);
        return taskDTO;
    }

//        private Function<Task, TaskDTO> mapToTaskDTO = t -> TaskDTO.builder()
//            .id(t.getId()).userId(t.getUserId()).name(t.getName())
//            .description(t.getDescription()).link(t.getLink())
//            .appName(t.getAppName()).timeCreated(t.getTimeCreated())
//            .timeUpdated(t.getTimeUpdated()).parent(t.getParent())
//            .children(t.getChildren()).build();


//    private TaskWithTagsDTO convertToTaskWithTagsDTO(Task task) {
//        ModelMapper modelMapper = new ModelMapper();
//
//        modelMapper.addMappings(new PropertyMap<Task, TaskWithTagsDTO>() {
//            @Override
//            protected void configure() {
//                skip().setParentTask(null);
//                skip().setChildren(null);
//                skip().setTags(null);
//            }
//        });
//
//        TaskWithTagsDTO taskWithTagsDTO = modelMapper.map(task, TaskWithTagsDTO.class);
//
//        if (task.getParent() != null) {
//            taskWithTagsDTO.setParentTask(convertToTaskWithTagsDTO(task.getParent()));
//        }
//
//        if (task.getChildren() != null) {
//            List<TaskWithTagsDTO> childrenDTOs = task.getChildren().stream()
//                    .map(this::convertToTaskWithTagsDTO)
//                    .collect(Collectors.toList());
//            taskWithTagsDTO.setChildren(childrenDTOs);
//        }
//
//        if (task.getTags() != null) {
//            Set<Tag> tags = task.getTags().stream()
//                    .map(tag -> {
//                        Tag newTag = new Tag();
//                        newTag.setId(tag.getId());
//                        newTag.setUserId(tag.getUserId());
//                        newTag.setName(tag.getName());
//                        return newTag;
//                    })
//                    .collect(Collectors.toSet());
//            taskWithTagsDTO.setTags(tags);
//        }
//
//        return taskWithTagsDTO;
//    }

//    private TaskWithTagsDTO convertToTaskWithTagsDTO(Task task) {
//        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.addConverter(new TaskToTaskWithTagsDTOConverter());
//        return modelMapper.map(task, TaskWithTagsDTO.class);
//    }

//    private TaskWithTagsDTO convertToTaskWithTagsDTO(Task task) {
//        if (task == null) {
//            return null;
//        }
//
//        TaskWithTagsDTO taskWithTagsDTO = new TaskWithTagsDTO();
//        taskWithTagsDTO.setId(task.getId());
//        taskWithTagsDTO.setUserId(task.getUserId());
//        taskWithTagsDTO.setTitle(task.getTitle());
//        taskWithTagsDTO.setTimeCreated(task.getTimeCreated());
//        taskWithTagsDTO.setTimeUpdated(task.getTimeUpdated());
//        taskWithTagsDTO.setTimeDue(task.getTimeDue());
//        taskWithTagsDTO.setDescription(task.getDescription());
//        taskWithTagsDTO.setLink(task.getLink());
//        taskWithTagsDTO.setAppName(task.getAppName());
//        taskWithTagsDTO.setParentTask(convertToTaskWithTagsDTO(task.getParent()));
//
//        if (task.getChildren() != null) {
//            List<TaskWithTagsDTO> childrenDTOs = task.getChildren().stream()
//                    .map(this::convertToTaskWithTagsDTO)
//                    .collect(Collectors.toList());
//            taskWithTagsDTO.setChildren(childrenDTOs);
//        }
//
//        if (task.getTags() != null) {
//            Set<Tag> tags = task.getTags().stream()
//                    .map(tag -> {
//                        Tag newTag = new Tag();
//                        newTag.setId(tag.getId());
//                        newTag.setUserId(tag.getUserId());
//                        newTag.setName(tag.getName());
//                        return newTag;
//                    })
//                    .collect(Collectors.toSet());
//            taskWithTagsDTO.setTags(tags);
//        }
//
//        return taskWithTagsDTO;
//    }

//    private TaskWithTagsDTO convertToTaskWithTagsDTO(Task task) {
//        if (task == null) {
//            return null;
//        }
//
//        TaskWithTagsDTO taskWithTagsDTO = new TaskWithTagsDTO();
//        taskWithTagsDTO.setId(task.getId());
//        taskWithTagsDTO.setUserId(task.getUserId());
//        taskWithTagsDTO.setTitle(task.getTitle());
//        taskWithTagsDTO.setTimeCreated(task.getTimeCreated());
//        taskWithTagsDTO.setTimeUpdated(task.getTimeUpdated());
//        taskWithTagsDTO.setTimeDue(task.getTimeDue());
//        taskWithTagsDTO.setDescription(task.getDescription());
//        taskWithTagsDTO.setLink(task.getLink());
//        taskWithTagsDTO.setAppName(task.getAppName());
//
//        if (task.getParent() != null) {
//            TaskWithTagsDTO parentDTO = new TaskWithTagsDTO();
//            parentDTO.setId(task.getParent().getId());
//            parentDTO.setUserId(task.getParent().getUserId());
//            parentDTO.setTitle(task.getParent().getTitle());
//            taskWithTagsDTO.setParentTask(parentDTO);
//        }
//
//        if (task.getChildren() != null) {
//            List<TaskWithTagsDTO> childrenDTOs = task.getChildren().stream()
//                    .map(child -> {
//                        TaskWithTagsDTO childDTO = new TaskWithTagsDTO();
//                        childDTO.setId(child.getId());
//                        childDTO.setUserId(child.getUserId());
//                        childDTO.setTitle(child.getTitle());
//                        return childDTO;
//                    })
//                    .collect(Collectors.toList());
//            taskWithTagsDTO.setChildren(childrenDTOs);
//        }
//
//        if (task.getTags() != null) {
//            Set<Tag> tags = task.getTags().stream()
//                    .map(tag -> {
//                        Tag newTag = new Tag();
//                        newTag.setId(tag.getId());
//                        newTag.setUserId(tag.getUserId());
//                        newTag.setName(tag.getName());
//                        return newTag;
//                    })
//                    .collect(Collectors.toSet());
//            taskWithTagsDTO.setTags(tags);
//        }
//
//        return taskWithTagsDTO;
//    }



//    public Page<Task> getTasksByTagName(String tagName, Pageable pageable) {
//        return taskRepository.findByTagsName(tagName, pageable);
//    }

    public Page<Task> getAllTasksByUserIdAndTagId(String userId, Long tagId, Pageable pageable) {
        return taskRepository.findAllByUserIdAndTagsId(userId, tagId, pageable);
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task createTask(TaskRequest taskRequest) {
        Task task = new Task();
        task.setUserId(taskRequest.getUserId());
        task.setTitle(taskRequest.getTitle());
//        task.setTimeCreated(taskRequest.getTimeCreated());
//        task.setTimeUpdated(taskRequest.getTimeUpdated());
        task.setTimeDue(taskRequest.getTimeDue());
        task.setDescription(taskRequest.getDescription());
        task.setLink(taskRequest.getLink());
        task.setAppName(taskRequest.getAppName());

        // Set parent task
//        if (taskRequest.getParentId() != null) {
//            Task parentTask = taskRepository.findById(taskRequest.getParentId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Parent task not found with ID: " + taskRequest.getParentId()));
//            task.setParent(parentTask);
//        }

        if (taskRequest.getParentId() != null) {
            Task parentTask = taskRepository.findById(taskRequest.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent task not found"));
            task.setParent(parentTask);
        }

        if (taskRequest.getChildrenIds() != null && !taskRequest.getChildrenIds().isEmpty()) {
            Set<Task> childrenTasks = taskRequest.getChildrenIds().stream()
                    .map(taskId -> taskRepository.findById(taskId)
                            .orElseThrow(() -> new ResourceNotFoundException("Child task not found")))
                    .collect(Collectors.toSet());

            // Set parent for the children tasks
            task.setChildren(childrenTasks);

            // Save the new task to the database
            task = taskRepository.save(task);

            // Get the ID of the new task
            Long taskId = task.getId();

            // Update the parent of the child tasks in the database
            childrenTasks.forEach(child -> {
                Task parentTask = taskRepository.findById(taskId)
                        .orElseThrow(() -> new ResourceNotFoundException("Parent task not found"));
                child.setParent(parentTask);
                taskRepository.save(child);
            });
        } else {
            // Save the new task to the database
            task = taskRepository.save(task);
        }

        // Set tags
        if (taskRequest.getTagIds() != null && !taskRequest.getTagIds().isEmpty()) {
            List<Tag> tags = tagRepository.findAllById(taskRequest.getTagIds());
            task.setTags(new HashSet<>(tags));
        }

//        if (taskRequest.getChildrenIds() != null && !taskRequest.getChildrenIds().isEmpty()) {
//            Set<Task> childrenTasks = taskRequest.getChildrenIds().stream()
//                    .map(this::getTaskById)
//                    .filter(Optional::isPresent)
//                    .map(Optional::get)
//                    .collect(Collectors.toSet());
//            task.setChildren(childrenTasks);
//        }

        return taskRepository.save(task);
    }

    public Task updateTask(Long taskId, TaskRequest taskRequest) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));

        // user Id for the task shold not be updated
//        task.setUserId(taskRequest.getUserId());
        task.setTitle(taskRequest.getTitle());
//        task.setTimeCreated(taskRequest.getTimeCreated());
//        task.setTimeUpdated(taskRequest.getTimeUpdated());
        task.setTimeDue(taskRequest.getTimeDue());
        task.setDescription(taskRequest.getDescription());
        task.setLink(taskRequest.getLink());
        task.setAppName(taskRequest.getAppName());

        // Set parent task
        if (taskRequest.getParentId() != null) {
            Task parentTask = taskRepository.findById(taskRequest.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent task not found with ID: " + taskRequest.getParentId()));
            task.setParent(parentTask);
        } else {
            task.setParent(null);
        }

        // Set tags
        if (taskRequest.getTagIds() != null && !taskRequest.getTagIds().isEmpty()) {
            List<Tag> tags = tagRepository.findAllById(taskRequest.getTagIds());
            task.setTags(new HashSet<>(tags));
        } else {
            task.setTags(Collections.emptySet());
        }

//        // Set child tasks
//        if (taskRequest.getChildTaskIds() != null && !taskRequest.getChildTaskIds().isEmpty()) {
//            List<Task> childTasks = taskRepository.findAllById(taskRequest.getChildTaskIds());
//            for (Task childTask : childTasks) {
//                childTask.setParent(task);
//            }
//            taskRepository.saveAll(childTasks);
//        } else {
//            // Unlink existing child tasks
//            List<Task> existingChildTasks = taskRepository.findAllByParentId(taskId);
//            for (Task childTask : existingChildTasks) {
//                childTask.setParent(null);
//            }
//            taskRepository.saveAll(existingChildTasks);
//        }
//
//        return taskRepository.save(task);

        // Set child tasks
        if (taskRequest.getChildrenIds() != null && !taskRequest.getChildrenIds().isEmpty()) {
            List<Task> childTasks = taskRepository.findAllById(taskRequest.getChildrenIds());
            for (Task childTask : childTasks) {
                childTask.setParent(task);
            }
            task.setChildren(new HashSet<>(childTasks));
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

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
