package com.ecotourexpress.ecotourexpress.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecotourexpress.ecotourexpress.config.exception.ResourceNotFoundException;
import com.ecotourexpress.ecotourexpress.model.Hospedaje;
import com.ecotourexpress.ecotourexpress.service.HospedajeService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

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
    public Hospedaje newHospedaje(@Valid @RequestBody Hospedaje hospedaje) {
        hospedaje.setDisponible(true);
        return hospedajeService.saveHospedaje(hospedaje);
    }

    // Seleccionar hospedaje por ID (Editar)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Hospedaje> updateHospedaje(@PathVariable int id, @Valid @RequestBody Hospedaje hospedajeDetails) {
        Hospedaje hospedaje = hospedajeService.getHospedajeById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hospedaje no encontrado con id: " + id));

        hospedaje.setTipo(hospedajeDetails.getTipo());
        hospedaje.setCapacidad(hospedajeDetails.getCapacidad());
        hospedaje.setCantidad(hospedajeDetails.getCantidad());
        hospedaje.setPrecio(hospedajeDetails.getPrecio());
        hospedaje.setDisponible(true);
        hospedaje.setDescripcion(hospedajeDetails.getDescripcion());

        final Hospedaje updatedHospedaje = hospedajeService.saveHospedaje(hospedaje);
        return ResponseEntity.ok(updatedHospedaje);
    }

    // Eliminar Hospedaje
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteHospedaje(@PathVariable int id) {
        hospedajeService.deleteHospedaje(id);
    }
}
