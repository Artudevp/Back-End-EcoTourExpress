package com.ecotourexpress.ecotourexpress.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ecotourexpress.ecotourexpress.model.Cliente;
import com.ecotourexpress.ecotourexpress.model.dto.ClienteDTO;

public interface ClienteRepository extends CrudRepository<Cliente, Integer> {
    @Query("SELECT c FROM Cliente c WHERE c.cedula = :cedula")
    Cliente findByCedula(@Param("cedula") Integer cedula);
    boolean existsByCedula(Integer cedula);

    @Query("SELECT new com.ecotourexpress.ecotourexpress.model.dto.ClienteDTO(c.cedula, c.nombre, c.edad, c.genero) FROM Cliente c WHERE c.ID_cliente = :id")
    ClienteDTO findClienteDTOById(@Param("id") Long id);
    
    @Query("SELECT new com.ecotourexpress.ecotourexpress.model.dto.ClienteDTO(c.ID_cliente, c.cedula, c.nombre, c.edad, c.genero) FROM Cliente c")
    List<ClienteDTO> findAllClientesDTO();

}
