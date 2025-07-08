package com.uade.soundseekers.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.uade.soundseekers.entity.Image;
import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

    // Metodo para encontrar imágenes por nombre de imagen
    List<Image> findByImageName(String imageName);

    // Metodo para encontrar imágenes asociadas a un evento específico
    List<Image> findByEventId(Long event_id);
}