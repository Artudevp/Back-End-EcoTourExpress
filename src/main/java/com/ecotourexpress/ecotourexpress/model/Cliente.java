package com.ecotourexpress.ecotourexpress.model;

import java.util.List;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID_cliente;

    @Column
    private String Nombre_cli;

    @Column
    private int Edad;

    @Column
    private char Genero;

    @ManyToOne
    @JoinColumn(name = "ID_habitacion")
    private Hospedaje habitacion;

    @ManyToMany
    @JoinTable(
        name = "cliente_actividad", 
        joinColumns = @JoinColumn(name = "ID_cliente"),
        inverseJoinColumns = @JoinColumn(name = "ID_actividad"))
    private List<Actividad> actividades;

    @ManyToMany
    @JoinTable(
        name = "cliente_ruta",
        joinColumns = @JoinColumn(name = "ID_cliente"),
        inverseJoinColumns = @JoinColumn(name = "ID_ruta"))
    private List<Ruta> rutas;

    @ManyToMany
    @JoinTable(
        name = "cliente_producto",
        joinColumns = @JoinColumn(name = "ID_cliente"),
        inverseJoinColumns = @JoinColumn(name = "ID_producto"))
    private List<Producto> productos;
}
