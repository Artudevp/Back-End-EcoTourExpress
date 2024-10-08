package com.ecotourexpress.ecotourexpress.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/actividades")
public class ActividadController {

    // Conexión a service
    @Autowired
    private ActividadService actividadService;

    // Obtener lista de actividades
    @GetMapping
    public List<Actividad> getAllActividades() {
        return actividadService.getAllActividades();
    }

    // Agregar o actualizar Actividad
    @PostMapping
    public Actividad newActividad(@Valid @RequestBody Actividad actividad) {
        return actividadService.saveActividad(actividad);
    }

    // Seleccionar Actividad por ID (Editar)
    @PutMapping("/{id}")
    public ResponseEntity<Actividad> updateActividad(@PathVariable int id, @Valid @RequestBody Actividad actividadDetails) {
        Actividad actividad = actividadService.getActividadById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada con id: " + id));

        actividad.setNombre_act(actividadDetails.getNombre_act());
        actividad.setDuracion_act(actividadDetails.getDuracion_act());
        actividad.setPrecio_act(actividadDetails.getPrecio_act());
        actividad.setCapacidad(actividadDetails.getCapacidad());

        final Actividad updatedActividad = actividadService.saveActividad(actividad);
        return ResponseEntity.ok(updatedActividad);
    }

    // Eliminar Actividad
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActividad(@PathVariable int id) {
        actividadService.deleteActividad(id);
        return ResponseEntity.noContent().build();
    }

}
