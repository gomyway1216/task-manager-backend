package com.yudaiyaguchi.taskmanager.repository;

import com.yudaiyaguchi.taskmanager.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
//    Page<Task> findAll(Pageable pageable);
    Page<Task> findAllByUserId(String userId, Pageable pageable);
    Page<Task> findAllByUserIdAndTagsId(String userId, Long tagId, Pageable pageable);
    List<Task> findAllByParentId(Long parentId);

    @Query("SELECT t FROM Task t JOIN FETCH t.parent p LEFT JOIN FETCH t.children c WHERE t.id = :taskId")
    Optional<Task> findByIdWithParentAndChildren(@Param("taskId") Long taskId);
}
