package com.ecotourexpress.ecotourexpress.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ecotourexpress.ecotourexpress.model.Producto;
import com.ecotourexpress.ecotourexpress.repository.ProductoRepository;

@Service
public class ProductoService {


    // Conexion al repositorio de Producto
    @Autowired
    private ProductoRepository productoRepository;

    // Crear producto
    public Producto saveProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    // Obtener todos los productos
    public List<Producto> getAllProductos() {
        return (List<Producto>) productoRepository.findAll();
    }

    // Seleccionar producto por ID (Editar)
    public Optional<Producto> getProductoById(int id) {
        return productoRepository.findById(id);
    }
    
    // Eliminar producto
    public void deleteProducto(int id) {
        productoRepository.deleteById(id);
    }
}
