package com.ecotourexpress.ecotourexpress.model;

import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
    private int Duración_ruta;

    @Column
    private double Precio;

    @ManyToMany(mappedBy = "rutas")
    private List<Cliente> clientes;

    @ManyToMany
    private List<Actividad> actividades;
}
