package com.facturacion.backend.repositories;

import com.facturacion.backend.models.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
    List<Auditoria> findByUsuarioIdUsuario(Long idUsuario);
    List<Auditoria> findByTablaAfectada(String tablaAfectada);
    List<Auditoria> findByFechaAccionBetween(LocalDateTime inicio, LocalDateTime fin);
}