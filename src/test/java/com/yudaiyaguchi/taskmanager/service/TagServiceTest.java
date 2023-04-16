package com.yudaiyaguchi.taskmanager.service;

import com.yudaiyaguchi.taskmanager.exception.ResourceNotFoundException;
import com.yudaiyaguchi.taskmanager.model.Tag;
import com.yudaiyaguchi.taskmanager.repository.TagRepository;
import com.yudaiyaguchi.taskmanager.request.TagRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TagServiceTest {

    @InjectMocks
    private TagService tagService;

    @Mock
    private TagRepository tagRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    private TagRequest createTagRequest(String userId, String name) {
        TagRequest tagRequest = new TagRequest();
        tagRequest.setUserId(userId);
        tagRequest.setName(name);
        return tagRequest;
    }

    @Test
    public void testCreateTag() {
        TagRequest tagRequest = createTagRequest("user-1", "tag-name");
        Tag tag = new Tag();
        tag.setUserId("user-1");
        tag.setName("tag-name");

        when(tagRepository.save(any(Tag.class))).thenReturn(tag);

        Tag createdTag = tagService.createTag(tagRequest);
        assertEquals(tag, createdTag);
    }

    @Test
    public void testUpdateTag() {
        Long tagId = 1L;
        TagRequest tagRequest = createTagRequest("user-1", "updated-tag-name");
        Tag tag = new Tag();
        tag.setId(tagId);
        tag.setUserId("user-1");
        tag.setName("tag-name");

        when(tagRepository.findByIdAndUserId(tagId, "user-1")).thenReturn(tag);
        when(tagRepository.save(tag)).thenReturn(tag);

        Tag updatedTag = tagService.updateTag(tagId, tagRequest);
        assertEquals(tag, updatedTag);
        assertEquals("updated-tag-name", updatedTag.getName());
    }

    @Test
    public void testUpdateTagNotFound() {
        Long tagId = 1L;
        TagRequest tagRequest = createTagRequest("user-1", "updated-tag-name");

        when(tagRepository.findByIdAndUserId(tagId, "user-1")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> tagService.updateTag(tagId, tagRequest));
    }

    @Test
    public void testGetTagWithTasksByUserIdAndTagId() {
        String userId = "user-1";
        Long tagId = 1L;
        Tag tag = new Tag();
        tag.setId(tagId);
        tag.setUserId(userId);
        tag.setName("tag-name");

        when(tagRepository.findByIdAndUserId(tagId, userId)).thenReturn(tag);

        Tag foundTag = tagService.getTagWithTasksByUserIdAndTagId(userId, tagId);
        assertEquals(tag, foundTag);
    }

    @Test
    public void testGetTagsByUserIdWithoutTasks() {
        String userId = "user-1";
        Tag tag1 = new Tag();
        tag1.setId(1L);
        tag1.setUserId(userId);
        tag1.setName("tag-name-1");

        Tag tag2 = new Tag();
        tag2.setId(2L);
        tag2.setUserId(userId);
        tag2.setName("tag-name-2");

        when(tagRepository.findByUserId(userId)).thenReturn(Arrays.asList(tag1, tag2));

        List<Tag> tags = tagService.getTagsByUserIdWithoutTasks(userId);
        assertEquals(2, tags.size());
        assertEquals(tag1, tags.get(0));
        assertEquals(tag2, tags.get(1));
    }

    @Test
    public void testDeleteTagByUserIdAndTagId() {
        String userId = "user-1";
        Long tagId = 1L;
        Tag tag = new Tag();
        tag.setId(tagId);
        tag.setUserId(userId);
        tag.setName("tag-name");
        tag.setTasks(new HashSet<>());

        when(tagRepository.findByIdAndUserId(tagId, userId)).thenReturn(tag);

        tagService.deleteTagByUserIdAndTagId(userId, tagId);

        verify(tagRepository, times(1)).delete(tag);
    }

    @Test
    public void testDeleteTagByUserIdAndTagIdNotFound() {
        String userId = "user-1";
        Long tagId = 1L;

        when(tagRepository.findByIdAndUserId(tagId, userId)).thenReturn(null);

        tagService.deleteTagByUserIdAndTagId(userId, tagId);

        verify(tagRepository, never()).delete(any(Tag.class));
    }
}
