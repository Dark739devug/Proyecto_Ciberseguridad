package com.facturacion.backend.repositories;

import com.facturacion.backend.models.ConfiguracionSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ConfiguracionSistemaRepository extends JpaRepository<ConfiguracionSistema, Long> {
    Optional<ConfiguracionSistema> findByClave(String clave);
}
