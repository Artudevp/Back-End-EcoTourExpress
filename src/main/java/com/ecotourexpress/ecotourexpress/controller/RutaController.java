package com.ecotourexpress.ecotourexpress.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/rutas")
public class RutaController {

    // Conexión a Service
    @Autowired
    private RutaService rutaService;
    
    // Obtener lista de rutas
    @GetMapping
    public List<Ruta> getAllRutas() {
        return rutaService.getAllRutas();
    }

    // Agregar o actualizar ruta
    @PostMapping
    public Ruta newRuta(@Valid @RequestBody Ruta ruta) {
        return rutaService.saveRuta(ruta);
    }

    // Seleccionar ruta por ID (Editar)
    @PutMapping("/{id}")
    public ResponseEntity<Ruta> updateRuta(@PathVariable int id, @Valid @RequestBody Ruta rutaDetails) {
        Ruta ruta = rutaService.getRutaById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrado con id: " + id));
        
        ruta.setNombre_ruta(rutaDetails.getNombre_ruta());
        ruta.setDuración_ruta(rutaDetails.getDuración_ruta());
        ruta.setPrecio(rutaDetails.getPrecio());
        ruta.setCapacidad(rutaDetails.getCapacidad());

        final Ruta updatedRuta = rutaService.saveRuta(ruta);
        return ResponseEntity.ok(updatedRuta);
    }

    // Elimar ruta
    @DeleteMapping("/{id}")
    public void deleteRuta(@PathVariable int id) {
        rutaService.deleteRuta(id);
    }


    // Agregar actividades a una ruta
    @PutMapping("/{id_ruta}/actividades")
    public ResponseEntity<Ruta> addActividadesToRuta(
            @PathVariable int id_ruta,
            @RequestBody List<Actividad> actividades) {
        Ruta updatedRuta = rutaService.addActividadesToRuta(id_ruta, actividades);
        return ResponseEntity.ok(updatedRuta);
    }


    // Obtener todas las actividades de una ruta
    @GetMapping("/{id_ruta}/actividades")
    public List<Actividad> getActividadesOfRuta(@PathVariable int id_ruta) {
        return rutaService.getActividadesOfRuta(id_ruta);
    }

    // Eliminar actividad de una ruta
    @DeleteMapping("/{id_ruta}/actividades/{id_actividad}")
    public ResponseEntity<Ruta> removeActividadFromRuta(
            @PathVariable int id_ruta,
            @PathVariable int id_actividad) {
        Ruta updatedRuta = rutaService.removeActividadFromRuta(id_ruta, id_actividad);
        return ResponseEntity.ok(updatedRuta);
    }
}
