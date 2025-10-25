package com.facturacion.backend.repositories;

import com.facturacion.backend.models.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {

    Optional<Factura> findByNumeroFactura(String numeroFactura);
    Optional<Factura> findByNumeroAutorizacion(String numeroAutorizacion);

    List<Factura> findByEstado_IdEstado(Long idEstado);
    List<Factura> findByCliente_IdCliente(Long idCliente);
    List<Factura> findByUsuarioCreacion_IdUsuario(Long idUsuario);
    List<Factura> findByEstablecimiento_IdEstablecimiento(Long idEstablecimiento);

    @Query("SELECT f FROM Factura f WHERE f.fechaEmision BETWEEN :fechaInicio AND :fechaFin")
    List<Factura> findByFechaEmisionBetween(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

    @Query("SELECT COALESCE(MAX(f.correlativo), 0) FROM Factura f WHERE f.establecimiento.idEstablecimiento = :idEstablecimiento AND f.serie = :serie")
    Integer findUltimoCorrelativoPorEstablecimientoYSerie(
            @Param("idEstablecimiento") Long idEstablecimiento,
            @Param("serie") String serie
    );

    List<Factura> findByCliente_IdClienteAndEstado_IdEstado(Long idCliente, Long idEstado);
}