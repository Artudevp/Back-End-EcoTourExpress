package com.ecotourexpress.ecotourexpress.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ecotourexpress.ecotourexpress.model.Actividad;
import com.ecotourexpress.ecotourexpress.repository.ActividadRepository;

@Service
public class ActividadService {

    // Conexión al repositorio de actividad
    @Autowired
    private ActividadRepository actividadRepository;

    // Crear actividad
    public Actividad saveActividad(Actividad actividad) {
        return actividadRepository.save(actividad);
    }

    // Obtener actividades
    public List<Actividad> getAllActividades() {
        return (List<Actividad>) actividadRepository.findAll();
    }

    // Seleccionar actividad por ID (Editar)
    public Optional<Actividad> getActividadById(int id) {
        return actividadRepository.findById(id);
    }

    // Eliminar actividad
    public void deleteActividad(int id) {
        actividadRepository.deleteById(id);
    }
}
