package com.yudaiyaguchi.taskmanager.controller;

import com.yudaiyaguchi.taskmanager.controller.TagController;
import com.yudaiyaguchi.taskmanager.model.Tag;
import com.yudaiyaguchi.taskmanager.request.TagRequest;
import com.yudaiyaguchi.taskmanager.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TagControllerTest {

    @InjectMocks
    private TagController tagController;

    @Mock
    private TagService tagService;

    private String userId;
    private Long tagId;
    private Tag tag;
    private TagRequest tagRequest;
    private List<Tag> tagList;

    @BeforeEach
    public void setUp() {
        userId = "test-user";
        tagId = 1L;

        tag = new Tag();
        tag.setId(tagId);
        tag.setUserId(userId);
        tag.setName("Test Tag");

        tagRequest = new TagRequest();
        tagRequest.setUserId(userId);
        tagRequest.setName("Test Tag");

        tagList = Arrays.asList(tag);
    }

    @Test
    public void testGetAllTagsByUserId() {
        when(tagService.getTagsByUserIdWithoutTasks(userId)).thenReturn(tagList);

        ResponseEntity<List<Tag>> response = tagController.getAllTagsByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tagList, response.getBody());
        verify(tagService).getTagsByUserIdWithoutTasks(userId);
    }

    @Test
    public void testGetAllTagsByUserIdNotFound() {
        when(tagService.getTagsByUserIdWithoutTasks(userId)).thenReturn(Collections.emptyList());

        ResponseEntity<List<Tag>> response = tagController.getAllTagsByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
        verify(tagService).getTagsByUserIdWithoutTasks(userId);
    }

    @Test
    public void testGetTagById() {
        when(tagService.getTagWithTasksByUserIdAndTagId(userId, tagId)).thenReturn(tag);

        ResponseEntity<Tag> response = tagController.getTagById(userId, tagId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tag, response.getBody());
        verify(tagService).getTagWithTasksByUserIdAndTagId(userId, tagId);
    }

    @Test
    public void testGetTagByIdNotFound() {
        when(tagService.getTagWithTasksByUserIdAndTagId(userId, tagId)).thenReturn(null);

        ResponseEntity<Tag> response = tagController.getTagById(userId, tagId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(tagService).getTagWithTasksByUserIdAndTagId(userId, tagId);
    }

    @Test
    public void testCreateTag() {
        when(tagService.createTag(tagRequest)).thenReturn(tag);

        ResponseEntity<Tag> response = tagController.createTag(tagRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(tag, response.getBody());
        verify(tagService).createTag(tagRequest);
    }

    @Test
    public void testCreateTagInvalidInput() {
        TagRequest invalidTagRequest = new TagRequest();
        invalidTagRequest.setUserId(null);
        invalidTagRequest.setName(null);

        ResponseEntity<Tag> response = tagController.createTag(invalidTagRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(tagService, times(0)).createTag(any(TagRequest.class));
    }

    @Test
    public void testUpdateTag() {
        when(tagService.updateTag(tagId, tagRequest)).thenReturn(tag);

        ResponseEntity<Tag> response = tagController.updateTag(tagId, tagRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tag, response.getBody());
        verify(tagService).updateTag(tagId, tagRequest);
    }

    @Test
    public void testUpdateTagNotFound() {
        when(tagService.updateTag(tagId, tagRequest)).thenReturn(null);

        ResponseEntity<Tag> response = tagController.updateTag(tagId, tagRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(tagService).updateTag(tagId, tagRequest);
    }

    @Test
    public void testDeleteTask() {
        ResponseEntity<Void> response = tagController.deleteTask(userId, tagId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(tagService).deleteTagByUserIdAndTagId(userId, tagId);
    }
}