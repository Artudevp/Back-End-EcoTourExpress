package com.ecotourexpress.ecotourexpress.repository;

import org.springframework.data.repository.CrudRepository;
import com.ecotourexpress.ecotourexpress.model.Producto;

public interface ProductoRepository extends CrudRepository<Producto, Integer> {
}
