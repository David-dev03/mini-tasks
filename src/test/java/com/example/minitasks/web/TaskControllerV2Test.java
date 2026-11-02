package com.example.minitasks.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskControllerV2.class)
class TaskControllerV2Test {

    @Autowired
    MockMvc mvc;

    @Test
    void crear_devuelve201ConLocation() throws Exception {
        mvc.perform(post("/v2/tasks").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Tarea V2\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", startsWith("/v2/tasks/")))
                .andExpect(jsonPath("$.title").value("Tarea V2"))
                .andExpect(jsonPath("$.done").value(false));
    }

    @Test
    void listar_devuelve200ConArray() throws Exception {
        mvc.perform(get("/v2/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void borrar_devuelve204() throws Exception {
        mvc.perform(delete("/v2/tasks/1"))
                .andExpect(status().isNoContent());
    }
}
