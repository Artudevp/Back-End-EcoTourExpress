package com.ecotourexpress.ecotourexpress.model;

import jakarta.persistence.*;
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
    private String Nombre_p;

    @Column
    private int Precio_p;

    @Column
    private int Cantidad_disponible;
}
