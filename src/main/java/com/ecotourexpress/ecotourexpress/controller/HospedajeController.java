package com.ecotourexpress.ecotourexpress.controller;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecotourexpress.ecotourexpress.model.Hospedaje;
import com.ecotourexpress.ecotourexpress.service.HospedajeService;

import jakarta.transaction.Transactional;

@RestController
@Transactional
@RequestMapping("/hospedajes")
public class HospedajeController {

    //Conexión a service
    @Autowired
    private HospedajeService hospedajeService;

    // ==========================================
    // CRUD HOSPEDAJE
    // Métodos para manejar las operaciones CRUD
    // ==========================================

    // Listar hospedaje
    @GetMapping
    @PreAuthorize("permitAll()")
    public List<Hospedaje> getAllHospedajes() {
        return hospedajeService.getAllHospedajes();
    }
    
    // Agregar o actualizar Hospedaje
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Hospedaje> newHospedaje(
            @RequestParam("tipo") String tipo,
            @RequestParam("capacidad") int capacidad,
            @RequestParam("cantidad") int cantidad,
            @RequestParam("precio") int precio,
            @RequestParam("descripcion") String descripcion,
            @RequestParam(value = "files", required = false) List<MultipartFile> files) throws IOException {

        Hospedaje hospedaje = new Hospedaje();
        hospedaje.setTipo(tipo);
        hospedaje.setCapacidad(capacidad);
        hospedaje.setCantidad(cantidad);
        hospedaje.setPrecio(precio);
        hospedaje.setDescripcion(descripcion);
        hospedaje.setDisponible(true);

        Hospedaje savedHospedaje = hospedajeService.saveHospedaje(hospedaje, files);
        return ResponseEntity.ok(savedHospedaje);
    }

    // Seleccionar hospedaje por ID (Editar)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Hospedaje> updateHospedaje(
            @PathVariable int id,
            @RequestParam("tipo") String tipo,
            @RequestParam("capacidad") int capacidad,
            @RequestParam("cantidad") int cantidad,
            @RequestParam("precio") int precio,
            @RequestParam("descripcion") String descripcion,
            @RequestParam(value = "files", required = false) List<MultipartFile> newFiles) throws IOException {

        Hospedaje hospedajeDetails = new Hospedaje();
        hospedajeDetails.setTipo(tipo);
        hospedajeDetails.setCapacidad(capacidad);
        hospedajeDetails.setCantidad(cantidad);
        hospedajeDetails.setPrecio(precio);
        hospedajeDetails.setDescripcion(descripcion);

        Hospedaje updatedHospedaje = hospedajeService.updateHospedaje(id, hospedajeDetails, newFiles);
        return ResponseEntity.ok(updatedHospedaje);
    }

    // Eliminar Hospedaje
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteHospedaje(@PathVariable int id) {
        hospedajeService.deleteHospedaje(id);
    }
}
