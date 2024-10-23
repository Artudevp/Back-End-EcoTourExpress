package com.ecotourexpress.ecotourexpress.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.ecotourexpress.ecotourexpress.config.exception.ResourceNotFoundException;
import com.ecotourexpress.ecotourexpress.model.Actividad;
import com.ecotourexpress.ecotourexpress.model.Cliente;
import com.ecotourexpress.ecotourexpress.model.Hospedaje;
import com.ecotourexpress.ecotourexpress.model.Producto;
import com.ecotourexpress.ecotourexpress.model.Ruta;
import com.ecotourexpress.ecotourexpress.service.ClienteService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@Transactional
@RequestMapping("/clientes")
public class ClienteController {

    // Conexión a service
    @Autowired
    private ClienteService clienteService;

    // Obtener todos los clientes
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Cliente> getAllClientes() {
        return clienteService.getAllClientes();
    }

    // Crear un nuevo cliente
    @PostMapping
    public Cliente newCliente(@Valid @RequestBody Cliente cliente) {
        return clienteService.saveCliente(cliente);
    }
    
    // Obtener cliente por ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Cliente getClienteById(@PathVariable int id) {
        return clienteService.getClienteById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
    }

    // Editar un cliente
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Cliente> updateCliente(@PathVariable int id, @Valid @RequestBody Cliente clienteDetails) {
        Cliente cliente = clienteService.getClienteById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));

        cliente.setNombre_cli(clienteDetails.getNombre_cli());
        cliente.setEdad(clienteDetails.getEdad());
        cliente.setGenero(clienteDetails.getGenero());
        cliente.setCedula(clienteDetails.getCedula());

        final Cliente updatedCliente = clienteService.saveCliente(cliente);
        return ResponseEntity.ok(updatedCliente);
    }

    // Eliminar cliente
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCliente(@PathVariable int id) {
        clienteService.deleteCliente(id);
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
   public Cliente addActividadesToCliente(
           @PathVariable @Min(1) int id_cliente,
           @RequestBody @Valid List<Integer> actividadIds) {
       return clienteService.addActividadesToCliente(id_cliente, actividadIds);
   }   

    // Eliminar una actividad de un cliente
    @DeleteMapping("/{id_cliente}/actividades/{id_actividad}")
    @PreAuthorize("hasRole('ADMIN')")
    public Cliente removeActividadFromCliente(
            @PathVariable @Min(1) int id_cliente,
            @PathVariable @Min(1) int id_actividad) {
        return clienteService.removeActividadFromCliente(id_cliente, id_actividad);
    }

    // Agregar ruta(s) a Cliente
    @PutMapping("/{id_cliente}/rutas")
    @PreAuthorize("hasRole('ADMIN')")
    public Cliente addRutasToCliente(
            @PathVariable @Min(1) int id_cliente,
            @RequestBody @Valid List<Integer> rutaIds) {
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
    public Cliente removeRutaFromCliente(
            @PathVariable @Min(1) int id_cliente,
            @PathVariable @Min(1) int id_ruta) {
        return clienteService.removeRutaFromCliente(id_cliente, id_ruta);
    }

    // Asignar hospedaje a Cliente
    @PutMapping("/{id_cliente}/hospedaje/{id_hospedaje}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Cliente> addHospedajeToCliente(
            @PathVariable @Min(1) int id_cliente,
            @PathVariable @Min(1) int id_hospedaje) {
        Cliente clienteActualizado = clienteService.addHospedajeToCliente(id_cliente, id_hospedaje);
        return ResponseEntity.ok(clienteActualizado);
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
    public Cliente removeHospedajeFromCliente(@PathVariable @Min(1) int id_cliente) {
        return clienteService.removeHospedajeFromCliente(id_cliente);
    }

    // Añadir productos al cliente
    @PostMapping("/{id_cliente}/productos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Cliente> addProductosToCliente(@PathVariable int id_cliente, @RequestBody List<Integer> id_productos) {
        Cliente clienteActualizado = clienteService.addProductosToCliente(id_cliente, id_productos);
        return ResponseEntity.ok(clienteActualizado);
    }

    // Obtener productos asignados a cliente
    @GetMapping("/{id_cliente}/productos")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Producto> getProductoOfCliente(@PathVariable @Min(1) int id_cliente) {
        return clienteService.getProductoOfCliente(id_cliente);
    }
    
    // Eliminar productos del cliente
    @DeleteMapping("/{id_cliente}/productos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Cliente> removeProductosFromCliente(
            @PathVariable int id_cliente,
            @RequestBody List<Integer> id_productos) {
        
        Cliente cliente = clienteService.removeProductosFromCliente(id_cliente, id_productos);
        return ResponseEntity.ok(cliente);
    }
}
