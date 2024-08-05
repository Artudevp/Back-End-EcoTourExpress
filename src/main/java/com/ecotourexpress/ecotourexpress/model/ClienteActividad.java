package com.ecotourexpress.ecotourexpress.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@IdClass(ClienteActividadKey.class)
public class ClienteActividad {

    @Id
    @Column(name = "ID_cliente")
    private int clienteId;

    @Id
    @Column(name = "ID_actividad")
    private int actividadId;

    // Other fields, getters, and setters

    @ManyToOne
    @JoinColumn(name = "ID_cliente", insertable = false, updatable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "ID_actividad", insertable = false, updatable = false)
    private Actividad actividad;
}
