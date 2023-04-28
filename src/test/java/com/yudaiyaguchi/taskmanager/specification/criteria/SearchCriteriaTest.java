package com.yudaiyaguchi.taskmanager.specification.criteria;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class SearchCriteriaTest {

    @Test
    void testSearchCriteria() {
        SearchCriteria searchCriteria = new SearchCriteria("status", ":", "Complete");

        assertEquals("status", searchCriteria.getKey());
        assertEquals(":", searchCriteria.getOperation());
        assertEquals("Complete", searchCriteria.getValue());
    }
}
