package com.ecotourexpress.ecotourexpress.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecotourexpress.ecotourexpress.config.exception.ResourceNotFoundException;
import com.ecotourexpress.ecotourexpress.model.Actividad;
import com.ecotourexpress.ecotourexpress.service.ActividadService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

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
    public Actividad newActividad(@Valid @RequestBody Actividad actividad) {
        actividad.setDisponible(true);
        return actividadService.saveActividad(actividad);
    }

    // Seleccionar Actividad por ID (Editar)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Actividad> updateActividad(@PathVariable int id, @Valid @RequestBody Actividad actividadDetails) {
        Actividad actividad = actividadService.getActividadById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada con id: " + id));

        actividad.setNombre(actividadDetails.getNombre());
        actividad.setDuracion(actividadDetails.getDuracion());
        actividad.setPrecio(actividadDetails.getPrecio());
        actividad.setCapacidad(actividadDetails.getCapacidad());
        actividad.setDisponible(true);
        actividad.setDescripcion(actividadDetails.getDescripcion());

        final Actividad updatedActividad = actividadService.saveActividad(actividad);
        return ResponseEntity.ok(updatedActividad);
    }

    // Eliminar Actividad
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteActividad(@PathVariable int id) {
        actividadService.deleteActividad(id);
        return ResponseEntity.noContent().build();
    }

}
