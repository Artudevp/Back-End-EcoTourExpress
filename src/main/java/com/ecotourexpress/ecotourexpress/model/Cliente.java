package com.ecotourexpress.ecotourexpress.model;

import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID_cliente;

    @Column(unique = true, nullable = false)
    @NotNull(message = "La cédula no puede ser nula")
    @Positive(message = "La cédula debe ser un número positivo")
    private Integer cedula;

    @SuppressWarnings("deprecation")
    @Column(nullable = false)
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String Nombre_cli;

    @Column(nullable = false)
    @Min(value = 18, message = "La edad no puede ser negativa")
    @Max(value = 120, message = "La edad no puede ser mayor a 120 años")
    private int Edad;

    @Column
    @Pattern(regexp = "[MFO]", message = "El género debe ser 'M', 'F' u 'O'")
    private String Genero;

    @ManyToOne
    @JoinColumn(name = "ID_habitacion")
    private Hospedaje habitacion;

    @ManyToMany
    @JoinTable(
        name = "cliente_actividad", 
        joinColumns = @JoinColumn(name = "ID_cliente"),
        inverseJoinColumns = @JoinColumn(name = "ID_actividad"))
    private List<Actividad> actividades;

    @ManyToMany
    @JoinTable(
        name = "cliente_ruta",
        joinColumns = @JoinColumn(name = "ID_cliente"),
        inverseJoinColumns = @JoinColumn(name = "ID_ruta"))
    private List<Ruta> rutas;

    @ManyToMany
    @JoinTable(
        name = "cliente_producto",
        joinColumns = @JoinColumn(name = "ID_cliente"),
        inverseJoinColumns = @JoinColumn(name = "ID_producto"))
    private List<Producto> productos;
    
}
