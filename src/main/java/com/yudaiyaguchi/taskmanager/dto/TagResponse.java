package com.yudaiyaguchi.taskmanager.dto;

import com.yudaiyaguchi.taskmanager.model.Task;
import lombok.Data;
import java.util.Set;

@Data
public class TagResponse {
    private Long id;
    private String userId;
    private String name;
    private Set<Task> tasks;
}
