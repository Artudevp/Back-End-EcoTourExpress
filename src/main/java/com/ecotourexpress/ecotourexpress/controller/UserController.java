package com.ecotourexpress.ecotourexpress.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecotourexpress.ecotourexpress.config.exception.ResourceNotFoundException;
import com.ecotourexpress.ecotourexpress.model.User;
import com.ecotourexpress.ecotourexpress.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    // Conexión a service
    @Autowired
    private UserService userService;
      
    // Obtener lista de users
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Agregar o actualizar user
    @PostMapping
    public User newUser(@Valid @RequestBody User user) {
        return userService.saveUser(user);
    }

    // Seleccionar user por ID (Editar)
    @PutMapping("/{id}")
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
    public void deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
    }

}
