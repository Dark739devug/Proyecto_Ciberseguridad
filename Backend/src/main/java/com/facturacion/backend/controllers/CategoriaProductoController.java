package com.facturacion.backend.controllers;

import com.facturacion.backend.models.CategoriaProducto;
import com.facturacion.backend.repositories.CategoriaProductoRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@SecurityRequirement(name = "bearerAuth")
public class CategoriaProductoController {

    @Autowired
    private CategoriaProductoRepository categoriaRepository;

    @GetMapping
    public List<CategoriaProducto> listarTodas() {
        return categoriaRepository.findAll();
    }

    @GetMapping("/{id}")
    public CategoriaProducto obtenerPorId(@PathVariable Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Categoría no encontrada"
                ));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaProducto crear(@RequestBody CategoriaProducto categoria) {
        return categoriaRepository.save(categoria);
    }

    @PutMapping("/{id}")
    public CategoriaProducto actualizar(@PathVariable Long id, @RequestBody CategoriaProducto categoria) {
        if (!categoriaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada");
        }
        categoria.setIdCategoria(id);
        return categoriaRepository.save(categoria);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        categoriaRepository.deleteById(id);
    }
}

