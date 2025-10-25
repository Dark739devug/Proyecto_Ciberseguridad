package com.facturacion.backend.services;

import com.facturacion.backend.models.CategoriaProducto;
import com.facturacion.backend.repositories.CategoriaProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CategoriaProductoService {

    private final CategoriaProductoRepository categoriaRepository;

    public CategoriaProductoService(CategoriaProductoRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<CategoriaProducto> obtenerTodas() {
        return categoriaRepository.findAll();
    }

    public List<CategoriaProducto> obtenerActivas() {
        return categoriaRepository.findByActivo(true);
    }

    public CategoriaProducto obtenerPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));
    }

    @Transactional
    public CategoriaProducto crear(CategoriaProducto categoria) {
        return categoriaRepository.save(categoria);
    }

    @Transactional
    public CategoriaProducto actualizar(Long id, CategoriaProducto categoriaActualizada) {
        CategoriaProducto categoria = obtenerPorId(id);

        categoria.setNombreCategoria(categoriaActualizada.getNombreCategoria());
        categoria.setDescripcion(categoriaActualizada.getDescripcion());

        return categoriaRepository.save(categoria);
    }

    @Transactional
    public void desactivar(Long id) {
        CategoriaProducto categoria = obtenerPorId(id);
        categoria.setActivo(false);
        categoriaRepository.save(categoria);
    }
}