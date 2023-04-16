package com.yudaiyaguchi.taskmanager.controller;

import com.yudaiyaguchi.taskmanager.request.TagRequest;
import com.yudaiyaguchi.taskmanager.model.Tag;
import com.yudaiyaguchi.taskmanager.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Tag>> getAllTagsByUserId(@PathVariable String userId) {
        List<Tag> tagsWithTasks = tagService.getTagsByUserIdWithoutTasks(userId);
        return ResponseEntity.ok(tagsWithTasks);
    }

    @GetMapping("/user/{userId}/tag/{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable String userId, @PathVariable Long id) {
        Tag tag = tagService.getTagWithTasksByUserIdAndTagId(userId, id);
        return ResponseEntity.ok(tag);
    }

    @PostMapping
    public ResponseEntity<Tag> createTag(@RequestBody TagRequest tagRequest) {
        if (tagRequest.getUserId() == null || tagRequest.getName() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Tag createdTag = tagService.createTag(tagRequest);
        return new ResponseEntity<>(createdTag, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tag> updateTag(@PathVariable Long id, @RequestBody TagRequest tagRequest) {
        Tag updatedTag = tagService.updateTag(id, tagRequest);
        if (updatedTag == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(updatedTag);
    }

    @DeleteMapping("/user/{userId}/tag/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String userId, @PathVariable Long id) {
        tagService.deleteTagByUserIdAndTagId(userId, id);
        return ResponseEntity.noContent().build();
    }
}
