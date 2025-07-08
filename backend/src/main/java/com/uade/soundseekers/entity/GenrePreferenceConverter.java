package com.uade.soundseekers.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.uade.soundseekers.entity.MusicGenre;

@Converter
public class GenrePreferenceConverter implements AttributeConverter<Map<MusicGenre, Integer>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<MusicGenre, Integer> genrePreferences) {
        try {
            return objectMapper.writeValueAsString(genrePreferences);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting Map to JSON string: " + e.getMessage());
        }
    }

    @Override
    public Map<MusicGenre, Integer> convertToEntityAttribute(String genrePreferencesJson) {
        try {
            return objectMapper.readValue(genrePreferencesJson, Map.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error converting JSON string to Map: " + e.getMessage());
        }
    }
}
