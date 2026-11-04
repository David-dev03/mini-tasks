package com.example.minitasks.services;

import com.example.minitasks.dto.CreateTaskDTO;
import com.example.minitasks.dto.TaskResponseDTO;
import com.example.minitasks.entities.Task;
import com.example.minitasks.repositories.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TaskService {

    private final TaskRepository repo;

    public TaskService(TaskRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDTO> list(Boolean done) {
        List<Task> tasks = (done == null) ? repo.findAll() : repo.findByDone(done);
        return tasks.stream().map(TaskResponseDTO::from).toList();
    }

    @Transactional(readOnly = true)
    public TaskResponseDTO findById(Long id) {
        return TaskResponseDTO.from(getOrThrow(id));
    }

    public TaskResponseDTO create(CreateTaskDTO dto) {
        return TaskResponseDTO.from(repo.save(new Task(dto.title())));
    }

    public TaskResponseDTO markDone(Long id) {
        Task task = getOrThrow(id);
        task.setDone(true);
        return TaskResponseDTO.from(task);
    }

    private Task getOrThrow(Long id) {
        return repo.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }
}
