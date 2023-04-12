package com.yudaiyaguchi.taskmanager.service;

import com.yudaiyaguchi.taskmanager.dto.TaskWithTagsDTO;
import com.yudaiyaguchi.taskmanager.model.Tag;
import com.yudaiyaguchi.taskmanager.model.Task;
import org.modelmapper.AbstractConverter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TaskToTaskWithTagsDTOConverter extends AbstractConverter<Task, TaskWithTagsDTO> {
    @Override
    protected TaskWithTagsDTO convert(Task task) {
        if (task == null) {
            return null;
        }

        TaskWithTagsDTO taskWithTagsDTO = new TaskWithTagsDTO();
        taskWithTagsDTO.setId(task.getId());
        taskWithTagsDTO.setUserId(task.getUserId());
        taskWithTagsDTO.setTitle(task.getTitle());
        taskWithTagsDTO.setTimeCreated(task.getTimeCreated());
        taskWithTagsDTO.setTimeUpdated(task.getTimeUpdated());
        taskWithTagsDTO.setTimeDue(task.getTimeDue());
        taskWithTagsDTO.setDescription(task.getDescription());
        taskWithTagsDTO.setLink(task.getLink());
        taskWithTagsDTO.setAppName(task.getAppName());

        if (task.getParent() != null) {
            taskWithTagsDTO.setParentTask(convert(task.getParent()));
        }

        if (task.getChildren() != null) {
            List<TaskWithTagsDTO> childrenDTOs = task.getChildren().stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
            taskWithTagsDTO.setChildren(childrenDTOs);
        }

        if (task.getTags() != null) {
            Set<Tag> tags = task.getTags().stream()
                    .map(tag -> {
                        Tag newTag = new Tag();
                        newTag.setId(tag.getId());
                        newTag.setUserId(tag.getUserId());
                        newTag.setName(tag.getName());
                        return newTag;
                    })
                    .collect(Collectors.toSet());
            taskWithTagsDTO.setTags(tags);
        }

        return taskWithTagsDTO;
    }
}
