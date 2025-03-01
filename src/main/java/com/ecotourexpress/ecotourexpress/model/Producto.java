package com.ecotourexpress.ecotourexpress.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    @NotBlank(message = "La categoria del producto no puede estar vacía.")
    private String categoria;

    @Column
    @NotBlank(message = "El nombre del producto no puede estar vacío.")
    private String nombre;

    @Column
    @NotNull(message = "El precio no puede estar vacío")
    @Min(value = 100, message = "El precio mínimo es de 100.")
    private int precio;

    @Column
    @Min(value = 0, message = "La cantidad no debe ser negativa.")
    @NotNull(message = "La cantidad disponible no puede estar vacía")
    private int cantidad;

    @Column
    private String descripcion;

    @Column
    private boolean disponible;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "producto_media", joinColumns = @JoinColumn(name = "producto_id"))
    @Column(name = "media_url")
    private List<String> mediaUrls = new ArrayList<>();

    @ManyToMany (mappedBy = "productos")
    @JsonIgnore
    private List<Cliente> clientes;

    public int getId() {
        return id;
    }
}
