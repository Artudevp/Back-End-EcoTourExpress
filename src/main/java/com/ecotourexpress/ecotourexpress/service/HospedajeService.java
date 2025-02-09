package com.ecotourexpress.ecotourexpress.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecotourexpress.ecotourexpress.model.Hospedaje;
import com.ecotourexpress.ecotourexpress.repository.HospedajeRepository;

import jakarta.transaction.Transactional;

@Service
public class HospedajeService {

    // Conexion a repositorio de Hospedaje
    @Autowired
    private HospedajeRepository hospedajeRepository;

    // ==========================================
    // CRUD HOSPEDAJE
    // Métodos para manejar el CRUD
    // ==========================================

    @Autowired
    private MediaService mediaService;

    public Hospedaje saveHospedaje(Hospedaje hospedaje, List<MultipartFile> files) throws IOException {
        if (files != null && !files.isEmpty()) {
            List<String> mediaUrls = mediaService.uploadImages(files);
            hospedaje.setMediaUrls(mediaUrls);
        }
        return hospedajeRepository.save(hospedaje);
    }

    // Obtener lista de hospedajes
    @Transactional
    public List<Hospedaje> getAllHospedajes() {
        List<Hospedaje> hospedajes = StreamSupport
            .stream(hospedajeRepository.findAll().spliterator(), false)
            .collect(Collectors.toList());

        hospedajes.forEach(h -> h.getMediaUrls().size());
        return hospedajes;
    }

    // Editar hospedaje
    @Transactional
    public Hospedaje updateHospedaje(int id, Hospedaje hospedajeDetails, List<MultipartFile> newFiles) throws IOException {
        Hospedaje hospedaje = hospedajeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hospedaje no encontrado"));

        hospedaje.setTipo(hospedajeDetails.getTipo());
        hospedaje.setCapacidad(hospedajeDetails.getCapacidad());
        hospedaje.setCantidad(hospedajeDetails.getCantidad());
        hospedaje.setPrecio(hospedajeDetails.getPrecio());
        hospedaje.setDescripcion(hospedajeDetails.getDescripcion());

        List<String> currentMediaUrls = hospedaje.getMediaUrls();
        if (newFiles != null && !newFiles.isEmpty()) {
            List<String> newMediaUrls = mediaService.uploadImages(newFiles);
            if (currentMediaUrls == null) {
                currentMediaUrls = new ArrayList<>();
            }
            currentMediaUrls.addAll(newMediaUrls);
        }
        hospedaje.setMediaUrls(currentMediaUrls);

        return hospedajeRepository.save(hospedaje);
    }

    // Eliminar hospedaje
    public void deleteHospedaje(int id) {
        Hospedaje hospedaje = hospedajeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hospedaje no encontrado"));

        mediaService.deleteImages(hospedaje.getMediaUrls());
        hospedajeRepository.delete(hospedaje);
    }
}
