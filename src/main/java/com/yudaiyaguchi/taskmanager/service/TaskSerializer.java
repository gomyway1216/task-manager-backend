package com.yudaiyaguchi.taskmanager.service;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.yudaiyaguchi.taskmanager.model.Task;

import java.io.IOException;

public class TaskSerializer extends JsonSerializer<Task> {

    @Override
    public void serialize(Task task, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", task.getId());
        jsonGenerator.writeStringField("userId", task.getUserId());
        jsonGenerator.writeStringField("title", task.getTitle());
        jsonGenerator.writeStringField("timeCreated", task.getTimeCreated().toString());
        jsonGenerator.writeStringField("timeUpdated", task.getTimeUpdated() != null ? task.getTimeUpdated().toString() : null);
        jsonGenerator.writeStringField("timeDue", task.getTimeDue().toString());
        jsonGenerator.writeStringField("description", task.getDescription());
        jsonGenerator.writeStringField("link", task.getLink());
        jsonGenerator.writeStringField("appName", task.getAppName());

        if (task.getParent() != null) {
            jsonGenerator.writeNumberField("parent", task.getParent().getId());
        } else {
            jsonGenerator.writeNullField("parent");
        }

        jsonGenerator.writeArrayFieldStart("children");

        for (Task child : task.getChildren()) {
            jsonGenerator.writeNumber(child.getId());
        }

        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }
}
