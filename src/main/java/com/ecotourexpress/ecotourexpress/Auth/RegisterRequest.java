package com.ecotourexpress.ecotourexpress.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    String correo;
    String contraseña;
    String nombre;
    String apellido;
    String username;
}
