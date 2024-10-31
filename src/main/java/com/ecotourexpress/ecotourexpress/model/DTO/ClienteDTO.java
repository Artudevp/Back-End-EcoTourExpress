package com.ecotourexpress.ecotourexpress.model.DTO;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClienteDTO {

    private int ID_cliente;

    @NotNull(message = "La cédula no puede ser nula")
    @Positive(message = "La cédula debe ser un número positivo")
    private Integer cedula;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String Nombre_cli;

    @Min(value = 18, message = "La edad no puede ser negativa")
    @Max(value = 120, message = "La edad no puede ser mayor a 120 años")
    private byte Edad;

    @Pattern(regexp = "[MFO]", message = "El género debe ser 'M', 'F' u 'O'")
    private String Genero;

    public ClienteDTO(Integer cedula, String nombreCli, @Min(value = 18, message = "La edad no puede ser negativa") @Max(value = 120, message = "La edad no puede ser mayor a 120 años") byte edad, String genero) {
        this.cedula = cedula;
        this.Nombre_cli = nombreCli;
        this.Edad = edad;
        this.Genero = genero;
    }
    
    public ClienteDTO() {}
}
