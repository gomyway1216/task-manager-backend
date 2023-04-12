package com.yudaiyaguchi.taskmanager.repository;


import com.yudaiyaguchi.taskmanager.dto.TagWithTasksDTO;
import com.yudaiyaguchi.taskmanager.model.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TagMapper {
//    @Mapping(target = "tasks", source = "tag.tasks")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "tasks", target = "tasks")
    TagWithTasksDTO toTagWithTasksDTO(Tag tag);
}