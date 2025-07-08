package com.uade.soundseekers.controllers;

import com.uade.soundseekers.entity.Event;
import com.uade.soundseekers.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @Autowired
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Event>> recommendEventsForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(recommendationService.recommendEvents(userId));
    }
}
