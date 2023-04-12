//package com.yudaiyaguchi.taskmanager.service;
//
//public class Sample {
//    // TaskService.java
//    public Task createTask(TaskRequest taskRequest) {
//        Task task = new Task();
//        task.setTitle(taskRequest.getTitle());
//        task.setDescription(taskRequest.getDescription());
//        task.setTimeCreated(taskRequest.getTimeCreated());
//        task.setTimeUpdated(taskRequest.getTimeUpdated());
//        task.setTimeDue(taskRequest.getTimeDue());
//        task.setUserId(taskRequest.getUserId());
//
//        // Set parent task
//        if (taskRequest.getParentId() != null) {
//            Task parentTask = taskRepository.findById(taskRequest.getParentId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Parent task not found with ID: " + taskRequest.getParentId()));
//            task.setParent(parentTask);
//        }
//
//        // Set tags
//        if (taskRequest.getTagIds() != null && !taskRequest.getTagIds().isEmpty()) {
//            List<Tag> tags = tagRepository.findAllById(taskRequest.getTagIds());
//            task.setTags(new HashSet<>(tags));
//        }
//
//        // Save the task first to generate its ID
//        Task savedTask = taskRepository.save(task);
//
//        // Set child tasks
//        if (taskRequest.getChildTaskIds() != null && !taskRequest.getChildTaskIds().isEmpty()) {
//            List<Task> childTasks = taskRepository.findAllById(taskRequest.getChildTaskIds());
//            for (Task childTask : childTasks) {
//                childTask.setParent(savedTask);
//            }
//            taskRepository.saveAll(childTasks);
//        }
//
//        return savedTask;
//    }
//
//}
//
