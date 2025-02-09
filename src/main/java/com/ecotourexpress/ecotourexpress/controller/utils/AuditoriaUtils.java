package com.ecotourexpress.ecotourexpress.controller.utils;

import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.ecotourexpress.ecotourexpress.model.Auditoria;
import com.ecotourexpress.ecotourexpress.repository.AuditoriaRepository;


@Component
public class AuditoriaUtils {

    private static AuditoriaRepository auditoriaRepository;

    public AuditoriaUtils(AuditoriaRepository auditoriaRepository) {
        AuditoriaUtils.auditoriaRepository = auditoriaRepository;
    }

    public static void registrarAccion(String accion, String recurso, String mensaje) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String usuario = authentication.getName();
            LocalDateTime timestamp = LocalDateTime.now();

            Auditoria auditoria = new Auditoria();
            auditoria.setUsuario(usuario);
            auditoria.setAccion(accion);
            auditoria.setRecurso(recurso);
            auditoria.setMensaje(mensaje);
            auditoria.setTimestamp(timestamp);

            auditoriaRepository.save(auditoria);
        }
    }
}

