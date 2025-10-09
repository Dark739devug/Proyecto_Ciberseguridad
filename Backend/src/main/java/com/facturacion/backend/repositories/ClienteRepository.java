package com.facturacion.backend.repositories;

import com.facturacion.backend.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Hereda todos los métodos CRUD para la entidad Cliente
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}