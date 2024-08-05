package com.ecotourexpress.ecotourexpress.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
