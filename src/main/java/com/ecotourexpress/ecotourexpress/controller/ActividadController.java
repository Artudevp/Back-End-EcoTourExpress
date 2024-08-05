package com.ecotourexpress.ecotourexpress.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ecotourexpress.ecotourexpress.model.Actividad;
import com.ecotourexpress.ecotourexpress.service.ActividadService;

@RestController
@RequestMapping("/actividades")
public class ActividadController {

    @Autowired
    private ActividadService actividadService;

    @PostMapping("/nueva")
    public Actividad newActividad(@RequestBody Actividad actividad) {
        return actividadService.saveActividad(actividad);
    }

    @GetMapping("/todas")
    public List<Actividad> getAllActividades() {
        return actividadService.getAllActividades();
    }

    @DeleteMapping("/{id}")
    public void deleteActividad(@PathVariable int id) {
        actividadService.deleteActividad(id);
    }
}
