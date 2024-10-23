package com.ecotourexpress.ecotourexpress.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecotourexpress.ecotourexpress.config.exception.ResourceNotFoundException;
import com.ecotourexpress.ecotourexpress.model.Cliente;
import com.ecotourexpress.ecotourexpress.model.User;
import com.ecotourexpress.ecotourexpress.service.UserService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@Transactional
@RequestMapping("/users")
public class UserController {

    // Conexión a service
    @Autowired
    private UserService userService;
      
    // Obtener lista de users
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Agregar o actualizar user
    @PostMapping
    @PreAuthorize("permitAll()")
    public User newUser(@Valid @RequestBody User user) {
        return userService.saveUser(user);
    }

    // Seleccionar user por ID (Editar)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> updateUser(@PathVariable int id, @Valid @RequestBody User userDetails) {
        User user = userService.getUserById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User no encontrado con id: " + id));
        
        user.setNombre(userDetails.getNombre());
        user.setCorreo(userDetails.getCorreo());
        user.setRol(userDetails.getRol());
        user.setContraseña(userDetails.getContraseña());

        final User updatedUser = userService.saveUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    // Eliminar user
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
    }

    // Relacionar usuario con cliente existente
    @PostMapping("/{id_usuario}/clientes")
    public ResponseEntity<Object> addClienteToUsuario(@PathVariable int id_usuario, @RequestBody Cliente cliente) {
        try {
            User usuarioActualizado = userService.addClienteToUsuario(id_usuario, cliente);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body("Cliente no encontrado. Por favor, crea un nuevo cliente.");
        }
    }

    // Relacionar usuario con cliente nuevo
    @PostMapping("/{id_usuario}/clientes/nuevo")
    public ResponseEntity<User> crearClienteYAsociar(@PathVariable int id_usuario, @RequestBody Cliente cliente) {
        User usuarioActualizado = userService.crearClienteYAsociar(id_usuario, cliente);
        return ResponseEntity.ok(usuarioActualizado);
    }
}
