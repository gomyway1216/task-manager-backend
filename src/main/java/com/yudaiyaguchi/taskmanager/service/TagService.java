package com.yudaiyaguchi.taskmanager.service;

import com.yudaiyaguchi.taskmanager.request.*;
import com.yudaiyaguchi.taskmanager.exception.ResourceNotFoundException;
import com.yudaiyaguchi.taskmanager.model.Tag;
import com.yudaiyaguchi.taskmanager.model.Task;
import com.yudaiyaguchi.taskmanager.repository.TagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class TagService {

    private static Logger LOGGER = LoggerFactory.getLogger(TagService.class);
    @Autowired
    private TagRepository tagRepository;

    public Tag createTag(TagRequest tagRequest) {
        Tag tag = new Tag();
        tag.setUserId(tagRequest.getUserId());
        tag.setName(tagRequest.getName());
        return tagRepository.save(tag);
    }

    public Tag updateTag(Long tagId, TagRequest tagRequest) {
        Tag tag = tagRepository.findByIdAndUserId(tagId, tagRequest.getUserId());
        if(tag == null) {
            throw new ResourceNotFoundException("Tag not found");
        }

        tag.setName(tagRequest.getName());
        return tagRepository.save(tag);
    }

    public Tag getTagWithTasksByUserIdAndTagId(String userId, Long tagId) {
       Tag tagR = tagRepository.findByIdAndUserId(tagId, userId);
       return tagR;
    }

    public List<Tag> getTagsByUserIdWithoutTasks(String userId) {
        List<Tag> tags = tagRepository.findByUserId(userId);
        return tags;
    }

    public void deleteTagByUserIdAndTagId(String userId, Long tagId) {
        Tag tag = tagRepository.findByIdAndUserId(tagId, userId);
        if(tag != null) {
            // Remove the relationships between the tag and its associated tasks
            Set<Task> tasks = tag.getTasks();
            for (Task task : tasks) {
                task.getTags().remove(tag);
            }
            tagRepository.delete(tag);
        }
    }
}
