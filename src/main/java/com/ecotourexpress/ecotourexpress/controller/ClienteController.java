package com.ecotourexpress.ecotourexpress.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ecotourexpress.ecotourexpress.controller.utils.AuditoriaUtils;
import com.ecotourexpress.ecotourexpress.controller.utils.SecurityUtils;
import com.ecotourexpress.ecotourexpress.model.Actividad;
import com.ecotourexpress.ecotourexpress.model.Hospedaje;
import com.ecotourexpress.ecotourexpress.model.Producto;
import com.ecotourexpress.ecotourexpress.model.Ruta;
import com.ecotourexpress.ecotourexpress.model.dto.ClienteDTO;
import com.ecotourexpress.ecotourexpress.repository.ClienteRepository;
import com.ecotourexpress.ecotourexpress.service.ClienteService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@Transactional
@RequestMapping("/clientes")
public class ClienteController {
    
    //Conexion a repositorio y service
    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteRepository clienteRepository;

    // ==========================================
    // CRUD CLIENTES
    // Métodos para manejar las operaciones CRUD
    // ==========================================


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

    // ==========================================
    // ACTIVIDADES - CLIENTES
    // Métodos para manejar las actividades
    // ==========================================

    // Obtener todas las actividades de un cliente
    @GetMapping("/{id_cliente}/actividades")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Actividad> getActividadesOfCliente(@PathVariable(required = false) Integer id_cliente) {
        int authenticatedUserId = SecurityUtils.getAuthenticatedUserId();
        int finalClientId = SecurityUtils.resolveClientId(id_cliente, clienteRepository);

        AuditoriaUtils.registrarAccion("CONSULTAR", "Actividad", 
            "El usuario con ID: " + authenticatedUserId + " consultó actividades del cliente con ID: " + finalClientId);

        return clienteService.getActividadesOfCliente(finalClientId);
    }

    // Asignar actividades a un cliente
    @PutMapping("/{id_cliente}/actividades")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ClienteDTO addActividadesToCliente(
            @PathVariable(required = false) Integer id_cliente,
            @RequestBody @Valid List<Integer> actividadIds) {
        int authenticatedUserId = SecurityUtils.getAuthenticatedUserId();
        int finalClientId = SecurityUtils.resolveClientId(id_cliente, clienteRepository);

        AuditoriaUtils.registrarAccion("ASOCIAR", "Actividad", 
            "El usuario con ID: " + authenticatedUserId + " asoció actividades " + actividadIds + " al cliente con ID: " + finalClientId);

        return clienteService.addActividadesToCliente(finalClientId, actividadIds);
    }

    // Eliminar actividad a un cliente
    @DeleteMapping("/{id_cliente}/actividades/{id_actividad}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ClienteDTO removeActividadFromCliente(
            @PathVariable(required = false) Integer id_cliente,
            @PathVariable @Min(1) int id_actividad) {
        int authenticatedUserId = SecurityUtils.getAuthenticatedUserId();
        int finalClientId = SecurityUtils.resolveClientId(id_cliente, clienteRepository);

        AuditoriaUtils.registrarAccion("ELIMINAR", "Actividad", 
            "El usuario con ID: " + authenticatedUserId + " eliminó la actividad con ID: " + id_actividad + " del cliente con ID: " + finalClientId);

        return clienteService.removeActividadFromCliente(finalClientId, id_actividad);
    }

    // ==========================================
    // RUTAS - CLIENTES
    // Métodos para manejar las rutas
    // ==========================================

    // Obtener las rutas de un cliente
    @GetMapping("/{id_cliente}/rutas")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Ruta> getRutasOfCliente(@PathVariable(required = false) Integer id_cliente) {
        int finalClientId = SecurityUtils.resolveClientId(id_cliente, clienteRepository);
        return clienteService.getRutasOfCliente(finalClientId);
    }

    // Asignar rutas a un cliente
    @PutMapping("/{id_cliente}/rutas")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ClienteDTO addRutasToCliente(
            @PathVariable(required = false) Integer id_cliente,
            @RequestBody @Valid List<Integer> rutaIds) {
        int finalClientId = SecurityUtils.resolveClientId(id_cliente, clienteRepository);

        AuditoriaUtils.registrarAccion("ASOCIAR", "Ruta", 
                String.format("Usuario autenticado o administrador asoció las rutas %s al cliente con ID: %d", rutaIds, finalClientId));

        return clienteService.addRutasToCliente(finalClientId, rutaIds);
    }

