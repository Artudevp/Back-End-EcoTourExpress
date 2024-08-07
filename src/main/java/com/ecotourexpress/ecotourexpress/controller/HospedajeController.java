package com.ecotourexpress.ecotourexpress.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecotourexpress.ecotourexpress.model.Hospedaje;
import com.ecotourexpress.ecotourexpress.service.HospedajeService;

import exception.ResourceNotFoundException;

@RestController
@RequestMapping("/hospedajes")
public class HospedajeController {

    @Autowired
    private HospedajeService hospedajeService;

    @PostMapping("/nuevo")
    public Hospedaje newHospedaje(@RequestBody Hospedaje hospedaje) {
        return hospedajeService.saveHospedaje(hospedaje);
    }

    @GetMapping("/todos")
    public List<Hospedaje> getAllHospedajes() {
        return hospedajeService.getAllHospedajes();
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<Hospedaje> updateHospedaje(@PathVariable int id, @RequestBody Hospedaje hospedajeDetails) {
        Hospedaje hospedaje = hospedajeService.getHospedajeById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Hospedaje no encontrado con id: " + id));
        
        hospedaje.setTipo_hab(hospedajeDetails.getTipo_hab());
        hospedaje.setCapacidad(hospedajeDetails.getCapacidad());
        hospedaje.setDisponibilidad(hospedajeDetails.getDisponibilidad());
        hospedaje.setPrecio_hab(hospedajeDetails.getPrecio_hab());

        final Hospedaje updatedHospedaje = hospedajeService.saveHospedaje(hospedaje);
        return ResponseEntity.ok(updatedHospedaje);
    }

    @PostMapping("/delete/{id}")
    public void deleteHospedaje(@PathVariable int id) {
        hospedajeService.deleteHospedaje(id);
    }
}
