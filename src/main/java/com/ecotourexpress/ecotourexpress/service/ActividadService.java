package com.ecotourexpress.ecotourexpress.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecotourexpress.ecotourexpress.model.Actividad;
import com.ecotourexpress.ecotourexpress.repository.ActividadRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ActividadService {

    // Conexiones
    @Autowired
    private ActividadRepository actividadRepository;

    @Autowired
    private MediaService mediaService;

    // ==========================================
    // CRUD ACTIVIDADES
    // Métodos para manejar las operaciones CRUD
    // ==========================================

    // Crear actividad
    public Actividad saveActividad(Actividad actividad, List<MultipartFile> files) throws IOException {
        if (files != null && !files.isEmpty()) {
            List<String> mediaUrls = mediaService.uploadImages(files);
            actividad.setMediaUrls(mediaUrls);
        }
        return actividadRepository.save(actividad);
    }

    // Actualizar actividad conservando imágenes anteriores
    public Actividad updateActividad(int id, Actividad actividadDetails, List<MultipartFile> newFiles) throws IOException {
        Actividad actividad = actividadRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Actividad no encontrada"));

        // Actualizar los datos básicos
        actividad.setNombre(actividadDetails.getNombre());
        actividad.setDescripcion(actividadDetails.getDescripcion());
        actividad.setDuracion(actividadDetails.getDuracion());
        actividad.setCapacidad(actividadDetails.getCapacidad());
        actividad.setPrecio(actividadDetails.getPrecio());
        
        // Manejo de imágenes
        List<String> currentMediaUrls = actividad.getMediaUrls();
        if (newFiles != null && !newFiles.isEmpty()) {
            List<String> newMediaUrls = mediaService.uploadImages(newFiles);
            if (currentMediaUrls == null) {
                currentMediaUrls = new ArrayList<>();
            }
            currentMediaUrls.addAll(newMediaUrls);
        }
        actividad.setMediaUrls(currentMediaUrls);
        
        return actividadRepository.save(actividad);
    }

    // Obtener actividades
    @Transactional
    public List<Actividad> getAllActividades() {
        List<Actividad> actividades = StreamSupport
            .stream(actividadRepository.findAll().spliterator(), false)
            .collect(Collectors.toList());
        
        actividades.forEach(a -> a.getMediaUrls().size());
        return actividades;
    }

    // Eliminar actividad
    public void deleteActividad(int id) {
        Actividad actividad = actividadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));
        
        mediaService.deleteImages(actividad.getMediaUrls());
        actividadRepository.delete(actividad);
    }
}
