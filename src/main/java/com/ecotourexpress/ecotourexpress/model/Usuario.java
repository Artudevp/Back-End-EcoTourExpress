package com.ecotourexpress.ecotourexpress.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID_usuario;

    @Column
    private String Nombre;

    @Column
    private String Correo;

    @Column
    private String Contraseña;
}
