package com.yudaiyaguchi.taskmanager.model;

import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TagTest {

    @Test
    public void testGetterSetterMethods() {
        Tag tag = new Tag();

        Long id = 1L;
        String userId = "user-1";
        String name = "tag-name";
        Set<Task> tasks = new HashSet<>();

        tag.setId(id);
        tag.setUserId(userId);
        tag.setName(name);
        tag.setTasks(tasks);

        assertEquals(id, tag.getId());
        assertEquals(userId, tag.getUserId());
        assertEquals(name, tag.getName());
        assertEquals(tasks, tag.getTasks());
    }
}