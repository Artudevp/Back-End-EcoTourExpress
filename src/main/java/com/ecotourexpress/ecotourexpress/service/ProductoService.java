package com.ecotourexpress.ecotourexpress.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ecotourexpress.ecotourexpress.model.Producto;
import com.ecotourexpress.ecotourexpress.repository.ProductoRepository;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public Producto saveProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public List<Producto> getAllProductos() {
        return (List<Producto>) productoRepository.findAll();
    }

    public void deleteProducto(int id) {
        productoRepository.deleteById(id);
    }
}
