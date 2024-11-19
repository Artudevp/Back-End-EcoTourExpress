package com.ecotourexpress.ecotourexpress.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Modelo de respuesta estándar
    private ResponseEntity<Map<String, String>> createErrorResponse(String error, String detalle, HttpStatus status) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("Error", error);
        errorResponse.put("Detalle", detalle);
        return new ResponseEntity<>(errorResponse, status);
    }

    // Manejador para excepciones de validación
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> detalles = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            detalles.add(error.getField() + ": " + error.getDefaultMessage());
        }
        String detalleFinal = String.join("; ", detalles);
        return createErrorResponse("Ocurrió un error en la solicitud", detalleFinal, HttpStatus.BAD_REQUEST);
    }


    // Manejador para cédula ya registrada
    @ExceptionHandler(CedulaYaRegistradaException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Map<String, String>> handleCedulaYaRegistradaException(CedulaYaRegistradaException ex) {
        return createErrorResponse(ex.getMessage(), "", HttpStatus.CONFLICT);
    }

    // Manejador para correo ya registrado
    @ExceptionHandler(CorreoYaRegistradoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Map<String, String>> handleCorreoYaRegistradoException(CorreoYaRegistradoException ex) {
        return createErrorResponse(ex.getMessage(), "", HttpStatus.CONFLICT);
    }

    // Manejador para usuario ya registrado
    @ExceptionHandler(UsernameYaRegistradoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Map<String, String>> handleUsernameYaRegistradoException(UsernameYaRegistradoException ex) {
        return createErrorResponse(ex.getMessage(), "", HttpStatus.CONFLICT);
    }

    // Manejador general de errores 
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleAllExceptions(Exception ex) {
        return createErrorResponse("Ocurrió un error en la solicitud", ex.getMessage(), HttpStatus.FORBIDDEN);
    }
}
