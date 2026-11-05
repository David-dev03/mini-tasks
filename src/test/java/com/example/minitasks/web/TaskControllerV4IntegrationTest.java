package com.example.minitasks.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerV4IntegrationTest {

    @Autowired
    MockMvc mvc;

    private String crear(String title) throws Exception {
        String body = mvc.perform(post("/v4/tasks").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"" + title + "\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return body.replaceAll(".*\"id\":(\\d+).*", "$1");
    }

    @Test
    void crear_devuelve201ConLocation() throws Exception {
        mvc.perform(post("/v4/tasks").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Persistida\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.title").value("Persistida"))
                .andExpect(jsonPath("$.done").value(false));
    }

    @Test
    void crear_tituloVacio_devuelve400() throws Exception {
        mvc.perform(post("/v4/tasks").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void obtenerPorId_existente_devuelve200() throws Exception {
        String id = crear("Consultar");

        mvc.perform(get("/v4/tasks/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Consultar"));
    }

    @Test
    void obtenerPorId_inexistente_devuelve404() throws Exception {
        mvc.perform(get("/v4/tasks/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void marcarHecha_existente_devuelve200YPersisteElCambio() throws Exception {
        String id = crear("Completar");

        mvc.perform(patch("/v4/tasks/" + id + "/done"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.done").value(true));

        mvc.perform(get("/v4/tasks/" + id))
                .andExpect(jsonPath("$.done").value(true));
    }

    @Test
    void marcarHecha_inexistente_devuelve404() throws Exception {
        mvc.perform(patch("/v4/tasks/99999/done"))
                .andExpect(status().isNotFound());
    }

    @Test
    void listar_filtrandoPorDone_soloDevuelveCompletadas() throws Exception {
        String id = crear("Filtrar");
        mvc.perform(patch("/v4/tasks/" + id + "/done"));
        crear("Sin completar");

        mvc.perform(get("/v4/tasks").param("done", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].done", everyItem(is(true))));
    }
}
