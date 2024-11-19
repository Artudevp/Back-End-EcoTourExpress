package com.ecotourexpress.ecotourexpress.Auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecotourexpress.ecotourexpress.Jwt.JwtService;
import com.ecotourexpress.ecotourexpress.model.Rol;
import com.ecotourexpress.ecotourexpress.model.User;
import com.ecotourexpress.ecotourexpress.model.dto.UserDTO;
import com.ecotourexpress.ecotourexpress.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public User getUserFromToken(String token) {
        // Extraer el username del token
        String username = jwtService.getUsernameFromToken(token);

        // Buscar el usuario en la base de datos
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el username: " + username));
    }

    public AuthResponse login(LoginRequest request) {
        // Autenticación del usuario
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getContraseña()));
    
        // Recuperar el usuario y convertirlo a UserDTO
        UserDTO userDTO = userRepository.findByUsername(request.getUsername())
                .map(user -> convertToDTO(user))
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el nombre: " + request.getUsername()));
    
        // Generar el token usando UserDTO
        String token = jwtService.getToken(userDTO);
    
        return AuthResponse.builder()
            .token(token)
            .build();
    }
    


    public AuthResponse register(RegisterRequest request, UserDetails currentUser) {
        // Verifica si el usuario actual es admin antes de permitir crear otro admin
        if (request.getRol() == Rol.ROLE_ADMIN && currentUser == null) {
            throw new IllegalStateException("Solo los administradores pueden crear otros administradores.");
        }

        // Verifica si el correo ya está registrado
        if (userRepository.existsByCorreo(request.getCorreo())) {
            throw new IllegalArgumentException("El correo " + request.getCorreo() + " ya está registrado.");
        }

        // Verifica si el nombre de usuario ya está registrado
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario " + request.getUsername() + " ya está registrado.");
        }

        // Determinar el rol del usuario, asignar ROLE_USER si no es especificado o si el usuario no es admin
        Rol userRol = (request.getRol() != null && currentUser != null) ? request.getRol() : Rol.ROLE_USER;

        // Crear y guardar el usuario
        User user = User.builder()
            .username(request.getUsername())
            .correo(request.getCorreo())
            .contraseña(passwordEncoder.encode(request.getContraseña()))
            .nombre(request.getNombre())
            .apellido(request.getApellido())
            .rol(userRol)
            .build();

        userRepository.save(user);

        // Generar y devolver el token de autenticación
        return AuthResponse.builder()
            .token(jwtService.getToken(user))
            .build();
    }


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

    
}
