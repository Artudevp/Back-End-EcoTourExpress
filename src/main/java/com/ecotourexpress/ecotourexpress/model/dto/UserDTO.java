package com.ecotourexpress.ecotourexpress.model.dto;

import com.ecotourexpress.ecotourexpress.model.Rol;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private int id;

    private String nombre;

    private String apellido;

    private String correo;
    
    private String username;

    private String contraseña;
    
    @Enumerated(EnumType.STRING)
    Rol rol;

}
