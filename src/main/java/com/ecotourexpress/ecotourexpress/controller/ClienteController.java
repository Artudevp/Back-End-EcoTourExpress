package com.ecotourexpress.ecotourexpress.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.ecotourexpress.ecotourexpress.model.Cliente;
import com.ecotourexpress.ecotourexpress.model.Actividad;
import com.ecotourexpress.ecotourexpress.service.ClienteService;

import exception.ResourceNotFoundException;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // Obtener todos los clientes
    @GetMapping
    public List<Cliente> getAllClientes() {
        return clienteService.getAllClientes();
    }

    // Crear un nuevo cliente
    @PostMapping
    public Cliente newCliente(@RequestBody Cliente cliente) {
        return clienteService.saveCliente(cliente);
    }

    // Editar un cliente
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable int id, @RequestBody Cliente clienteDetails) {
        Cliente cliente = clienteService.getClienteById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));

        cliente.setNombre_cli(clienteDetails.getNombre_cli());
        cliente.setEdad(clienteDetails.getEdad());
        cliente.setGenero(clienteDetails.getGenero());
        cliente.setHabitacion(clienteDetails.getHabitacion());
        cliente.setActividades(clienteDetails.getActividades());
        cliente.setRutas(clienteDetails.getRutas());
        cliente.setProductos(clienteDetails.getProductos());

        final Cliente updatedCliente = clienteService.saveCliente(cliente);
        return ResponseEntity.ok(updatedCliente);
    }

    // Obtener un cliente por ID
    @GetMapping("/{id}")
    public Cliente getClienteById(@PathVariable int id) {
        return clienteService.getClienteById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
    }

    // Eliminar un cliente
    @DeleteMapping("/{id}")
    public void deleteCliente(@PathVariable int id) {
        clienteService.deleteCliente(id);
    }

    // Obtener todas las actividades de un cliente
    @GetMapping("/{id_cliente}/actividades")
    public List<Actividad> getActividadesOfCliente(@PathVariable int id_cliente) {
        return clienteService.getActividadesOfCliente(id_cliente);
    }

    // Asociar actividades a un cliente
    @PutMapping("/{id_cliente}/actividades")
    public Cliente addActividadesToCliente(
            @PathVariable int id_cliente,
            @RequestBody List<Actividad> actividades) {
        return clienteService.addActividadesToCliente(id_cliente, actividades);
    }

    // Eliminar una actividad de un cliente
    @DeleteMapping("/{id_cliente}/actividades/{id_actividad}")
    public Cliente removeActividadFromCliente(
            @PathVariable int id_cliente,
            @PathVariable int id_actividad) {
        return clienteService.removeActividadFromCliente(id_cliente, id_actividad);
    }
}
