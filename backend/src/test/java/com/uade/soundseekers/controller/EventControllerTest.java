/*
package com.uade.soundseekers.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uade.soundseekers.controllers.EventController;
import com.uade.soundseekers.entity.Event;
import com.uade.soundseekers.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventController.class)
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Autowired
    private ObjectMapper objectMapper;

    private Event event1;
    private Event event2;

    @BeforeEach
    public void setup() {
        event1 = new Event();
        event1.setId(1L);
        event1.setName("Concierto Rock");
        event1.setDescription("Un gran concierto de rock.");
        event1.setLocation("Buenos Aires");
        event1.setGenre("Rock");
        event1.setDateTime(LocalDateTime.of(2024, 12, 25, 20, 0));
        event1.setPrice(50.0);
        // Asigna un organizador si es necesario

        event2 = new Event();
        event2.setId(2L);
        event2.setName("Festival Jazz");
        event2.setDescription("Festival anual de jazz.");
        event2.setLocation("Córdoba");
        event2.setGenre("Jazz");
        event2.setDateTime(LocalDateTime.of(2025, 1, 15, 18, 0));
        event2.setPrice(30.0);
        // Asigna un organizador si es necesario
    }

    // **1. Test para obtener todos los eventos**
    @Test
    public void testGetAllEvents() throws Exception {
        List<Event> events = Arrays.asList(event1, event2);

        given(eventService.getAllEvents()).willReturn(events);

        mockMvc.perform(get("/api/events")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(events.size())))
                .andExpect(jsonPath("$[0].name", is(event1.getName())))
                .andExpect(jsonPath("$[1].name", is(event2.getName())));
    }

    // **2. Test para obtener un evento por ID (existente)**
    @Test
    public void testGetEventById_Exists() throws Exception {
        given(eventService.getEventById(1L)).willReturn(Optional.of(event1));

        mockMvc.perform(get("/api/events/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(event1.getName())))
                .andExpect(jsonPath("$.description", is(event1.getDescription())));
    }

    // **3. Test para obtener un evento por ID (no existente)**
    @Test
    public void testGetEventById_NotExists() throws Exception {
        given(eventService.getEventById(99L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/events/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // **4. Test para crear un nuevo evento**
    @Test
    public void testCreateEvent() throws Exception {
        Event newEvent = new Event();
        newEvent.setName("Concierto Pop");
        newEvent.setDescription("Concierto de música pop.");
        newEvent.setLocation("Rosario");
        newEvent.setGenre("Pop");
        newEvent.setDateTime(LocalDateTime.of(2025, 5, 20, 19, 30));
        newEvent.setPrice(40.0);

        given(eventService.saveEvent(ArgumentMatchers.any(Event.class))).willReturn(newEvent);

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(newEvent.getName())))
                .andExpect(jsonPath("$.location", is(newEvent.getLocation())));
    }

    // **5. Test para actualizar un evento existente**
    @Test
    public void testUpdateEvent_Exists() throws Exception {
        Event updatedEvent = new Event();
        updatedEvent.setEvent_id(1L);
        updatedEvent.setName("Concierto Rock Actualizado");
        updatedEvent.setDescription("Un gran concierto de rock con actualizaciones.");
        updatedEvent.setLocation("Buenos Aires");
        updatedEvent.setGenre("Rock");
        updatedEvent.setDateTime(LocalDateTime.of(2024, 12, 25, 21, 0));
        updatedEvent.setPrice(60.0);

        given(eventService.updateEvent(eq(1L), (Event) any(Event.class))).willReturn(updatedEvent);

        mockMvc.perform(put("/api/events/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEvent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(updatedEvent.getName())))
                .andExpect(jsonPath("$.price", is(updatedEvent.getPrice())));
    }

    // **6. Test para actualizar un evento que no existe**
    @Test
    public void testUpdateEvent_NotExists() throws Exception {
        Event updatedEvent = new Event();
        updatedEvent.setName("Concierto Rock Actualizado");
        updatedEvent.setDescription("Un gran concierto de rock con actualizaciones.");
        updatedEvent.setLocation("Buenos Aires");
        updatedEvent.setGenre("Rock");
        updatedEvent.setDateTime(LocalDateTime.of(2024, 12, 25, 21, 0));
        updatedEvent.setPrice(60.0);

        given(eventService.updateEvent(eq(99L), (Event) any(Event.class))).willThrow(new RuntimeException("Evento no encontrado con el ID: 99"));

        mockMvc.perform(put("/api/events/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedEvent)))
                .andExpect(status().isNotFound());
    }

    // **7. Test para eliminar un evento existente**
    @Test
    public void testDeleteEvent_Exists() throws Exception {
        willDoNothing().given(eventService).deleteEvent(1L);

        mockMvc.perform(delete("/api/events/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    // **8. Test para eliminar un evento que no existe**
    @Test
    public void testDeleteEvent_NotExists() throws Exception {
        willThrow(new RuntimeException("Evento no encontrado con el ID: 99")).given(eventService).deleteEvent(99L);

        mockMvc.perform(delete("/api/events/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // **9. Test para buscar eventos por ubicación y género**
    @Test
    public void testGetEventsByLocationAndGenre() throws Exception {
        List<Event> events = Arrays.asList(event1);

        given(eventService.getEventsByLocationAndGenre("Buenos Aires", "Rock")).willReturn(events);

        mockMvc.perform(get("/api/events/search")
                        .param("location", "Buenos Aires")
                        .param("genre", "Rock")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(events.size())))
                .andExpect(jsonPath("$[0].location", is("Buenos Aires")))
                .andExpect(jsonPath("$[0].genre", is("Rock")));
    }

    // **10. Test para buscar eventos por rango de fechas**
    @Test
    public void testGetEventsByDateRange() throws Exception {
        List<Event> events = Arrays.asList(event1, event2);
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 12, 31, 23, 59);

        given(eventService.getEventsByDateRange(startDate, endDate)).willReturn(events);

        mockMvc.perform(get("/api/events/date-range")
                        .param("startDateTime", startDate.toString())
                        .param("endDateTime", endDate.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(events.size())));
    }

    // **11. Test para buscar eventos por precio máximo**
    @Test
    public void testGetEventsByPrice() throws Exception {
        List<Event> events = Arrays.asList(event2);

        given(eventService.getEventsByPriceLessThanEqual(30.0)).willReturn(events);

        mockMvc.perform(get("/api/events/price")
                        .param("price", "30.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(events.size())))
                .andExpect(jsonPath("$[0].price", is(30.0)));
    }

    // **12. Test para buscar eventos por género y rango de fechas**
    @Test
    public void testGetEventsByGenreAndDateRange() throws Exception {
        List<Event> events = Arrays.asList(event1);
        LocalDateTime startDate = LocalDateTime.of(2024, 12, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 23, 59);

        given(eventService.getEventsByGenreAndDateRange("Rock", startDate, endDate)).willReturn(events);

        mockMvc.perform(get("/api/events/genre-date-range")
                        .param("genre", "Rock")
                        .param("startDateTime", startDate.toString())
                        .param("endDateTime", endDate.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(events.size())))
                .andExpect(jsonPath("$[0].genre", is("Rock")))
                .andExpect(jsonPath("$[0].dateTime", is(event1.getDateTime().toString())));
    }

    // **13. Test para buscar eventos por ubicación, género y rango de fechas**
    @Test
    public void testGetEventsByLocationAndGenreAndDateRange() throws Exception {
        List<Event> events = Arrays.asList(event1);
        LocalDateTime startDate = LocalDateTime.of(2024, 12, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 23, 59);

        given(eventService.getEventsByLocationAndGenreAndDateRange("Buenos Aires", "Rock", startDate, endDate))
                .willReturn(events);

        mockMvc.perform(get("/api/events/search-advanced")
                        .param("location", "Buenos Aires")
                        .param("genre", "Rock")
                        .param("startDateTime", startDate.toString())
                        .param("endDateTime", endDate.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(events.size())))
                .andExpect(jsonPath("$[0].location", is("Buenos Aires")))
                .andExpect(jsonPath("$[0].genre", is("Rock")))
                .andExpect(jsonPath("$[0].dateTime", is(event1.getDateTime().toString())));
    }
}*/
