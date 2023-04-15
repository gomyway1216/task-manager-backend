package com.yudaiyaguchi.taskmanager.repository;

import com.yudaiyaguchi.taskmanager.model.Tag;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    @EntityGraph(value = "Tag.tasks", type = EntityGraph.EntityGraphType.FETCH)
    List<Tag> findAllByUserId(String userId);

    List<Tag> findByUserId(String userId);

    // the below query causes circular reference as tasks are fetched from tags and they contain tags which again
    // contain tasks. This is a known issue with Spring Data JPA and Hibernate.
    // actually. this issue is resolved by adding @JsonIgnoreProperties("tags") to Task class
    // and @JsonIgnoreProperties("tasks") to Tag class.
    Tag findByIdAndUserId(Long id, String userId);
}
