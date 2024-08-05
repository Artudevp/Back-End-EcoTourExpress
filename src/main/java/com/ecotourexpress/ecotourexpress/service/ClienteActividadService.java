package com.ecotourexpress.ecotourexpress.service;

import com.ecotourexpress.ecotourexpress.model.ClienteActividad;
import com.ecotourexpress.ecotourexpress.model.ClienteActividadKey;
import com.ecotourexpress.ecotourexpress.repository.ClienteActividadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteActividadService {

    @Autowired
    private ClienteActividadRepository clienteActividadRepository;

    public List<ClienteActividad> getAllClienteActividades() {
        return clienteActividadRepository.findAll();
    }

    public Optional<ClienteActividad> getClienteActividad(ClienteActividadKey id) {
        return clienteActividadRepository.findById(id);
    }

    public ClienteActividad saveClienteActividad(ClienteActividad clienteActividad) {
        return clienteActividadRepository.save(clienteActividad);
    }

    public void deleteClienteActividad(ClienteActividadKey id) {
        clienteActividadRepository.deleteById(id);
    }
}
