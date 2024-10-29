package com.ecotourexpress.ecotourexpress.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ecotourexpress.ecotourexpress.model.Cliente;

public interface ClienteRepository extends CrudRepository<Cliente, Integer> {
    @Query("SELECT c FROM Cliente c WHERE c.cedula = :cedula")
    Cliente findByCedula(@Param("cedula") Integer cedula);
    boolean existsByCedula(Integer cedula);
    @SuppressWarnings("null")
    @Override
    List<Cliente> findAll();
}
