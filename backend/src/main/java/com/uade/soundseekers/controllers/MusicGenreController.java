package com.uade.soundseekers.controllers;

import com.uade.soundseekers.entity.MusicGenre;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/music-genres")
public class MusicGenreController {

    @GetMapping
    public ResponseEntity<List<MusicGenre>> getAllMusicGenres() {
        List<MusicGenre> genres = Arrays.asList(MusicGenre.values());
        return ResponseEntity.ok(genres);
    }
}
