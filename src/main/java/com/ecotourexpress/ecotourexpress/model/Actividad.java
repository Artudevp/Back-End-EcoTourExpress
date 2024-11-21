package com.ecotourexpress.ecotourexpress.model;

import java.util.List;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    @NotBlank(message = "El nombre de la actividad no puede estar vacío.")
    private String nombre;

    @Column
    @NotNull(message = "La duracion de la actividad no puede estar vacío.")
    @Min(value = 1, message = "La duración debe ser al menos 1 hora.")
    private int duracion; // Duración en horas

    @Column
    @NotNull(message = "El Precio de la actividad no puede estar vacío.")
    @Min(value = 100, message = "El precio minimo es de 100.")
    private int precio; // Precio en pesos (COP)

    @Column
    @NotNull(message = "La capacidad de la actividad no puede estar vacío.")
    @Min(value = 1, message = "La capacidad debe ser al menos 1.")
    private int capacidad;

    @Column
    private String descripcion;

    @Column
    private boolean disponible;

    @ManyToMany(mappedBy = "actividades")
    @JsonIgnore
    private List<Cliente> clientes;

    @ManyToMany(mappedBy = "actividades")
    @JsonIgnore
    private List<Ruta> rutas;
}
