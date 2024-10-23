package com.ecotourexpress.ecotourexpress.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecotourexpress.ecotourexpress.config.exception.ResourceNotFoundException;
import com.ecotourexpress.ecotourexpress.model.Producto;
import com.ecotourexpress.ecotourexpress.service.ProductoService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@Transactional
@RequestMapping("/productos")
public class ProductoController {

    // Conexión a service
    @Autowired
    private ProductoService productoService;

    // Obtener lista de productos
    @GetMapping
    @PreAuthorize("permitAll()")
    public List<Producto> getAllProductos() {
        return productoService.getAllProductos();
    }

    // Agregar o Actualizar producto
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Producto newProducto(@Valid @RequestBody Producto producto) {
        return productoService.saveProducto(producto);
    }

    // Seleccionar producto por ID (editar)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> updateProducto(@PathVariable int id, @Valid @RequestBody Producto productoDetails) {
        Producto producto = productoService.getProductoById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));

        producto.setNombre_p(productoDetails.getNombre_p());
        producto.setCategoria(productoDetails.getCategoria());
        producto.setPrecio_p(productoDetails.getPrecio_p());
        producto.setCantidad_disponible(productoDetails.getCantidad_disponible());

        final Producto updatedProducto = productoService.saveProducto(producto);
        return ResponseEntity.ok(updatedProducto);
    }

    // Eliminar producto
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProducto(@PathVariable int id) {
        productoService.deleteProducto(id);
    }
}
