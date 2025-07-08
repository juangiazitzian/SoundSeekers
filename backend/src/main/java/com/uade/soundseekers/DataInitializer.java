package com.uade.soundseekers;

import com.uade.soundseekers.dto.EventDTO;
import com.uade.soundseekers.entity.Localidad;
import com.uade.soundseekers.entity.MusicGenre;
import com.uade.soundseekers.entity.Role;
import com.uade.soundseekers.entity.User;
import com.uade.soundseekers.repository.UserRepository;
import com.uade.soundseekers.service.EventService;
import com.uade.soundseekers.service.LocalidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private EventService eventService;

    @Autowired
    private LocalidadService localidadService;

    @Autowired
    private  PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        List<Localidad> localidades = List.of(
            Localidad.builder().nombre("Almagro").latitud(-34.6056).longitud(-58.4192).build(),
            Localidad.builder().nombre("Balvanera").latitud(-34.6099).longitud(-58.4053).build(),
            Localidad.builder().nombre("Barracas").latitud(-34.6372).longitud(-58.3831).build(),
            Localidad.builder().nombre("Belgrano").latitud(-34.5612).longitud(-58.4585).build(),
            Localidad.builder().nombre("Boedo").latitud(-34.6261).longitud(-58.4174).build(),
            Localidad.builder().nombre("Caballito").latitud(-34.6197).longitud(-58.4306).build(),
            Localidad.builder().nombre("Chacarita").latitud(-34.5871).longitud(-58.4582).build(),
            Localidad.builder().nombre("Coghlan").latitud(-34.5617).longitud(-58.4684).build(),
            Localidad.builder().nombre("Colegiales").latitud(-34.5743).longitud(-58.4497).build(),
            Localidad.builder().nombre("Constitución").latitud(-34.6284).longitud(-58.3859).build(),
            Localidad.builder().nombre("Flores").latitud(-34.6345).longitud(-58.4675).build(),
            Localidad.builder().nombre("Floresta").latitud(-34.6297).longitud(-58.4912).build(),
            Localidad.builder().nombre("La Boca").latitud(-34.6348).longitud(-58.3633).build(),
            Localidad.builder().nombre("La Paternal").latitud(-34.5969).longitud(-58.4642).build(),
            Localidad.builder().nombre("Liniers").latitud(-34.6425).longitud(-58.5201).build(),
            Localidad.builder().nombre("Mataderos").latitud(-34.6629).longitud(-58.5088).build(),
            Localidad.builder().nombre("Monte Castro").latitud(-34.6129).longitud(-58.4965).build(),
            Localidad.builder().nombre("Monserrat").latitud(-34.6113).longitud(-58.3807).build(),
            Localidad.builder().nombre("Nueva Pompeya").latitud(-34.6458).longitud(-58.4249).build(),
            Localidad.builder().nombre("Nuñez").latitud(-34.5463).longitud(-58.4544).build(),
            Localidad.builder().nombre("Palermo").latitud(-34.5826).longitud(-58.4329).build(),
            Localidad.builder().nombre("Parque Avellaneda").latitud(-34.6424).longitud(-58.4805).build(),
            Localidad.builder().nombre("Parque Chacabuco").latitud(-34.6355).longitud(-58.4409).build(),
            Localidad.builder().nombre("Parque Chas").latitud(-34.5891).longitud(-58.4827).build(),
            Localidad.builder().nombre("Parque Patricios").latitud(-34.6352).longitud(-58.4024).build(),
            Localidad.builder().nombre("Puerto Madero").latitud(-34.6091).longitud(-58.3628).build(),
            Localidad.builder().nombre("Recoleta").latitud(-34.5889).longitud(-58.3938).build(),
            Localidad.builder().nombre("Retiro").latitud(-34.5929).longitud(-58.3733).build(),
            Localidad.builder().nombre("Saavedra").latitud(-34.5639).longitud(-58.4895).build(),
            Localidad.builder().nombre("San Cristóbal").latitud(-34.6224).longitud(-58.4007).build(),
            Localidad.builder().nombre("San Nicolás").latitud(-34.6083).longitud(-58.3772).build(),
            Localidad.builder().nombre("San Telmo").latitud(-34.6215).longitud(-58.3736).build(),
            Localidad.builder().nombre("Vélez Sarsfield").latitud(-34.6412).longitud(-58.4939).build(),
            Localidad.builder().nombre("Versalles").latitud(-34.6292).longitud(-58.5104).build(),
            Localidad.builder().nombre("Villa Crespo").latitud(-34.5991).longitud(-58.4374).build(),
            Localidad.builder().nombre("Villa del Parque").latitud(-34.6089).longitud(-58.4868).build(),
            Localidad.builder().nombre("Villa Devoto").latitud(-34.5975).longitud(-58.5136).build(),
            Localidad.builder().nombre("Villa Gral. Mitre").latitud(-34.6101).longitud(-58.4697).build(),
            Localidad.builder().nombre("Villa Lugano").latitud(-34.6736).longitud(-58.4769).build(),
            Localidad.builder().nombre("Villa Luro").latitud(-34.6375).longitud(-58.5039).build(),
            Localidad.builder().nombre("Villa Ortúzar").latitud(-34.5859).longitud(-58.4717).build(),
            Localidad.builder().nombre("Villa Pueyrredón").latitud(-34.5779).longitud(-58.4998).build(),
            Localidad.builder().nombre("Villa Riachuelo").latitud(-34.6789).longitud(-58.4893).build(),
            Localidad.builder().nombre("Villa Santa Rita").latitud(-34.6151).longitud(-58.4852).build(),
            Localidad.builder().nombre("Villa Soldati").latitud(-34.6694).longitud(-58.4425).build(),
            Localidad.builder().nombre("Villa Urquiza").latitud(-34.5739).longitud(-58.4914).build()
        );

        localidadService.saveAll(localidades);

        List<User> users = List.of(
                createUser("juan.perez@gmail.com", "Juan", "Pérez", "juanp", 25, Role.ARTIST, localidades.get(0), List.of(MusicGenre.CUARTETO, MusicGenre.POP)),
                createUser("maria.gomez@gmail.com", "Maria", "Gomez", "mariag", 30, Role.ARTIST, localidades.get(1), List.of(MusicGenre.ROCK, MusicGenre.FOLKLORE)),
                createUser("carlos.lopez@gmail.com", "Carlos", "Lopez", "carlosl", 28, Role.CLIENT, localidades.get(2), List.of(MusicGenre.RUMBA, MusicGenre.INDIE)),
                createUser("ana.martinez@gmail.com", "Ana", "Martinez", "anam", 22, Role.CLIENT, localidades.get(3), List.of(MusicGenre.SALSA, MusicGenre.HIP_HOP)),
                createUser("pedro.fernandez@gmail.com", "Pedro", "Fernandez", "pedrof", 35, Role.ARTIST, localidades.get(4), List.of(MusicGenre.ELECTRONICA, MusicGenre.REGGAETON)),
                createUser("laura.rodriguez@gmail.com", "Laura", "Rodriguez", "laurar", 26, Role.CLIENT, localidades.get(5), List.of(MusicGenre.BLUES, MusicGenre.PUNK)),
                createUser("martin.sanchez@gmail.com", "Martin", "Sanchez", "martins", 31, Role.ARTIST, localidades.get(6), List.of(MusicGenre.TANGO, MusicGenre.JAZZ)),
                createUser("sofia.diaz@gmail.com", "Sofia", "Diaz", "sofiad", 27, Role.CLIENT, localidades.get(7), List.of(MusicGenre.CLASICO, MusicGenre.ALTERNATIVE)),
                createUser("lucas.perez@gmail.com", "Lucas", "Perez", "lucasp", 29, Role.ARTIST, localidades.get(8), List.of(MusicGenre.PUNK_ROCK, MusicGenre.GRUNGE)),
                createUser("elena.moreno@gmail.com", "Elena", "Moreno", "elenam", 24, Role.CLIENT, localidades.get(9), List.of(MusicGenre.BOSSA_NOVA, MusicGenre.FUSION))
        );

        userRepository.saveAll(users);

        List<EventDTO> events = List.of(
            createEventDTO("Concierto de Rock en Buenos Aires", "Una noche increíble de rock en vivo.", -34.603722, -58.381592, LocalDateTime.of(2024, 12, 10, 21, 0), 1500.0, List.of("ROCK"), users.get(0).getId(), localidades.get(0).getId()),
            createEventDTO("Festival de Jazz en Balvanera", "Jazz en Balvanera.", -34.6099, -58.4053, LocalDateTime.of(2024, 11, 5, 19, 0), 2000.0, List.of("JAZZ"), users.get(1).getId(), localidades.get(1).getId()),
            createEventDTO("Fiesta de Cumbia en Barracas", "Cumbia en Barracas.", -34.6372, -58.3831, LocalDateTime.of(2024, 12, 20, 23, 0), 1000.0, List.of("CUMBIA"), users.get(2).getId(), localidades.get(2).getId()),
            createEventDTO("Fiesta Electrónica en Belgrano", "Música electrónica en Belgrano.", -34.5612, -58.4585, LocalDateTime.of(2024, 10, 31, 22, 0), 3000.0, List.of("ELECTRONICA"), users.get(3).getId(), localidades.get(3).getId()),
            createEventDTO("Reggae en Boedo", "Reggae en Boedo.", -34.6261, -58.4174, LocalDateTime.of(2024, 10, 25, 20, 0), 1800.0, List.of("REGGAE"), users.get(4).getId(), localidades.get(4).getId()),
            createEventDTO("Hip Hop en Caballito", "Hip Hop en Caballito.", -34.6197, -58.4306, LocalDateTime.of(2024, 12, 15, 22, 0), 1200.0, List.of("HIP_HOP"), users.get(5).getId(), localidades.get(5).getId()),
            createEventDTO("Punk Rock en Chacarita", "Punk Rock en Chacarita.", -34.5871, -58.4582, LocalDateTime.of(2024, 12, 12, 22, 0), 800.0, List.of("PUNK_ROCK"), users.get(6).getId(), localidades.get(6).getId()),
            createEventDTO("Folk en Coghlan", "Folk en Coghlan.", -34.5617, -58.4684, LocalDateTime.of(2024, 12, 25, 18, 0), 1500.0, List.of("FOLKLORE"), users.get(7).getId(), localidades.get(7).getId()),
            createEventDTO("Tango en Colegiales", "Tango en Colegiales.", -34.5743, -58.4497, LocalDateTime.of(2024, 12, 10, 19, 0), 1000.0, List.of("TANGO"), users.get(8).getId(), localidades.get(8).getId()),
            createEventDTO("Reggaetón en Constitución", "Ritmos de reggaetón en Constitución.", -34.6284, -58.3859, LocalDateTime.of(2024, 12, 22, 23, 30), 1500.0, List.of("REGGAETON"), users.get(9).getId(), localidades.get(9).getId()),
            createEventDTO("Festival de Salsa en Flores", "Disfruta de un festival de salsa en Flores.", -34.6345, -58.4675, LocalDateTime.of(2024, 11, 18, 20, 0), 1300.0, List.of("SALSA"), users.get(0).getId(), localidades.get(10).getId()),
            createEventDTO("Blues en Floresta", "Noche de blues en Floresta.", -34.6297, -58.4912, LocalDateTime.of(2024, 12, 20, 19, 0), 1400.0, List.of("BLUES"), users.get(1).getId(), localidades.get(11).getId()),
            createEventDTO("Pop en La Boca", "Concierto de pop en La Boca.", -34.6348, -58.3633, LocalDateTime.of(2024, 12, 10, 21, 0), 1500.0, List.of("POP"), users.get(2).getId(), localidades.get(12).getId()),
            createEventDTO("Noche de Indie en La Paternal", "Indie en vivo en La Paternal.", -34.5969, -58.4642, LocalDateTime.of(2024, 10, 18, 21, 0), 900.0, List.of("INDIE"), users.get(3).getId(), localidades.get(13).getId()),
            createEventDTO("Festival Alternativo en Liniers", "Un festival de sonidos alternativos.", -34.6425, -58.5201, LocalDateTime.of(2024, 12, 8, 19, 0), 1100.0, List.of("ALTERNATIVE"), users.get(4).getId(), localidades.get(14).getId()),
            createEventDTO("Cuarteto en Mataderos", "Un clásico cuarteto en Mataderos.", -34.6629, -58.5088, LocalDateTime.of(2025, 1, 18, 22, 0), 1000.0, List.of("CUARTETO"), users.get(5).getId(), localidades.get(15).getId()),
            createEventDTO("Jazz en Monte Castro", "Una noche de jazz en Monte Castro.", -34.6129, -58.4965, LocalDateTime.of(2025, 2, 10, 18, 0), 1500.0, List.of("JAZZ"), users.get(6).getId(), localidades.get(16).getId()),
            createEventDTO("Rumba en Monserrat", "Rumba en Monserrat.", -34.6113, -58.3807, LocalDateTime.of(2024, 12, 20, 23, 0), 1000.0, List.of("RUMBA"), users.get(7).getId(), localidades.get(17).getId()),
            createEventDTO("Rock en Nueva Pompeya", "Concierto de rock en Nueva Pompeya.", -34.6458, -58.4249, LocalDateTime.of(2025, 3, 5, 19, 0), 2000.0, List.of("ROCK"), users.get(8).getId(), localidades.get(18).getId()),
            createEventDTO("Clásicos en Nuñez", "Concierto de música clásica en Nuñez.", -34.5463, -58.4544, LocalDateTime.of(2025, 2, 28, 18, 0), 1600.0, List.of("CLASICO"), users.get(9).getId(), localidades.get(19).getId()),
            createEventDTO("Festival de Fusion en Palermo", "Una mezcla de ritmos en Palermo.", -34.5826, -58.4329, LocalDateTime.of(2025, 1, 28, 21, 0), 1700.0, List.of("FUSION"), users.get(0).getId(), localidades.get(20).getId()),
            createEventDTO("Festival de Tango en Parque Avellaneda", "Disfruta del tango en Parque Avellaneda.", -34.6424, -58.4805, LocalDateTime.of(2025, 1, 18, 22, 0), 1000.0, List.of("TANGO"), users.get(1).getId(), localidades.get(21).getId()),
            createEventDTO("Noche de Música Latina en Parque Chacabuco", "Música latina en Parque Chacabuco.", -34.6355, -58.4409, LocalDateTime.of(2025, 3, 5, 23, 0), 1500.0, List.of("SALSA"), users.get(2).getId(), localidades.get(22).getId()),
            createEventDTO("Punk en Parque Chas", "Punk en vivo en Parque Chas.", -34.5891, -58.4827, LocalDateTime.of(2024, 12, 8, 22, 0), 1200.0, List.of("PUNK"), users.get(3).getId(), localidades.get(23).getId()),
            createEventDTO("Folk Moderno en Parque Patricios", "Folk moderno en Parque Patricios.", -34.6352, -58.4024, LocalDateTime.of(2025, 4, 12, 20, 0), 1300.0, List.of("FOLKLORE_MODERNO"), users.get(4).getId(), localidades.get(24).getId()),
            createEventDTO("Jazz en Puerto Madero", "Jazz en vivo en Puerto Madero.", -34.6091, -58.3628, LocalDateTime.of(2024, 12, 22, 20, 0), 1500.0, List.of("JAZZ"), users.get(5).getId(), localidades.get(25).getId())
        );

        events.forEach(eventService::createEvent);
    }

    private User createUser(String email, String name, String lastName, String username, int age, Role role, Localidad localidad, List<MusicGenre> genres) {
        return User.builder()
                .email(email)
                .name(name)
                .password(passwordEncoder.encode("Password123"))
                .lastName(lastName)
                .username(username)
                .edad(age)
                .isEmailVerified(true)
                .role(role)
                .localidad(localidad)
                .generosMusicalesPreferidos(genres)
                .build();
    }

    private EventDTO createEventDTO(String name, String description, double latitude, double longitude, LocalDateTime dateTime, double price, List<String> genres, Long organizerId, Long localidadId) {
        return EventDTO.builder()
                .name(name)
                .description(description)
                .latitude(latitude)
                .longitude(longitude)
                .dateTime(dateTime)
                .price(price)
                .genres(genres)
                .organizerId(organizerId)
                .localidadId(localidadId)
                .build();
    }
}