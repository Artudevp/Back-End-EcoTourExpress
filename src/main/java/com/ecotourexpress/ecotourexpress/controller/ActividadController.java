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
import com.ecotourexpress.ecotourexpress.model.Actividad;
import com.ecotourexpress.ecotourexpress.service.ActividadService;

import exception.ResourceNotFoundException;

import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/actividades")
public class ActividadController {

    @Autowired
    private ActividadService actividadService;

    @GetMapping
    public List<Actividad> getAllActividades() {
        return actividadService.getAllActividades();
    }

    @PostMapping
    public Actividad newActividad(@RequestBody Actividad actividad) {
        return actividadService.saveActividad(actividad);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Actividad> updateActividad(@PathVariable int id, @RequestBody Actividad actividadDetails) {
        Actividad actividad = actividadService.getActividadById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada con id: " + id));

        actividad.setNombre_act(actividadDetails.getNombre_act());
        actividad.setDuración_act(actividadDetails.getDuración_act());
        actividad.setPrecio_act(actividadDetails.getPrecio_act());
        actividad.setClientes(actividadDetails.getClientes());
        actividad.setRutas(actividadDetails.getRutas());

        final Actividad updatedActividad = actividadService.saveActividad(actividad);
        return ResponseEntity.ok(updatedActividad);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActividad(@PathVariable int id) {
        actividadService.deleteActividad(id);
        return ResponseEntity.noContent().build();
    }

}
