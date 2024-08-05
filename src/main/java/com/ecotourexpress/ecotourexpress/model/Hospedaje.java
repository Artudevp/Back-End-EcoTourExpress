package com.ecotourexpress.ecotourexpress.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Hospedaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID_habitacion;

    @Column
    private String Tipo_hab;

    @Column
    private int Capacidad;

    @Column
    private int Disponibilidad;

    @Column
    private int Precio_hab; // Precio en pesos (COP)
}