package com.yudaiyaguchi.taskmanager.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ResourceNotFoundExceptionTest {

    @Test
    public void testDefaultConstructor() {
        ResourceNotFoundException exception = new ResourceNotFoundException();
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    public void testMessageConstructor() {
        String message = "Resource not found";
        ResourceNotFoundException exception = new ResourceNotFoundException(message);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    public void testMessageAndCauseConstructor() {
        String message = "Resource not found";
        Throwable cause = new IllegalArgumentException("Invalid argument");
        ResourceNotFoundException exception = new ResourceNotFoundException(message, cause);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}