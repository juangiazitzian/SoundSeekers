package com.uade.soundseekers.repository;

import com.uade.soundseekers.entity.Event;
import com.uade.soundseekers.entity.EventInteraction;
import com.uade.soundseekers.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventInteractionRepository extends JpaRepository<EventInteraction, Long> {
    List<EventInteraction> findByUser(User user);
    List<EventInteraction> findByUserId(Long userId);
    Optional<EventInteraction> findByUserAndEvent(User user, Event event); // Este método ya está definido

}
