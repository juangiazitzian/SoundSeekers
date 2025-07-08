package com.uade.soundseekers.service;

import com.uade.soundseekers.entity.*;
import com.uade.soundseekers.exception.*;
import com.uade.soundseekers.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private EventInteractionRepository interactionRepository;

    @Autowired
    private SearchQueryRepository searchQueryRepository;

    @Autowired
    private EventDAOImpl eventRepository;

    @Autowired
    private UserRepository userRepository;

    // Construye el perfil de preferencias del usuario basado en los 'likes', 'asiste', historial de búsquedas, localidad y géneros preferidos
    public UserPreferenceProfile buildUserProfile(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new NotFoundException("Usuario no encontrado.");
        }

        User user = userOpt.get();
        List<EventInteraction> interactions = interactionRepository.findByUserId(userId);
        List<SearchQuery> searchQueries = searchQueryRepository.findByUserId(userId);

        if (interactions.isEmpty() && searchQueries.isEmpty()) {
            return new UserPreferenceProfile(new HashMap<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
        }

        Map<MusicGenre, Integer> genrePreferences = new HashMap<>();
        Set<String> likedEvents = new HashSet<>();
        Set<String> assistedEvents = new HashSet<>();
        Set<Long> preferredLocalidades = new HashSet<>();

        // Incluir la localidad primaria del usuario como localidad preferida
        if (user.getLocalidad() != null) {
            preferredLocalidades.add(user.getLocalidad().getId());
        }

        // Añadir los géneros preferidos del usuario con un peso base
        for (MusicGenre preferredGenre : user.getGenerosMusicalesPreferidos()) {
            genrePreferences.put(preferredGenre, genrePreferences.getOrDefault(preferredGenre, 0) + 2);
        }

        // Procesar interacciones para preferencias de género y localidad
        for (EventInteraction interaction : interactions) {
            if (interaction.isLiked()) likedEvents.add(interaction.getEvent().getId().toString());
            if (interaction.isAssisted()) assistedEvents.add(interaction.getEvent().getId().toString());

            for (MusicGenre genre : interaction.getEvent().getGenres()) {
                genrePreferences.put(genre, genrePreferences.getOrDefault(genre, 0) + 1);
            }

            if (interaction.getEvent().getLocalidad() != null) {
                preferredLocalidades.add(interaction.getEvent().getLocalidad().getId());
            }
        }

        // Procesar consultas de búsqueda para preferencias de género y localidad
        for (SearchQuery search : searchQueries) {
            for (MusicGenre genre : search.getGenres()) {
                genrePreferences.put(genre, genrePreferences.getOrDefault(genre, 0) + 1);
            }
            if (search.getLocalidadId() != null) {
                preferredLocalidades.add(search.getLocalidadId());
            }
        }

        return new UserPreferenceProfile(genrePreferences, likedEvents, assistedEvents, preferredLocalidades);
    }

    // Recomienda eventos basados en el perfil del usuario, incluyendo localidad y géneros preferidos
    public List<Event> recommendEvents(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        UserPreferenceProfile profile = buildUserProfile(userId);

        // Combina las preferencias basadas en interacciones con las preferencias estáticas del usuario
        Map<MusicGenre, Integer> genrePreferences = new HashMap<>(profile.getGenrePreferences());
        user.getGenerosMusicalesPreferidos().forEach(genre -> genrePreferences.putIfAbsent(genre, 1) // Preferencia estática con un puntaje bajo si no existen interacciones
        );

        Set<Long> preferredLocalidades = new HashSet<>(profile.getPreferredLocalidades());
        if (preferredLocalidades.isEmpty() && user.getLocalidad() != null) {
            preferredLocalidades.add(user.getLocalidad().getId());
        }

        List<Event> allEvents = eventRepository.findAll();
        if (allEvents.isEmpty()) {
            throw new NotFoundException("No se encontraron eventos disponibles.");
        }

        List<Event> recommendedEvents = allEvents.stream().filter(event -> event.getGenres().stream().anyMatch(genrePreferences::containsKey) || (event.getLocalidad() != null && preferredLocalidades.contains(event.getLocalidad().getId()))).sorted((e1, e2) -> {
            int genreScore1 = genrePreferences.getOrDefault(e1.getGenres().get(0), 0);
            int genreScore2 = genrePreferences.getOrDefault(e2.getGenres().get(0), 0);

            boolean localidadPreference1 = e1.getLocalidad() != null && preferredLocalidades.contains(e1.getLocalidad().getId());
            boolean localidadPreference2 = e2.getLocalidad() != null && preferredLocalidades.contains(e1.getLocalidad().getId());

            // Prioriza por preferencia de localidad, luego por puntaje de género
            if (localidadPreference1 && !localidadPreference2) return -1;
            if (!localidadPreference1 && localidadPreference2) return 1;
            return Integer.compare(genreScore2, genreScore1);
        }).limit(5).collect(Collectors.toList());

        if (recommendedEvents.size() < 5) {
            Set<Long> recommendedEventIds = recommendedEvents.stream().map(Event::getId).collect(Collectors.toSet());
            List<Event> additionalEvents = allEvents.stream().filter(event -> !recommendedEventIds.contains(event.getId())).limit(5 - recommendedEvents.size()).toList();
            recommendedEvents.addAll(additionalEvents);
        }

        return recommendedEvents;
    }
}