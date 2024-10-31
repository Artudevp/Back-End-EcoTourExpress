package com.ecotourexpress.ecotourexpress.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ecotourexpress.ecotourexpress.model.Cliente;
import com.ecotourexpress.ecotourexpress.model.DTO.ClienteDTO;

public interface ClienteRepository extends CrudRepository<Cliente, Integer> {
    @Query("SELECT c FROM Cliente c WHERE c.cedula = :cedula")
    Cliente findByCedula(@Param("cedula") Integer cedula);
    boolean existsByCedula(Integer cedula);

    @Query("SELECT new com.ecotourexpress.ecotourexpress.model.DTO.ClienteDTO(c.cedula, c.Nombre_cli, c.Edad, c.Genero) FROM Cliente c WHERE c.id = :id")
    ClienteDTO findClienteDTOById(@Param("id") Long id);

    @Query("SELECT new com.ecotourexpress.ecotourexpress.model.DTO.ClienteDTO(c.cedula, c.Nombre_cli, c.Edad, c.Genero) FROM Cliente c")
    List<ClienteDTO> findAllClientesDTO();
}
