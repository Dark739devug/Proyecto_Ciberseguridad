package com.facturacion.backend.repositories;

import com.facturacion.backend.models.BitacoraCertificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BitacoraCertificacionRepository extends JpaRepository<BitacoraCertificacion, Long> {
    List<BitacoraCertificacion> findByFacturaIdFactura(Long idFactura);
    List<BitacoraCertificacion> findByExitoso(Boolean exitoso);
}