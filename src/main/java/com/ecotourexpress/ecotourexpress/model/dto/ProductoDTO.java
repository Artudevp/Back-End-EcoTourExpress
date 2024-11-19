package com.ecotourexpress.ecotourexpress.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ProductoDTO {

    private int ID_producto;

    @NotBlank(message = "La categoria del producto no puede estar vacía.")
    private String categoria;

    @NotBlank(message = "El nombre del producto no puede estar vacío.")
    private String nombre;

    @NotNull(message = "El precio no puede estar vacío")
    @Min(value = 100, message = "El precio mínimo es de 100.")
    private int precio;

    @Min(value = 0, message = "La cantidad no debe ser negativa.")
    @NotNull(message = "La cantidad disponible no puede estar vacía")
    private int cantidad;

    private String descripcion;

    private boolean disponible;
}