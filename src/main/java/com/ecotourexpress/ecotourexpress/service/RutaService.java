package com.ecotourexpress.ecotourexpress.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ecotourexpress.ecotourexpress.model.Ruta;
import com.ecotourexpress.ecotourexpress.repository.RutaRepository;

@Service
public class RutaService {

    @Autowired
    private RutaRepository rutaRepository;

    public Ruta saveRuta(Ruta ruta) {
        return rutaRepository.save(ruta);
    }

    public List<Ruta> getAllRutas() {
        return (List<Ruta>) rutaRepository.findAll();
    }

    public void deleteRuta(int id) {
        rutaRepository.deleteById(id);
    }
}
