package com.yudaiyaguchi.taskmanager;

import org.apache.log4j.BasicConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskManagerApplication {

	public static void main(String[] args) {
		BasicConfigurator.configure();
		SpringApplication.run(TaskManagerApplication.class, args);
	}
}
