package com.ecotourexpress.ecotourexpress.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Hospedaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID_habitacion;

    @Column
    @NotBlank(message = "El tipo de habitacion no puede estar vacio")
    private String tipo;

    @Column
    @NotNull(message = "La capacidad de habitacion no puede estar vacio")
    @Min(value = 1, message = "La capacidad debe ser al menos 1.")
    private int capacidad;

    @Column
    @NotNull(message = "La disponibilidad de habitacion no puede estar vacio")
    @Min(value = 1, message = "La disponibilidad debe ser al menos 1.")
    private int cantidad;

    @Column
    @NotNull(message = "El precio de habitacion no puede estar vacio")
    @Min(value = 30000, message = "El precio minimo es de 30000")
    private int precio; // Precio en pesos (COP)

    @Column
    private String descripcion;

    @Column
    private boolean disponible;

    

    @OneToMany(mappedBy = "habitacion")
    @JsonIgnore
    private List<Cliente> clientes;
}