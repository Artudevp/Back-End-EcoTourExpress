package com.ecotourexpress.ecotourexpress.Auth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecotourexpress.ecotourexpress.model.Rol;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping(value = "register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        // Verifica si el rol solicitado es ROLE_ADMIN
        if (request.getRol() == Rol.ROLE_ADMIN) {
            // Obtener la autenticación del contexto de seguridad
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Verificar si el usuario está autenticado y tiene el rol de ROLE_ADMIN
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new IllegalStateException("Solo los administradores autenticados pueden crear otros administradores.");
            }

            Object principal = authentication.getPrincipal();
            if (!(principal instanceof UserDetails)) {
                throw new IllegalStateException("No se pudo obtener el usuario autenticado.");
            }

            UserDetails currentUser = (UserDetails) principal;

            // Verificar si el usuario autenticado tiene el rol de ROLE_ADMIN
            if (!currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                throw new IllegalStateException("Solo los administradores pueden crear otros administradores.");
            }

            // Llamar al servicio con el usuario autenticado
            return ResponseEntity.ok(authService.register(request, currentUser));
        }

        // Si no es ROLE_ADMIN, crea el usuario con rol de ROLE_USER
        return ResponseEntity.ok(authService.register(request, null));
    }

}
