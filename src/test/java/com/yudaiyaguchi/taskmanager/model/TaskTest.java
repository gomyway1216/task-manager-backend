package com.yudaiyaguchi.taskmanager.model;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskTest {

    @Test
    public void testGetterSetterMethods() {
        Task task = new Task();

        Long id = 1L;
        String userId = "user-1";
        String title = "task-title";
        Instant timeCreated = Instant.now();
        Instant timeUpdated = Instant.now();
        Instant timeDue = Instant.now().plusSeconds(3600);
        String description = "task-description";
        String link = "task-link";
        String appName = "task-app";
        Task parent = new Task();
        Set<Task> children = new HashSet<>();
        Set<Tag> tags = new HashSet<>();

        task.setId(id);
        task.setUserId(userId);
        task.setTitle(title);
        task.setTimeCreated(timeCreated);
        task.setTimeUpdated(timeUpdated);
        task.setTimeDue(timeDue);
        task.setDescription(description);
        task.setLink(link);
        task.setAppName(appName);
        task.setParent(parent);
        task.setChildren(children);
        task.setTags(tags);

        assertEquals(id, task.getId());
        assertEquals(userId, task.getUserId());
        assertEquals(title, task.getTitle());
        assertEquals(timeCreated, task.getTimeCreated());
        assertEquals(timeUpdated, task.getTimeUpdated());
        assertEquals(timeDue, task.getTimeDue());
        assertEquals(description, task.getDescription());
        assertEquals(link, task.getLink());
        assertEquals(appName, task.getAppName());
        assertEquals(parent, task.getParent());
        assertEquals(children, task.getChildren());
        assertEquals(tags, task.getTags());
    }
}

