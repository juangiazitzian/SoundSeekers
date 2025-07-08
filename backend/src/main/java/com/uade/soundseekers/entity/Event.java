package com.uade.soundseekers.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Getter
@Setter
@Table(name = "Event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Double latitude;

    private Double longitude;

    private LocalDateTime dateTime;

    private Double price;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Image> images = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "event_users", joinColumns = @JoinColumn(name = "event_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> attendees = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "event_likes_users", joinColumns = @JoinColumn(name = "event_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> likes = new ArrayList<>();

    @ElementCollection(targetClass = MusicGenre.class)
    @Enumerated(EnumType.STRING)
    private List<MusicGenre> genres = new ArrayList<>();

    @ManyToOne
    private User organizer;

    @ManyToOne
    @JoinColumn(name = "localidad_id", nullable = true)
    private Localidad localidad;
}