package com.uade.soundseekers.controllers;

import com.uade.soundseekers.dto.MessageResponseDto;
import com.uade.soundseekers.service.LocalidadService;
import com.uade.soundseekers.service.UserInteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/user-interactions")
public class UserInteractionController {

    private final UserInteractionService userInteractionService;

    @Autowired
    public UserInteractionController(UserInteractionService userInteractionService) {
        this.userInteractionService = userInteractionService;
    }

    @PostMapping("/{userId}/events/{eventId}/like")
    public ResponseEntity<MessageResponseDto> recordLike(@PathVariable Long userId, @PathVariable Long eventId) {
        return ResponseEntity.ok(userInteractionService.recordLike(userId, eventId));
    }

    @DeleteMapping("/{userId}/events/{eventId}/like")
    public ResponseEntity<MessageResponseDto> deleteLike(@PathVariable Long userId, @PathVariable Long eventId) {
        return ResponseEntity.ok(userInteractionService.toggleLike(userId, eventId));
    }

    @PostMapping("/{userId}/events/{eventId}/assist")
    public ResponseEntity<MessageResponseDto> recordAssist(@PathVariable Long userId, @PathVariable Long eventId) {
        return ResponseEntity.ok(userInteractionService.recordAssist(userId, eventId));
    }

    @DeleteMapping("/{userId}/events/{eventId}/assist")
    public ResponseEntity<MessageResponseDto> deleteAssist(@PathVariable Long userId, @PathVariable Long eventId) {
        return ResponseEntity.ok(userInteractionService.toggleAssist(userId, eventId));
    }

    @PostMapping("/{userId}/search")
    public ResponseEntity<MessageResponseDto> recordSearch(
        @PathVariable Long userId,
        @RequestParam(required = false) List<String> genres,
        @RequestParam(required = false) Double minPrice,
        @RequestParam(required = false) Double maxPrice,
        @RequestParam(required = false) LocalDateTime startDateTime,
        @RequestParam(required = false) LocalDateTime endDateTime,
        @RequestParam(required = false) Long localidadId) {
        return ResponseEntity.ok(userInteractionService.recordSearch(userId, genres, minPrice, maxPrice, startDateTime, endDateTime, localidadId));
    }
}