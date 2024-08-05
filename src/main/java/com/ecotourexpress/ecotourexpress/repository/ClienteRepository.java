package com.ecotourexpress.ecotourexpress.repository;

import org.springframework.data.repository.CrudRepository;
import com.ecotourexpress.ecotourexpress.model.Cliente;

public interface ClienteRepository extends CrudRepository<Cliente, Integer> {
}
