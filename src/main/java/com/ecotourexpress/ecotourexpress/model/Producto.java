package com.ecotourexpress.ecotourexpress.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID_producto;

    @Column
    @NotBlank(message = "La categoria del producto no puede estar vacía.")
    private String Categoria;

    @Column
    @NotBlank(message = "El nombre del producto no puede estar vacío.")
    private String Nombre_p;

    @Column
    @Min(value = 100, message = "El precio minimo es de 100.")
    private int Precio_p;

    @Column
    @Min(value = 0, message = "La cantidad no debe ser negativa")
    private int Cantidad_disponible;

    @Column
    private String Descripcion_p;

    @ManyToMany (mappedBy = "productos")
    @JsonIgnore
    private List<Cliente> clientes;

    public int getID_producto() {
        return ID_producto;
    }    
}
