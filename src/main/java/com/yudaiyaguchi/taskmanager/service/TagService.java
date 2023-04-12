package com.yudaiyaguchi.taskmanager.service;

import com.yudaiyaguchi.taskmanager.dto.TagResponse;
import com.yudaiyaguchi.taskmanager.dto.TagWithTasksDTO;
import com.yudaiyaguchi.taskmanager.model.Tag;
import com.yudaiyaguchi.taskmanager.repository.TagMapper;
import com.yudaiyaguchi.taskmanager.repository.TagRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TagMapper tagMapper;

//    public List<TagResponse> getAllTagsByUserId(String userId) {
////        return tagRepository.findAllByUserId(userId);
//        List<Tag> tags = tagRepository.findTagsByUserIdWithTasks(userId);
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
//        return tagDTOs;
//    }

    public List<TagWithTasksDTO> findAllByUserIdWithTasks(String userId) {
        List<Tag> tags = tagRepository.findAllByUserId(userId);
        return tags.stream()
                .map(tagMapper::toTagWithTasksDTO)
                .collect(Collectors.toList());
    }

//    public List<Tag> findAllByUserIdWithTasks(String userId) {
//        List<Tag> tags = tagRepository.findAllByUserId(userId);
//        tags.forEach(tag -> Hibernate.initialize(tag.getTasks()));
//        return tags;
//    }

    public Optional<Tag> getTagById(Long id) {
        return tagRepository.findById(id);
    }

    public Tag createTag(Tag tag) {
        return tagRepository.save(tag);
    }

    public Tag updateTag(Tag tag) {
        return tagRepository.save(tag);
    }

    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }
}
