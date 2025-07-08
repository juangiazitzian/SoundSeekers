package com.uade.soundseekers.service;

import com.uade.soundseekers.dto.LocalidadDTO;
import com.uade.soundseekers.entity.Localidad;
import com.uade.soundseekers.exception.NotFoundException;
import com.uade.soundseekers.repository.LocalidadRepository;
import com.uade.soundseekers.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocalidadService {

    @Autowired
    private LocalidadRepository localidadRepository;

    // Obtener todas las localidades
    public List<LocalidadDTO> getAllLocalidades() {
        List<Localidad> localidades = localidadRepository.findAll();
        if (localidades.isEmpty()) {
            throw new NotFoundException("No se encontraron localidades.");
        }
        return localidades.stream()
            .map(localidad -> {
                LocalidadDTO dto = new LocalidadDTO();
                dto.setId(localidad.getId());
                dto.setNombre(localidad.getNombre());
                dto.setLatitud(localidad.getLatitud());
                dto.setLongitud(localidad.getLongitud());
                return dto;
            })
            .collect(Collectors.toList());
    }

    // Guardar todas las localidades
    public void saveAll(List<Localidad> localidades) {
        if (localidades == null || localidades.isEmpty()) {
            throw new BadRequestException("La lista de localidades no puede estar vacía.");
        }
        try {
            localidadRepository.saveAll(localidades);
        } catch (Exception e) {
            throw new BadRequestException("Hubo un error al guardar las localidades: " + e.getMessage());
        }
    }
}
