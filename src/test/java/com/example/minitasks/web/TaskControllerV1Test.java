package com.example.minitasks.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskControllerV1.class)
class TaskControllerV1Test {

    @Autowired
    MockMvc mvc;

    @Test
    void ping_devuelvePong() throws Exception {
        mvc.perform(get("/v1/ping"))
                .andExpect(status().isOk())
                .andExpect(content().string("pong"));
    }

    @Test
    void crear_devuelve201ConTareaNoCompletada() throws Exception {
        mvc.perform(post("/v1/tasks").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Estudiar Spring\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Estudiar Spring"))
                .andExpect(jsonPath("$.done").value(false));
    }

    @Test
    void listar_incluyeLaTareaCreada() throws Exception {
        mvc.perform(post("/v1/tasks").contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Tarea listada\"}"));

        mvc.perform(get("/v1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.title == 'Tarea listada')]").exists());
    }

    @Test
    void obtenerPorId_existente_devuelve200ConLaTarea() throws Exception {
        String body = mvc.perform(post("/v1/tasks").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Buscar por id\"}"))
                .andReturn().getResponse().getContentAsString();
        String id = body.replaceAll(".*\"id\":(\\d+).*", "$1");

        mvc.perform(get("/v1/tasks/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(Long.parseLong(id)))
                .andExpect(jsonPath("$.title").value("Buscar por id"))
                .andExpect(jsonPath("$.done").value(false));
    }

    @Test
    void obtenerPorId_inexistente_devuelve404() throws Exception {
        mvc.perform(get("/v1/tasks/9999"))
                .andExpect(status().isNotFound());
    }
}
