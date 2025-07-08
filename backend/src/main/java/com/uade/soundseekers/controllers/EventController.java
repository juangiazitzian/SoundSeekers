package com.uade.soundseekers.controllers;

import com.uade.soundseekers.dto.EventDTO;
import com.uade.soundseekers.entity.Event;
import com.uade.soundseekers.entity.Image;
import com.uade.soundseekers.entity.MusicGenre;
import com.uade.soundseekers.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://front-seminario.s3-website.us-east-2.amazonaws.com/")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // Obtener todos los eventos
    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    // Crear un nuevo evento
    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody EventDTO eventDTO) {
        return ResponseEntity.ok(eventService.createEvent(eventDTO));
    }

    // Editar un evento existente
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @RequestBody EventDTO eventDTO) {
        return ResponseEntity.ok(eventService.updateEvent(id, eventDTO));
    }

    // Eliminar un evento
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.deleteEvent(id));
    }

    // Filtros avanzados
    @GetMapping("/filters")
    public List<Event> getEventsByFilters(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) List<String> genres,
        @RequestParam(required = false) LocalDateTime startDate,
        @RequestParam(required = false) LocalDateTime endDate,
        @RequestParam(required = false) Double minPrice,
        @RequestParam(required = false) Double maxPrice,
        @RequestParam(required = false) Long localidadId) {
        // Convierte la lista de Strings a una lista de Enums manualmente
        List<MusicGenre> genreEnumList = null;
        if (genres != null) {
            genreEnumList = genres.stream().map(genre -> MusicGenre.valueOf(genre.toUpperCase())) // Convierte cada String a su Enum correspondiente
                .collect(Collectors.toList());
        }
        return eventService.getEventsByFilters(name, genreEnumList, startDate, endDate, minPrice, maxPrice, localidadId);
    }

    // Búsqueda de eventos por proximidad (latitud, longitud, radio)
    @GetMapping("/proximity")
    public List<Event> searchEventsByProximity(@RequestParam Double lat, @RequestParam Double lng, @RequestParam Double radius) {
        return eventService.searchEventsByProximity(lat, lng, radius);
    }

    @PostMapping("/{id}/images")
    public ResponseEntity<Void> addImageToEvent(@PathVariable Long id, @RequestBody Image image) {
        eventService.addImageToEvent(id, image);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/images/{imageId}")
    public ResponseEntity<Void> removeImageFromEvent(@PathVariable Long id, @PathVariable Long imageId) {
        eventService.removeImageFromEvent(id, imageId);
        return ResponseEntity.ok().build();
    }


    //listado de eventos por artista
    @GetMapping("/artist/{artistId}")
    public ResponseEntity<List<Event>> getEventsByArtistId(@PathVariable Long artistId) {
        List<Event> events = eventService.getEventsByArtistId(artistId);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/user/{userId}/attending")
    public List<Event> getEventsByUserAttendance(@PathVariable Long userId) {
        return eventService.getEventsByUserAttendance(userId);
    }

    @GetMapping("/user/{userId}/likes")
    public List<Event> getEventsByUserLike(@PathVariable Long userId) {
        return eventService.getEventsByUserLikes(userId);
    }

}