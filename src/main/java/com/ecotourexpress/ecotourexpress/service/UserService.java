package com.ecotourexpress.ecotourexpress.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ecotourexpress.ecotourexpress.config.exception.ResourceNotFoundException;
import com.ecotourexpress.ecotourexpress.model.Cliente;
import com.ecotourexpress.ecotourexpress.model.User;
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
        // Si el cliente ya existe, lo asocia con el usuario
        clienteExistente.setUsuario(usuario); // Asocia el usuario al cliente existente
        usuario.setCliente(clienteExistente); // Asocia el cliente existente al usuario
        clienteRepository.save(clienteExistente); // Guarda los cambios
    } else {
        // Si el cliente no existe, lanza una excepción o devuelve un mensaje
        throw new ResourceNotFoundException("Cliente no encontrado. Por favor, crea un nuevo cliente.");
    }

    // Guarda el usuario (no es necesario guardar nuevamente si solo cambió el cliente)
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
            // Si el cliente ya existe, lo asocia con el usuario
            clienteExistente.setUsuario(usuario);
            usuario.setCliente(clienteExistente);
            clienteRepository.save(clienteExistente); // Guarda los cambios
        } else {
            // Si el cliente no existe, crea un nuevo cliente
            cliente.setUsuario(usuario); // Asocia el usuario al nuevo cliente
            clienteRepository.save(cliente); // Guarda el nuevo cliente
            usuario.setCliente(cliente); // Asocia el nuevo cliente al usuario
        }
    
        // Guarda el usuario (no es necesario guardar nuevamente si solo cambió el cliente)
        return userRepository.save(usuario);
    }
    
}
