package com.facturacion.backend.repositories;

import com.facturacion.backend.models.Establecimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstablecimientoRepository extends JpaRepository<Establecimiento, Long> {

    Optional<Establecimiento> findByNit(String nit);

    Optional<Establecimiento> findByCodigoEstablecimiento(String codigoEstablecimiento);

    List<Establecimiento> findByActivo(Boolean activo);
}