package com.yudaiyaguchi.taskmanager.repository;

import com.yudaiyaguchi.taskmanager.dto.TaskNameIdDTO;
import com.yudaiyaguchi.taskmanager.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findAllByUserId(String userId, Pageable pageable);

    List<Task> findAllByParentId(Long parentId);

    Task findByIdAndUserId(Long id, String userId);

    @Query("SELECT new com.yudaiyaguchi.taskmanager.dto.TaskNameIdDTO(t.id, t.title) FROM Task t WHERE t.userId = :userId")
    List<TaskNameIdDTO> findNameIdByUserId(@Param("userId") String userId);
}
