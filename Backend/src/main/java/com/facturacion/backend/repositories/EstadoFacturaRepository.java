package com.facturacion.backend.repositories;

import com.facturacion.backend.models.EstadoFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EstadoFacturaRepository extends JpaRepository<EstadoFactura, Long> {
    Optional<EstadoFactura> findByCodigoEstado(String codigoEstado);
}