package com.ecotourexpress.ecotourexpress.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private int Disponibilidad; // Cambia el tipo si es necesario

    @Column
    private double Precio_hab;
}
