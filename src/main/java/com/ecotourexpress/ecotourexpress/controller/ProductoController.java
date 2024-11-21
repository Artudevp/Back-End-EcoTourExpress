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
import com.ecotourexpress.ecotourexpress.model.dto.ProductoDTO;
import com.ecotourexpress.ecotourexpress.service.ProductoService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@Transactional
@RequestMapping("/productos")
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    // Obtener lista de productos
    @GetMapping
    @PreAuthorize("permitAll()")
    public List<ProductoDTO> getAllProductos() {
        return productoService.getAllProductos();
    }

    // Agregar o Actualizar producto
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ProductoDTO newProducto(@Valid @RequestBody ProductoDTO productoDTO) {
        Producto producto = productoService.saveProducto(productoDTO);
        return new ProductoDTO(producto.getId(),
                            producto.getCategoria(),
                            producto.getNombre(),
                            producto.getPrecio(),
                            producto.getCantidad(),
                            producto.getDescripcion(),
                            producto.isDisponible());
    }



    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoDTO> updateProducto(@PathVariable int id, @Valid @RequestBody ProductoDTO productoDetails) {
        Producto producto = productoService.getProductoById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));

        producto.setNombre(productoDetails.getNombre());
        producto.setCategoria(productoDetails.getCategoria());
        producto.setPrecio(productoDetails.getPrecio());
        producto.setCantidad(productoDetails.getCantidad());
        producto.setDisponible(true);
        producto.setDescripcion(productoDetails.getDescripcion());

        final Producto updatedProducto = productoService.saveProducto(producto);
        
        return ResponseEntity.ok(new ProductoDTO(
            updatedProducto.getId(),
            updatedProducto.getCategoria(),
            updatedProducto.getNombre(),
            updatedProducto.getPrecio(),
            updatedProducto.getCantidad(),
            updatedProducto.getDescripcion(),
            updatedProducto.isDisponible()
        ));
    }

    

    // Eliminar producto
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProducto(@PathVariable int id) {
        productoService.deleteProducto(id);
    }
}
