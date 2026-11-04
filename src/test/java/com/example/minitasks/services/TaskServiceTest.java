package com.example.minitasks.services;

import com.example.minitasks.dto.CreateTaskDTO;
import com.example.minitasks.dto.TaskResponseDTO;
import com.example.minitasks.entities.Task;
import com.example.minitasks.repositories.TaskRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    private final TaskRepository repo = mock(TaskRepository.class);
    private final TaskService service = new TaskService(repo);

    @Test
    void findById_existente_devuelveDTO() {
        when(repo.findById(1L)).thenReturn(Optional.of(new Task("Leer")));

        TaskResponseDTO dto = service.findById(1L);

        assertThat(dto.title()).isEqualTo("Leer");
        assertThat(dto.done()).isFalse();
    }

    @Test
    void findById_inexistente_lanzaTaskNotFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void markDone_marcaLaTareaComoCompletada() {
        Task task = new Task("Pendiente");
        when(repo.findById(1L)).thenReturn(Optional.of(task));

        TaskResponseDTO dto = service.markDone(1L);

        assertThat(dto.done()).isTrue();
        assertThat(task.isDone()).isTrue();
    }

    @Test
    void markDone_inexistente_lanzaTaskNotFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.markDone(99L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void list_conFiltro_usaFindByDone() {
        when(repo.findByDone(true)).thenReturn(List.of(new Task("Hecha")));

        assertThat(service.list(true)).hasSize(1);
        verify(repo).findByDone(true);
        verify(repo, never()).findAll();
    }

    @Test
    void create_guardaTareaNoCompletada() {
        when(repo.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        TaskResponseDTO dto = service.create(new CreateTaskDTO("Nueva"));

        assertThat(dto.title()).isEqualTo("Nueva");
        assertThat(dto.done()).isFalse();
    }
}
