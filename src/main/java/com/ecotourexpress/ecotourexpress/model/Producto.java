package com.ecotourexpress.ecotourexpress.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID_producto;

    @Column
    private String Categoria;

    @Column
    private String Nombre_P;

    @Column
    private double Precio_P;

    @Column
    private int Cantidad_disponible;
}
