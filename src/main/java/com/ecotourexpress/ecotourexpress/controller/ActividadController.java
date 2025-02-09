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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecotourexpress.ecotourexpress.model.Actividad;
import com.ecotourexpress.ecotourexpress.service.ActividadService;

import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@Transactional
@RequestMapping("/actividades")
public class ActividadController {

    // Conexión a service
    @Autowired
    private ActividadService actividadService;

    // ==========================================
    // CRUD ACTIVIDADES
    // Métodos para manejar las operaciones CRUD
    // ==========================================

    // Listar actividades
    @GetMapping
    @PreAuthorize("permitAll()")
    public List<Actividad> getAllActividades() {
        return actividadService.getAllActividades();
    }

    // Agregar o actualizar Actividad
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Actividad> newActividad(
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("capacidad") int capacidad,
            @RequestParam("precio") int precio,
            @RequestParam("duracion") int duracion,
            @RequestParam(value = "files", required = false) List<MultipartFile> files) throws IOException {

        Actividad actividad = new Actividad();
        actividad.setNombre(nombre);
        actividad.setDescripcion(descripcion);
        actividad.setDuracion(duracion);
        actividad.setCapacidad(capacidad);
        actividad.setPrecio(precio);
        actividad.setDisponible(true);
        
        Actividad savedActividad = actividadService.saveActividad(actividad, files);
        return ResponseEntity.ok(savedActividad);
    }

    // Seleccionar Actividad por ID (Editar)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Actividad> updateActividad(
            @PathVariable int id,
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("capacidad") int capacidad,
            @RequestParam("duracion") int duracion,
            @RequestParam("precio") int precio,
            @RequestParam(value = "files", required = false) List<MultipartFile> newFiles) throws IOException {

        Actividad actividadDetails = new Actividad();
        actividadDetails.setNombre(nombre);
        actividadDetails.setDescripcion(descripcion);
        actividadDetails.setDuracion(duracion);
        actividadDetails.setCapacidad(capacidad);
        actividadDetails.setPrecio(precio);
        
        Actividad updatedActividad = actividadService.updateActividad(id, actividadDetails, newFiles);
        return ResponseEntity.ok(updatedActividad);
    }

    // Eliminar Actividad
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteActividad(@PathVariable int id) {
        actividadService.deleteActividad(id);
    }

}
