package com.ecotourexpress.ecotourexpress.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ecotourexpress.ecotourexpress.model.Cliente;
import com.ecotourexpress.ecotourexpress.model.Actividad;
import com.ecotourexpress.ecotourexpress.repository.ClienteRepository;
import com.ecotourexpress.ecotourexpress.repository.ActividadRepository;

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
        return (List<Cliente>) clienteRepository.findAll();
    }

    public Cliente getClienteById(int id) {
        return clienteRepository.findById(id).orElse(null);
    }

    public void deleteCliente(int id) {
        clienteRepository.deleteById(id);
    }

    public Cliente addActividadesToCliente(int id_cliente, List<Actividad> actividades) {
        Cliente cliente = clienteRepository.findById(id_cliente).orElse(null);
        if (cliente != null) {
            cliente.setActividades(actividades);
            clienteRepository.save(cliente);
        }
        return cliente;
    }

    public List<Actividad> getActividadesOfCliente(int id_cliente) {
        Cliente cliente = clienteRepository.findById(id_cliente).orElse(null);
        return cliente != null ? cliente.getActividades() : null;
    }

    public Cliente removeActividadFromCliente(int id_cliente, int id_actividad) {
        Cliente cliente = clienteRepository.findById(id_cliente).orElse(null);
        Actividad actividad = actividadRepository.findById(id_actividad).orElse(null);
        if (cliente != null && actividad != null) {
            cliente.getActividades().remove(actividad);
            clienteRepository.save(cliente);
        }
        return cliente;
    }
}
