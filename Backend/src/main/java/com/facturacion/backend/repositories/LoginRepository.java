package com.facturacion.backend.repositories;

import com.facturacion.backend.models.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<Login, Long> {

    // Buscar login por email (corregido según schema)
    Optional<Login> findByEmail(String email);
}