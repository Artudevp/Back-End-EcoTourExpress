package com.ecotourexpress.ecotourexpress.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class Ruta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID_ruta;

    @Column
    @NotBlank(message = "El nombre de la ruta no puede estar vacio.")
    private String Nombre_ruta;

    @Column
    @Min(value = 1, message = "la duracion debe ser al menos 1 hora")
    private int Duración_ruta; // Duración en horas

    @Column
    @Min(value = 0, message = "El precio no puede ser negativo.")
    private int Precio; // Precio en pesos (COP)

    @Column
    @Min(value = 1, message = "La capacidad debe ser de al menos")
    private int Capacidad;

    @Column
    private String Descripcion_ruta;

    @ManyToMany(mappedBy = "rutas")
    @JsonIgnore
    private List<Cliente> clientes;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "ruta_actividad",
        joinColumns = @JoinColumn(name = "ID_ruta"),
        inverseJoinColumns = @JoinColumn(name = "ID_actividad")
    )
    private List<Actividad> actividades;
}