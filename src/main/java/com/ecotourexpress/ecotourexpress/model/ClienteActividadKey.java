package com.ecotourexpress.ecotourexpress.model;

import java.io.Serializable;
import java.util.Objects;

public class ClienteActividadKey implements Serializable {
    
    private int clienteId;
    private int actividadId;
    
    // Constructor vacío (requerido por JPA)
    public ClienteActividadKey() {}

    // Constructor con parámetros
    public ClienteActividadKey(int clienteId, int actividadId) {
        this.clienteId = clienteId;
        this.actividadId = actividadId;
    }

    // Getters y Setters
    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public int getActividadId() {
        return actividadId;
    }

    public void setActividadId(int actividadId) {
        this.actividadId = actividadId;
    }

    // Equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClienteActividadKey that = (ClienteActividadKey) o;
        return clienteId == that.clienteId && actividadId == that.actividadId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(clienteId, actividadId);
    }
}
