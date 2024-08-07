package com.ecotourexpress.ecotourexpress.model;

import java.util.List;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Ruta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID_ruta;

    @Column
    private String Nombre_ruta;

    @Column
    private int Duración_ruta; // Duración en horas

    @Column
    private int Precio; // Precio en pesos (COP)

    @ManyToMany
    @JoinTable(
        name = "ruta_cliente",
        joinColumns = @JoinColumn(name = "ID_ruta"),
        inverseJoinColumns = @JoinColumn(name = "ID_cliente")
    )
    private List<Cliente> clientes;

    @ManyToMany
    @JoinTable(
        name = "ruta_actividad",
        joinColumns = @JoinColumn(name = "ID_ruta"),
        inverseJoinColumns = @JoinColumn(name = "ID_actividad")
    )
    private List<Actividad> actividades;
}