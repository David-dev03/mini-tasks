package com.example.minitasks.dto;

import com.example.minitasks.entities.Task;

public record TaskResponseDTO(Long id, String title, boolean done) {

    public static TaskResponseDTO from(Task task) {
        return new TaskResponseDTO(task.getId(), task.getTitle(), task.isDone());
    }
}
