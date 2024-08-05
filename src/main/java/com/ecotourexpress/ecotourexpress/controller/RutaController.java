package com.ecotourexpress.ecotourexpress.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ecotourexpress.ecotourexpress.model.Ruta;
import com.ecotourexpress.ecotourexpress.service.RutaService;

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

    @DeleteMapping("/{id}")
    public void deleteRuta(@PathVariable int id) {
        rutaService.deleteRuta(id);
    }
}
