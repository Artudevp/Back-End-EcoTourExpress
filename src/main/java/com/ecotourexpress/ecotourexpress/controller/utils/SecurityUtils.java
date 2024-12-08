package com.ecotourexpress.ecotourexpress.controller.utils;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import com.ecotourexpress.ecotourexpress.model.Cliente;
import com.ecotourexpress.ecotourexpress.model.User;
import com.ecotourexpress.ecotourexpress.repository.ClienteRepository;

public class SecurityUtils {

    public static int validateAndGetClientId(Integer requestClientId, int authenticatedClientId) {
        if (!isAdmin() && (requestClientId != null && !requestClientId.equals(authenticatedClientId))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para realizar esta acción.");
        }
        return requestClientId != null ? requestClientId : authenticatedClientId;
    }

    public static int validateAndGetUserId(Integer requestUserId, int authenticatedUserId) {
        if (!isAdmin() && (requestUserId != null && !requestUserId.equals(authenticatedUserId))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para realizar esta accion.");
        }
        return requestUserId != null ? requestUserId : authenticatedUserId;
    }

    public static boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null &&
               authentication.getAuthorities().stream()
                   .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    public static int getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }
        User authenticatedUser = (User) authentication.getPrincipal();
        return authenticatedUser.getId();
    }

    public static int getAuthenticatedClientId(ClienteRepository clienteRepository) {
        int authenticatedUserId = getAuthenticatedUserId();
        Cliente cliente = clienteRepository.findByUsuarioId(authenticatedUserId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Cliente no asociado al usuario autenticado"));
        return cliente.getId();
    }

}
