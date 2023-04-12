package com.yudaiyaguchi.taskmanager.repository;

import com.yudaiyaguchi.taskmanager.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findAllByUserId(String userId);

    @Query("SELECT t FROM Tag t JOIN FETCH t.tasks WHERE t.userId = :userId")
    List<Tag> findAllByUserIdWithTasks(@Param("userId") String userId);
}
