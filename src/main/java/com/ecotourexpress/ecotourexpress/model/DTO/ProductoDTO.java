package com.ecotourexpress.ecotourexpress.model.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ProductoDTO {

    private int id_Producto;

    @NotBlank(message = "La categoria del producto no puede estar vacía.")
    private String categoria;

    @NotBlank(message = "El nombre del producto no puede estar vacío.")
    private String nombre_P;

    @NotNull(message = "El precio no puede estar vacío")
    @Min(value = 100, message = "El precio mínimo es de 100.")
    private int precio_P;

    @Min(value = 0, message = "La cantidad no debe ser negativa.")
    @NotNull(message = "La cantidad disponible no puede estar vacía")
    private int cantidad_Disponible;

    private String descripcion_P;

    private boolean Disponible;
}