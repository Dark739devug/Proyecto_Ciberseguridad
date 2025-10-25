package com.facturacion.backend.repositories;

import com.facturacion.backend.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByNit(String nit);
    List<Cliente> findByRazonSocialContainingIgnoreCase(String razonSocial);
    List<Cliente> findByActivo(Boolean activo);
}