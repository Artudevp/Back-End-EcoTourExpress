package com.ecotourexpress.ecotourexpress.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.ecotourexpress.ecotourexpress.model.Rol;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String username;
    private String correo;
    private String contraseña;
    private String nombre;
    private String apellido;
    private Rol rol;
}
