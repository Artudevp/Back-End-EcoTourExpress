package com.ecotourexpress.ecotourexpress.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ecotourexpress.ecotourexpress.model.Cliente;
import com.ecotourexpress.ecotourexpress.model.Actividad;
import com.ecotourexpress.ecotourexpress.service.ClienteService;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;


    // Crear un nuevo cliente
    @PostMapping("/nuevo")
    public Cliente newCliente(@RequestBody Cliente cliente) {
        return clienteService.saveCliente(cliente);
    }

    // Obtener todos los clientes
    @GetMapping("/todos")
    public List<Cliente> getAllClientes() {
        return clienteService.getAllClientes();
    }

    // Obtener un cliente por ID
    @GetMapping("/{id}")
    public Cliente getClienteById(@PathVariable int id) {
        return clienteService.getClienteById(id);
    }

    // Eliminar un cliente
    @DeleteMapping("/{id}")
    public void deleteCliente(@PathVariable int id) {
        clienteService.deleteCliente(id);
    }

    // Asociar actividades a un cliente
    @PutMapping("/{id_cliente}/actividades")
    public Cliente addActividadesToCliente(
        @PathVariable int id_cliente,
        @RequestBody List<Actividad> actividades) {
        return clienteService.addActividadesToCliente(id_cliente, actividades);
    }

    // Obtener todas las actividades de un cliente
    @GetMapping("/{id_cliente}/actividades")
    public List<Actividad> getActividadesOfCliente(@PathVariable int id_cliente) {
        return clienteService.getActividadesOfCliente(id_cliente);
    }

    // Eliminar una actividad de un cliente
    @DeleteMapping("/{id_cliente}/actividades/{id_actividad}")
    public Cliente removeActividadFromCliente(
        @PathVariable int id_cliente,
        @PathVariable int id_actividad) {
        return clienteService.removeActividadFromCliente(id_cliente, id_actividad);
    }
}

