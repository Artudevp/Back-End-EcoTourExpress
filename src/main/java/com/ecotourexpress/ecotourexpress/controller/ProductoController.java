package com.ecotourexpress.ecotourexpress.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ecotourexpress.ecotourexpress.model.Producto;
import com.ecotourexpress.ecotourexpress.service.ProductoService;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @PostMapping("/nuevo")
    public Producto newProducto(@RequestBody Producto producto) {
        return productoService.saveProducto(producto);
    }

    @GetMapping("/todos")
    public List<Producto> getAllProductos() {
        return productoService.getAllProductos();
    }

    @DeleteMapping("/{id}")
    public void deleteProducto(@PathVariable int id) {
        productoService.deleteProducto(id);
    }
}
