package com.ecotourexpress.ecotourexpress.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.ecotourexpress.ecotourexpress.model.Producto;

public interface ProductoRepository extends CrudRepository<Producto, Integer> {
    @SuppressWarnings("null")
    @Override
    List<Producto> findAll();
}
