package com.ecotourexpress.ecotourexpress.service;

import com.ecotourexpress.ecotourexpress.model.Cliente;
import com.ecotourexpress.ecotourexpress.model.Actividad;
import com.ecotourexpress.ecotourexpress.repository.ClienteRepository;
import com.ecotourexpress.ecotourexpress.repository.ActividadRepository;
import exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ActividadRepository actividadRepository;

    public Cliente saveCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public List<Cliente> getAllClientes() {
        List<Cliente> clientes = new ArrayList<>();
        clienteRepository.findAll().forEach(clientes::add);
        return clientes;
    }

    public Optional<Cliente> getClienteById(int id) {
        return clienteRepository.findById(id);
    }

    public void deleteCliente(int id) {
        clienteRepository.deleteById(id);
    }

    public Cliente addActividadesToCliente(int id_cliente, List<Actividad> actividades) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
        for (Actividad actividad : actividades) {
            Actividad act = actividadRepository.findById(actividad.getID_actividad())
                    .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada con id: " + actividad.getID_actividad()));
            cliente.getActividades().add(act);
        }
        return clienteRepository.save(cliente);
    }

    public List<Actividad> getActividadesOfCliente(int id_cliente) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
        return cliente.getActividades();
    }

    public Cliente removeActividadFromCliente(int id_cliente, int id_actividad) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
        cliente.getActividades().removeIf(actividad -> actividad.getID_actividad() == id_actividad);
        return clienteRepository.save(cliente);
    }
}
