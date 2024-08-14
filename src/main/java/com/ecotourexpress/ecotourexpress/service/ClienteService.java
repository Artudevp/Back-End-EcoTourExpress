package com.ecotourexpress.ecotourexpress.service;

import com.ecotourexpress.ecotourexpress.model.Cliente;
import com.ecotourexpress.ecotourexpress.model.Actividad;
import com.ecotourexpress.ecotourexpress.model.Ruta;
import com.ecotourexpress.ecotourexpress.repository.ClienteRepository;
import com.ecotourexpress.ecotourexpress.repository.ActividadRepository;
import com.ecotourexpress.ecotourexpress.repository.RutaRepository;
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

    @Autowired
    private RutaRepository rutaRepository;

    // Guardar o actualizar cliente
    public Cliente saveCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    // Obtener todos los clientes
    public List<Cliente> getAllClientes() {
        List<Cliente> clientes = new ArrayList<>();
        clienteRepository.findAll().forEach(clientes::add);
        return clientes;
    }

    // Obtener cliente por ID
    public Optional<Cliente> getClienteById(int id) {
        return clienteRepository.findById(id);
    }

    // Eliminar cliente
    public void deleteCliente(int id) {
        clienteRepository.deleteById(id);
    }

    // Añadir actividades al cliente
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

    // Obtener actividades del cliente
    public List<Actividad> getActividadesOfCliente(int id_cliente) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
        return cliente.getActividades();
    }

    // Eliminar actividad del cliente
    public Cliente removeActividadFromCliente(int id_cliente, int id_actividad) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
        cliente.getActividades().removeIf(actividad -> actividad.getID_actividad() == id_actividad);
        return clienteRepository.save(cliente);
    }

    // Añadir rutas al cliente
    public Cliente addRutasToCliente(int id_cliente, List<Ruta> rutas) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
        for (Ruta ruta : rutas) {
            Ruta rut = rutaRepository.findById(ruta.getID_ruta())
                    .orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrada con id: " + ruta.getID_ruta()));
            cliente.getRutas().add(rut);
        }
        return clienteRepository.save(cliente);
    }

    // Obtener rutas del cliente
    public List<Ruta> getRutasOfCliente(int id_cliente) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
        return cliente.getRutas();
    }

    // Eliminar ruta del cliente
    public Cliente removeRutaFromCliente(int id_cliente, int id_ruta) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
        cliente.getRutas().removeIf(ruta -> ruta.getID_ruta() == id_ruta);
        return clienteRepository.save(cliente);
    }

    // // Añadir hospedaje al cliente
    // public Cliente addHospedajeToCliente(int id_cliente, Hospedaje hospedaje) {
    //     Cliente cliente = clienteRepository.findById(id_cliente)
    //             .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
    //     Hospedaje hosp = hospedajeRepository.findById(hospedaje.getID_hospedaje())
    //             .orElseThrow(() -> new ResourceNotFoundException("Hospedaje no encontrado con id: " + hospedaje.getID_hospedaje()));
    //     cliente.setHospedaje(hosp);
    //     return clienteRepository.save(cliente);
    // }

    // // Obtener hospedaje del cliente
    // public Hospedaje getHospedajeOfCliente(int id_cliente) {
    //     Cliente cliente = clienteRepository.findById(id_cliente)
    //             .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
    //     return cliente.getHospedaje();
    // }

    // // Eliminar hospedaje del cliente
    // public Cliente removeHospedajeFromCliente(int id_cliente) {
    //     Cliente cliente = clienteRepository.findById(id_cliente)
    //             .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
    //     cliente.setHospedaje(null);
    //     return clienteRepository.save(cliente);
    // }
}
