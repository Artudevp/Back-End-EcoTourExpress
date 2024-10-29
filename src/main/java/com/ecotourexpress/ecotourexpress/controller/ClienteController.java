package com.ecotourexpress.ecotourexpress.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ecotourexpress.ecotourexpress.model.Actividad;
import com.ecotourexpress.ecotourexpress.model.Hospedaje;
import com.ecotourexpress.ecotourexpress.model.Producto;
import com.ecotourexpress.ecotourexpress.model.Ruta;
import com.ecotourexpress.ecotourexpress.model.DTO.ClienteDTO;
import com.ecotourexpress.ecotourexpress.service.ClienteService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@Transactional
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // Obtener todos los clientes
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<ClienteDTO> getAllClientes() {
        return clienteService.getAllClientes();
    }

    // Crear un nuevo cliente
    @PostMapping
    public ClienteDTO newCliente(@Valid @RequestBody ClienteDTO clienteDTO) {
        return clienteService.saveCliente(clienteDTO);
    }

    // Obtener cliente por ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ClienteDTO getClienteById(@PathVariable int id) {
        return clienteService.getClienteById(id);
    }

    // Editar un cliente
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteDTO> updateCliente(@PathVariable int id, @Valid @RequestBody ClienteDTO clienteDTO) {
        ClienteDTO updatedCliente = clienteService.updateCliente(id, clienteDTO);
        return ResponseEntity.ok(updatedCliente);
    }


    // Eliminar cliente
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCliente(@PathVariable int id) {
        clienteService.deleteCliente(id);
        return ResponseEntity.noContent().build();
    }

    // Obtener todas las actividades de un cliente
    @GetMapping("/{id_cliente}/actividades")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Actividad> getActividadesOfCliente(@PathVariable int id_cliente) {
        return clienteService.getActividadesOfCliente(id_cliente);
    }

    // Asociar actividades a un cliente
    @PutMapping("/{id_cliente}/actividades")
    @PreAuthorize("hasRole('ADMIN')")
    public ClienteDTO addActividadesToCliente(@PathVariable @Min(1) int id_cliente, @RequestBody @Valid List<Integer> actividadIds) {
        return clienteService.addActividadesToCliente(id_cliente, actividadIds);
    }

    // Eliminar una actividad de un cliente
    @DeleteMapping("/{id_cliente}/actividades/{id_actividad}")
    @PreAuthorize("hasRole('ADMIN')")
    public ClienteDTO removeActividadFromCliente(@PathVariable @Min(1) int id_cliente, @PathVariable @Min(1) int id_actividad) {
        return clienteService.removeActividadFromCliente(id_cliente, id_actividad);
    }

    // Agregar rutas a Cliente
    @PutMapping("/{id_cliente}/rutas")
    @PreAuthorize("hasRole('ADMIN')")
    public ClienteDTO addRutasToCliente(@PathVariable @Min(1) int id_cliente, @RequestBody @Valid List<Integer> rutaIds) {
        return clienteService.addRutasToCliente(id_cliente, rutaIds);
    }

    // Obtener rutas asignadas a cliente
    @GetMapping("/{id_cliente}/rutas")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Ruta> getRutasOfCliente(@PathVariable @Min(1) int id_cliente) {
        return clienteService.getRutasOfCliente(id_cliente);
    }

    // Eliminar ruta de Cliente
    @DeleteMapping("/{id_cliente}/rutas/{id_ruta}")
    @PreAuthorize("hasRole('ADMIN')")
    public ClienteDTO removeRutaFromCliente(@PathVariable @Min(1) int id_cliente, @PathVariable @Min(1) int id_ruta) {
        return clienteService.removeRutaFromCliente(id_cliente, id_ruta);
    }

    // Asignar hospedaje a Cliente
    @PutMapping("/{id_cliente}/hospedaje/{id_hospedaje}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteDTO> addHospedajeToCliente(@PathVariable @Min(1) int id_cliente, @PathVariable @Min(1) int id_hospedaje) {
        ClienteDTO updatedCliente = clienteService.addHospedajeToCliente(id_cliente, id_hospedaje);
        return ResponseEntity.ok(updatedCliente);
    }

    // Obtener hospedaje asignado a cliente
    @GetMapping("/{id_cliente}/hospedaje")
    @PreAuthorize("hasRole('ADMIN')")
    public Hospedaje getHospedajeOfCliente(@PathVariable @Min(1) int id_cliente) {
        return clienteService.getHospedajeOfCliente(id_cliente);
    }

    // Eliminar hospedaje de cliente
    @DeleteMapping("/{id_cliente}/hospedaje")
    @PreAuthorize("hasRole('ADMIN')")
    public ClienteDTO removeHospedajeFromCliente(@PathVariable @Min(1) int id_cliente) {
        return clienteService.removeHospedajeFromCliente(id_cliente);
    }

    // Añadir productos al cliente
    @PostMapping("/{id_cliente}/productos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteDTO> addProductosToCliente(@PathVariable int id_cliente, @RequestBody List<Integer> id_productos) {
        ClienteDTO updatedCliente = clienteService.addProductosToCliente(id_cliente, id_productos);
        return ResponseEntity.ok(updatedCliente);
    }

    // Obtener productos asignados a cliente
    @GetMapping("/{id_cliente}/productos")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Producto> getProductosOfCliente(@PathVariable @Min(1) int id_cliente) {
        return clienteService.getProductosOfCliente(id_cliente);
    }

    // Eliminar productos del cliente
    @DeleteMapping("/{id_cliente}/productos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteDTO> removeProductosFromCliente(
            @PathVariable int id_cliente, @RequestBody List<Integer> id_productos) {
        ClienteDTO updatedCliente = clienteService.removeProductoFromCliente(id_cliente, id_productos);
        return ResponseEntity.ok(updatedCliente);
    }
}
