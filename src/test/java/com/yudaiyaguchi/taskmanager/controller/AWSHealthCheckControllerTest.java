package com.yudaiyaguchi.taskmanager.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AWSHealthCheckController.class)
public class AWSHealthCheckControllerTest {

    @Autowired
    private AWSHealthCheckController awsHealthCheckController;

    @Test
    public void testHealthCheck() {
        ResponseEntity responseEntity = awsHealthCheckController.healthCheck();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}

