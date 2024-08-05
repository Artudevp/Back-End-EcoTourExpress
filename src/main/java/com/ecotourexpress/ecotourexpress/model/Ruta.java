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

    @ManyToMany(mappedBy = "rutas")
    private List<Cliente> clientes;

    @ManyToMany
    private List<Actividad> actividades;
}