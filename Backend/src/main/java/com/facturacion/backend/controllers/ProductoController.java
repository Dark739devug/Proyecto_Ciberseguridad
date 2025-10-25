package com.facturacion.backend.controllers;

import com.facturacion.backend.models.Producto;
import com.facturacion.backend.repositories.ProductoRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@SecurityRequirement(name = "bearerAuth")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    @GetMapping("/activos")
    public List<Producto> listarActivos() {
        return productoRepository.findByActivoTrue();
    }

    @GetMapping("/{id}")
    public Producto obtenerPorId(@PathVariable Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producto no encontrado"
                ));
    }

    @GetMapping("/codigo/{codigo}")
    public Producto obtenerPorCodigo(@PathVariable String codigo) {
        return productoRepository.findByCodigoProducto(codigo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producto no encontrado"
                ));
    }

    @GetMapping("/categoria/{idCategoria}")
    public List<Producto> listarPorCategoria(@PathVariable Long idCategoria) {
        return productoRepository.findByCategoriaIdCategoria(idCategoria);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Producto crear(@RequestBody Producto producto) {
        return productoRepository.save(producto);
    }

    @PutMapping("/{id}")
    public Producto actualizar(@PathVariable Long id, @RequestBody Producto producto) {
        if (!productoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado");
        }
        producto.setIdProducto(id);
        return productoRepository.save(producto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        productoRepository.deleteById(id);
    }
}