package com.ecotourexpress.ecotourexpress.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecotourexpress.ecotourexpress.config.exception.ResourceNotFoundException;
import com.ecotourexpress.ecotourexpress.model.Actividad;
import com.ecotourexpress.ecotourexpress.model.Ruta;
import com.ecotourexpress.ecotourexpress.repository.ActividadRepository;
import com.ecotourexpress.ecotourexpress.repository.RutaRepository;

@Service
public class RutaService {

    // Conexion al repositorio de ruta y actividad
    @Autowired
    private RutaRepository rutaRepository;

    @Autowired
    private ActividadRepository actividadRepository;

    // ==========================================
    // CRUD RUTAS
    // Métodos para manejar el CRUD
    // ==========================================

    // Crear ruta
    public Ruta saveRuta(Ruta ruta) {
        return rutaRepository.save(ruta);
    }

    // Mostrar todas las rutas e imagenes
    public List<Ruta> getAllRutas() {
        List<Ruta> rutas = rutaRepository.findAll();
    
        rutas.forEach(ruta -> {
            List<String> actividadMediaUrls = ruta.getActividades().stream()
                .flatMap(actividad -> actividad.getMediaUrls().stream())
                .collect(Collectors.toList());
    
            ruta.setActividadMediaUrls(actividadMediaUrls);
        });
    
        return rutas;
    }

    // Seleccionar ruta por ID (Para editar)
    public Optional<Ruta> getRutaById(int id) {
        return rutaRepository.findById(id);
    }

    // Eliminar Ruta
    public void deleteRuta(int id) {
        rutaRepository.deleteById(id);
    }

    // ==========================================
    // ACTIVIDADES - RUTAS
    // Métodos para manejar las actividades
    // ==========================================

    // Agregar actividades a una ruta
    public Ruta addActividadesToRuta(int id_ruta, List<Integer> actividadIds) {
        Ruta ruta = rutaRepository.findById(id_ruta)
                .orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrada con id: " + id_ruta));
        
        for (Integer actividadId : actividadIds){
            Actividad act = actividadRepository.findById(actividadId)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada con id: " + actividadId));
            ruta.getActividades().add(act);
        }

        return rutaRepository.save(ruta);
    }


    // Obtener todas las actividades de una ruta
    public List<Actividad> getActividadesOfRuta(int id_ruta) {
        Ruta ruta = rutaRepository.findById(id_ruta)
                .orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrada con id: " + id_ruta));
        
        return ruta.getActividades();
    }

    // Eliminar actividad de una ruta
    public Ruta removeActividadFromRuta(int id_ruta, int id_actividad) {
        Ruta ruta = rutaRepository.findById(id_ruta)
                .orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrada con id: " + id_ruta));

        ruta.getActividades().removeIf(actividad -> actividad.getId() == id_actividad);
        return rutaRepository.save(ruta);
    }
}
