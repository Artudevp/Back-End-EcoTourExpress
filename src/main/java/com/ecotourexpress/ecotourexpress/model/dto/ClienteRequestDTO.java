package com.ecotourexpress.ecotourexpress.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClienteRequestDTO {
    @NotNull(message = "La cédula es obligatoria")
    private Integer cedula;
}
