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
import com.ecotourexpress.ecotourexpress.model.Actividad;
import com.ecotourexpress.ecotourexpress.model.Ruta;
import com.ecotourexpress.ecotourexpress.service.RutaService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@Transactional
@RequestMapping("/rutas")
public class RutaController {

    // Conexión a Service
    @Autowired
    private RutaService rutaService;

    // ==========================================
    // CRUD CLIENTES
    // Métodos para manejar las operaciones CRUD
    // ==========================================
    
    // Obtener lista de rutas
    @GetMapping
    @PreAuthorize("permitAll()")
    public List<Ruta> getAllRutas() {
        return rutaService.getAllRutas();
    }

    // Agregar o actualizar ruta
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Ruta newRuta(@Valid @RequestBody Ruta ruta) {
        ruta.setDisponible(true);
        return rutaService.saveRuta(ruta);
    }

    // Seleccionar ruta por ID (Editar)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Ruta> updateRuta(@PathVariable int id, @Valid @RequestBody Ruta rutaDetails) {
        Ruta ruta = rutaService.getRutaById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrado con id: " + id));
        
        ruta.setNombre(rutaDetails.getNombre());
        ruta.setDuracion(rutaDetails.getDuracion());
        ruta.setPrecio(rutaDetails.getPrecio());
        ruta.setCapacidad(rutaDetails.getCapacidad());
        ruta.setDescripcion(rutaDetails.getDescripcion());
        ruta.setDisponible(true);

        final Ruta updatedRuta = rutaService.saveRuta(ruta);
        return ResponseEntity.ok(updatedRuta);
    }

    // Elimar ruta
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteRuta(@PathVariable int id) {
        rutaService.deleteRuta(id);
    }

    // ==========================================
    // RUTAS - ACTIVIDADES
    // Métodos para relacionar actividades
    // ==========================================


    // Agregar actividades a una ruta
    @PutMapping("/{id_ruta}/actividades")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Ruta> addActividadesToRuta(
            @PathVariable int id_ruta,
            @RequestBody List<Integer> actividadIds) {
        Ruta updatedRuta = rutaService.addActividadesToRuta(id_ruta, actividadIds);
        return ResponseEntity.ok(updatedRuta);
    }


    // Obtener todas las actividades de una ruta
    @GetMapping("/{id_ruta}/actividades")
    @PreAuthorize("permitAll()")
    public List<Actividad> getActividadesOfRuta(@PathVariable int id_ruta) {
        return rutaService.getActividadesOfRuta(id_ruta);
    }

    // Eliminar actividad de una ruta
    @DeleteMapping("/{id_ruta}/actividades/{id_actividad}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Ruta> removeActividadFromRuta(
            @PathVariable int id_ruta,
            @PathVariable int id_actividad) {
        Ruta updatedRuta = rutaService.removeActividadFromRuta(id_ruta, id_actividad);
        return ResponseEntity.ok(updatedRuta);
    }
}
