package com.ecotourexpress.ecotourexpress.model.dto;


import com.ecotourexpress.ecotourexpress.model.Genero;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDTO {

    private int id;

    @NotNull(message = "La cédula no puede ser nula")
    @Positive(message = "La cédula debe ser un número positivo")
    private Integer cedula;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @Min(value = 18, message = "La edad no puede ser negativa")
    @Max(value = 120, message = "La edad no puede ser mayor a 120 años")
    private byte edad;

    private Genero genero;

    public ClienteDTO(Integer cedula, String nombre, byte edad, Genero genero) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.edad = edad;
        this.genero = genero;
    }
}
