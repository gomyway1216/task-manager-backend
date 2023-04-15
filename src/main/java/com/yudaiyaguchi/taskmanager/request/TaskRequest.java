package com.yudaiyaguchi.taskmanager.request;

import lombok.Data;
import java.time.Instant;
import java.util.List;

@Data
public class TaskRequest {

    private String userId;
    private String title;
    private Instant timeCreated;
    private Instant timeUpdated;
    private Instant timeDue;
    private String description;
    private String link;
    private String appName;
    private Long parentId;
    private List<Long> childrenIds;
    private List<Long> tagIds;
}