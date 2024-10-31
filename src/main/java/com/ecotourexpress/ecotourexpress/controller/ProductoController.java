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
import com.ecotourexpress.ecotourexpress.model.DTO.ProductoDTO;
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
        return new ProductoDTO(producto.getID_producto(),
                            producto.getCategoria(),
                            producto.getNombre_p(),
                            producto.getPrecio_p(),
                            producto.getCantidad_disponible(),
                            producto.getDescripcion_p(),
                            producto.isDisponible());
    }



    // Seleccionar producto por ID (editar)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoDTO> updateProducto(@PathVariable int id, @Valid @RequestBody ProductoDTO productoDetails) {
        Producto producto = productoService.getProductoById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));

        producto.setNombre_p(productoDetails.getNombre_P());
        producto.setCategoria(productoDetails.getCategoria());
        producto.setPrecio_p(productoDetails.getPrecio_P());
        producto.setCantidad_disponible(productoDetails.getCantidad_Disponible());
        producto.setDisponible(true);

        final Producto updatedProducto = productoService.saveProducto(productoDetails);
        return ResponseEntity.ok(new ProductoDTO(updatedProducto.getID_producto(),
                                                  updatedProducto.getCategoria(),
                                                  updatedProducto.getNombre_p(),
                                                  updatedProducto.getPrecio_p(),
                                                  updatedProducto.getCantidad_disponible(),
                                                  updatedProducto.getDescripcion_p(),
                                                  updatedProducto.isDisponible()));
    }

    // Eliminar producto
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProducto(@PathVariable int id) {
        productoService.deleteProducto(id);
    }
}
