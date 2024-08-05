package com.ecotourexpress.ecotourexpress.model;

import java.util.List;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID_actividad;

    @Column
    private String Nombre_act;

    @Column
    private int Duración_act;

    @Column
    private double Precio_act;

    @ManyToMany(mappedBy = "actividades")
    private List<Cliente> clientes;

    @ManyToMany(mappedBy = "actividades")
    private List<Ruta> rutas;
}