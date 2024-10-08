package com.ecotourexpress.ecotourexpress.model;

import java.util.List;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID_actividad;

    @Column
    @NotBlank(message = "El nombre de la actividad no puede estar vacío.")
    private String Nombre_act;

    @Column
    @Min(value = 1, message = "La duración debe ser al menos 1 hora.")
    private int Duracion_act; // Duración en horas

    @Column
    @Min(value = 100, message = "El precio minimo es de 100.")
    private int Precio_act; // Precio en pesos (COP)

    @Column
    @Min(value = 1, message = "La capacidad debe ser al menos 1.")
    private int Capacidad;

    @ManyToMany(mappedBy = "actividades")
    @JsonIgnore
    private List<Cliente> clientes;

    @ManyToMany(mappedBy = "actividades")
    @JsonIgnore
    private List<Ruta> rutas;
}
