package com.uade.soundseekers.service;

import com.uade.soundseekers.entity.Image;
import com.uade.soundseekers.repository.ImageRepository;
import com.uade.soundseekers.exception.BadRequestException;
import com.uade.soundseekers.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    // Obtener todas las imágenes
    public List<Image> getAllImages() {
        List<Image> images = imageRepository.findAll();
        if (images.isEmpty()) {
            throw new NotFoundException("No se encontraron imágenes.");
        }
        return images;
    }

    // Obtener imágenes por nombre
    public List<Image> getImagesByName(String imageName) {
        if (imageName == null || imageName.trim().isEmpty()) {
            throw new BadRequestException("El nombre de la imagen no puede estar vacío.");
        }
        List<Image> images = imageRepository.findByImageName(imageName);
        if (images.isEmpty()) {
            throw new NotFoundException("No se encontraron imágenes con el nombre: " + imageName);
        }
        return images;
    }

    // Obtener imágenes por evento
    public List<Image> getImagesByEvent(Long event_id) {
        if (event_id == null || event_id <= 0) {
            throw new BadRequestException("El ID de evento no es válido.");
        }
        List<Image> images = imageRepository.findByEventId(event_id);
        if (images.isEmpty()) {
            throw new NotFoundException("No se encontraron imágenes para el evento con ID: " + event_id);
        }
        return images;
    }

    // Guardar una nueva imagen
    public Image saveImage(Image image) {
        if (image == null || image.getImageName() == null || image.getImageName().trim().isEmpty()) {
            throw new BadRequestException("La imagen no tiene un nombre válido.");
        }
        try {
            return imageRepository.save(image);
        } catch (Exception e) {
            throw new BadRequestException("Hubo un error al guardar la imagen: " + e.getMessage());
        }
    }

    // Eliminar una imagen por ID
    public void deleteImage(int id) {
        Image image = imageRepository.findById(id).orElseThrow(() -> new NotFoundException("No se encontró la imagen con ID: " + id));
        imageRepository.delete(image);
    }
}






