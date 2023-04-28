package com.yudaiyaguchi.taskmanager.specification;

import com.yudaiyaguchi.taskmanager.model.Task;
import com.yudaiyaguchi.taskmanager.specification.TaskSpecification;
import com.yudaiyaguchi.taskmanager.specification.criteria.SearchCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class TaskSpecificationTest {

    @Test
    void testToPredicate() {
        SearchCriteria searchCriteria = new SearchCriteria("status", ":", "Complete");
        TaskSpecification taskSpecification = new TaskSpecification(searchCriteria);

        CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<?> cq = Mockito.mock(CriteriaQuery.class);
        Root<Task> root = Mockito.mock(Root.class);
        Path path = Mockito.mock(Path.class);

        Mockito.when(root.get("status")).thenReturn(path);
        Mockito.when(path.getJavaType()).thenReturn(String.class);
        Mockito.when(cb.like(path, "%Complete%")).thenReturn(Mockito.mock(Predicate.class));

        Predicate predicate = taskSpecification.toPredicate(root, cq, cb);

        assertNotNull(predicate);
    }

    @Test
    void testToPredicateUnknownOperation() {
        SearchCriteria searchCriteria = new SearchCriteria("status", "?", "Complete");
        TaskSpecification taskSpecification = new TaskSpecification(searchCriteria);

        CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<?> cq = Mockito.mock(CriteriaQuery.class);
        Root<Task> root = Mockito.mock(Root.class);

        Predicate predicate = taskSpecification.toPredicate(root, cq, cb);

        assertNull(predicate);
    }
}
