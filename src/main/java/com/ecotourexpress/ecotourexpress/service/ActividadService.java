package com.ecotourexpress.ecotourexpress.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ecotourexpress.ecotourexpress.model.Actividad;
import com.ecotourexpress.ecotourexpress.repository.ActividadRepository;

@Service
public class ActividadService {

    @Autowired
    private ActividadRepository actividadRepository;

    public Actividad saveActividad(Actividad actividad) {
        return actividadRepository.save(actividad);
    }

    public List<Actividad> getAllActividades() {
        return (List<Actividad>) actividadRepository.findAll();
    }

    public void deleteActividad(int id) {
        actividadRepository.deleteById(id);
    }
}
