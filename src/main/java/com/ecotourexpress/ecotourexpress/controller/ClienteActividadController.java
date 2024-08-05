package com.ecotourexpress.ecotourexpress.controller;

import com.ecotourexpress.ecotourexpress.model.ClienteActividad;
import com.ecotourexpress.ecotourexpress.model.ClienteActividadKey;
import com.ecotourexpress.ecotourexpress.service.ClienteActividadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cliente-actividad")
public class ClienteActividadController {

    @Autowired
    private ClienteActividadService clienteActividadService;

    @GetMapping
    public List<ClienteActividad> getAll() {
        return clienteActividadService.getAllClienteActividades();
    }

    @PostMapping
    public ClienteActividad create(@RequestBody ClienteActividad clienteActividad) {
        return clienteActividadService.saveClienteActividad(clienteActividad);
    }

    @DeleteMapping("/{clienteId}/{actividadId}")
    public void delete(@PathVariable int clienteId, @PathVariable int actividadId) {
        ClienteActividadKey key = new ClienteActividadKey(clienteId, actividadId);
        clienteActividadService.deleteClienteActividad(key);
    }

    @GetMapping("/{clienteId}/{actividadId}")
    public ClienteActividad getById(@PathVariable int clienteId, @PathVariable int actividadId) {
        ClienteActividadKey key = new ClienteActividadKey(clienteId, actividadId);
        Optional<ClienteActividad> clienteActividad = clienteActividadService.getClienteActividad(key);
        return clienteActividad.orElse(null); // Devolver null si no se encuentra
    }
}
