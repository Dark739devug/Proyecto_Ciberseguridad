package com.facturacion.backend.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
public class Usuario {

    // Mapea id_usuario SERIAL PRIMARY KEY
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    // Mapea fecha_nacimiento DATE
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    // Mapea nit VARCHAR(20) UNIQUE
    @Column(nullable = false, unique = true, length = 20)
    private String nit;

    // Mapea dpi VARCHAR(20) UNIQUE
    @Column(nullable = false, unique = true, length = 20)
    private String dpi;

    // Relación One-to-One con Login. 'mappedBy' indica que la entidad 'Login' tiene la FK.
    // Esto es solo para navegación.
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Login login;
}