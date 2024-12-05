package com.ecotourexpress.ecotourexpress.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecotourexpress.ecotourexpress.Auth.AuthResponse;
import com.ecotourexpress.ecotourexpress.Auth.AuthService;
import com.ecotourexpress.ecotourexpress.Auth.RegisterRequest;
import com.ecotourexpress.ecotourexpress.config.exception.ResourceNotFoundException;
import com.ecotourexpress.ecotourexpress.model.Cliente;
import com.ecotourexpress.ecotourexpress.model.User;
import com.ecotourexpress.ecotourexpress.model.dto.ClienteRequestDTO;
import com.ecotourexpress.ecotourexpress.model.dto.UserDTO;
import com.ecotourexpress.ecotourexpress.repository.UserRepository;
import com.ecotourexpress.ecotourexpress.service.UserService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@Transactional
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, UserService userService, AuthService authService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    // Obtener lista de usuarios
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    // Registrar nuevo usuario
    @PostMapping
    @PreAuthorize("permitAll()")
    public UserDTO newUser(@Valid @RequestBody RegisterRequest registerRequest, @AuthenticationPrincipal UserDetails currentUser) {
        AuthResponse authResponse = authService.register(registerRequest, currentUser);
        User user = authService.getUserFromToken(authResponse.getToken());
        return authService.convertToDTO(user);
    }

    // Actualizar usuario
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUser(@PathVariable int id, @Valid @RequestBody UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User no encontrado con id: " + id));

        existingUser.setNombre(userDTO.getNombre());
        existingUser.setApellido(userDTO.getApellido());
        existingUser.setCorreo(userDTO.getCorreo());
        existingUser.setUsername(userDTO.getUsername());
        existingUser.setRol(userDTO.getRol());

        if (userDTO.getContraseña() != null && !userDTO.getContraseña().isEmpty()) {
            existingUser.setContraseña(passwordEncoder.encode(userDTO.getContraseña()));
        }

        User updatedUser = userRepository.save(existingUser);
        return ResponseEntity.ok(userService.convertToDTO(updatedUser));
    }

    // Eliminar usuario
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Usuario eliminado correctamente.");
    }

    // Vincular usuario a cliente existente
    @PostMapping("/{id_usuario}/clientes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> addClienteToUsuario(
            @PathVariable int id_usuario,
            @Valid @RequestBody ClienteRequestDTO clienteDTO) {
        User usuarioActualizado = userService.addClienteToUsuario(id_usuario, clienteDTO.getCedula());
        return ResponseEntity.ok(userService.convertToDTO(usuarioActualizado));
    }

    // Crear y vincular nuevo cliente
    @PostMapping("/{id_usuario}/clientes/nuevo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> crearClienteYAsociar(@PathVariable int id_usuario, @Valid @RequestBody Cliente cliente) {
        User usuarioActualizado = userService.crearClienteYAsociar(id_usuario, cliente);
        return ResponseEntity.ok(userService.convertToDTO(usuarioActualizado));
    }

    // Desvincular usuario de cliente
    @DeleteMapping("/{id_usuario}/clientes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> desvincularClienteDeUsuario(@PathVariable int id_usuario) {
        userService.desvincularClienteDeUsuario(id_usuario);
        return ResponseEntity.ok("Usuario desvinculado correctamente del cliente.");
    }
}