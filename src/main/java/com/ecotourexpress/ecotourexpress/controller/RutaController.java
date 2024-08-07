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

import com.ecotourexpress.ecotourexpress.model.Ruta;
import com.ecotourexpress.ecotourexpress.service.RutaService;

import exception.ResourceNotFoundException;

@RestController
@RequestMapping("/rutas")
public class RutaController {

    @Autowired
    private RutaService rutaService;

    @PostMapping("/nueva")
    public Ruta newRuta(@RequestBody Ruta ruta) {
        return rutaService.saveRuta(ruta);
    }

    @GetMapping("/todas")
    public List<Ruta> getAllRutas() {
        return rutaService.getAllRutas();
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<Ruta> updateRuta(@PathVariable int id, @RequestBody Ruta rutaDetails) {
        Ruta ruta = rutaService.getRutaById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrado con id: " + id));
        
        ruta.setNombre_ruta(rutaDetails.getNombre_ruta());
        ruta.setDuración_ruta(rutaDetails.getDuración_ruta());
        ruta.setPrecio(rutaDetails.getPrecio());
        ruta.setActividades(rutaDetails.getActividades());
        ruta.setClientes(rutaDetails.getClientes());

        final Ruta updatedRuta = rutaService.saveRuta(ruta);
        return ResponseEntity.ok(updatedRuta);
    }

    @PostMapping("/delete/{id}")
    public void deleteRuta(@PathVariable int id) {
        rutaService.deleteRuta(id);
    }
}
