package com.facturacion.backend.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

// Marca la clase como una entidad de JPA (tabla en la BD)
@Entity
@Table(name = "clientes")
// Lombok: Genera getters, setters, constructores y más automáticamente
@Data
@NoArgsConstructor
public class Cliente {

    // Clave primaria (autoincremental)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Columna obligatoria (no nula)
    @Column(nullable = false, length = 100)
    private String nombre;

    // RUC/DNI: Obligatorio y único
    @Column(nullable = false, unique = true, length = 20)
    private String ruc;

    private String direccion;

    private String telefono;

    private String email;
}