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
import com.ecotourexpress.ecotourexpress.model.dto.UserDTO;
import com.ecotourexpress.ecotourexpress.repository.ClienteRepository;
import com.ecotourexpress.ecotourexpress.repository.UserRepository;

@Service
public class UserService {
    
    // Conexion a PasswordEncoder y repositorios
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    // ==========================================
    // CRUD USUARIOS
    // Métodos para manejar el CRUD
    // ==========================================


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
    

    // Obtener todos los usuarios como UserDTO
    public List<UserDTO> getAllUsers() {
        return userRepository.findAllUsers();
    }

    // Obtener un usuario por ID como UserDTO
    public Optional<UserDTO> getUserById(int id) {
        return Optional.ofNullable(userRepository.findUserDTOById((long) id));
    }

    // Eliminar usuario
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    // ==========================================
    // USUARIOS - CLIENTES
    // Métodos para relacionar clientes
    // ==========================================

    // Ver cliente relacionado a usuario
    public Optional<Cliente> getClienteOfUser(int id_usuario){
        return clienteRepository.findByUsuarioId(id_usuario);
    }


    // Relacionar usuario con Cliente existente
    public User addClienteToUsuario(int id_usuario, Integer cedulaCliente) {
        // Verifica si el usuario existe
        User usuario = userRepository.findById(id_usuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id_usuario));
    
        // Verifica si el cliente ya existe
        Cliente clienteExistente = clienteRepository.findByCedula(cedulaCliente);
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

    // Desvincular usuario de cliente existente
    public User desvincularClienteDeUsuario(int id_usuario) {
        // Verifica si el usuario existe
        User usuario = userRepository.findById(id_usuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id_usuario));

        // Verifica si el cliente está asociado con el usuario
        Cliente cliente = usuario.getCliente();
        if (cliente == null) {
            throw new ResourceNotFoundException("Este usuario no tiene un cliente asociado.");
        }

        // Desvincula al cliente del usuario
        cliente.setUsuario(null);
        usuario.setCliente(null);

        // Guarda los cambios en ambas entidades
        clienteRepository.save(cliente); // Desvincula el cliente
        return userRepository.save(usuario); // Desvincula el usuario
    }

    // ==========================================
    // CONVERSION A DTO
    // Métodos para convertir a DTO
    // ==========================================


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
