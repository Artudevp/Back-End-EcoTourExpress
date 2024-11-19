package com.ecotourexpress.ecotourexpress.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ecotourexpress.ecotourexpress.model.Hospedaje;
import com.ecotourexpress.ecotourexpress.repository.HospedajeRepository;

@Service
public class HospedajeService {

    // Conexion a repositorio de Hospedaje
    @Autowired
    private HospedajeRepository hospedajeRepository;

    // Guardar o actualizar hospedaje 
    public Hospedaje saveHospedaje(Hospedaje hospedaje) {
        return hospedajeRepository.save(hospedaje);
    }

    // Obtener lista de hospedajes
    public List<Hospedaje> getAllHospedajes() {
        return (List<Hospedaje>) hospedajeRepository.findAll();
    }

    // Obtener hospedaje por ID (editar)
    public Optional<Hospedaje> getHospedajeById(int id) {
        return hospedajeRepository.findById(id);
    }

    // Eliminar hospedaje
    public void deleteHospedaje(int id) {
        hospedajeRepository.deleteById(id);
    }
}
