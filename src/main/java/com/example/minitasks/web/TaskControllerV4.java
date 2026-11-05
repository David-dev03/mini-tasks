package com.example.minitasks.web;

import com.example.minitasks.dto.CreateTaskDTO;
import com.example.minitasks.dto.TaskResponseDTO;
import com.example.minitasks.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v4/tasks")
public class TaskControllerV4 {

    private final TaskService service;

    public TaskControllerV4(TaskService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> list(@RequestParam(required = false) Boolean done) {
        return ResponseEntity.ok(service.list(done));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> create(@RequestBody @Valid CreateTaskDTO dto) {
        TaskResponseDTO created = service.create(dto);
        return ResponseEntity.created(URI.create("/v4/tasks/" + created.id())).body(created);
    }

    @PatchMapping("/{id}/done")
    public ResponseEntity<TaskResponseDTO> markDone(@PathVariable Long id) {
        return ResponseEntity.ok(service.markDone(id));
    }
}
