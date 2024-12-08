package com.ecotourexpress.ecotourexpress.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ecotourexpress.ecotourexpress.model.Cliente;
import com.ecotourexpress.ecotourexpress.model.dto.ClienteDTO;

public interface ClienteRepository extends CrudRepository<Cliente, Integer> {
    @Query("SELECT c FROM Cliente c WHERE c.cedula = :cedula")
    Cliente findByCedula(@Param("cedula") Integer cedula);
    boolean existsByCedula(Integer cedula);

    @Query("SELECT new com.ecotourexpress.ecotourexpress.model.dto.ClienteDTO(c.cedula, c.nombre, c.edad, c.genero) FROM Cliente c WHERE c.id = :id")
    ClienteDTO findClienteDTOById(@Param("id") int id);
    
    @Query("SELECT new com.ecotourexpress.ecotourexpress.model.dto.ClienteDTO(c.id, c.cedula, c.nombre, c.edad, c.genero) FROM Cliente c")
    List<ClienteDTO> findAllClientesDTO();

    Optional<Cliente> findByUsuarioId(int userId);
}
