package com.facturacion.backend.repositories;

import com.facturacion.backend.models.CategoriaProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaProductoRepository extends JpaRepository<CategoriaProducto, Long> {
    Optional<CategoriaProducto> findByNombreCategoria(String nombreCategoria);
    List<CategoriaProducto> findByActivo(Boolean activo);
}