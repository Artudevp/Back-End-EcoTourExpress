package com.ecotourexpress.ecotourexpress.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ecotourexpress.ecotourexpress.model.Hospedaje;
import com.ecotourexpress.ecotourexpress.repository.HospedajeRepository;

@Service
public class HospedajeService {

    @Autowired
    private HospedajeRepository hospedajeRepository;

    public Hospedaje saveHospedaje(Hospedaje hospedaje) {
        return hospedajeRepository.save(hospedaje);
    }

    public List<Hospedaje> getAllHospedajes() {
        return (List<Hospedaje>) hospedajeRepository.findAll();
    }

    public void deleteHospedaje(int id) {
        hospedajeRepository.deleteById(id);
    }
}
