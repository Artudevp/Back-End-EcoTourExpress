package com.ecotourexpress.ecotourexpress.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecotourexpress.ecotourexpress.config.exception.CorreoYaRegistradoException;
import com.ecotourexpress.ecotourexpress.config.exception.ResourceNotFoundException;
import com.ecotourexpress.ecotourexpress.config.exception.UsernameYaRegistradoException;
import com.ecotourexpress.ecotourexpress.model.Cliente;
import com.ecotourexpress.ecotourexpress.model.User;
import com.ecotourexpress.ecotourexpress.model.DTO.UserDTO;
import com.ecotourexpress.ecotourexpress.repository.ClienteRepository;
import com.ecotourexpress.ecotourexpress.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    // Crear usuario nuevo
    public User saveUser(User user) {
        if (userRepository.existsByCorreo(user.getCorreo())) {
            throw new CorreoYaRegistradoException("El correo " + user.getCorreo() + " ya está registrado.");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UsernameYaRegistradoException("El nombre de usuario " + user.getUsername() + " ya está registrado.");
        }
        user.setContraseña(passwordEncoder.encode(user.getContraseña()));
        return userRepository.save(user);
    }

    // Obtener todos los usuarios de la base de datos
    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    // Seleccionar usuario por ID (editar)
    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    // Eliminar usuario
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    // Relacionar usuario con Cliente existente
    public User addClienteToUsuario(int id_usuario, Cliente cliente) {
        // Verifica si el usuario existe
        User usuario = userRepository.findById(id_usuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id_usuario));

        // Verifica si el cliente ya existe
        Cliente clienteExistente = clienteRepository.findByCedula(cliente.getCedula());
        if (clienteExistente != null) {
            clienteExistente.setUsuario(usuario);
            usuario.setCliente(clienteExistente);
            clienteRepository.save(clienteExistente);
        } else {
            // Si el cliente no existe, lanza una excepción o devuelve un mensaje
            throw new ResourceNotFoundException("Cliente no encontrado. Por favor, crea un nuevo cliente.");
        }

        return userRepository.save(usuario);
    }

    // Relacionar usuario a cliente nuevo
    public User crearClienteYAsociar(int id_usuario, Cliente cliente) {
        // Verifica si el usuario existe
        User usuario = userRepository.findById(id_usuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id_usuario));

        // Verifica si el cliente ya existe
        Cliente clienteExistente = clienteRepository.findByCedula(cliente.getCedula());
        if (clienteExistente != null) {
            clienteExistente.setUsuario(usuario);
            usuario.setCliente(clienteExistente);
            clienteRepository.save(clienteExistente);
        } else {
            cliente.setUsuario(usuario);
            clienteRepository.save(cliente);
            usuario.setCliente(cliente);
        }

        return userRepository.save(usuario);
    }

    // Convertir User a UserDTO
    public UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getNombre(),
                user.getApellido(),
                user.getCorreo(),
                user.getUsername(),
                user.getContraseña(),
                user.getRol()
        );
    }

    // Convertir UserDTO a User
    public User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId()); // Si es necesario
        user.setNombre(userDTO.getNombre());
        user.setApellido(userDTO.getApellido());
        user.setCorreo(userDTO.getCorreo());
        user.setUsername(userDTO.getUsername());
        user.setContraseña(passwordEncoder.encode(userDTO.getContraseña()));
        user.setRol(userDTO.getRol());
        return user;
    }
}
