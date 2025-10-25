package com.facturacion.backend.repositories;

import com.facturacion.backend.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Optional<Producto> findByCodigoProducto(String codigoProducto);
    List<Producto> findByNombreProductoContainingIgnoreCase(String nombreProducto);
    List<Producto> findByCategoria_IdCategoria(Long idCategoria);
    List<Producto> findByActivo(Boolean activo);
}