package com.ecotourexpress.ecotourexpress.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ecotourexpress.ecotourexpress.model.Producto;
import com.ecotourexpress.ecotourexpress.model.DTO.ProductoDTO;
import com.ecotourexpress.ecotourexpress.repository.ProductoRepository;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;

    // Crear producto
    public Producto saveProducto(ProductoDTO productoDTO) {
        Producto producto = new Producto();
        producto.setNombre_p(productoDTO.getNombre_P());
        producto.setCategoria(productoDTO.getCategoria());
        producto.setPrecio_p(productoDTO.getPrecio_P());
        producto.setCantidad_disponible(productoDTO.getCantidad_Disponible());
        producto.setDescripcion_p(productoDTO.getDescripcion_P());
        producto.setDisponible(true);

        return productoRepository.save(producto);
    }

    // Obtener todos los productos
    public List<ProductoDTO> getAllProductos() {
        return productoRepository.findAll().stream()
                .map(producto -> new ProductoDTO(
                        producto.getID_producto(),
                        producto.getCategoria(),
                        producto.getNombre_p(),
                        producto.getPrecio_p(),
                        producto.getCantidad_disponible(),
                        producto.getDescripcion_p(),
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
}
