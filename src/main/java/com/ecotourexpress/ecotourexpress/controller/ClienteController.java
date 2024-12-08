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
        int authenticatedClientId = SecurityUtils.getAuthenticatedClientId(clienteRepository);
        int finalClientId = SecurityUtils.validateAndGetClientId(id_cliente, authenticatedClientId);
        return clienteService.getActividadesOfCliente(finalClientId);
    }

    // Asignar actividades a un cliente
    @PutMapping("/{id_cliente}/actividades")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ClienteDTO addActividadesToCliente(
            @PathVariable(required = false) Integer id_cliente,
            @RequestBody @Valid List<Integer> actividadIds) {
            int authenticatedClientId = SecurityUtils.getAuthenticatedClientId(clienteRepository);
            int finalClientId = SecurityUtils.validateAndGetClientId(id_cliente, authenticatedClientId);
        
        
        // AuditoriaUtils.registrarAccion("ASOCIAR", "Actividad", 
        //    "Cliente o administrador asoció actividades " + actividadIds + " al cliente con ID: " + finalClientId);

        return clienteService.addActividadesToCliente(finalClientId, actividadIds);
    }

    // Eliminar actividad a un cliente
    @DeleteMapping("/{id_cliente}/actividades/{id_actividad}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ClienteDTO removeActividadFromCliente(
            @PathVariable(required = false) Integer id_cliente,
            @PathVariable @Min(1) int id_actividad) {
            int authenticatedClientId = SecurityUtils.getAuthenticatedClientId(clienteRepository);
            int finalClientId = SecurityUtils.validateAndGetClientId(id_cliente, authenticatedClientId);
    
        
        // AuditoriaUtils.registrarAccion("ELIMINAR", "Actividad", 
        //    "Cliente o administrador eliminó la actividad con ID: " + id_actividad + " del cliente con ID: " + finalClientId);

        return clienteService.removeActividadFromCliente(finalClientId, id_actividad);
    }

    // ==========================================
    // RUTAS - CLIENTES
    // Métodos para manejar las rutas
    // ==========================================

    // Obtener las rutas de los clientes
    @GetMapping("/{id_cliente}/rutas")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Ruta> getRutasOfCliente(@PathVariable(required = false) Integer id_cliente) {
        int authenticatedClientId = SecurityUtils.getAuthenticatedUserId();
        int finalClientId = SecurityUtils.validateAndGetClientId(id_cliente, authenticatedClientId);
        return clienteService.getRutasOfCliente(finalClientId);
    }
    
    // Asignar rutas a un cliente
    @PutMapping("/{id_cliente}/rutas")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ClienteDTO addRutasToCliente(
            @PathVariable(required = false) Integer id_cliente,
            @RequestBody @Valid List<Integer> rutaIds) {
        int authenticatedClientId = SecurityUtils.getAuthenticatedUserId();
        int finalClientId = SecurityUtils.validateAndGetClientId(id_cliente, authenticatedClientId);

        // AuditoriaUtils.registrarAccion("ASOCIAR", "Ruta", 
        //         String.format("Usuario autenticado con ID %d asoció las rutas %s al cliente con ID: %d",
        //                 authenticatedClientId, rutaIds, finalClientId));

        return clienteService.addRutasToCliente(finalClientId, rutaIds);
    }

    // Eliminar ruta a un cliente
    @DeleteMapping("/{id_cliente}/rutas/{id_ruta}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ClienteDTO removeRutaFromCliente(
            @PathVariable(required = false) Integer id_cliente,
            @PathVariable @Min(1) int id_ruta) {
        int authenticatedClientId = SecurityUtils.getAuthenticatedUserId();
        int finalClientId = SecurityUtils.validateAndGetClientId(id_cliente, authenticatedClientId);

        // AuditoriaUtils.registrarAccion("ELIMINAR", "Ruta", 
        //         String.format("Usuario autenticado con ID %d eliminó la ruta %d del cliente con ID: %d",
        //                 authenticatedClientId, id_ruta, finalClientId));

        return clienteService.removeRutaFromCliente(finalClientId, id_ruta);
    }

    // ==========================================
    // HOSPEDAJE - CLIENTES
    // Métodos para manejar el hospedaje
    // ==========================================

    // Obtener el hospedaje del cliente
    @GetMapping("/{id_cliente}/hospedaje")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Hospedaje getHospedajeOfCliente(@PathVariable(required = false) Integer id_cliente) {
        int authenticatedClientId = SecurityUtils.getAuthenticatedUserId();
        int finalClientId = SecurityUtils.validateAndGetClientId(id_cliente, authenticatedClientId);
        return clienteService.getHospedajeOfCliente(finalClientId);
    }

    // Asignar hospedaje a cliente
    @PutMapping("/{id_cliente}/hospedaje/{id_hospedaje}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ClienteDTO> addHospedajeToCliente(
            @PathVariable(required = false) Integer id_cliente,
            @PathVariable @Min(1) int id_hospedaje) {
        int authenticatedClientId = SecurityUtils.getAuthenticatedUserId();
        int finalClientId = SecurityUtils.validateAndGetClientId(id_cliente, authenticatedClientId);

        // AuditoriaUtils.registrarAccion("ASOCIAR", "Hospedaje", 
        //         String.format("Usuario autenticado con ID %d asignó el hospedaje %d al cliente con ID: %d",
        //                 authenticatedClientId, id_hospedaje, finalClientId));

        ClienteDTO updatedCliente = clienteService.addHospedajeToCliente(finalClientId, id_hospedaje);
        return ResponseEntity.ok(updatedCliente);
    }

    // Eliminar hospedaje a cliente
    @DeleteMapping("/{id_cliente}/hospedaje")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ClienteDTO removeHospedajeFromCliente(@PathVariable(required = false) Integer id_cliente) {
        int authenticatedClientId = SecurityUtils.getAuthenticatedUserId();
        int finalClientId = SecurityUtils.validateAndGetClientId(id_cliente, authenticatedClientId);

        // AuditoriaUtils.registrarAccion("ELIMINAR", "Hospedaje", 
        //         String.format("Usuario autenticado con ID %d eliminó el hospedaje del cliente con ID: %d",
        //                 authenticatedClientId, finalClientId));

        return clienteService.removeHospedajeFromCliente(finalClientId);
    }

    // ==========================================
    // PRODUCTOS - CLIENTES
    // Métodos para manejar los productos
    // ==========================================

    // Obtener productos cliente
    @GetMapping("/{id_cliente}/productos")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Producto> getProductosOfCliente(@PathVariable(required = false) Integer id_cliente) {
        int authenticatedClientId = SecurityUtils.getAuthenticatedUserId();
        int finalClientId = SecurityUtils.validateAndGetClientId(id_cliente, authenticatedClientId);
        return clienteService.getProductosOfCliente(finalClientId);
    }

    // Asignar productos a cliente
    @PutMapping("/{id_cliente}/productos")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ClienteDTO> addProductosToCliente(
            @PathVariable(required = false) Integer id_cliente,
            @RequestBody @Valid List<Integer> id_productos) {
        int authenticatedClientId = SecurityUtils.getAuthenticatedUserId();
        int finalClientId = SecurityUtils.validateAndGetClientId(id_cliente, authenticatedClientId);

        // AuditoriaUtils.registrarAccion("ASOCIAR", "Producto", 
        //         String.format("Usuario autenticado con ID %d asoció los productos %s al cliente con ID: %d",
        //                 authenticatedClientId, id_productos, finalClientId));

        ClienteDTO updatedCliente = clienteService.addProductosToCliente(finalClientId, id_productos);
        return ResponseEntity.ok(updatedCliente);
    }

    // Eliminar productos de cliente
    @DeleteMapping("/{id_cliente}/productos")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ClienteDTO> removeProductosFromCliente(
            @PathVariable(required = false) Integer id_cliente,
            @RequestBody @Valid List<Integer> productoIds) {
        int authenticatedClientId = SecurityUtils.getAuthenticatedUserId();
        int finalClientId = SecurityUtils.validateAndGetClientId(id_cliente, authenticatedClientId);

        // AuditoriaUtils.registrarAccion("ELIMINAR", "Producto", 
        //         String.format("Usuario autenticado con ID %d eliminó los productos %s del cliente con ID: %d",
        //                 authenticatedClientId, productoIds, finalClientId));

        ClienteDTO updatedCliente = clienteService.removeProductosFromCliente(finalClientId, productoIds);
        return ResponseEntity.ok(updatedCliente);
    }
}
