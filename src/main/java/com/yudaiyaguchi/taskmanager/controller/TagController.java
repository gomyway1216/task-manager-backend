package com.yudaiyaguchi.taskmanager.controller;

import com.yudaiyaguchi.taskmanager.dto.TagResponse;
import com.yudaiyaguchi.taskmanager.dto.TagWithTasksDTO;
import com.yudaiyaguchi.taskmanager.model.Tag;
import com.yudaiyaguchi.taskmanager.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    @Autowired
    private TagService tagService;

//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<Tag>> getAllTags(@PathVariable String userId) {
//        return ResponseEntity.ok(tagService.getAllTagsByUserId(userId));
//    }

//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<TagResponse>> getAllTagsByUserId(@PathVariable String userId) {
////        List<Tag> tags = tagRepository.findAllByUserId(userId);
////        List<TagDTO> tagDTOs = tags.stream()
////                .map(tag -> {
////                    TagDTO tagDTO = new TagDTO();
////                    tagDTO.setId(tag.getId());
////                    tagDTO.setUserId(tag.getUserId());
////                    tagDTO.setName(tag.getName());
////                    tagDTO.setTasks(tag.getTasks());
////                    return tagDTO;
////                })
////                .collect(Collectors.toList());
////        return ResponseEntity.ok(tagDTOs);
////        List<TagResponse> tagDTOs = tagService.getAllTagsByUserId(userId);
////        return ResponseEntity.ok(tagDTOs);
//
//        List<Tag> tags = tagService.findAllByUserIdWithTasks(userId);
//        List<TagResponse> tagDTOs = tags.stream()
//                .map(tag -> {
//                    TagResponse tagDTO = new TagResponse();
//                    tagDTO.setId(tag.getId());
//                    tagDTO.setUserId(tag.getUserId());
//                    tagDTO.setName(tag.getName());
//                    tagDTO.setTasks(tag.getTasks());
//                    return tagDTO;
//                })
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(tagDTOs);
//    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TagWithTasksDTO>> getAllTagsByUserId(@PathVariable String userId) {
        List<TagWithTasksDTO> tagsWithTasks = tagService.findAllByUserIdWithTasks(userId);
        return ResponseEntity.ok(tagsWithTasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable Long id) {
        Optional<Tag> tag = tagService.getTagById(id);
        return tag.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag) {
        Tag createdTag = tagService.createTag(tag);
        return new ResponseEntity<>(createdTag, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tag> updateTag(@PathVariable Long id, @RequestBody Tag tag) {
        Optional<Tag> existingTag = tagService.getTagById(id);
        if (existingTag.isPresent()) {
            tag.setId(id);
            Tag updatedTag = tagService.updateTag(tag);
            return ResponseEntity.ok(updatedTag);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        Optional<Tag> existingTag = tagService.getTagById(id);
        if (existingTag.isPresent()) {
            tagService.deleteTag(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
