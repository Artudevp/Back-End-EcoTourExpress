package com.ecotourexpress.ecotourexpress.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ecotourexpress.ecotourexpress.model.Hospedaje;
import com.ecotourexpress.ecotourexpress.service.HospedajeService;

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

    @DeleteMapping("/{id}")
    public void deleteHospedaje(@PathVariable int id) {
        hospedajeService.deleteHospedaje(id);
    }
}
