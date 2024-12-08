package com.ecotourexpress.ecotourexpress.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ecotourexpress.ecotourexpress.model.Producto;
import com.ecotourexpress.ecotourexpress.model.dto.ProductoDTO;
import com.ecotourexpress.ecotourexpress.repository.ProductoRepository;

@Service
public class ProductoService {
    // Conexion a repositorio
    @Autowired
    private ProductoRepository productoRepository;

    // ==========================================
    // CRUD PRODUCTOS
    // Métodos para manejar el CRUD
    // ==========================================


    // Crear producto
    public Producto saveProducto(ProductoDTO productoDTO) {
        Producto producto = new Producto();
        producto.setNombre(productoDTO.getNombre());
        producto.setCategoria(productoDTO.getCategoria());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setCantidad(productoDTO.getCantidad());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setDisponible(true);

        return productoRepository.save(producto);
    }

    // Obtener todos los productos
    public List<ProductoDTO> getAllProductos() {
        return productoRepository.findAll().stream()
                .map(producto -> new ProductoDTO(
                        producto.getId(),
                        producto.getCategoria(),
                        producto.getNombre(),
                        producto.getPrecio(),
                        producto.getCantidad(),
                        producto.getDescripcion(),
                        producto.isDisponible()))
                .collect(Collectors.toList());
    }

    // Seleccionar producto por ID (Editar)
    public Optional<Producto> getProductoById(int id) {
        return productoRepository.findById(id);
    }

    // Eliminar producto
    public void deleteProducto(int id) {
        productoRepository.deleteById(id);
    }


    public Producto saveProducto(Producto producto) {
        return productoRepository.save(producto);
    }
    
}
