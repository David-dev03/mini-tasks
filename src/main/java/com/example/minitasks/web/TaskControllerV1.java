package com.example.minitasks.web;

import com.example.minitasks.dto.CreateTaskDTO;
import com.example.minitasks.model.SimpleTask;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/v1")
public class TaskControllerV1 {

    private final List<SimpleTask> inMemory = new ArrayList<>();
    private final AtomicLong seq = new AtomicLong(0);

    @GetMapping("/ping")
    public String ping() { return "pong"; }

    @GetMapping("/tasks")
    public List<SimpleTask> list() {
        return inMemory;
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<SimpleTask> getById(@PathVariable Long id) {
        return inMemory.stream()
                .filter(t -> Objects.equals(t.getId(), id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public SimpleTask create(@RequestBody CreateTaskDTO dto) {
        SimpleTask t = new SimpleTask(seq.incrementAndGet(), dto.title(), false);
        inMemory.add(t);
        return t;
    }

    @DeleteMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        inMemory.removeIf(t -> Objects.equals(t.getId(), id));
    }
}
