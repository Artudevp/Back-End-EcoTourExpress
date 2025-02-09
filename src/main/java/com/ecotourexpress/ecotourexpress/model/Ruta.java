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
public class Ruta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    @NotBlank(message = "El nombre de la ruta no puede estar vacio.")
    private String nombre;

    @Column
    @NotNull(message = "La duracion de la ruta no puede estar vacio.")
    @Min(value = 1, message = "la duracion debe ser al menos 1 hora")
    private int duracion; // Duración en horas

    @Column
    @NotNull(message = "El precio de la ruta no puede estar vacio.")
    @Min(value = 0, message = "El precio no puede ser negativo.")
    private int precio; // Precio en pesos (COP)

    @Column
    @NotNull(message = "La capacidad de la ruta no puede estar vacio.")
    @Min(value = 1, message = "La capacidad debe ser de al menos 1")
    private int capacidad;

    @Column
    private String descripcion;

    @Column
    private boolean disponible;

    @Transient
    private List<String> actividadMediaUrls;

    @ManyToMany(mappedBy = "rutas")
    @JsonIgnore
    private List<Cliente> clientes;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "ruta_actividad",
        joinColumns = @JoinColumn(name = "ruta"),
        inverseJoinColumns = @JoinColumn(name = "actividad")
    )
    private List<Actividad> actividades;
}