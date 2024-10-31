package com.ecotourexpress.ecotourexpress.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.ecotourexpress.ecotourexpress.model.DTO.UserDTO;
import com.ecotourexpress.ecotourexpress.service.UserService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@Transactional
@RequestMapping("/users")
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    // Obtener lista de users
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .collect(Collectors.toList());
    }

    // Agregar o actualizar user
    @PostMapping
    @PreAuthorize("permitAll()")
    public UserDTO newUser(@Valid @RequestBody UserDTO userDTO) {
        User user = userService.convertToEntity(userDTO);
        User savedUser = userService.saveUser(user);
        return userService.convertToDTO(savedUser);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUser(@PathVariable int id, @Valid @RequestBody UserDTO userDTO) {
        UserDTO existingUserDTO = userService.getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User no encontrado con id: " + id));

        // Actualiza los campos del DTO existente con los datos del DTO recibido
        existingUserDTO.setNombre(userDTO.getNombre());
        existingUserDTO.setApellido(userDTO.getApellido());
        existingUserDTO.setCorreo(userDTO.getCorreo());
        existingUserDTO.setUsername(userDTO.getUsername());
        existingUserDTO.setContraseña(passwordEncoder.encode(userDTO.getContraseña()));
        existingUserDTO.setRol(userDTO.getRol());

        // Convierte UserDTO a User y guarda en la base de datos
        User updatedUser = userService.saveUser(userService.convertToEntity(existingUserDTO));

        // Retorna el User actualizado convertido nuevamente a UserDTO
        return ResponseEntity.ok(userService.convertToDTO(updatedUser));
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
            return ResponseEntity.ok(userService.convertToDTO(usuarioActualizado));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body("Cliente no encontrado. Por favor, crea un nuevo cliente.");
        }
    }

    // Relacionar usuario con cliente nuevo
    @PostMapping("/{id_usuario}/clientes/nuevo")
    public ResponseEntity<UserDTO> crearClienteYAsociar(@PathVariable int id_usuario, @RequestBody Cliente cliente) {
        User usuarioActualizado = userService.crearClienteYAsociar(id_usuario, cliente);
        return ResponseEntity.ok(userService.convertToDTO(usuarioActualizado));
    }
}