    // Eliminar una ruta de un cliente
    @DeleteMapping("/{id_cliente}/rutas/{id_ruta}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ClienteDTO removeRutaFromCliente(
            @PathVariable(required = false) Integer id_cliente,
            @PathVariable @Min(1) int id_ruta) {
        int finalClientId = SecurityUtils.resolveClientId(id_cliente, clienteRepository);

        AuditoriaUtils.registrarAccion("ELIMINAR", "Ruta", 
                String.format("Usuario autenticado o administrador eliminó la ruta con ID: %d del cliente con ID: %d", id_ruta, finalClientId));

        return clienteService.removeRutaFromCliente(finalClientId, id_ruta);
    }

    // ==========================================
    // HOSPEDAJE - CLIENTES
    // Métodos para manejar el hospedaje
    // ==========================================

    // Obtener el hospedaje de un cliente
    @GetMapping("/{id_cliente}/hospedaje")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Hospedaje getHospedajeOfCliente(@PathVariable(required = false) Integer id_cliente) {
        int finalClientId = SecurityUtils.resolveClientId(id_cliente, clienteRepository);
        return clienteService.getHospedajeOfCliente(finalClientId);
    }

    // Asignar hospedaje a un cliente
    @PutMapping("/{id_cliente}/hospedaje/{id_hospedaje}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ClienteDTO> addHospedajeToCliente(
            @PathVariable(required = false) Integer id_cliente,
            @PathVariable @Min(1) int id_hospedaje) {
        int finalClientId = SecurityUtils.resolveClientId(id_cliente, clienteRepository);

        AuditoriaUtils.registrarAccion("ASOCIAR", "Hospedaje", 
                String.format("Usuario autenticado o administrador asignó el hospedaje %d al cliente con ID: %d", 
                        id_hospedaje, finalClientId));

        ClienteDTO updatedCliente = clienteService.addHospedajeToCliente(finalClientId, id_hospedaje);
        return ResponseEntity.ok(updatedCliente);
    }

    // Eliminar el hospedaje de un cliente
    @DeleteMapping("/{id_cliente}/hospedaje")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ClienteDTO removeHospedajeFromCliente(@PathVariable(required = false) Integer id_cliente) {
        int finalClientId = SecurityUtils.resolveClientId(id_cliente, clienteRepository);

        AuditoriaUtils.registrarAccion("ELIMINAR", "Hospedaje", 
                String.format("Usuario autenticado o administrador eliminó el hospedaje del cliente con ID: %d", finalClientId));

        return clienteService.removeHospedajeFromCliente(finalClientId);
    }

    // ==========================================
    // PRODUCTOS - CLIENTES
    // Métodos para manejar los productos
    // ==========================================

    // Obtener los productos de un cliente
    @GetMapping("/{id_cliente}/productos")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Producto> getProductosOfCliente(@PathVariable(required = false) Integer id_cliente) {
        int finalClientId = SecurityUtils.resolveClientId(id_cliente, clienteRepository);
        return clienteService.getProductosOfCliente(finalClientId);
    }

    // Asignar productos a un cliente
    @PutMapping("/{id_cliente}/productos")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ClienteDTO> addProductosToCliente(
            @PathVariable(required = false) Integer id_cliente,
            @RequestBody @Valid List<Integer> id_productos) {
        int finalClientId = SecurityUtils.resolveClientId(id_cliente, clienteRepository);

        AuditoriaUtils.registrarAccion("ASOCIAR", "Producto", 
                String.format("Usuario autenticado o administrador asoció los productos %s al cliente con ID: %d", 
                        id_productos, finalClientId));

        ClienteDTO updatedCliente = clienteService.addProductosToCliente(finalClientId, id_productos);
        return ResponseEntity.ok(updatedCliente);
    }

    // Eliminar productos de un cliente
    @DeleteMapping("/{id_cliente}/productos")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ClienteDTO> removeProductosFromCliente(
            @PathVariable(required = false) Integer id_cliente,
            @RequestBody @Valid List<Integer> productoIds) {
        int finalClientId = SecurityUtils.resolveClientId(id_cliente, clienteRepository);

        AuditoriaUtils.registrarAccion("ELIMINAR", "Producto", 
                String.format("Usuario autenticado o administrador eliminó los productos %s del cliente con ID: %d", 
                        productoIds, finalClientId));

        ClienteDTO updatedCliente = clienteService.removeProductosFromCliente(finalClientId, productoIds);
        return ResponseEntity.ok(updatedCliente);
    }
}
